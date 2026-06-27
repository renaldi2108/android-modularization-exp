package com.example.core.utils.coroutine

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SingletonCoroutineScope

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ActivityRetainedCoroutineScope
