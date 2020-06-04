package com.rooio.repairs

import org.junit.Assert.assertEquals
import org.junit.Test

class EquipmentTypeTest {

    @Test
    fun testHvacEquipmentType(){
        assertEquals("HVAC", EquipmentType.HVAC.toString())
        assertEquals(1, EquipmentType.HVAC.getIntRepr())
    }

    @Test
    fun testLightingAndElectricalEquipmentType(){
        assertEquals("Lighting and Electrical", EquipmentType.LIGHTING_AND_ELECTRICAL.toString())
        assertEquals(2, EquipmentType.LIGHTING_AND_ELECTRICAL.getIntRepr())
    }

    @Test
    fun testPlumbingEquipmentType(){
        assertEquals("Plumbing", EquipmentType.PLUMBING.toString())
        assertEquals(3, EquipmentType.PLUMBING.getIntRepr())
    }

    @Test
    fun testApplianceEquipmentType(){
        assertEquals("Appliance", EquipmentType.APPLIANCE.toString())
        assertEquals(4, EquipmentType.APPLIANCE.getIntRepr())
    }
}