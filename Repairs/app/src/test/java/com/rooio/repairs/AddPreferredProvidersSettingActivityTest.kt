package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
    fun testAddEmptyPhoneNumber(){
        val input = activity.findViewById<EditText>(R.id.newProvider)
        input.setText("")
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
        val input = activity.findViewById<EditText>(R.id.newProvider)
        input.setText("1231231234")
        val addButton = activity.findViewById<Button>(R.id.addProvider)
        addButton.performClick()
    }
}