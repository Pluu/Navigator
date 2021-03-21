package com.pluu.navigator.deeplink

import android.net.Uri
import androidx.annotation.RestrictTo
import androidx.core.net.toUri
import com.pluu.navigator.util.hasScheme
import java.util.regex.Matcher
import java.util.regex.Pattern

///////////////////////////////////////////////////////////////////////////
// ref: https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:navigation/navigation-common/src/main/java/androidx/navigation/NavDeepLink.java
///////////////////////////////////////////////////////////////////////////
internal class NavDeepLink(
    val uriPattern: String
) {
    private val arguments = mutableListOf<String>()
    private val paramArgMap = mutableMapOf<String, ParamQuery>()
    private var pattern: Pattern? = null
    private var isParameterizedQuery = false

    public var isExactDeepLink: Boolean = false
        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
        get
        internal set

    init {
        if (uriPattern.isNotEmpty()) {
            val parameterizedUri = Uri.parse(uriPattern)
            isParameterizedQuery = parameterizedUri.query != null
            val uriRegex = StringBuilder("^")
            if (!uriPattern.hasScheme()) {
                uriRegex.append("http[s]?://")
            }
            @Suppress("RegExpRedundantEscape")
            val fillInPattern = Pattern.compile("\\{(.+?)\\}")
            if (isParameterizedQuery) {
                var matcher = Pattern.compile("(\\?)").matcher(uriPattern)
                if (matcher.find()) {
                    buildPathRegex(
                        uriPattern.substring(0, matcher.start()),
                        uriRegex,
                        fillInPattern
                    )
                }
                isExactDeepLink = false
                for (paramName in parameterizedUri.queryParameterNames) {
                    val argRegex = StringBuilder()
                    val queryParam = parameterizedUri.getQueryParameter(paramName) as String
                    matcher = fillInPattern.matcher(queryParam)
                    var appendPos = 0
                    val param = ParamQuery()
                    // Build the regex for each query param
                    while (matcher.find()) {
                        param.addArgumentName(matcher.group(1) as String)
                        argRegex.append(
                            Pattern.quote(
                                queryParam.substring(
                                    appendPos,
                                    matcher.start()
                                )
                            )
                        )
                        argRegex.append("(.+?)?")
                        appendPos = matcher.end()
                    }
                    if (appendPos < queryParam.length) {
                        argRegex.append(Pattern.quote(queryParam.substring(appendPos)))
                    }
                    // Save the regex with wildcards unquoted, and add the param to the map with its
                    // name as the key
                    param.paramRegex = argRegex.toString().replace(".*", "\\E.*\\Q")
                    paramArgMap[paramName] = param
                }
            } else {
                isExactDeepLink = buildPathRegex(uriPattern, uriRegex, fillInPattern)
            }
            // Since we've used Pattern.quote() above, we need to
            // specifically escape any .* instances to ensure
            // they are still treated as wildcards in our final regex
            val finalRegex = uriRegex.toString().replace(".*", "\\E.*\\Q")
            pattern = Pattern.compile(finalRegex)
        }
    }

    private fun buildPathRegex(
        uri: String,
        uriRegex: StringBuilder,
        fillInPattern: Pattern
    ): Boolean {
        val matcher = fillInPattern.matcher(uri)
        var appendPos = 0
        // Track whether this is an exact deep link
        var exactDeepLink = !uri.contains(".*")
        while (matcher.find()) {
            val argName = matcher.group(1) as String
            arguments.add(argName)
            // Use Pattern.quote() to treat the input string as a literal
            uriRegex.append(Pattern.quote(uri.substring(appendPos, matcher.start())))
            uriRegex.append("(.+?)")
            appendPos = matcher.end()
            exactDeepLink = false
        }
        if (appendPos < uri.length) {
            // Use Pattern.quote() to treat the input string as a literal
            uriRegex.append(Pattern.quote(uri.substring(appendPos)))
        }
        // Match either the end of string if all params are optional or match the
        // question mark and 0 or more characters after it
        // We do not use '.*' here because the finalregex would replace it with a quoted
        // version below.
        uriRegex.append("($|(\\?(.)*))")
        return exactDeepLink
    }

    fun match(path: String): Boolean {
        return pattern?.matcher(path)?.matches() ?: false
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NullableCollection")
    // Pattern.compile has no nullability for the regex parameter
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun matchingArguments(
        deepLink: String,
    ): Map<String, Any>? = matchingArguments(deepLink.toUri())

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NullableCollection")
    // Pattern.compile has no nullability for the regex parameter
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun matchingArguments(
        deepLink: Uri,
    ): Map<String, Any>? {
        val matcher = pattern!!.matcher(deepLink.toString())
        if (!matcher.matches()) {
            return null
        }
        val resultMap = mutableMapOf<String, Any>()
        val size = this.arguments.size
        for (index in 0 until size) {
            val argumentName = this.arguments[index]
            val value = Uri.decode(matcher.group(index + 1))
            resultMap[argumentName] = value
        }
        if (isParameterizedQuery) {
            for (paramName in paramArgMap.keys) {
                var argMatcher: Matcher? = null
                val storedParam = paramArgMap[paramName]
                val inputParams = deepLink.getQueryParameter(paramName)
                if (inputParams != null) {
                    // Match the input arguments with the saved regex
                    argMatcher = Pattern.compile(storedParam!!.paramRegex).matcher(inputParams)
                    if (!argMatcher.matches()) {
                        return null
                    }
                }
                // Params could have multiple arguments, we need to handle them all
                for (index in 0 until storedParam!!.size()) {
                    var value: String? = null
                    if (argMatcher != null) {
                        value = Uri.decode(argMatcher.group(index + 1))
                    }
                    val argName = storedParam.getArgumentName(index)
                    if (value != null && value.replace("[{}]".toRegex(), "") != argName) {
                        resultMap[argName] = value
                    }
                }
            }
        }
        return resultMap
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NavDeepLink

        if (uriPattern != other.uriPattern) return false

        return true
    }

    override fun hashCode(): Int {
        return uriPattern.hashCode()
    }

    override fun toString(): String {
        return "NavDeepLink(uriPattern='$uriPattern', pattern='$pattern')"
    }

    private class ParamQuery {
        var paramRegex: String? = null
        private val arguments = mutableListOf<String>()

        fun addArgumentName(name: String) {
            arguments.add(name)
        }

        fun getArgumentName(index: Int): String {
            return arguments[index]
        }

        fun size(): Int {
            return arguments.size
        }
    }
}