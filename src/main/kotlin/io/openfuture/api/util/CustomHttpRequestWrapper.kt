package io.openfuture.api.util

import java.io.*
import java.nio.charset.Charset
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

@SuppressWarnings
class CustomHttpRequestWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    val bodyInStringFormat: String

    private fun readInputStreamInStringFormat(stream: InputStream, charset: Charset): String {
        var stream = stream
        val maxBodySize = 1024
        val bodyStringBuilder = StringBuilder()
        if (!stream.markSupported()) {
            stream = BufferedInputStream(stream)
        }
        stream.mark(maxBodySize + 1)
        val entity = ByteArray(maxBodySize + 1)
        val bytesRead = stream.read(entity)
        if (bytesRead != -1) {
            bodyStringBuilder.append(String(entity, 0, bytesRead.coerceAtMost(maxBodySize), charset))
            if (bytesRead > maxBodySize) {
                bodyStringBuilder.append("...")
            }
        }
        stream.reset()
        return bodyStringBuilder.toString()
    }

    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(inputStream))
    }

    override fun getInputStream(): ServletInputStream {
        val byteArrayInputStream = ByteArrayInputStream(bodyInStringFormat.toByteArray())
        return object : ServletInputStream() {
            private var finished = false
            override fun isFinished(): Boolean {
                return finished
            }

            override fun available(): Int {
                return byteArrayInputStream.available()
            }

            override fun close() {
                super.close()
                byteArrayInputStream.close()
            }

            override fun isReady(): Boolean {
                return true
            }

            override fun setReadListener(readListener: ReadListener) {
                throw UnsupportedOperationException()
            }

            override fun read(): Int {
                val data = byteArrayInputStream.read()
                if (data == -1) {
                    finished = true
                }
                return data
            }
        }
    }

    init {
        bodyInStringFormat =
            readInputStreamInStringFormat(request.inputStream, Charset.forName(request.characterEncoding))

    }
}