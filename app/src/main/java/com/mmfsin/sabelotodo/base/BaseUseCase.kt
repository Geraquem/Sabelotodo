package com.mmfsin.sabelotodo.base

abstract class BaseUseCase<params, T> {
    abstract suspend fun execute(params: params): T
}