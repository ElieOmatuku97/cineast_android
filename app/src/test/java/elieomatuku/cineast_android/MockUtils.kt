package elieomatuku.cineast_android

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mock.*
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit
import kotlin.collections.get


/**
 * Created by elieomatuku on 2019-12-29
 */

object MockUtils {


    fun unAuthorizedInterceptor(): MockInterceptor {
        val mockInterceptor = MockInterceptor().apply {

            rule(get or post or put, url startWith "http") {
                respond(HttpCode.HTTP_401_UNAUTHORIZED).header("WWW-Authenticate", "Basic")
            }
        }

        return mockInterceptor
    }

    fun workingInterceptor(endingUrl: String, filename: String): MockInterceptor {

        val mockInterceptor = MockInterceptor().apply {
            rule(get or post or put or delete, url endsWith endingUrl) {
                respond(HttpCode.HTTP_200_OK)
                        .header("WWW-Authenticate", "Basic")
                        .body(readFile(filename))
            }
        }
        return mockInterceptor
    }

    val ASSET_BASE_PATH = "../app/src/test/assets/json/"
    @Throws(IOException::class)
    fun readFile(filename: String): InputStream {
        return FileInputStream(ASSET_BASE_PATH + filename)
    }


    fun buildHttpClient(mockInterceptor: MockInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        val logLevel = HttpLoggingInterceptor.Level.BODY
        logging.level = logLevel

        return OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30000, TimeUnit.MILLISECONDS)
                .readTimeout(30000, TimeUnit.MILLISECONDS)
                .writeTimeout(30000, TimeUnit.MILLISECONDS)
                .addInterceptor(mockInterceptor)
                .build()
    }
}