package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.RequestQueue
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.Robolectric
import org.robolectric.Shadows

@RunWith(AndroidJUnit4::class)
class AddPreferredProvidersSettingActivityTest {
    private lateinit var activity: AddPreferredProvidersSettings

    @Mock
    val queue: RequestQueue = Mockito.mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setup() {
        RestApi.queue = queue
        activity = Robolectric.buildActivity(AddPreferredProvidersSettings::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testCancel(){
        activity.animateActivity(true)
        val cancelButton = activity.findViewById(R.id.backButton) as ImageView
        cancelButton.performClick()
        val expectedIntent = Intent(activity, PreferredProvidersSettings::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        Assert.assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun testAddEmptyPhoneNumber(){
        val input = activity.findViewById<EditText>(R.id.newProvider)
        input.setText("123123123123123")
        val addButton = activity.findViewById<Button>(R.id.addProvider)
        addButton.performClick()
        val errorMsg = activity.findViewById<TextView>(R.id.errorMessage)
        Assert.assertEquals(errorMsg.text, activity.resources.getString(R.string.error_phone))

    }

    @Test
    fun testAddTooLongPhoneNumber(){
        val input = activity.findViewById<EditText>(R.id.newProvider)
        input.setText("123123123123123")
        val addButton = activity.findViewById<Button>(R.id.addProvider)
        addButton.performClick()
        val errorMsg = activity.findViewById<TextView>(R.id.errorMessage)
        Assert.assertEquals(errorMsg.text.toString(), activity.resources.getString(R.string.error_phone))
    }

    @Test
    fun testAddPhoneNumber() {
        val input = activity.findViewById(R.id.newProvider) as EditText
        input.setText("1231231234")
        val addButton = activity.findViewById(R.id.addProvider) as Button
        addButton.performClick()
    }

    @Test
    fun testProviderResponseFunc() {
        activity.providerResponseFunc.apply(JSONArray())
        val expectedIntent = Intent(activity, PreferredProvidersSettings::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        Assert.assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun testProviderErrorFunc() {
        activity.providerErrorFunc.apply("Server error")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        Assert.assertEquals("Server error", error.text.toString())
    }

    @Test
    fun testDoesNotExist() {
        activity.providerErrorFunc.apply("Does not exist.")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        Assert.assertEquals(activity.resources.getString(R.string.error_provider), error.text.toString())
    }

    @Test
    fun testCheckResponseFunc() {
        val phone = activity.findViewById(R.id.newProvider) as EditText
        phone.setText("8090949")
        activity.checkResponseFunc.apply(JSONArray().put(JSONObject().put("phone", "123456")))
    }

    @Test
    fun testAlreadyAdded() {
        val phone = activity.findViewById(R.id.newProvider) as EditText
        phone.setText("123456")
        activity.checkResponseFunc.apply(JSONArray().put(JSONObject().put("phone", "123456")))
        val error = activity.findViewById(R.id.errorMessage) as TextView
        Assert.assertEquals(activity.resources.getString(R.string.already_added_provider), error.text.toString())
    }

    @Test
    fun testCheckErrorFunc() {
        activity.checkErrorFunc.apply("Server error")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        Assert.assertEquals("Server error", error.text.toString())
    }
}