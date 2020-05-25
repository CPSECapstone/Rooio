package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.RequestQueue
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.Robolectric
import org.robolectric.Shadows

@RunWith(AndroidJUnit4::class)
class AddPreferredProvidersLoginActivityTest {
    private lateinit var activity: AddPreferredProvidersLogin

    @Mock
    val queue: RequestQueue = Mockito.mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setup() {
        RestApi.queue = queue
        activity = Robolectric.buildActivity(AddPreferredProvidersLogin::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testAnimate() {
        activity.animateActivity(true)
        val cancelButton = activity.findViewById(R.id.cancel) as Button
        cancelButton.performClick()
        val expectedIntent = Intent(activity, PreferredProvidersLogin::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }
}
