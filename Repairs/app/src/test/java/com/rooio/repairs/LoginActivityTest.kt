package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Shadows

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    private var activity: Login? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        activity = Robolectric.buildActivity(Login::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testCancelLogin() {
        val button = activity!!.findViewById(R.id.cancelLogin) as Button
        button.performClick()
        val expectedIntent = Intent(activity, Landing::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun testEmptyPassword() {
        val button = activity!!.findViewById(R.id.connectAccount) as Button
        val username = activity!!.findViewById(R.id.usernameField) as TextView
        username.text = ""
        button.performClick()
        val error = activity!!.findViewById(R.id.errorMessage) as TextView
        assertEquals(error.text.toString(), "Incorrect username and/or password.")
    }

    @Test
    fun testEmptyUsername() {
        val button = activity!!.findViewById(R.id.connectAccount) as Button
        val password = activity!!.findViewById(R.id.passwordField) as TextView
        password.text = ""
        button.performClick()
        val error = activity!!.findViewById(R.id.errorMessage) as TextView
        assertEquals(error.text.toString(), "Incorrect username and/or password.")
    }

    @Test
    fun testResponseFunc() {
        activity!!.responseFunc.apply(JSONObject().put("token", "loginID"))
        assertEquals(activity!!.userToken, "loginID")
    }

    @Test
    fun testErrorFunc() {
        activity!!.errorFunc.apply("error")
        val error = activity!!.findViewById(R.id.errorMessage) as TextView
        assertEquals(error.text.toString(), "Incorrect username and/or password.")
    }
}