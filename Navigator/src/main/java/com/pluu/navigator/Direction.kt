package com.pluu.navigator

import com.pluu.navigator.util.toRouting
import java.io.Serializable

abstract class AbstractRoute : Destination() {
    /**
     * Register route
     *
     * @param creator Creator action
     */
    fun register(creator: CREATOR_ACTION) {
        Navigator.addDestination(this, creator)
    }

    /**
     * Register deeplink
     *
     * @param executor Executor action
     */
    fun register(executor: EXECUTOR_ACTION) {
        register(executor.toRouting())
    }

    /**
     * Register deeplink
     *
     * @param executor Executor action
     */
    internal fun register(executor: AbstractExecutor) {
        Navigator.addDestinationWithExecutor(this, executor)
    }

    /**
     * Register command action of deeplink
     *
     * @param T DeepLink Command
     * @param deepLinkCommand Command class
     */
    fun <T : DeepLinkCommand> register(deepLinkCommand: Class<T>) {
        register(deepLinkCommand.toRouting())
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
