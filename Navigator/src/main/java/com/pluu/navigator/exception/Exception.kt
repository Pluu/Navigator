package com.pluu.navigator.exception

class AlreadyRegisteredException(
    routeName: String
) : Throwable("route already registered $routeName")

class MissingRouteThrowable(
    routeName: String
) : Throwable("no route found for $routeName")

class MissingThrowable(
    type: String,
    routeName: String
) : Throwable("no $type found for $routeName")