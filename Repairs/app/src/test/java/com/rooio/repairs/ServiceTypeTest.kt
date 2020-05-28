package com.rooio.repairs

import org.junit.Assert.assertEquals
import org.junit.Test

class ServiceTypeTest {

    @Test
    fun testRepairServiceType() {
        assertEquals(ServiceType.REPAIR.toString(), "Repair")
        assertEquals(ServiceType.REPAIR.getInt(), 1)
    }

    @Test
    fun testInstallationServiceType() {
        assertEquals(ServiceType.INSTALLATION.toString(), "Installation")
        assertEquals(ServiceType.INSTALLATION.getInt(), 2)
    }

    @Test
    fun testMaintenanceServiceType() {
        assertEquals(ServiceType.MAINTENANCE.toString(), "Maintenance")
        assertEquals(ServiceType.MAINTENANCE.getInt(), 3)
    }
}