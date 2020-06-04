package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.RequestQueue
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
class PreferredProviderDetailsActivityTest {

    private lateinit var activity: PreferredProviderDetails

    @Mock
    val queue: RequestQueue = Mockito.mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        RestApi.queue = queue

        activity = Robolectric.buildActivity(PreferredProviderDetails::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testBackButton() {
        val backButton = activity.findViewById<View>(R.id.back_button_details) as ImageView
        backButton.performClick()
        val expectedIntent = Intent(activity, PreferredProvidersSettings::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        Assert.assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun testProviderRemoveErrorFunc() {
        activity.removeErrorFunc.apply("Server error")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        Assert.assertEquals(error.text.toString(), "Server error")
    }

    @Test
    fun testProviderRemoveResponseFunc() {
        activity.removeResponseFunc.apply(JSONObject())
        val expectedIntent = Intent(activity, PreferredProvidersSettings::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        Assert.assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun testProviderDetailErrorFunc() {
        activity.providerErrorFunc.apply("Server error")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        Assert.assertEquals(error.text.toString(), "Server error")
    }

    @Test
    fun testProviderDetailResponseFunc() {
        var providerJsonObj = JSONObject("{'id':2, 'name': 'Chang Electric', " +
                "'email': 'jessicachang38@gmail.com', 'phone': '+15625561583', " +
                "'website': 'http://www.yctr.com.tw/index_1.htm', " +
                "'logo': 'https://roopairs-capstone.s3.amazonaws.com/media/service_agency_logos/chang-electric/mark.gif?AWSAccessKeyId=AKIAJUE3LGYRGQFUDYYQ&Signature=z6zdfXVTIgp3C533IajG1WzonXQ%3D&Expires=1590959182', " +
                "'physical_address': '872 Higuera St, San Luis Obispo, CA', " +
                "'incorporated': '2019-10-27', " +
                "'contractor_license_number': '12345678', 'bio': '', " +
                "'starting_hourly_rate': '11222.00', 'certified': true, " +
                "'skills': ['Lighting and Electrical']}")
        activity.providerResponseFunc.apply(providerJsonObj)
        val overview = activity.findViewById(R.id.info_overview) as TextView
        val email = activity.findViewById(R.id.info_email) as TextView
        val skills = activity.findViewById(R.id.info_skills) as TextView
        val licenseNumber = activity.findViewById(R.id.info_license_number) as TextView
        val phoneNum = activity.findViewById(R.id.info_phone) as TextView
        val name = activity.findViewById(R.id.name) as TextView
        val logo = activity.findViewById(R.id.logo) as ImageView
        Assert.assertEquals("Overview TextView is not equal to provider bio",
                overview.text.toString(), "--")
        Assert.assertEquals("Email TextView is not equal to provider email",
                email.text.toString(), "jessicachang38@gmail.com")
        Assert.assertEquals("Skills TextView is not equal to provider skills",
                skills.text.toString(), "Lighting and Electrical")
        Assert.assertEquals("License Number TextView is not equal to provider contractor license number",
                licenseNumber.text.toString(), "12345678")
        Assert.assertEquals("Phone TextView is not equal to provider phone number",
                phoneNum.text.toString(), "+15625561583")
        Assert.assertEquals("Name TextView is not equal to provider name",
                name.text.toString(), "Chang Electric")
        Assert.assertEquals("Logo is missing when it shouldn't be",
                logo.visibility, View.VISIBLE)

    }

}