package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.RequestQueue
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.Robolectric
import org.robolectric.Shadows

@RunWith(AndroidJUnit4::class)
class AddLocationLoginActivityTest {

    private lateinit var activity: AddLocationLogin

    @Mock
    val queue: RequestQueue = Mockito.mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setup() {
        RestApi.queue = queue
        activity = Robolectric.buildActivity(AddLocationLogin::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testCancelLocation() {
        activity.animateActivity(false)
        val button = activity.findViewById(R.id.cancel) as Button
        button.performClick()
        val expectedIntent = Intent(activity, LocationLogin::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        Assert.assertEquals(expectedIntent.component, actual.component)
    }

}