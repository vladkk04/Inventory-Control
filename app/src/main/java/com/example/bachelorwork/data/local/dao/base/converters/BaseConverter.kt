package com.example.bachelorwork.data.local.dao.base.converters

interface BaseConverter<T, V> {

    fun convertTo(value: V): T

    fun convertFrom(value: T): V
}