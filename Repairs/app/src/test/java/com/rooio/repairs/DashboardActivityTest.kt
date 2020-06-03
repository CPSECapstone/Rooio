package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.view.View
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
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
        RestApi.userName = "luke"
        RestApi.queue = queue
        RestApi.userLocationID = "downtown"
        RestApi.employeeId="1"
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

    //testing navigation bar through the dashboard

    @Test
    fun navigationJobs() {
        val image = activity.findViewById<View>(R.id.jobs) as ImageView
        image.performClick()
        val expectedIntent = Intent(activity, Jobs::class.java)
        val actual: Intent = shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }
    @Test
    fun navigationDashboard() {
        val image = activity.findViewById<View>(R.id.dashboard) as ImageView
        image.performClick()
        val expectedIntent = Intent(activity, Dashboard::class.java)
        val actual: Intent = shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }
    @Test
    fun navigationEquipment() {
        val image = activity.findViewById<View>(R.id.equipment) as ImageView
        image.performClick()
        val expectedIntent = Intent(activity, Equipment::class.java)
        val actual: Intent = shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }
    @Test
    fun navigationArchived() {
        val image = activity.findViewById<View>(R.id.archived_arrow) as ImageView
        image.performClick()
        val expectedIntent = Intent(activity, JobsArchived::class.java)
        val actual: Intent = shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun navigationLogout() {
        val image = activity.findViewById<View>(R.id.logout) as ImageView
        image.performClick()
        val expectedIntent = Intent(activity, Landing::class.java)
        val actual: Intent = shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun navigationJobsText() {
        val image = activity.findViewById<View>(R.id.jobs_text) as TextView
        image.performClick()
        val expectedIntent = Intent(activity, Jobs::class.java)
        val actual: Intent = shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }
    @Test
    fun navigationDashboardText() {
        val image = activity.findViewById<View>(R.id.dashboard_text) as TextView
        image.performClick()
        val expectedIntent = Intent(activity, Dashboard::class.java)
        val actual: Intent = shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }
    @Test
    fun navigationEquipmentText() {
        val image = activity.findViewById<View>(R.id.equipment_text) as TextView
        image.performClick()
        val expectedIntent = Intent(activity, Equipment::class.java)
        val actual: Intent = shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }
    @Test
    fun navigationArchivedText() {
        val image = activity.findViewById<View>(R.id.archived_text) as TextView
        image.performClick()
        val expectedIntent = Intent(activity, JobsArchived::class.java)
        val actual: Intent = shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun navigationLogoutText() {
        val image = activity.findViewById<View>(R.id.logout_text) as TextView
        image.performClick()
        val expectedIntent = Intent(activity, Landing::class.java)
        val actual: Intent = shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun navigationSettingsText() {
        val image = activity.findViewById<View>(R.id.settings_text) as TextView
        image.performClick()
        val expectedIntent = Intent(activity, PreferredProvidersSettings::class.java)
        val actual: Intent = shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }






}