package com.rooio.repairs

import android.view.View
import android.widget.TextView
import androidx.arch.core.util.Function
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

@RunWith(AndroidJUnit4::class)
class EquipmentActivityTest {

    private lateinit var activity: Equipment

    private var equipmentJsonObj1: JSONObject = JSONObject()
            .put("id", "qwerty")
            .put("display_name", "equipment 1")
            .put("location", "kitchen")
            .put("manufacturer", "")
            .put("model_number", "")
            .put("serial_number", "")
            .put("last_service_date", "")
            .put("last_service_by", "")
            .put("notes", "")
            .put("service_location", "")
            .put("type", 1)
            .put("service_category", "")

    private var equipmentJsonObj2: JSONObject = JSONObject()
            .put("id", "qwerty")
            .put("display_name", "equipment 2")
            .put("location", "dining room")
            .put("manufacturer", "")
            .put("model_number", "")
            .put("serial_number", "")
            .put("last_service_date", "")
            .put("last_service_by", "")
            .put("notes", "")
            .put("service_location", "")
            .put("type", 1)
            .put("service_category", "")

    @Mock
    val queue: RequestQueue = Mockito.mock(RequestQueue::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        RestApi.queue = queue
        RestApi.userLocationID = "abcd"
        activity = Robolectric.buildActivity(Equipment::class.java)
                .create()
                .resume()
                .get()
    }

    @Test
    fun testResponseFuncLoad() {
        activity.responseFuncLoad.apply(JSONArray()
                .put(equipmentJsonObj1)
                .put(equipmentJsonObj2))
        assertEquals(activity.getEquipmentList().size, 2)
    }

    @Test
    fun testErrorFuncLoad() {
        val str = "Server error"
        activity.errorFuncLoad.apply(str)
        val error = activity.findViewById(R.id.equipmentPageNoSelectionText) as TextView
        assertEquals(str, error.text)
    }

    @Test
    fun testIsValidEquipmentRequest() {
        val params = HashMap<Any?, Any?>()
        params["display_name"] = "Equipment 1"
        params["serial_number"] = ""
        params["manufacturer"] =  ""
        params["location"] =  ""
        params["model_number"] =  ""
        params["type"] =  ""

        val url = "equipment/"
        val request = JsonRequest(true, url, params, Function { null }, Function { null }, true)

        assertEquals(true, activity.isValidEquipmentRequest(request))
    }

    @Test
    fun testIsNotValidEquipmentRequest() {
        val params = HashMap<Any?, Any?>()
        params["display_name"] = ""
        params["serial_number"] = ""
        params["manufacturer"] =  ""
        params["location"] =  ""
        params["model_number"] =  ""
        params["type"] =  ""

        val url = "equipment/"
        val request = JsonRequest(true, url, params, Function { null }, Function { null }, true)

        assertEquals(false, activity.isValidEquipmentRequest(request))
    }
}