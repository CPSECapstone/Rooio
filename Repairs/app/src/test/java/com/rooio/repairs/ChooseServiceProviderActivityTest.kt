package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun testResponseFunc() {
        val jsonObj1 = JSONObject()
                .put("id",1)
                .put("name","hey")
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
        val jsonObj2 = JSONObject()
                .put("id",1)
                .put("name","")
                .put("email","bathroom")
                .put("phone","hey")
                .put("website","hey")
                .put("logo","")
                .put("physical_address","hey")
                .put("incorporated","hey")
                .put("contractor_license_number","hey")
                .put("bio","hey")
                .put("starting_hourly_rate","hey")
                .put("certified",true)
                .put("skills", JSONArray().put("hey"))
        activity.providerResponseFunc.apply(JSONArray()
                .put(jsonObj1)
                .put(jsonObj2))
        val recycler = activity.findViewById(R.id.serviceProviderList) as RecyclerView
        // workaround Robolectric recyclerView issue
        recycler.measure(0,0)
        recycler.layout(0,0,100,1000)
        recycler.findViewHolderForAdapterPosition(0)!!.itemView.performClick()
        val search = activity.findViewById(R.id.searchBar) as EditText
        search.setText("hey")
        search.setText("")
    }

    @Test
    fun testErrorFunc() {
        activity.providerErrorFunc.apply("Server error")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        assertEquals(error.text.toString(), "Server error")
    }

    @Test
    fun testEmptyListMessage() {
        activity.providerResponseFunc.apply(JSONArray())
        val message = activity.findViewById(R.id.noProvider) as TextView
        assertEquals(message.text, "No provider found.")
    }

    @Test
    fun testSwitchingTabs() {
        val providerButton = activity.findViewById(R.id.preferredButton) as Button
        val networkButton = activity.findViewById(R.id.networkButton) as Button
        val message = activity.findViewById(R.id.networkText) as TextView
        networkButton.performClick()
        providerButton.performClick()
        assertEquals(message.text, "This service is currently not available.")

    }

}