package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.RequestQueue
import org.json.JSONArray
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
class ChangeLocationSettingsActivityTest {

    private lateinit var activity: ChangeLocationSettings

    @Mock
    val queue: RequestQueue = mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        RestApi.queue = queue
        activity = Robolectric.buildActivity(ChangeLocationSettings::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testAddAnotherLocation() {
        val button = activity.findViewById(R.id.addAnother) as TextView
        button.performClick()
        val expectedIntent = Intent(activity, AddLocationSettings::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun testResponseFunc() {
        val jsonObj = JSONObject()
                .put("physical_address", "1 Grand Ave.")
                .put("id", "1")
        activity.responseFunc.apply(JSONArray()
                .put(jsonObj))
        assertEquals(ChangeLocationSettings.addressList[0], "1 Grand Ave.")
        assertEquals(ChangeLocationSettings.locationIds[0], "1")
    }

    @Test
    fun testErrorFunc() {
        activity.errorFunc.apply("Server error")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        assertEquals(error.text.toString(), "Server error")
    }
}