package com.rooio.repairs

import org.junit.Assert.assertEquals
import org.junit.Test

class GraphTypeTest {

    @Test
    fun testGraphType() {
        assertEquals(GraphType.EQUIPMENT.getInt(), 1)
        assertEquals(GraphType.DASHBOARD.getInt(), 2)
    }

    @Test
    fun testOptionType(){
        assertEquals(GraphType.OptionType.COST.getInt(), 1)
        assertEquals(GraphType.OptionType.JOBS.getInt(), 2)
    }

    @Test
    fun testTimeType(){
        assertEquals(GraphType.TimeType.MONTH.getInt(), 1)
        assertEquals(GraphType.TimeType.YEAR.getInt(), 2)
    }

    @Test
    fun testJobType(){
        assertEquals(GraphType.JobType.REPAIR.getInt(), 1)
        assertEquals(GraphType.JobType.INSTALLATION.getInt(), 2)
        assertEquals(GraphType.JobType.MAINTENANCE.getInt(), 3)
        assertEquals(GraphType.JobType.ALL.getInt(), 4)
    }

}