package com.mmfsin.sabelotodo.base

abstract class BaseUseCaseNoParams<T> {
    abstract suspend fun execute(): T
}