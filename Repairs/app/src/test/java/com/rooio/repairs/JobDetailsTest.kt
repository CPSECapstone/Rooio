package com.rooio.repairs

import android.widget.TextView
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
    fun testConvertToNewFormat() {
        val date = "2020-01-23T20:23:45.123Z"
        val formattedDate = "2020-01-23 20:23:45"
        assertEquals(formattedDate, activity.convertToNewFormat(date))
    }

    @Test
    fun test2ConvertToNewFormat() {
        val date = "2020-01-23T20:23:45Z"
        val formattedDate = "2020-01-23 20:23:45"
        assertEquals(formattedDate, activity.convertToNewFormat(date))
    }

    @Test
    fun testConvertToNewFormat2() {
        val date = "2020-01-23"
        val formattedDate = "2020-01-23"
        assertEquals(formattedDate, activity.convertToNewFormat2(date))
    }

    @Test
    fun testErrorFunc() {
        activity.errorFunc.apply("Server error")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        assertEquals(error.text.toString(), "Server error")
    }
}
