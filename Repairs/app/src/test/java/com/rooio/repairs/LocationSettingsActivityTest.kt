package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.RequestQueue
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.robolectric.Robolectric
import org.robolectric.Shadows


@RunWith(AndroidJUnit4::class)
class LocationSettingsActivityTest {

    private lateinit var activity: LocationSettings

    @Mock
    val queue: RequestQueue = mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        RestApi.userLocationID = "Test"
        RestApi.queue = queue
        activity = Robolectric.buildActivity(LocationSettings::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testChangeLocation() {
        val button = activity.findViewById(R.id.changeLocation) as Button
        button.performClick()
        val expectedIntent = Intent(activity, ChangeLocationSettings::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun testResponseFunc() {
        activity.responseFunc.apply(JSONObject()
                .put("physical_address", "1 Grand Ave."))
        val currLocation = activity.findViewById(R.id.currentLocation) as TextView
        assertEquals(currLocation.text.toString(), "1 Grand Ave.")
    }

    @Test
    fun testErrorFunc() {
        activity.errorFunc.apply("Server error")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        assertEquals(error.text.toString(), "Server error")
    }
}