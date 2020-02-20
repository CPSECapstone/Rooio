package com.rooio.repairs

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.Robolectric

@RunWith(AndroidJUnit4::class)
class EquipmentActivityTest{

    private var activity: Equipment? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        activity = Robolectric.buildActivity(Equipment::class.java)
                .create()
                .resume()
                .get()
    }


}