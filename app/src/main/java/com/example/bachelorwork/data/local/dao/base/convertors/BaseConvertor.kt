package com.example.bachelorwork.data.local.dao.base.convertors

interface BaseConvertor<T, V> {

    fun convertTo(value: V): T

    fun convertFrom(value: T): V
}