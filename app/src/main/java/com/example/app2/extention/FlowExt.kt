package com.example.app2.extention

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withResumed
import androidx.lifecycle.withStarted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun LifecycleOwner.launchWhenResumed(block: CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        lifecycle.withResumed {
            block()
        }
    }
}

fun LifecycleOwner.launchWhenStarted(block: CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        lifecycle.withStarted {
            block()
        }
    }
}