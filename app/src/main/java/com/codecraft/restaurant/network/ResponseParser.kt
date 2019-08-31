package com.codecraft.restaurant.network

import java.io.InputStream
import java.io.InputStreamReader

object ResponseParser {

    fun parseResponseFromServer(input: InputStream): String{
        val response = StringBuffer()

        val isw = InputStreamReader(input)

        var data = isw.read()
        while (data != -1) {
            val current = data.toChar()
            data = isw.read()
            response.append(current)
        }
        return response.toString()
    }
}