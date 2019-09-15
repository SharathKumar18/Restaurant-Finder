package com.demo.restaurant.network

import android.os.AsyncTask
import com.demo.restaurant.utils.Logger
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object ResponseFetchAsyncTask {

    private var networkFetchAsyncTask: ResponseFetchAsyncTask? = null

    class ResponseFetchAsyncTask(resultCallback: OnResultListener) :
        AsyncTask<String, Void, String?>() {

        private var weakReference: WeakReference<OnResultListener> = WeakReference(resultCallback)

        override fun doInBackground(vararg strings: String): String? {
            var response: String? = null
            val request = strings[0]
            Logger.i("RestaurantData", "Url:$request")
            try {
                val conn: HttpURLConnection = createHttpConnection(request)
                val responseCode: Int = conn.responseCode
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    val input = conn.inputStream
                    response = ResponseParser.parseResponseFromServer(input)
                }
            } catch (e: Exception) {
                weakReference.get()?.onResultFailed(e.printStackTrace().toString())
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

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Logger.i("RestaurantData", "\nResponse$result")
            weakReference.get()?.onResultSuccess(result)
            cancel(true)
        }
    }

    fun fetchResultFromServer(url: String, listener: OnResultListener) {
        networkFetchAsyncTask = ResponseFetchAsyncTask(listener)
        networkFetchAsyncTask?.execute(url)
    }

    interface OnResultListener {
        fun <T> onResultSuccess(restaurant: T)
        fun onResultFailed(value: String)
    }
}