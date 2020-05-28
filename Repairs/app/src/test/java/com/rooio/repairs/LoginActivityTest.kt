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
class LoginActivityTest {

    private lateinit var activity: Login

    @Mock
    val queue: RequestQueue = mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        RestApi.queue = queue
        activity = Robolectric.buildActivity(Login::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testCancelLogin() {
        val button = activity.findViewById(R.id.cancelLogin) as Button
        button.performClick()
        val expectedIntent = Intent(activity, Landing::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun testEmptyPassword() {
        val button = activity.findViewById(R.id.connectAccount) as Button
        val username = activity.findViewById(R.id.usernameField) as TextView
        username.text = ""
        button.performClick()
        val error = activity.findViewById(R.id.errorMessage) as TextView
        assertEquals(error.text.toString(), "Incorrect username and/or password.")
    }

    @Test
    fun testEmptyUsername() {
        val button = activity.findViewById(R.id.connectAccount) as Button
        val password = activity.findViewById(R.id.passwordField) as TextView
        password.text = ""
        button.performClick()
        val error = activity.findViewById(R.id.errorMessage) as TextView
        assertEquals(error.text.toString(), "Incorrect username and/or password.")
    }

    @Test
    fun testResponseFunc() {
        activity.responseFunc.apply(JSONObject()
                .put("token", "loginID")
                .put("first_name", "Hey"))
        assertEquals(RestApi.userToken, "loginID")
        assertEquals(RestApi.userName, "Hey")
    }

    @Test
    fun testSendLoginInfo() {
        val button = activity.findViewById(R.id.connectAccount) as Button
        val username = activity.findViewById(R.id.usernameField) as TextView
        val password = activity.findViewById(R.id.passwordField) as TextView
        username.text = "username"
        password.text = "password"
        button.performClick()
    }

    @Test
    fun testErrorFunc() {
        activity.errorFunc.apply("Server error")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        assertEquals(error.text.toString(), "Server error")
    }

}