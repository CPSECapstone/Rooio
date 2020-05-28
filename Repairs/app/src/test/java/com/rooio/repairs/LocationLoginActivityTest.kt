package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.widget.ListView
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.RequestQueue
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.robolectric.Robolectric
import org.robolectric.Shadows


@RunWith(AndroidJUnit4::class)
class LocationLoginActivityTest {

    private lateinit var activity: LocationLogin

    @Mock
    val queue: RequestQueue = mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        RestApi.queue = queue
        activity = Robolectric.buildActivity(LocationLogin::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testAddAnotherLocation() {
        activity.animateActivity(false)
        val button = activity.findViewById(R.id.addAnother) as TextView
        button.performClick()
        val expectedIntent = Intent(activity, AddLocationLogin::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun testLocationResponseFuncIntent() {
        val jsonObj1 = JSONObject()
                .put("physical_address", "1 Grand Ave.")
                .put("id", "1")
        val jsonObj2 = JSONObject()
                .put("physical_address", "1 Grand Ave.")
                .put("id", "1")
        activity.responseFunc.apply(JSONArray()
                .put(jsonObj1).put(jsonObj2))
        val list = activity.findViewById(R.id.locationListView) as ListView
        list.performItemClick(list.adapter.getView(0, null, null),
                0, list.adapter.getItemId(0))
        val expectedIntent = Intent(activity, PreferredProvidersLogin::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }

}