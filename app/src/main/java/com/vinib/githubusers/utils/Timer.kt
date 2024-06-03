package com.vinib.githubusers.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

class Timer(val onTimerEnded: () -> Unit) {
    private var timer: Job? = null

    fun start(milliSeconds: Int) {
        timer = CoroutineScope(EmptyCoroutineContext).launch {
            repeat(milliSeconds) {
                delay(1)
            }
            onTimerEnded()
        }
    }

    fun stop() {
        timer?.cancel()
    }
}