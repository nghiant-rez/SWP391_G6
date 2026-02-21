package com.swp391.group6.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task Model Tests")
class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @Test
    @DisplayName("Default constructor sets default values")
    void testDefaultConstructor() {
        assertNotNull(task);
        assertEquals(Task.TYPE_OTHER, task.getTaskType());
        assertEquals(Task.STATUS_TODO, task.getStatus());
        assertEquals(Task.PRIORITY_MEDIUM, task.getPriority());
    }

    @Test
    @DisplayName("Task type constants are defined correctly")
    void testTaskTypeConstants() {
        assertEquals("FOLLOW_UP", Task.TYPE_FOLLOW_UP);
        assertEquals("SITE_VISIT", Task.TYPE_SITE_VISIT);
        assertEquals("DELIVERY", Task.TYPE_DELIVERY);
        assertEquals("INSTALLATION", Task.TYPE_INSTALLATION);
        assertEquals("MAINTENANCE", Task.TYPE_MAINTENANCE);
        assertEquals("OTHER", Task.TYPE_OTHER);
    }

    @Test
    @DisplayName("Status constants are defined correctly")
    void testStatusConstants() {
        assertEquals("TODO", Task.STATUS_TODO);
        assertEquals("IN_PROGRESS", Task.STATUS_IN_PROGRESS);
        assertEquals("DONE", Task.STATUS_DONE);
        assertEquals("CANCELLED", Task.STATUS_CANCELLED);
    }

    @Test
    @DisplayName("Priority constants are defined correctly")
    void testPriorityConstants() {
        assertEquals("LOW", Task.PRIORITY_LOW);
        assertEquals("MEDIUM", Task.PRIORITY_MEDIUM);
        assertEquals("HIGH", Task.PRIORITY_HIGH);
        assertEquals("URGENT", Task.PRIORITY_URGENT);
    }

    @Test
    @DisplayName("TASK_TYPES array contains all types")
    void testTaskTypesArray() {
        String[] types = Task.TASK_TYPES;
        assertEquals(6, types.length);
        assertArrayEquals(new String[]{
            Task.TYPE_FOLLOW_UP, Task.TYPE_SITE_VISIT, Task.TYPE_DELIVERY,
            Task.TYPE_INSTALLATION, Task.TYPE_MAINTENANCE, Task.TYPE_OTHER
        }, types);
    }

    @Test
    @DisplayName("STATUSES array contains all statuses")
    void testStatusesArray() {
        String[] statuses = Task.STATUSES;
        assertEquals(4, statuses.length);
        assertArrayEquals(new String[]{
            Task.STATUS_TODO, Task.STATUS_IN_PROGRESS,
            Task.STATUS_DONE, Task.STATUS_CANCELLED
        }, statuses);
    }

    @Test
    @DisplayName("PRIORITIES array contains all priorities")
    void testPrioritiesArray() {
        String[] priorities = Task.PRIORITIES;
        assertEquals(4, priorities.length);
        assertArrayEquals(new String[]{
            Task.PRIORITY_LOW, Task.PRIORITY_MEDIUM,
            Task.PRIORITY_HIGH, Task.PRIORITY_URGENT
        }, priorities);
    }

    @Test
    @DisplayName("Setters and getters work correctly")
    void testSettersAndGetters() {
        task.setId(1);
        task.setTitle("Complete installation");
        task.setDescription("Install equipment at customer site");
        task.setAssignerId(10);
        task.setAssigneeId(20);
        task.setRelatedContractId(5);
        task.setRelatedCustomerId(15);
        task.setTaskType(Task.TYPE_INSTALLATION);
        task.setStatus(Task.STATUS_IN_PROGRESS);
        task.setPriority(Task.PRIORITY_HIGH);
        task.setCompletionNotes("Completed successfully");
        task.setDeleted(false);

        assertEquals(1, task.getId());
        assertEquals("Complete installation", task.getTitle());
        assertEquals("Install equipment at customer site", task.getDescription());
        assertEquals(10, task.getAssignerId());
        assertEquals(20, task.getAssigneeId());
        assertEquals(5, task.getRelatedContractId());
        assertEquals(15, task.getRelatedCustomerId());
        assertEquals(Task.TYPE_INSTALLATION, task.getTaskType());
        assertEquals(Task.STATUS_IN_PROGRESS, task.getStatus());
        assertEquals(Task.PRIORITY_HIGH, task.getPriority());
        assertEquals("Completed successfully", task.getCompletionNotes());
        assertFalse(task.isDeleted());
    }

    @Test
    @DisplayName("Joined fields work correctly")
    void testJoinedFields() {
        task.setAssignerName("Manager John");
        task.setAssigneeName("Staff Jane");

        assertEquals("Manager John", task.getAssignerName());
        assertEquals("Staff Jane", task.getAssigneeName());
    }

    @Test
    @DisplayName("DateTime fields work correctly")
    void testDateTimeFields() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime future = now.plusDays(7);
        LocalDateTime completed = now.plusDays(5);

        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        task.setDueDate(future);
        task.setCompletedAt(completed);

        assertEquals(now, task.getCreatedAt());
        assertEquals(now, task.getUpdatedAt());
        assertEquals(future, task.getDueDate());
        assertEquals(completed, task.getCompletedAt());
    }

    @Test
    @DisplayName("IsOverdue returns false when no due date")
    void testIsOverdueNoDueDate() {
        task.setDueDate(null);
        task.setStatus(Task.STATUS_TODO);

        assertFalse(task.isOverdue());
    }

    @Test
    @DisplayName("IsOverdue returns false for completed tasks")
    void testIsOverdueCompletedTask() {
        task.setDueDate(LocalDateTime.now().minusDays(1));
        task.setStatus(Task.STATUS_DONE);

        assertFalse(task.isOverdue());
    }

    @Test
    @DisplayName("IsOverdue returns false for cancelled tasks")
    void testIsOverdueCancelledTask() {
        task.setDueDate(LocalDateTime.now().minusDays(1));
        task.setStatus(Task.STATUS_CANCELLED);

        assertFalse(task.isOverdue());
    }

    @Test
    @DisplayName("IsOverdue returns true for past due incomplete tasks")
    void testIsOverduePastDue() {
        task.setDueDate(LocalDateTime.now().minusDays(1));
        task.setStatus(Task.STATUS_IN_PROGRESS);

        assertTrue(task.isOverdue());
    }

    @Test
    @DisplayName("IsOverdue returns false for future due tasks")
    void testIsOverdueFutureDue() {
        task.setDueDate(LocalDateTime.now().plusDays(1));
        task.setStatus(Task.STATUS_TODO);

        assertFalse(task.isOverdue());
    }

    @Test
    @DisplayName("IsValidStaffTransition validates TODO to IN_PROGRESS")
    void testValidStaffTransitionTodoToInProgress() {
        assertTrue(Task.isValidStaffTransition(
            Task.STATUS_TODO, Task.STATUS_IN_PROGRESS));
    }

    @Test
    @DisplayName("IsValidStaffTransition validates IN_PROGRESS to DONE")
    void testValidStaffTransitionInProgressToDone() {
        assertTrue(Task.isValidStaffTransition(
            Task.STATUS_IN_PROGRESS, Task.STATUS_DONE));
    }

    @Test
    @DisplayName("IsValidStaffTransition allows same status")
    void testValidStaffTransitionSameStatus() {
        assertTrue(Task.isValidStaffTransition(
            Task.STATUS_TODO, Task.STATUS_TODO));
    }

    @Test
    @DisplayName("IsValidStaffTransition rejects TODO to DONE")
    void testInvalidStaffTransitionTodoToDone() {
        assertFalse(Task.isValidStaffTransition(
            Task.STATUS_TODO, Task.STATUS_DONE));
    }

    @Test
    @DisplayName("IsValidStaffTransition rejects DONE to IN_PROGRESS")
    void testInvalidStaffTransitionDoneToInProgress() {
        assertFalse(Task.isValidStaffTransition(
            Task.STATUS_DONE, Task.STATUS_IN_PROGRESS));
    }

    @Test
    @DisplayName("IsValidStaffTransition rejects CANCELLED transitions")
    void testInvalidStaffTransitionCancelled() {
        assertFalse(Task.isValidStaffTransition(
            Task.STATUS_CANCELLED, Task.STATUS_TODO));
    }

    @Test
    @DisplayName("ToString includes key fields")
    void testToString() {
        task.setId(42);
        task.setTitle("Test Task");
        task.setAssigneeId(100);
        task.setStatus(Task.STATUS_TODO);
        task.setPriority(Task.PRIORITY_URGENT);

        String toString = task.toString();

        assertTrue(toString.contains("id=42"));
        assertTrue(toString.contains("title='Test Task'"));
        assertTrue(toString.contains("assigneeId=100"));
        assertTrue(toString.contains("status='TODO'"));
        assertTrue(toString.contains("priority='URGENT'"));
    }

    @Test
    @DisplayName("Nullable Integer fields accept null")
    void testNullableIntegerFields() {
        task.setRelatedContractId(null);
        task.setRelatedCustomerId(null);

        assertNull(task.getRelatedContractId());
        assertNull(task.getRelatedCustomerId());
    }

    @Test
    @DisplayName("Task type display returns Vietnamese text")
    void testTaskTypeDisplay() {
        task.setTaskType(Task.TYPE_FOLLOW_UP);
        assertEquals("Theo doi", task.getTaskTypeDisplay());

        task.setTaskType(Task.TYPE_SITE_VISIT);
        assertEquals("Kham sat", task.getTaskTypeDisplay());

        task.setTaskType(Task.TYPE_DELIVERY);
        assertEquals("Giao hang", task.getTaskTypeDisplay());

        task.setTaskType(Task.TYPE_INSTALLATION);
        assertEquals("Lap dat", task.getTaskTypeDisplay());

        task.setTaskType(Task.TYPE_MAINTENANCE);
        assertEquals("Bao tri", task.getTaskTypeDisplay());

        task.setTaskType(Task.TYPE_OTHER);
        assertEquals("Khac", task.getTaskTypeDisplay());
    }

    @Test
    @DisplayName("Status display returns Vietnamese text")
    void testStatusDisplay() {
        task.setStatus(Task.STATUS_TODO);
        assertEquals("Chua lam", task.getStatusDisplay());

        task.setStatus(Task.STATUS_IN_PROGRESS);
        assertEquals("Dang thuc hien", task.getStatusDisplay());

        task.setStatus(Task.STATUS_DONE);
        assertEquals("Hoan thanh", task.getStatusDisplay());

        task.setStatus(Task.STATUS_CANCELLED);
        assertEquals("Da huy", task.getStatusDisplay());
    }

    @Test
    @DisplayName("Priority display returns Vietnamese text")
    void testPriorityDisplay() {
        task.setPriority(Task.PRIORITY_LOW);
        assertEquals("Thap", task.getPriorityDisplay());

        task.setPriority(Task.PRIORITY_MEDIUM);
        assertEquals("Trung binh", task.getPriorityDisplay());

        task.setPriority(Task.PRIORITY_HIGH);
        assertEquals("Cao", task.getPriorityDisplay());

        task.setPriority(Task.PRIORITY_URGENT);
        assertEquals("Khan cap", task.getPriorityDisplay());
    }

    @Test
    @DisplayName("Fully populated task object")
    void testFullyPopulatedTask() {
        LocalDateTime now = LocalDateTime.now();

        task.setId(99);
        task.setTitle("Emergency Repair");
        task.setDescription("Urgent equipment repair needed");
        task.setAssignerId(5);
        task.setAssigneeId(10);
        task.setRelatedContractId(20);
        task.setRelatedCustomerId(30);
        task.setTaskType(Task.TYPE_MAINTENANCE);
        task.setStatus(Task.STATUS_DONE);
        task.setPriority(Task.PRIORITY_URGENT);
        task.setDueDate(now.plusDays(1));
        task.setCompletedAt(now);
        task.setCompletionNotes("Repair completed successfully");
        task.setDeleted(false);
        task.setCreatedAt(now.minusDays(2));
        task.setUpdatedAt(now);
        task.setAssignerName("Manager Alice");
        task.setAssigneeName("Tech Bob");

        assertEquals(99, task.getId());
        assertEquals("Emergency Repair", task.getTitle());
        assertNotNull(task.getDescription());
        assertEquals(5, task.getAssignerId());
        assertEquals(10, task.getAssigneeId());
        assertEquals(20, task.getRelatedContractId());
        assertEquals(30, task.getRelatedCustomerId());
        assertEquals(Task.TYPE_MAINTENANCE, task.getTaskType());
        assertEquals(Task.STATUS_DONE, task.getStatus());
        assertEquals(Task.PRIORITY_URGENT, task.getPriority());
        assertNotNull(task.getDueDate());
        assertNotNull(task.getCompletedAt());
        assertNotNull(task.getCompletionNotes());
        assertFalse(task.isDeleted());
        assertNotNull(task.getCreatedAt());
        assertNotNull(task.getUpdatedAt());
        assertEquals("Manager Alice", task.getAssignerName());
        assertEquals("Tech Bob", task.getAssigneeName());
    }

    @Test
    @DisplayName("Task without related contract and customer")
    void testTaskWithoutRelatedEntities() {
        task.setId(50);
        task.setTitle("General Task");
        task.setRelatedContractId(null);
        task.setRelatedCustomerId(null);

        assertNull(task.getRelatedContractId());
        assertNull(task.getRelatedCustomerId());
    }

    @Test
    @DisplayName("Completion notes for incomplete tasks")
    void testCompletionNotesIncompleteTask() {
        task.setStatus(Task.STATUS_TODO);
        task.setCompletionNotes(null);

        assertNull(task.getCompletionNotes());
    }
}