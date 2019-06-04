package unl.fct.smart_grow.http

import android.os.AsyncTask
import unl.fct.smart_grow.security.MockJwtStorage
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

const val TIMEOUT = 10*1000

class HttpTask(callback: (String?) -> Unit) : AsyncTask<String, Unit, String>()  {

    var callback = callback

    override fun doInBackground(vararg params: String): String? {
        val url = URL(params[1])
        val httpClient = url.openConnection() as HttpURLConnection
        httpClient.readTimeout = TIMEOUT
        httpClient.connectTimeout = TIMEOUT
        httpClient.requestMethod = params[0]
        httpClient.setRequestProperty("Authorization", "Bearer: ${MockJwtStorage.jwt}")

        if (params[0] == "POST") {
            httpClient.instanceFollowRedirects = false
            httpClient.doOutput = true
            httpClient.doInput = true
            httpClient.useCaches = false
            httpClient.setRequestProperty("Content-Type", "application/json; charset=utf-8")
        }
        try {
            if (params[0] == "POST" || params[0] == "PUT" || params[0] == "DELETE") {
                httpClient.connect()
                val os = httpClient.getOutputStream()
                val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                writer.write(params[2])
                writer.flush()
                writer.close()
                os.close()
            }
            if (httpClient.responseCode == HttpURLConnection.HTTP_OK || httpClient.responseCode == HttpURLConnection.HTTP_CREATED || httpClient.responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                //val respond = httpClient.responseMessage
                val stream = BufferedInputStream(httpClient.inputStream)

                return readStream(inputStream = stream)
            } else {
                println("ERROR ${httpClient.responseCode}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            httpClient.disconnect()
        }

        return null
    }

    private fun readStream(inputStream: BufferedInputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { stringBuilder.append(it) }
        return stringBuilder.toString()
    }


    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        callback(result)
    }
}