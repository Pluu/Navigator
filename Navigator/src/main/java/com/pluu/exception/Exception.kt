package com.pluu.exception

class AlreadyRegisteredException(
    routeName: String
) : Throwable("route already registered $routeName")

class MissingRouteThrowable(
    routeName: String
) : Throwable("no intent found for $routeName")