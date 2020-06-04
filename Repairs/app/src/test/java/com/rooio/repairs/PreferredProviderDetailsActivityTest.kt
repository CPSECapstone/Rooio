package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
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
class PreferredProviderDetailsActivityTest {

    private lateinit var activity: PreferredProviderDetails

    @Mock
    val queue: RequestQueue = Mockito.mock(RequestQueue::class.java)

    private val ID = 1
    private val NAME = "Preferred Provider 1"
    private val EMAIL = "test@gmail.com"
    private val PHONE = "1112223333"
    private val WEBSITE = ""
    private val LOGO = ""
    private val ADDRESS = "1 Testing Lane, Test, CA, USA"
    private val INCORPORATED = ""
    private val CONTRACTOR_LICENSE_NUM = ""
    private val BIO = ""
    private val STARTING_HOURLY_RATE = "100"
    private val CERTIFIED = true
    private val SKILLS = JSONArray("['HVAC', 'Plumbing']")
    private val EMPTY = "--"

    private var preferredProviderDetailsObj1: JSONObject = JSONObject()
            .put("id", ID)
            .put("name", NAME)
            .put("email", EMAIL)
            .put("phone", PHONE)
            .put("website", WEBSITE)
            .put("logo", LOGO)
            .put("physical_address", ADDRESS)
            .put("incorporated", INCORPORATED)
            .put("contractor_license_number", CONTRACTOR_LICENSE_NUM)
            .put("bio", BIO)
            .put("starting_hourly_rate", STARTING_HOURLY_RATE)
            .put("certified", CERTIFIED)
            .put("skills", SKILLS)

    private var preferredProviderDetailsObj2: JSONObject = JSONObject()
            .put("id", ID)
            .put("name", NAME)
            .put("email", "")
            .put("phone", "")
            .put("website", WEBSITE)
            .put("logo", "not null logo")
            .put("physical_address", ADDRESS)
            .put("incorporated", INCORPORATED)
            .put("contractor_license_number", CONTRACTOR_LICENSE_NUM)
            .put("bio", BIO)
            .put("starting_hourly_rate", "")
            .put("certified", CERTIFIED)
            .put("skills", SKILLS)

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
    fun testProviderResponseFunc() {
        activity.providerResponseFunc.apply(preferredProviderDetailsObj1)
        val loading = activity.findViewById<ProgressBar>(R.id.loadingPanel)
        Assert.assertEquals(View.GONE, loading.visibility)
    }

    @Test
    fun testProviderDetailErrorFunc() {
        activity.providerErrorFunc.apply("Server error")
        val error = activity.findViewById(R.id.errorMessage) as TextView
        Assert.assertEquals(error.text.toString(), "Server error")
    }

    @Test
    fun testProviderRemoveResponseFunc() {
        activity.removeResponseFunc.apply(Unit)
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
    fun testLoadElements() {
        activity.loadElements(preferredProviderDetailsObj1)
        val overview = activity.findViewById<TextView>(R.id.info_overview)
        val email = activity.findViewById<TextView>(R.id.info_email)
        val skills = activity.findViewById<TextView>(R.id.info_skills)
        val licenseNumber = activity.findViewById<TextView>(R.id.info_license_number)
        val phone = activity.findViewById<TextView>(R.id.info_phone)
        val logo = activity.findViewById<ImageView>(R.id.logo)
        val name = activity.findViewById<TextView>(R.id.name)
        val price = activity.findViewById<TextView>(R.id.price)
        Assert.assertEquals(EMPTY, overview.text)
        Assert.assertEquals(EMAIL, email.text)
        Assert.assertEquals("HVAC, Plumbing", skills.text)
        Assert.assertEquals(EMPTY, licenseNumber.text)
        Assert.assertEquals(PHONE, phone.text)
        Assert.assertEquals(View.GONE, logo.visibility)
        Assert.assertEquals(NAME, name.text)
        Assert.assertEquals(" $100 / hour starting cost", price.text.toString())
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

    @Test
    fun testLoadElements2() {
        activity.loadElements(preferredProviderDetailsObj2)
        val overview = activity.findViewById<TextView>(R.id.info_overview)
        val email = activity.findViewById<TextView>(R.id.info_email)
        val skills = activity.findViewById<TextView>(R.id.info_skills)
        val licenseNumber = activity.findViewById<TextView>(R.id.info_license_number)
        val phone = activity.findViewById<TextView>(R.id.info_phone)
        val logo = activity.findViewById<ImageView>(R.id.logo)
        val name = activity.findViewById<TextView>(R.id.name)
        val price = activity.findViewById<TextView>(R.id.price)
        Assert.assertEquals(EMPTY, overview.text)
        Assert.assertEquals(EMPTY, email.text)
        Assert.assertEquals("HVAC, Plumbing", skills.text)
        Assert.assertEquals(EMPTY, licenseNumber.text)
        Assert.assertEquals(EMPTY, phone.text)
        Assert.assertEquals(View.VISIBLE, logo.visibility)
        Assert.assertEquals(NAME, name.text)
    }



}