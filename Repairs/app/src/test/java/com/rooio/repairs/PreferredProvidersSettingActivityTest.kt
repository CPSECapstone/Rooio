package com.rooio.repairs

import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.RequestQueue
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.Robolectric
import org.robolectric.Shadows

@RunWith(AndroidJUnit4::class)
class PreferredProvidersSettingActivityTest {

    private lateinit var activity: PreferredProvidersSettings

    @Mock
    val queue: RequestQueue = Mockito.mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setup() {
        RestApi.queue = queue
        activity = Robolectric.buildActivity(PreferredProvidersSettings::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testAddAnotherProvider() {
        val addAnotherButton = activity.findViewById<TextView>(R.id.addAnother)
        addAnotherButton.performClick()
        val expectedIntent = Intent(activity, AddPreferredProvidersSettings::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun testOnResume() {
        activity.onResume()
        assertEquals(true, RestApi.am.isStreamMute(AudioManager.STREAM_SYSTEM))
    }

    @Test
    fun testOnPause() {
        activity.onPause()
        assertEquals(false, RestApi.am.isStreamMute(AudioManager.STREAM_SYSTEM))
    }



}