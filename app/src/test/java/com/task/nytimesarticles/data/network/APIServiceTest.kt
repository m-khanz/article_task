package com.task.nytimesarticles.data.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.task.nytimesarticles.data.network.NYArticlesService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class APIServiceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var retrofitService: NYArticlesService
    private var mockWebServer = MockWebServer()

    //initializing...
    @Before
    fun setup() {
        mockWebServer.start(8080)

        retrofitService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(NYArticlesService::class.java)
    }

    @Test
    fun `get articles from remote test`() = runBlocking {
        readResponseFromFile()
        val response = retrofitService.getArticles().body()
        assertThat(response?.articlesList).isNotNull()
//        assertThat(response?.articlesList).isNotEmpty()
        assertThat(response?.articlesList!!.size).isEqualTo(20)//because everytime we are getting 20 articles...!!!

    }

    //reading data from the local file to compare the data from remote...!!!
    private fun readResponseFromFile(headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!
            .getResourceAsStream("retrofit-response/content.json")
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse.setBody(source.readString(Charsets.UTF_8))
        )
    }

    //once test is completed, closing the mock server...
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
