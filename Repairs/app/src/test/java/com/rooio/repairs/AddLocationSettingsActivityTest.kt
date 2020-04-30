package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
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
class AddLocationSettingsActivityTest {

    private lateinit var activity: AddLocationSettings

    @Mock
    val queue: RequestQueue = Mockito.mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setup() {
        RestApi.queue = queue
        activity = Robolectric.buildActivity(AddLocationSettings::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testCancelLocation() {
        activity.animateActivity(true)
        val button = activity.findViewById(R.id.backButton) as ImageView
        button.performClick()
        val expectedIntent = Intent(activity, ChangeLocationSettings::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        Assert.assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun testEmptyLocation() {
        val button = activity.findViewById(R.id.addLocation) as Button
        val location = activity.findViewById(R.id.autocomplete_setting) as AutoCompleteTextView
        location.setText("")
        button.performClick()
        val error = activity.findViewById(R.id.errorMessage) as TextView
        Assert.assertEquals("Not a valid address.", error.text.toString())
    }

    @Test
    fun testLocation() {
        val button = activity.findViewById(R.id.addLocation) as Button
        val location = activity.findViewById(R.id.autocomplete_setting) as AutoCompleteTextView
        location.setText("5988 E Weaver Circle")
        button.performClick()
    }

    @Test
    fun testLocationResponseFunc() {
        activity.locationResponseFunc.apply(JSONArray())
        val expectedIntent = Intent(activity, LocationSettings::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        Assert.assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun testLocationErrorFunc() {
        activity.locationErrorFunc.apply("Server error")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        Assert.assertEquals("Server error", error.text.toString())
    }

    @Test
    fun testCheckResponseFunc() {
        val location = activity.findViewById(R.id.autocomplete_setting) as AutoCompleteTextView
        location.setText("5988 E Weaver Circle")
        activity.checkResponseFunc.apply(JSONArray().put(JSONObject().put("physical_address", "12345")))
    }

    @Test
    fun testAlreadyAdded() {
        val location = activity.findViewById(R.id.autocomplete_setting) as AutoCompleteTextView
        location.setText("12345")
        activity.checkResponseFunc.apply(JSONArray().put(JSONObject().put("physical_address", "12345")))
        val error = activity.findViewById(R.id.errorMessage) as TextView
        Assert.assertEquals("You have already added this location.", error.text.toString())
    }

    @Test
    fun testCheckErrorFunc() {
        activity.checkErrorFunc.apply("Server error")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        Assert.assertEquals("Server error", error.text.toString())
    }
}