package com.aowen.predcompanion.core.common.network

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val predCompanionDispatcher: PredCompanionDispatchers)

enum class PredCompanionDispatchers {
    Default,
    IO,
}