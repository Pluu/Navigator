package com.pluu.deeplink

import android.net.Uri
import java.util.*
import java.util.regex.Pattern

class NavDeepLink(
    private val uri: String
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
                // TODO
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

    fun matchingArguments(deepLink: Uri): Map<String, Any>? {
        val matcher = pattern?.matcher(deepLink.toString())
        if (matcher == null || !matcher.matches()) return null

//        val size = arguments.size
//        for (index in 0 until size) {
//            val argumentName = arguments[index]
//            val value = Uri.decode(matcher.group(index + 1))
//            val argument: NavArgument = arguments(argumentName)
//            if (parseArgument(bundle, argumentName, value, argument)) {
//                return null
//            }
//        }
//        if (mIsParameterizedQuery) {
//            for ((paramName, storedParam) in mParamArgMap) {
//                var argMatcher: Matcher? = null
//                val inputParams = deepLink.getQueryParameter(paramName)
//                if (inputParams != null) {
//                    // Match the input arguments with the saved regex
//                    argMatcher = Pattern.compile(storedParam.paramRegex).matcher(inputParams)
//                    if (!argMatcher.matches()) {
//                        return null
//                    }
//                }
//                // Params could have multiple arguments, we need to handle them all
//                for (index in 0 until storedParam.size()) {
//                    var value = if (argMatcher != null) {
//                        Uri.decode(argMatcher.group(index + 1))
//                    } else {
//                        null
//                    }
//
//                    val argName: String = storedParam.getArgumentName(index)
//                    val argument: NavArgument = arguments.get(argName)
//                    // Missing parameter so see if it has a default value or is Nullable
//                    if (argument != null
//                        && (value == null || value.replace("[{}]".toRegex(), "") == argName)
//                    ) {
//                        if (argument.getDefaultValue() != null) {
//                            value = argument.getDefaultValue().toString()
//                        } else if (argument.isNullable()) {
//                            value = null
//                        }
//                    }
//                    if (parseArgument(bundle, argName, value, argument)) {
//                        return null
//                    }
//                }
//            }
//        }
        return null
    }
}

private class ParamQuery() {
    var paramRegex: String? = null
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

}