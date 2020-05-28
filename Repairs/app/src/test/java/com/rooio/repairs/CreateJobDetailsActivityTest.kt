package com.rooio.repairs

import android.widget.Button
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.RequestQueue
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.Robolectric

@RunWith(AndroidJUnit4::class)
class CreateJobDetailsActivityTest {

    private lateinit var activity: CreateJobDetails

    @Mock
    val queue: RequestQueue = Mockito.mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        RestApi.queue = queue
        RestApi.userLocationID = "1"
        activity = Robolectric.buildActivity(CreateJobDetails::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testBackButton() {
        val send = activity.findViewById(R.id.sendRequestButton) as Button
       val errorMsg = activity.findViewById(R.id.errorMessage) as TextView
        send.performClick()
        Assert.assertEquals(errorMsg.text.toString(), "Please fill out all required fields.")
    }
}