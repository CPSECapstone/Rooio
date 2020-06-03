package com.rooio.repairs

import android.app.Application
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
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
import org.robolectric.Shadows

@RunWith(AndroidJUnit4::class)
class EquipmentActivityTest {

    private lateinit var activity: Equipment

    private var equipmentJsonObj1: JSONObject = JSONObject()
            .put("id", "1")
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
            .put("id", "2")
            .put("display_name", "equipment 2")
            .put("location", "kitchen")
            .put("manufacturer", "")
            .put("model_number", "")
            .put("serial_number", "")
            .put("last_service_date", "")
            .put("last_service_by", "")
            .put("notes", "")
            .put("service_location", "")
            .put("type", 2)
            .put("service_category", "")

    private var equipmentJsonObj3: JSONObject = JSONObject()
            .put("id", "3")
            .put("display_name", "equipment 3")
            .put("location", "dining room")
            .put("manufacturer", "")
            .put("model_number", "")
            .put("serial_number", "")
            .put("last_service_date", "")
            .put("last_service_by", "")
            .put("notes", "")
            .put("service_location", "")
            .put("type", 3)
            .put("service_category", "")

    private var equipmentJsonObj4: JSONObject = JSONObject()
            .put("id", "4")
            .put("display_name", "equipment 4")
            .put("location", "dining room")
            .put("manufacturer", "")
            .put("model_number", "")
            .put("serial_number", "")
            .put("last_service_date", "")
            .put("last_service_by", "")
            .put("notes", "")
            .put("service_location", "")
            .put("type", 4)
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
    fun testAddEquipmentClick() {
        val button = activity.findViewById<Button>(R.id.addEquipmentButton)
        button.performClick()
        val addEquipmentConstraint = activity.findViewById<ConstraintLayout>(R.id.addEquipmentConstraint)
        assertEquals(View.VISIBLE, addEquipmentConstraint.visibility)
    }

    @Test
    fun testResponseFuncAdd(){
        activity.responseFuncAdd.apply(null)
        val expectedIntent = Intent(activity, Equipment::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun testErrorFuncAdd() {
        val str = "Error Add"
        activity.errorFuncAdd.apply(str)
        val addEquipmentConstraint = activity.findViewById<ConstraintLayout>(R.id.addEquipmentConstraint)
        assertEquals(View.GONE, addEquipmentConstraint.visibility)

        val errorMsg = activity.findViewById<TextView>(R.id.equipmentPageNoSelectionText)
        assertEquals(str, errorMsg.text)
    }

    @Test
    fun testEditButton() {
        val button = activity.findViewById<Button>(R.id.editButton)
        button.performClick()
        val constraint = activity.findViewById<ConstraintLayout>(R.id.editEquipmentConstraint)
        assertEquals(View.VISIBLE, constraint.visibility)
    }

    @Test
    fun testResponseFuncSave() {
        activity.responseFuncSave.apply(null)
        val editConstraintLayout = activity.findViewById<ConstraintLayout>(R.id.editEquipmentConstraint)
        val equipmentDetails = activity.findViewById<ConstraintLayout>(R.id.equipmentDetailsConstraint)
        val equipmentAnalytics = activity.findViewById<ConstraintLayout>(R.id.analyticsConstraint)
        assertEquals(View.GONE, editConstraintLayout.visibility)
        assertEquals(View.VISIBLE, equipmentDetails.visibility)
        assertEquals(View.VISIBLE, equipmentAnalytics.visibility)
    }

    @Test
    fun testErrorFuncSave() {
        activity.errorFuncSave.apply("")
        val editConstraintLayout = activity.findViewById<ConstraintLayout>(R.id.editEquipmentConstraint)
        val errorMsg = activity.findViewById<TextView>(R.id.equipmentPageNoSelectionText)
        assertEquals(View.GONE, editConstraintLayout.visibility)
        assertEquals("Equipment details could not be saved at this time.", errorMsg.text)
    }

    @Test
    fun testCancelButton() {
        val button = activity.findViewById<Button>(R.id.cancelButton)
        button.performClick()
        val addEquipmentConstraint = activity.findViewById<ConstraintLayout>(R.id.addEquipmentConstraint)
        val editEquipmentConstrain = activity.findViewById<ConstraintLayout>(R.id.editEquipmentConstraint)
        val errorMsg = activity.findViewById<TextView>(R.id.equipmentPageNoSelectionText)
        assertEquals(View.GONE, addEquipmentConstraint.visibility)
        assertEquals(View.GONE, editEquipmentConstrain.visibility)
        assertEquals("Select one of the equipment items on the left to view details and analytics.", errorMsg.text)
    }

    @Test
    fun testResponseFuncLoad() {
        activity.responseFuncLoad.apply(JSONArray()
                .put(equipmentJsonObj1)
                .put(equipmentJsonObj2)
                .put(equipmentJsonObj3)
                .put(equipmentJsonObj4))
        assertEquals(activity.getEquipmentList().size, 4)
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

    @Test
    fun testAdapter() {
        val equipmentList = ArrayList<EquipmentData>()
        equipmentList.add(EquipmentData(equipmentJsonObj1))
        equipmentList.add(EquipmentData(equipmentJsonObj2))
        equipmentList.add(EquipmentData(equipmentJsonObj3))
        equipmentList.add(EquipmentData(equipmentJsonObj4))
        activity.setEquipmentList(equipmentList)
        val recyclerView = activity.findViewById<RecyclerView>(R.id.equipmentList)
        val adapter = EquipmentCustomAdapter(activity, equipmentList, 0)
        assertEquals(adapter.itemCount, recyclerView.adapter?.itemCount)

        val viewGroup = activity.findViewById<ViewGroup>(android.R.id.content)
        val myViewHolder = adapter.onCreateViewHolder(viewGroup, 0)
        adapter.onBindViewHolder(myViewHolder, 0)
        val button = myViewHolder.button
        button.performClick()
        val expectedIntent = Intent(activity, Equipment::class.java)
        val actual: Intent = Shadows.shadowOf(Application()).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)

        adapter.onBindViewHolder(myViewHolder, 1)
        assertEquals(View.GONE, myViewHolder.location.visibility)
    }
}