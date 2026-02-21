package com.swp391.group6.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ServiceRequest Model Tests")
class ServiceRequestTest {

    private ServiceRequest serviceRequest;

    @BeforeEach
    void setUp() {
        serviceRequest = new ServiceRequest();
    }

    @Test
    @DisplayName("Default constructor sets default values")
    void testDefaultConstructor() {
        assertNotNull(serviceRequest);
        assertEquals(ServiceRequest.TYPE_INQUIRY, serviceRequest.getRequestType());
        assertEquals(ServiceRequest.STATUS_OPEN, serviceRequest.getStatus());
        assertEquals(ServiceRequest.PRIORITY_MEDIUM, serviceRequest.getPriority());
    }

    @Test
    @DisplayName("Request type constants are defined correctly")
    void testRequestTypeConstants() {
        assertEquals("REPAIR", ServiceRequest.TYPE_REPAIR);
        assertEquals("MAINTENANCE", ServiceRequest.TYPE_MAINTENANCE);
        assertEquals("COMPLAINT", ServiceRequest.TYPE_COMPLAINT);
        assertEquals("INQUIRY", ServiceRequest.TYPE_INQUIRY);
        assertEquals("WARRANTY", ServiceRequest.TYPE_WARRANTY);
        assertEquals("OTHER", ServiceRequest.TYPE_OTHER);
    }

    @Test
    @DisplayName("Status constants are defined correctly")
    void testStatusConstants() {
        assertEquals("OPEN", ServiceRequest.STATUS_OPEN);
        assertEquals("IN_PROGRESS", ServiceRequest.STATUS_IN_PROGRESS);
        assertEquals("RESOLVED", ServiceRequest.STATUS_RESOLVED);
        assertEquals("CLOSED", ServiceRequest.STATUS_CLOSED);
    }

    @Test
    @DisplayName("Priority constants are defined correctly")
    void testPriorityConstants() {
        assertEquals("LOW", ServiceRequest.PRIORITY_LOW);
        assertEquals("MEDIUM", ServiceRequest.PRIORITY_MEDIUM);
        assertEquals("HIGH", ServiceRequest.PRIORITY_HIGH);
        assertEquals("URGENT", ServiceRequest.PRIORITY_URGENT);
    }

    @Test
    @DisplayName("REQUEST_TYPES array contains all types")
    void testRequestTypesArray() {
        String[] types = ServiceRequest.REQUEST_TYPES;
        assertEquals(6, types.length);
        assertArrayEquals(new String[]{
            ServiceRequest.TYPE_REPAIR,
            ServiceRequest.TYPE_MAINTENANCE,
            ServiceRequest.TYPE_COMPLAINT,
            ServiceRequest.TYPE_INQUIRY,
            ServiceRequest.TYPE_WARRANTY,
            ServiceRequest.TYPE_OTHER
        }, types);
    }

    @Test
    @DisplayName("STATUSES array contains all statuses")
    void testStatusesArray() {
        String[] statuses = ServiceRequest.STATUSES;
        assertEquals(4, statuses.length);
        assertArrayEquals(new String[]{
            ServiceRequest.STATUS_OPEN,
            ServiceRequest.STATUS_IN_PROGRESS,
            ServiceRequest.STATUS_RESOLVED,
            ServiceRequest.STATUS_CLOSED
        }, statuses);
    }

    @Test
    @DisplayName("PRIORITIES array contains all priorities")
    void testPrioritiesArray() {
        String[] priorities = ServiceRequest.PRIORITIES;
        assertEquals(4, priorities.length);
        assertArrayEquals(new String[]{
            ServiceRequest.PRIORITY_LOW,
            ServiceRequest.PRIORITY_MEDIUM,
            ServiceRequest.PRIORITY_HIGH,
            ServiceRequest.PRIORITY_URGENT
        }, priorities);
    }

