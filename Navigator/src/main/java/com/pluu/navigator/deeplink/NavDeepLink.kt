package com.pluu.navigator.deeplink

import android.net.Uri
import androidx.core.net.toUri
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

///////////////////////////////////////////////////////////////////////////
// ref: https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:navigation/navigation-common/src/main/java/androidx/navigation/NavDeepLink.java
///////////////////////////////////////////////////////////////////////////
internal class NavDeepLink(
    val uri: String
) {
    private val SCHEME_PATTERN = "^[a-zA-Z]+[+\\w\\-.]*:".toPattern()

    private val arguments = ArrayList<String>()
    private val mParamArgMap = HashMap<String, ParamQuery>()

    private var mExactDeepLink = false
    private var mIsParameterizedQuery = false
    private var pattern: Pattern? = null

    init {
        if (uri.isNotEmpty()) {
            val parameterizedUri = Uri.parse(uri)
            mIsParameterizedQuery = parameterizedUri.query != null

            val uriRegex = StringBuilder("^")

            if (!SCHEME_PATTERN.matcher(uri).find()) {
                uriRegex.append("http[s]?://")
            }
            val fillInPattern = Pattern.compile("\\{(.+?)\\}")
            if (mIsParameterizedQuery) {
                var matcher = Pattern.compile("(\\?)").matcher(uri)
                if (matcher.find()) {
                    buildPathRegex(uri.substring(0, matcher.start()), uriRegex, fillInPattern)
                }
                mExactDeepLink = false
                for (paramName in parameterizedUri.queryParameterNames) {
                    val argRegex = StringBuilder()
                    val queryParam = parameterizedUri.getQueryParameter(paramName)
                    matcher = fillInPattern.matcher(queryParam)
                    var appendPos = 0
                    val param = ParamQuery()
                    // Build the regex for each query param
                    while (matcher.find()) {
                        param.addArgumentName(matcher.group(1))
                        argRegex.append(
                            Pattern.quote(
                                queryParam!!.substring(
                                    appendPos,
                                    matcher.start()
                                )
                            )
                        )
                        argRegex.append("(.+?)?")
                        appendPos = matcher.end()
                    }
                    if (appendPos < queryParam!!.length) {
                        argRegex.append(Pattern.quote(queryParam.substring(appendPos)))
                    }
                    // Save the regex with wildcards unquoted, and add the param to the map with its
                    // name as the key
                    param.setParamRegex(argRegex.toString().replace(".*", "\\E.*\\Q"))
                    mParamArgMap[paramName!!] = param
                }
            } else {
                mExactDeepLink = buildPathRegex(uri, uriRegex, fillInPattern)
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
            val argName = matcher.group(1)
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

    fun matchingArguments(
        deepLink: String,
    ): Map<String, Any>? = matchingArguments(deepLink.toUri())

    fun matchingArguments(
        deepLink: Uri,
    ): Map<String, Any>? {
        val matcher = pattern?.matcher(deepLink.toString())
        if (matcher == null || !matcher.matches()) return null

        val resultMap = mutableMapOf<String, Any>()

        val size = arguments.size
        for (index in 0 until size) {
            val argumentName = arguments[index]
            val value = Uri.decode(matcher.group(index + 1))
            resultMap[argumentName] = value
        }
        if (mIsParameterizedQuery) {
            for ((paramName, storedParam) in mParamArgMap) {
                var argMatcher: Matcher?
                val inputParams = deepLink.getQueryParameter(paramName)
                if (inputParams != null) {
                    // Match the input arguments with the saved regex
                    argMatcher = Pattern.compile(storedParam.paramRegex).matcher(inputParams)
                    if (!argMatcher.matches()) {
                        return null
                    }
                    // Params could have multiple arguments, we need to handle them all
                    for (index in 0 until storedParam.size()) {
                        var value: String? = null
                        if (argMatcher != null) {
                            value = Uri.decode(argMatcher.group(index + 1))
                        }
                        val argName = storedParam.getArgumentName(index)
                        if (value != null) {
                            resultMap[argName] = value
                        }
                    }
                }
            }
        }
        return resultMap
    }

    fun isExactDeepLink() = mExactDeepLink

    override fun toString(): String {
        return "NavDeepLink(uri='$uri', pattern='$pattern')"
    }
}

private class ParamQuery {
    var paramRegex: String = ""
        private set
    private val mArguments = ArrayList<String>()

    fun addArgumentName(name: String) {
        mArguments.add(name)
    }

    fun getArgumentName(index: Int): String {
        return mArguments[index]
    }

    fun size(): Int {
        return mArguments.size
    }

    fun setParamRegex(regex: String) {
        paramRegex = regex
    }

}