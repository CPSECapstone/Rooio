package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.RequestQueue
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.Robolectric
import org.robolectric.Shadows

@RunWith(AndroidJUnit4::class)
class ChooseEquipmentActivityTest {

    private lateinit var activity: ChooseEquipment

    @Mock
    val queue: RequestQueue = Mockito.mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        RestApi.queue = queue
        RestApi.userLocationID = "1"
        activity = Robolectric.buildActivity(ChooseEquipment::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testBackButton() {
        val backButton = activity.findViewById<View>(R.id.back_button) as ImageView
        backButton.performClick()
        val expectedIntent = Intent(activity, Dashboard::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        Assert.assertEquals(expectedIntent.component, actual.component)
    }

}