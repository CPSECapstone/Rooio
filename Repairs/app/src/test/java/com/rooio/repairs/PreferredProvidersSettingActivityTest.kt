package com.rooio.repairs

import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.RequestQueue
import org.json.JSONArray
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
    fun testPreferredProviderResponseFunc() {
        val jsonObj1 = JSONObject()
                .put("name","Electric Company")
                .put("id",1)
                .put("logo", "hey")
        val jsonObj2 = JSONObject()
                .put("name","Electric Company 2")
                .put("id",2)
                .put("logo", "")
        val jsonObj3 = JSONObject()
                .put("name","Electric Company 2")
                .put("id",2)
        activity.responseFunc.apply(JSONArray()
                .put(jsonObj1)
                .put(jsonObj2)
                .put(jsonObj3))
        val list = activity.findViewById(R.id.providerListView) as ListView
        list.performItemClick(list.adapter.getView(0, null, null),
                0, list.adapter.getItemId(0))
        val expectedIntent = Intent(activity, PreferredProviderDetails::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }

    //Adapter test coverage, with a change to RecyclerView these would not be necessary
    @Test
    fun testEmptyAdapter() {
        val jsonObj1 = JSONObject()
                .put("name","Electric Company")
                .put("id",1)
                .put("logo", "hey")
        val jsonObj2 = JSONObject()
                .put("name","Electric Company 2")
                .put("id",2)
        activity.responseFunc.apply(JSONArray()
                .put(jsonObj1)
                .put(jsonObj2))
        val list = activity.findViewById(R.id.providerListView) as ListView
        assertEquals(list.adapter.isEmpty(), false) //put into separate test for empty providers?
        assertEquals(list.adapter.getItemViewType(0), 0) //also separate test?
        assertEquals(list.adapter.getItem(0), 0)
        list.adapter.unregisterDataSetObserver(null)
    }

    @Test
    fun testPreferredProviderErrorFunc() {
        activity.errorFunc.apply("Server error")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        assertEquals(error.text.toString(), "Server error")
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

    @Test
    fun testClickProviderSpinner() {
        activity.animateActivity(true)
        val spinner = activity.findViewById(R.id.settings_spinner) as Spinner
        spinner.setSelection(1)
        val expectedIntent = Intent(activity, ChangeLocationSettings::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }

}