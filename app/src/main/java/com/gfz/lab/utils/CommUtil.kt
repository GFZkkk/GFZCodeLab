package com.gfz.lab.utils

import java.util.*

fun getLowerCamelCase(str: String): String{
    val word = str.lowercase(Locale.getDefault()).split("_")
    val result = StringBuilder()
    word.forEach {
        if(it.isNotBlank()){
            if (result.isNotBlank()){
                result.append(it[0] - 32)
                result.append(it.substring(1))
            }else{
                result.append(it)
            }
        }
    }
    return result.toString()
}


