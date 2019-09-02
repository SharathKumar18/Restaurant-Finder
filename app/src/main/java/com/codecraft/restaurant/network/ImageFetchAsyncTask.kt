package com.codecraft.restaurant.network

import android.graphics.Bitmap
import android.os.AsyncTask
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.codecraft.restaurant.R
import com.codecraft.restaurant.utils.Logger
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


object ImageFetchAsyncTask {

    private var networkFetchAsyncTask: ImageFetchAsyncTask? = null

    class ImageFetchAsyncTask(imageView: ImageView) :
        AsyncTask<String, Void, Bitmap?>() {

        private var weakReference: WeakReference<ImageView> = WeakReference(imageView)

        override fun doInBackground(vararg strings: String): Bitmap? {
            val request = strings[0]
            Logger.i("RestaurantData", "Url:$request")
            try {
                val conn: HttpURLConnection = createImageConnection(request)
                val responseCode: Int = conn.responseCode
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    val input = conn.inputStream
                    return ResponseParser.parseImageFromServer(input)
                }
            } catch (e: Exception) {
                if (!isCancelled) {
                    cancel(true)
                }
            }
            return null
        }

        override fun onPostExecute(result: Bitmap?) {
            Logger.i("RestaurantData", "Response:$result")
            if (result != null) {
                weakReference.get()?.setImageBitmap(result)
            } else {
                weakReference.get()?.setImageDrawable(weakReference.get()?.context?.let {
                    ContextCompat.getDrawable(
                        it, R.drawable.ic_action_restaurant
                    )
                })
            }
        }
    }

    fun fetchImageFromServer(url: String, image: ImageView) {
        networkFetchAsyncTask = ImageFetchAsyncTask(image)
        networkFetchAsyncTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url)
    }

    private fun createImageConnection(
        request: String
    ): HttpURLConnection {
        val url = URL(request)
        val connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        return connection
    }
}