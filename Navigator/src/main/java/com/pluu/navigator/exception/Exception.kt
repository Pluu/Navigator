package com.pluu.navigator.exception

class MissingRouteThrowable(
    routeName: String
) : Throwable("no route found for $routeName")
