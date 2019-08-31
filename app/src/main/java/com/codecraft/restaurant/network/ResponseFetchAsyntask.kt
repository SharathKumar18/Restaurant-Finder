package com.codecraft.restaurant.network

import android.os.AsyncTask
import com.codecraft.restaurant.data.response.Restaurant
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class ResponseFetchAsyntask {

    lateinit var resultCallback:OnResultListener

    inner class ResponsefetchAsyntask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg strings: String): String? {
            var response:String?=null
            val request = strings[0]
            try {
                val conn: HttpURLConnection = createHttpConnection(request)
                val responseCode: Int = conn.responseCode

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    val input = conn.inputStream
                    response= ResponseParser.parseResponseFromServer(input)
                }
            } catch (e: Exception) {
                resultCallback.onResultFailed(e.printStackTrace().toString())
            }
            return response
        }

        private fun createHttpConnection(
            request: String
        ): HttpURLConnection {
            val url = URL(request)

            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.readTimeout = 15000
            conn.connectTimeout = 15000
            conn.requestMethod = "GET"
            conn.doInput = true
            conn.setRequestProperty(
                "Content-Type",
                "application/x-www-form-urlencoded"
            )
            conn.doOutput = true
            return conn
        }


        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            resultCallback.onResultSuccess(s)
            cancel(true)
        }
    }

    fun fetchResultFromServer(url: String) {
        ResponsefetchAsyntask().execute(url)
    }

    fun setResultListener(listener: OnResultListener) {
        resultCallback = listener
    }


    interface OnResultListener {
        fun onResultSuccess(restaurant: String)
        fun onResultFailed(value:String)
    }

    companion object {
        private var responseFetch: ResponseFetchAsyntask? = null
        fun getInstance(): ResponseFetchAsyntask? {
            if (responseFetch == null) {
                responseFetch = ResponseFetchAsyntask()
            }
            return responseFetch
        }
    }
}