    @Test
    @DisplayName("Setters and getters work correctly")
    void testSettersAndGetters() {
        serviceRequest.setId(1);
        serviceRequest.setRequestCode("SR-0001");
        serviceRequest.setCustomerId(10);
        serviceRequest.setContractId(5);
        serviceRequest.setDeviceId(7);
        serviceRequest.setRequestType(ServiceRequest.TYPE_REPAIR);
        serviceRequest.setSubject("Device malfunction");
        serviceRequest.setDescription("The device is not working properly");
        serviceRequest.setPriority(ServiceRequest.PRIORITY_HIGH);
        serviceRequest.setStatus(ServiceRequest.STATUS_IN_PROGRESS);
        serviceRequest.setAssignedTo(3);
        serviceRequest.setResolution("Fixed the issue");
        serviceRequest.setDeleted(false);

        assertEquals(1, serviceRequest.getId());
        assertEquals("SR-0001", serviceRequest.getRequestCode());
        assertEquals(10, serviceRequest.getCustomerId());
        assertEquals(5, serviceRequest.getContractId());
        assertEquals(7, serviceRequest.getDeviceId());
        assertEquals(ServiceRequest.TYPE_REPAIR, serviceRequest.getRequestType());
        assertEquals("Device malfunction", serviceRequest.getSubject());
        assertEquals("The device is not working properly", serviceRequest.getDescription());
        assertEquals(ServiceRequest.PRIORITY_HIGH, serviceRequest.getPriority());
        assertEquals(ServiceRequest.STATUS_IN_PROGRESS, serviceRequest.getStatus());
        assertEquals(3, serviceRequest.getAssignedTo());
        assertEquals("Fixed the issue", serviceRequest.getResolution());
        assertFalse(serviceRequest.isDeleted());
    }

    @Test
    @DisplayName("Joined fields work correctly")
    void testJoinedFields() {
        serviceRequest.setCustomerName("John Doe");
        serviceRequest.setAssignedToName("Jane Smith");
        serviceRequest.setDeviceSerialNumber("DEV-12345");

        assertEquals("John Doe", serviceRequest.getCustomerName());
        assertEquals("Jane Smith", serviceRequest.getAssignedToName());
        assertEquals("DEV-12345", serviceRequest.getDeviceSerialNumber());
    }

    @Test
    @DisplayName("DateTime fields work correctly")
    void testDateTimeFields() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime resolved = now.plusDays(1);

        serviceRequest.setCreatedAt(now);
        serviceRequest.setUpdatedAt(now);
        serviceRequest.setResolvedAt(resolved);

