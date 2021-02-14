package com.pluu.navigator

import java.io.Serializable

abstract class AbstractRoute : Destination() {
    fun register(creator: CREATOR_ACTION) {
        Navigator.addDestination(this, creator)
    }

    fun register(executor: EXECUTOR_ACTION) {
        Navigator.addDestination(this, executor)
    }

    fun register(executor: AbstractExecutor) {
        Navigator.addDestinationWithExecutor(this, executor)
    }

    fun <T : DeepLinkCommand> register(deepLinkCommand: Class<T>) {
        Navigator.addDestinationWithExecutor(this, CommandRouting(deepLinkCommand))
    }
}

abstract class Direction : AbstractRoute()

abstract class DirectionParam : Serializable

abstract class DirectionWithParam<T : DirectionParam> : AbstractRoute()

class DeepLink(
    deepLinkPath: String
) : AbstractRoute() {
    init {
        setPath(deepLinkPath)
    }
}
