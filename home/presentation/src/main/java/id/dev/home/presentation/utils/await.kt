package id.dev.home.presentation.utils

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { cont ->
    addOnCompleteListener { task ->
        if (task.exception != null) {
            cont.resumeWithException(task.exception!!)
        } else {
            cont.resume(task.result)
        }
    }
}