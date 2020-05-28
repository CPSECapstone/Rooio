package com.rooio.repairs

import android.app.Application
import android.content.Intent
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

@RunWith(AndroidJUnit4::class)
class PreferredProvidersLoginActivityTest {

    private lateinit var activity: PreferredProvidersLogin

    @Mock
    val queue: RequestQueue = Mockito.mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setup() {
        RestApi.queue = queue
        activity = Robolectric.buildActivity(PreferredProvidersLogin::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testAddAnotherProvider() {
        val addAnotherButton = activity.findViewById<TextView>(R.id.addAnother)
        addAnotherButton.performClick()
        val expectedIntent = Intent(activity, AddPreferredProvidersLogin::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun testContinue() {
        activity.animateActivity(false)
        val continueButton = activity.findViewById<TextView>(R.id.done)
        continueButton.performClick()
        val expectedIntent = Intent(activity, Dashboard::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }


}