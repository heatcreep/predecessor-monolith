package com.aowen.monolith.network.utils

import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll

suspend fun awaitAll(vararg jobs: Job) {
    jobs.asList().joinAll()
}