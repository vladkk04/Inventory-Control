package com.example.inventorycotrol.util

import kotlin.enums.EnumEntries

fun <T : Enum<T>> EnumEntries<T>.namesTyped() = this.map { it.name }.toTypedArray()