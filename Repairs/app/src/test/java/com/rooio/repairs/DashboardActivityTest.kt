package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.RequestQueue
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.Robolectric
import org.robolectric.Shadows
import org.robolectric.Shadows.shadowOf

@RunWith(AndroidJUnit4::class)
class DashboardActivityTest {

    private lateinit var activity: Dashboard

    @Mock
    val queue: RequestQueue = Mockito.mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setup() {
        RestApi.queue = queue
        RestApi.userLocationID = "eeffba"
        activity = Robolectric.buildActivity(Dashboard::class.java)
                .create()
                .resume()
                .get()
    }


    @Test
    fun applianceButtonTest() {
        val button = activity.findViewById<View>(R.id.applianceButton) as Button
        button.performClick()
        val expectedIntent = Intent(activity, ChooseEquipment::class.java)
        val actual: Intent = shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }
    @Test
    fun hvacButtonTest() {
        val button = activity.findViewById<View>(R.id.hvacButton) as Button
        button.performClick()

        val expectedIntent = Intent(activity, ChooseEquipment::class.java)
        val actual: Intent = shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }
    @Test
    fun plumbingButtonTest() {
        val button = activity.findViewById<View>(R.id.plumbingButton) as Button
        button.performClick()
        val expectedIntent = Intent(activity, ChooseEquipment::class.java)
        val actual: Intent = shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }
    @Test
    fun lightingButtonTest() {
        val button = activity.findViewById<View>(R.id.lightingButton) as Button
        button.performClick()
        val expectedIntent = Intent(activity, ChooseEquipment::class.java)
        val actual: Intent = shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }


}