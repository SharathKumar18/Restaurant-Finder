package com.codecraft.restaurant.network

import android.graphics.Bitmap
import android.os.AsyncTask
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.codecraft.restaurant.R
import kotlinx.android.synthetic.main.fragment_detail.view.*
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


object ImageFetchAsyntask {

    private var networkFetchAsyncTask: ImageFetchAsyncTask? = null

    class ImageFetchAsyncTask(imageView: ImageView) :
        AsyncTask<String, Void, Bitmap?>() {

        private var weakReference: WeakReference<ImageView> = WeakReference(imageView)

        override fun doInBackground(vararg strings: String): Bitmap? {
            val request = strings[0]
            try {
                val conn: HttpURLConnection = createImageConnection(request)
                val responseCode: Int = conn.responseCode
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    val input = conn.inputStream
                    return ResponseParser.parseImageFromServer(input)
                }else{
                    setBackgroundImage()
                }
            } catch (e: Exception) {
                setBackgroundImage()
                if (!isCancelled) {
                    cancel(true)
                }
            }
            return null
        }

        private fun setBackgroundImage() {
            weakReference.get()?.setImageDrawable(weakReference.get()?.context?.let {
                ContextCompat.getDrawable(
                    it, R.drawable.ic_action_restuarant
                )
            })
        }

        override fun onPostExecute(result: Bitmap?) {
            weakReference.get()?.setImageBitmap(result)
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

        val connection = url
            .openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        return connection
    }
}