package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.RequestQueue
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
class RegistrationActivityTest {

    private lateinit var activity: Registration

    @Mock
    val queue: RequestQueue = mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        RestApi.queue = queue
        activity = Robolectric.buildActivity(Registration::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testCancelRegistration() {
        val button = activity.findViewById(R.id.cancelRegistration) as Button
        button.performClick()
        val expectedIntent = Intent(activity, Landing::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }


    @Test
    fun testRegistrationResponseFunc() {
        val jsonObj = JSONObject()
                .put("token","1")
                .put("first_name","luke")
        activity.responseFunc.apply(jsonObj)
        assertEquals(RestApi.userName, "luke")
    }


    @Test
    fun testRegistrationErrorFunc() {
        activity.errorFunc.apply("Server error")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        assertEquals(error.text.toString(), "Server error")
    }

    @Test
    fun testAlreadyRegisteredErrorFunc() {
        activity.errorFunc.apply("Does not exist.")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        assertEquals(error.text.toString(), "Email is already registered with a Roopairs account.")
    }

    @Test
    fun testInvalidPassword() {
        val button = activity.findViewById(R.id.register) as Button
        val firstName = activity.findViewById(R.id.firstName) as TextView
        val lastName = activity.findViewById(R.id.lastName) as TextView
        val email = activity.findViewById(R.id.email) as TextView
        val password = activity.findViewById(R.id.password) as TextView
        val verifyPassword = activity.findViewById(R.id.verifyPassword) as TextView
        val restaurantName = activity.findViewById(R.id.restaurantName) as TextView

        firstName.text = "Yusuf"
        lastName.text = "Baha"
        email.text = "ybahaduer@calpoly.edu"
        password.text = "1"
        verifyPassword.text = "1"
        restaurantName.text = "bobby"
        button.performClick()
        val error = activity.findViewById(R.id.errorMessage) as TextView
        assertEquals(error.text.toString(), "Invalid password.")
    }

    @Test
    fun testDifferentPasswords() {
        val button = activity.findViewById(R.id.register) as Button
        val firstName = activity.findViewById(R.id.firstName) as TextView
        val lastName = activity.findViewById(R.id.lastName) as TextView
        val email = activity.findViewById(R.id.email) as TextView
        val password = activity.findViewById(R.id.password) as TextView
        val verifyPassword = activity.findViewById(R.id.verifyPassword) as TextView
        val restaurantName = activity.findViewById(R.id.restaurantName) as TextView

        firstName.text = "Yusuf"
        lastName.text = "Baha"
        email.text = "ybahaduer@calpoly.edu"
        password.text = "123456788"
        verifyPassword.text = "123456789"
        restaurantName.text = "bobby"
        button.performClick()
        val error = activity.findViewById(R.id.errorMessage) as TextView
        assertEquals(error.text.toString(), "Invalid password.")
    }

    fun testInvalidEmail() {
        val button = activity.findViewById(R.id.register) as Button
        val firstName = activity.findViewById(R.id.firstName) as TextView
        val lastName = activity.findViewById(R.id.lastName) as TextView
        val email = activity.findViewById(R.id.email) as TextView
        val password = activity.findViewById(R.id.password) as TextView
        val verifyPassword = activity.findViewById(R.id.verifyPassword) as TextView
        val restaurantName = activity.findViewById(R.id.restaurantName) as TextView


        firstName.text = "Yusuf"
        lastName.text = "Baha"
        email.text = "ybahaduer3calpolyedu"
        password.text = "Red120poer!o"
        verifyPassword.text = "Red120poer!o"
        restaurantName.text = "bobby2"
        button.performClick()
        val error = activity.findViewById(R.id.errorMessage) as TextView
        assertEquals(error.text.toString(), "Invalid email address.")
    }


    @Test
    fun testUnfilledFields() {
        val button = activity.findViewById(R.id.register) as Button
        val firstName = activity.findViewById(R.id.firstName) as TextView
        val email = activity.findViewById(R.id.email) as TextView
        val password = activity.findViewById(R.id.password) as TextView
        val verifyPassword = activity.findViewById(R.id.verifyPassword) as TextView
        val restaurantName = activity.findViewById(R.id.restaurantName) as TextView


        firstName.text = "Yusuf"
        email.text = "ybahaduer3@calpoly.edu"
        password.text = "Red120poer!o"
        verifyPassword.text = "Red120poer!o"
        restaurantName.text = "bobby2"
        button.performClick()
        val error = activity.findViewById(R.id.errorMessage) as TextView
        assertEquals(error.text.toString(), "Please fill out all fields.")

    }
    @Test
    fun testRegistrationInfo() {
        val button = activity.findViewById(R.id.register) as Button
        val firstName = activity.findViewById(R.id.firstName) as TextView
        val lastName = activity.findViewById(R.id.lastName) as TextView
        val email = activity.findViewById(R.id.email) as TextView
        val password = activity.findViewById(R.id.password) as TextView
        val verifyPassword = activity.findViewById(R.id.verifyPassword) as TextView
        val restaurantName = activity.findViewById(R.id.restaurantName) as TextView


        firstName.text = "Yusuf"
        lastName.text = "Baha"
        email.text = "ybahaduer3@calpoly.edu"
        password.text = "Red120poer!o"
        verifyPassword.text = "Red120poer!o"
        restaurantName.text = "bobby2"
        button.performClick()
    }


}