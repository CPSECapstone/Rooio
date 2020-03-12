package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.view.View
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
class ChooseServiceProviderActivityTest {

    private lateinit var activity: ChooseServiceProvider

    @Mock
    val queue: RequestQueue = Mockito.mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        RestApi.queue = queue
        RestApi.userLocationID = "1"
        activity = Robolectric.buildActivity(ChooseServiceProvider::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testBackButton() {
        val backButton = activity.findViewById(R.id.backButton) as ImageView
        backButton.performClick()
        val expectedIntent = Intent(activity, ChooseEquipment::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        Assert.assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun testResponseFunc() {
        activity.equipmentId = "1"
        val jsonObj1 = JSONObject()
                .put("id",1)
                .put("name","")
                .put("email","bathroom")
                .put("phone","hey")
                .put("website","hey")
                .put("logo","hey")
                .put("physical_address","hey")
                .put("incorporated","hey")
                .put("contractor_license_number","hey")
                .put("bio","hey")
                .put("starting_hourly_rate","hey")
                .put("certified",true)
                .put("skills", JSONArray())
        activity.providerResponseFunc.apply(JSONArray()
                .put(jsonObj1))
    }

    @Test
    fun testErrorFunc() {
        activity.providerErrorFunc.apply("Server error")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        Assert.assertEquals(error.text.toString(), "Server error")
    }

}