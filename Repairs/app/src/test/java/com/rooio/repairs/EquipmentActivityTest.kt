package com.rooio.repairs

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.RequestQueue
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.Robolectric

@RunWith(AndroidJUnit4::class)
class EquipmentActivityTest {

    private lateinit var activity: Equipment

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
    fun testSupersedeEquipmentAdapter(){
//        activity.supersedeEquipmentAdapter()
    }

    @Test
    fun testResponseFuncLoad(){
        activity.responseFuncLoad.apply(JSONArray()
                .put(JSONObject()
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
                        .put("service_category", ""))
                .put(JSONObject()
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
                        .put("service_category", "")))


    }
}