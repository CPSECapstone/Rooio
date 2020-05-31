package com.rooio.repairs

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.RequestQueue
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.Robolectric

@RunWith(AndroidJUnit4::class)
class JobDetailsTest {

    private lateinit var activity: JobDetails

    @Mock
    val queue: RequestQueue = Mockito.mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        RestApi.queue = queue
        RestApi.userLocationID = "1"
        activity = Robolectric.buildActivity(JobDetails::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun convertToNewFormat() {
        val date = ""
        assertEquals(activity.convertToNewFormat(date), "")
    }

    @Test
    fun convertToNewFormat2() {
        val date = ""
        assertEquals(activity.convertToNewFormat2(date), "")
    }
}