        assertEquals(now, serviceRequest.getCreatedAt());
        assertEquals(now, serviceRequest.getUpdatedAt());
        assertEquals(resolved, serviceRequest.getResolvedAt());
    }

    @Test
    @DisplayName("Formatted date methods return correct format")
    void testFormattedDateMethods() {
        LocalDateTime testDate = LocalDateTime.of(2024, 3, 15, 14, 30);

        serviceRequest.setCreatedAt(testDate);
        serviceRequest.setUpdatedAt(testDate);
        serviceRequest.setResolvedAt(testDate);

        assertEquals("15/03/2024 14:30", serviceRequest.getCreatedAtFormatted());
        assertEquals("15/03/2024 14:30", serviceRequest.getUpdatedAtFormatted());
        assertEquals("15/03/2024 14:30", serviceRequest.getResolvedAtFormatted());
    }

    @Test
    @DisplayName("Formatted date methods return null for null dates")
    void testFormattedDateMethodsWithNull() {
        serviceRequest.setCreatedAt(null);
        serviceRequest.setUpdatedAt(null);
        serviceRequest.setResolvedAt(null);

        assertNull(serviceRequest.getCreatedAtFormatted());
        assertNull(serviceRequest.getUpdatedAtFormatted());
        assertNull(serviceRequest.getResolvedAtFormatted());
    }

    @Test
    @DisplayName("IsValidTransition validates OPEN to IN_PROGRESS")
    void testValidTransitionOpenToInProgress() {
        assertTrue(ServiceRequest.isValidTransition(
            ServiceRequest.STATUS_OPEN,
            ServiceRequest.STATUS_IN_PROGRESS
        ));
    }

    @Test
    @DisplayName("IsValidTransition validates IN_PROGRESS to RESOLVED")
    void testValidTransitionInProgressToResolved() {
        assertTrue(ServiceRequest.isValidTransition(
            ServiceRequest.STATUS_IN_PROGRESS,
            ServiceRequest.STATUS_RESOLVED
        ));
    }

    @Test
    @DisplayName("IsValidTransition validates RESOLVED to CLOSED")
    void testValidTransitionResolvedToClosed() {
        assertTrue(ServiceRequest.isValidTransition(
            ServiceRequest.STATUS_RESOLVED,
            ServiceRequest.STATUS_CLOSED
        ));
    }

    @Test
    @DisplayName("IsValidTransition allows same status")
    void testValidTransitionSameStatus() {
        assertTrue(ServiceRequest.isValidTransition(
            ServiceRequest.STATUS_OPEN,
            ServiceRequest.STATUS_OPEN
        ));
    }

    @Test
    @DisplayName("IsValidTransition rejects invalid OPEN to RESOLVED")
    void testInvalidTransitionOpenToResolved() {
        assertFalse(ServiceRequest.isValidTransition(
            ServiceRequest.STATUS_OPEN,
            ServiceRequest.STATUS_RESOLVED
        ));
    }

    @Test
    @DisplayName("IsValidTransition rejects invalid IN_PROGRESS to CLOSED")
    void testInvalidTransitionInProgressToClosed() {
        assertFalse(ServiceRequest.isValidTransition(
            ServiceRequest.STATUS_IN_PROGRESS,
            ServiceRequest.STATUS_CLOSED
        ));
    }

    @Test
    @DisplayName("IsValidTransition rejects invalid RESOLVED to IN_PROGRESS")
    void testInvalidTransitionResolvedToInProgress() {
        assertFalse(ServiceRequest.isValidTransition(
            ServiceRequest.STATUS_RESOLVED,
            ServiceRequest.STATUS_IN_PROGRESS
        ));
    }

    @Test
    @DisplayName("IsValidTransition rejects backward transition")
    void testInvalidBackwardTransition() {
        assertFalse(ServiceRequest.isValidTransition(
            ServiceRequest.STATUS_CLOSED,
            ServiceRequest.STATUS_OPEN
        ));
    }

    @Test
    @DisplayName("ToString includes key fields")
    void testToString() {
        serviceRequest.setId(42);
        serviceRequest.setRequestCode("SR-0042");
        serviceRequest.setCustomerId(100);
        serviceRequest.setStatus(ServiceRequest.STATUS_OPEN);
        serviceRequest.setPriority(ServiceRequest.PRIORITY_URGENT);

        String toString = serviceRequest.toString();

        assertTrue(toString.contains("id=42"));
        assertTrue(toString.contains("requestCode='SR-0042'"));
        assertTrue(toString.contains("customerId=100"));
        assertTrue(toString.contains("status='OPEN'"));
        assertTrue(toString.contains("priority='URGENT'"));
    }

    @Test
    @DisplayName("Nullable Integer fields accept null")
    void testNullableIntegerFields() {
        serviceRequest.setContractId(null);
        serviceRequest.setDeviceId(null);
        serviceRequest.setAssignedTo(null);

        assertNull(serviceRequest.getContractId());
        assertNull(serviceRequest.getDeviceId());
        assertNull(serviceRequest.getAssignedTo());
    }

    @Test
    @DisplayName("Request type display returns Vietnamese text")
    void testRequestTypeDisplay() {
        serviceRequest.setRequestType(ServiceRequest.TYPE_REPAIR);
        assertEquals("Sua chua", serviceRequest.getRequestTypeDisplay());

        serviceRequest.setRequestType(ServiceRequest.TYPE_MAINTENANCE);
        assertEquals("Bao tri", serviceRequest.getRequestTypeDisplay());

        serviceRequest.setRequestType(ServiceRequest.TYPE_COMPLAINT);
        assertEquals("Khieu nai", serviceRequest.getRequestTypeDisplay());

        serviceRequest.setRequestType(ServiceRequest.TYPE_INQUIRY);
        assertEquals("Yeu cau thong tin", serviceRequest.getRequestTypeDisplay());

        serviceRequest.setRequestType(ServiceRequest.TYPE_WARRANTY);
        assertEquals("Bao hanh", serviceRequest.getRequestTypeDisplay());

        serviceRequest.setRequestType(ServiceRequest.TYPE_OTHER);
        assertEquals("Khac", serviceRequest.getRequestTypeDisplay());
    }

    @Test
    @DisplayName("Status display returns Vietnamese text")
    void testStatusDisplay() {
        serviceRequest.setStatus(ServiceRequest.STATUS_OPEN);
        assertEquals("Mo", serviceRequest.getStatusDisplay());

        serviceRequest.setStatus(ServiceRequest.STATUS_IN_PROGRESS);
        assertEquals("Dang xu ly", serviceRequest.getStatusDisplay());

        serviceRequest.setStatus(ServiceRequest.STATUS_RESOLVED);
        assertEquals("Da giai quyet", serviceRequest.getStatusDisplay());

        serviceRequest.setStatus(ServiceRequest.STATUS_CLOSED);
        assertEquals("Da dong", serviceRequest.getStatusDisplay());
    }

    @Test
    @DisplayName("Priority display returns Vietnamese text")
    void testPriorityDisplay() {
        serviceRequest.setPriority(ServiceRequest.PRIORITY_LOW);
        assertEquals("Thap", serviceRequest.getPriorityDisplay());

        serviceRequest.setPriority(ServiceRequest.PRIORITY_MEDIUM);
        assertEquals("Trung binh", serviceRequest.getPriorityDisplay());

        serviceRequest.setPriority(ServiceRequest.PRIORITY_HIGH);
        assertEquals("Cao", serviceRequest.getPriorityDisplay());

        serviceRequest.setPriority(ServiceRequest.PRIORITY_URGENT);
        assertEquals("Khan cap", serviceRequest.getPriorityDisplay());
    }

    @Test
    @DisplayName("Fully populated service request")
    void testFullyPopulatedServiceRequest() {
        LocalDateTime now = LocalDateTime.now();

        serviceRequest.setId(99);
        serviceRequest.setRequestCode("SR-0099");
        serviceRequest.setCustomerId(50);
        serviceRequest.setContractId(25);
        serviceRequest.setDeviceId(15);
        serviceRequest.setRequestType(ServiceRequest.TYPE_WARRANTY);
        serviceRequest.setSubject("Warranty claim");
        serviceRequest.setDescription("Device stopped working within warranty period");
        serviceRequest.setPriority(ServiceRequest.PRIORITY_URGENT);
        serviceRequest.setStatus(ServiceRequest.STATUS_RESOLVED);
        serviceRequest.setAssignedTo(8);
        serviceRequest.setResolution("Replaced device under warranty");
        serviceRequest.setResolvedAt(now);
        serviceRequest.setDeleted(false);
        serviceRequest.setCreatedAt(now.minusDays(5));
        serviceRequest.setUpdatedAt(now);
        serviceRequest.setCustomerName("Alice Johnson");
        serviceRequest.setAssignedToName("Bob Williams");
        serviceRequest.setDeviceSerialNumber("DEV-99999");

        // Verify all fields
        assertEquals(99, serviceRequest.getId());
        assertEquals("SR-0099", serviceRequest.getRequestCode());
        assertEquals(50, serviceRequest.getCustomerId());
        assertEquals(25, serviceRequest.getContractId());
        assertEquals(15, serviceRequest.getDeviceId());
        assertEquals(ServiceRequest.TYPE_WARRANTY, serviceRequest.getRequestType());
        assertEquals("Warranty claim", serviceRequest.getSubject());
        assertNotNull(serviceRequest.getDescription());
        assertEquals(ServiceRequest.PRIORITY_URGENT, serviceRequest.getPriority());
        assertEquals(ServiceRequest.STATUS_RESOLVED, serviceRequest.getStatus());
        assertEquals(8, serviceRequest.getAssignedTo());
        assertNotNull(serviceRequest.getResolution());
        assertNotNull(serviceRequest.getResolvedAt());
        assertFalse(serviceRequest.isDeleted());
        assertNotNull(serviceRequest.getCreatedAt());
        assertNotNull(serviceRequest.getUpdatedAt());
        assertEquals("Alice Johnson", serviceRequest.getCustomerName());
        assertEquals("Bob Williams", serviceRequest.getAssignedToName());
        assertEquals("DEV-99999", serviceRequest.getDeviceSerialNumber());
    }
}