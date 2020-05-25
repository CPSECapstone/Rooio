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
class AddPreferredProvidersActivityTest {
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
    fun testIsValidPhoneNumber() {
        val phoneInput = "1231231234"
        assertEquals(true, activity.isValidPhoneNumber(phoneInput))
    }

    @Test
    fun testIsAnotherValidPhoneNumber() {
        val phoneInput = "11231231234"
        assertEquals(true, activity.isValidPhoneNumber(phoneInput))
    }

    @Test
    fun testTooLongPhoneNumber() {
        val phoneInput = "112312312345"
        assertEquals(false, activity.isValidPhoneNumber(phoneInput))
    }

    @Test
    fun testTooShortPhoneNumber() {
        val phoneInput = "123123123"
        assertEquals(false, activity.isValidPhoneNumber(phoneInput))
    }

    @Test
    fun testEmptyPhoneNumber() {
        val phoneInput = ""
        assertEquals(false, activity.isValidPhoneNumber(phoneInput))
    }
}