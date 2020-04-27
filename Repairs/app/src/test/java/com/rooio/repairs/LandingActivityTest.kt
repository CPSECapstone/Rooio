package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.json.JSONArray
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import java.util.*


@RunWith(AndroidJUnit4::class)
class LandingActivityTest {

    private lateinit var scenario: ActivityScenario<Landing>

    @Before
    @Throws(Exception::class)
    fun setUp() {
        scenario = ActivityScenario.launch(Landing::class.java)
    }

    @Test
    fun testConnectAccount() {
        scenario.onActivity {activity ->
            val button = activity.findViewById<View>(R.id.connectAccount) as Button
            button.performClick()
            val expectedIntent = Intent(activity, Login::class.java)
            val actual: Intent = shadowOf(Application()).nextStartedActivity
            assertEquals(expectedIntent.component, actual.component)
        }
    }

    @Test
    fun testCreateAccount() {
        scenario.onActivity { activity ->
            val button = activity!!.findViewById<View>(R.id.createAccount) as Button
            button.performClick()
            val expectedIntent = Intent(activity, Registration::class.java)
            val actual: Intent = shadowOf(Application()).nextStartedActivity
            assertEquals(expectedIntent.component, actual.component)
        }
    }
}