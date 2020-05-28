package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.RequestQueue
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
class PreferredProviderDetailsActivityTest {

    private lateinit var activity: PreferredProviderDetails

    @Mock
    val queue: RequestQueue = Mockito.mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        RestApi.queue = queue

        activity = Robolectric.buildActivity(PreferredProviderDetails::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testBackButton() {
        val backButton = activity.findViewById<View>(R.id.back_button_details) as ImageView
        backButton.performClick()
        val expectedIntent = Intent(activity, PreferredProvidersSettings::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        Assert.assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun testProviderRemoveErrorFunc() {
        activity.removeErrorFunc.apply("Server error")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        Assert.assertEquals(error.text.toString(), "Server error")
    }

    @Test
    fun testProviderRemoveResponseFunc() {
        activity.providerResponseFunc.apply(JSONObject())
    }

    @Test
    fun testProviderDetailErrorFunc() {
        activity.providerErrorFunc.apply("Server error")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        Assert.assertEquals(error.text.toString(), "Server error")
    }

    @Test
    fun testProviderDetailResponseFunc() {
        activity.providerResponseFunc.apply(JSONObject())
    }

}