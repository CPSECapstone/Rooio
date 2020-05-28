package com.rooio.repairs

import org.junit.Assert.assertEquals
import org.junit.Test

class StatusTypeTest {

    @Test
    fun testPendingStatusType() {
        assertEquals(StatusType.PENDING.toString(), "Pending")
        assertEquals(StatusType.PENDING.getInt(), 0)
    }

    @Test
    fun testDeclinedStatusType() {
        assertEquals(StatusType.DECLINED.toString(), "Declined")
        assertEquals(StatusType.DECLINED.getInt(), 1)
    }

    @Test
    fun testAcceptedStatusType() {
        assertEquals(StatusType.ACCEPTED.toString(), "Accepted")
        assertEquals(StatusType.ACCEPTED.getInt(), 2)
    }

    @Test
    fun testCompletedStatusType() {
        assertEquals(StatusType.COMPLETED.toString(), "Completed")
        assertEquals(StatusType.COMPLETED.getInt(), 3)
    }

    @Test
    fun testCancelledStatusType() {
        assertEquals(StatusType.CANCELLED.toString(), "Cancelled")
        assertEquals(StatusType.CANCELLED.getInt(), 4)
    }

    @Test
    fun testStartedStatusType() {
        assertEquals(StatusType.STARTED.toString(), "Started")
        assertEquals(StatusType.STARTED.getInt(), 5)
    }

    @Test
    fun testPausedStatusType() {
        assertEquals(StatusType.PAUSED.toString(), "Paused")
        assertEquals(StatusType.PAUSED.getInt(), 6)
    }

}