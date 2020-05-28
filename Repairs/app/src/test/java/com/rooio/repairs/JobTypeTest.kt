package com.rooio.repairs

import org.junit.Assert.assertEquals
import org.junit.Test

class JobTypeTest {

    @Test
    fun testPendingStatusType() {
        assertEquals(JobType.PENDING.toString(), "Pending")
        assertEquals(JobType.PENDING.getInt(), 0)
    }

    @Test
    fun testDeclinedStatusType() {
        assertEquals(JobType.DECLINED.toString(), "Declined")
        assertEquals(JobType.DECLINED.getInt(), 1)
    }

    @Test
    fun testScheduledStatusType() {
        assertEquals(JobType.SCHEDULED.toString(), "Scheduled")
        assertEquals(JobType.SCHEDULED.getInt(), 2)
    }

    @Test
    fun testCompletedStatusType() {
        assertEquals(JobType.COMPLETED.toString(), "Completed")
        assertEquals(JobType.COMPLETED.getInt(), 3)
    }

    @Test
    fun testCancelledStatusType() {
        assertEquals(JobType.CANCELLED.toString(), "Cancelled")
        assertEquals(JobType.CANCELLED.getInt(), 4)
    }

    @Test
    fun testStartedStatusType() {
        assertEquals(JobType.STARTED.toString(), "Started")
        assertEquals(JobType.STARTED.getInt(), 5)
    }

    @Test
    fun testPausedStatusType() {
        assertEquals(JobType.PAUSED.toString(), "Paused")
        assertEquals(JobType.PAUSED.getInt(), 6)
    }

    @Test
    fun testInProgressStatusType() {
        assertEquals(JobType.IN_PROGRESS.toString(), "In Progress")
        assertEquals(JobType.IN_PROGRESS.getInt(), 7)
    }

}