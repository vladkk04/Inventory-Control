package com.example.bachelorwork.data.local.converters.common

interface BaseConverter<T, V> {

    fun convertTo(value: V): T

    fun convertFrom(value: T): V
}