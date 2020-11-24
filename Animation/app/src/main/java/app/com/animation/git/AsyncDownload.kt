package app.com.animation.git

import android.os.AsyncTask
import app.com.animation.MainActivity
import app.com.animation.R
import app.com.animation.viewer.CodeViewFragment
import app.com.animation.viewer.CppParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class AsyncDownload(private val fragment: ConnectionFragment) : AsyncTask<Int, Int, Boolean>() {
    private var content = ConnectionFragment.EXCEPTED

    private fun correctHttp(http: String): URL {
        val split = http.split("/")
        val rawHttp = StringBuilder()
        rawHttp.append(PREFIX)
        for (x in 3 until split.size) {
            if (split[x] == TO_DELETE) {
                continue
            }
            rawHttp.append("/${split[x]}")
        }
        return URL(rawHttp.toString())
    }

    override fun doInBackground(vararg params: Int?): Boolean {
        var reader: BufferedReader? = null
        var success = false
        try {
            val url = correctHttp(fragment.http)
            val connection = url.openConnection() as HttpsURLConnection
            connection.requestMethod = "GET"
            connection.readTimeout = TIMEOUT
            connection.connect()
            reader = BufferedReader(InputStreamReader(connection.inputStream))
            content = reader.readText()
            success = true

        } catch (e: Throwable) {
        } finally {
            reader?.close()
        }
        return success
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        if (content == ConnectionFragment.CANCELLED || content == ConnectionFragment.EXCEPTED) {
            MainActivity.downloadStatus = -1
            fragment.updateStatus()
        } else {
            val parser = CppParser(
                content, fragment.resources,
                fragment.http.substringAfterLast('/'), true
            )
            parser.parse()
            with(CodeViewFragment) {
                openSources.add(parser)
                current = openSources.size - 1
            }
            MainActivity.downloadStatus = 1
            fragment.updateStatus()
            (fragment.activity as MainActivity).openViewer()
        }
    }

    companion object {
        private const val TIMEOUT = 8000
        private const val PREFIX = "https://raw.githubusercontent.com"
        private const val TO_DELETE = "blob"
    }

}