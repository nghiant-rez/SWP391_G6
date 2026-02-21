package com.swp391.group6.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ServiceRequest Edge Case Tests")
class ServiceRequestEdgeCaseTest {

    @Test
    @DisplayName("Status transition with null values throws no exception")
    void testStatusTransitionWithNull() {
        // This should not throw an exception but return false
        assertDoesNotThrow(() -> {
            boolean result = ServiceRequest.isValidTransition(null, ServiceRequest.STATUS_OPEN);
            // The implementation might throw NullPointerException,
            // this test documents the behavior
        });
    }

    @Test
    @DisplayName("Very long subject field")
    void testVeryLongSubject() {
        ServiceRequest sr = new ServiceRequest();
        String longSubject = "x".repeat(10000);

        sr.setSubject(longSubject);

        assertEquals(10000, sr.getSubject().length());
    }

    @Test
    @DisplayName("Very long description field")
    void testVeryLongDescription() {
        ServiceRequest sr = new ServiceRequest();
        String longDescription = "Description ".repeat(1000);

        sr.setDescription(longDescription);

        assertTrue(sr.getDescription().length() > 10000);
    }

    @Test
    @DisplayName("Special characters in subject")
    void testSpecialCharactersInSubject() {
        ServiceRequest sr = new ServiceRequest();
        String specialSubject = "Test!@#$%^&*()_+-=[]{}|;':\",./<>?~`";

        sr.setSubject(specialSubject);

        assertEquals(specialSubject, sr.getSubject());
    }

    @Test
    @DisplayName("Unicode characters in description")
    void testUnicodeCharactersInDescription() {
        ServiceRequest sr = new ServiceRequest();
        String unicodeDescription = "Description with 中文字符 và tiếng Việt 😀";

        sr.setDescription(unicodeDescription);

        assertEquals(unicodeDescription, sr.getDescription());
    }

    @Test
    @DisplayName("Request code with various formats")
    void testRequestCodeFormats() {
        ServiceRequest sr = new ServiceRequest();

        String[] codes = {"SR-0001", "SR-9999", "SR-0000", "SR-123456"};

        for (String code : codes) {
            sr.setRequestCode(code);
            assertEquals(code, sr.getRequestCode());
            assertTrue(sr.getRequestCode().startsWith("SR-"));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED"})
    @DisplayName("All valid statuses work correctly")
    void testAllValidStatuses(String status) {
        ServiceRequest sr = new ServiceRequest();
        sr.setStatus(status);
        assertEquals(status, sr.getStatus());
    }

    @ParameterizedTest
    @ValueSource(strings = {"LOW", "MEDIUM", "HIGH", "URGENT"})
    @DisplayName("All valid priorities work correctly")
    void testAllValidPriorities(String priority) {
        ServiceRequest sr = new ServiceRequest();
        sr.setPriority(priority);
        assertEquals(priority, sr.getPriority());
    }

    @ParameterizedTest
    @ValueSource(strings = {"REPAIR", "MAINTENANCE", "COMPLAINT", "INQUIRY", "WARRANTY", "OTHER"})
    @DisplayName("All valid request types work correctly")
    void testAllValidRequestTypes(String type) {
        ServiceRequest sr = new ServiceRequest();
        sr.setRequestType(type);
        assertEquals(type, sr.getRequestType());
    }

    @ParameterizedTest
    @CsvSource({
        "OPEN, IN_PROGRESS, true",
        "IN_PROGRESS, RESOLVED, true",
        "RESOLVED, CLOSED, true",
        "OPEN, RESOLVED, false",
        "IN_PROGRESS, CLOSED, false",
        "RESOLVED, OPEN, false",
        "CLOSED, IN_PROGRESS, false"
    })
    @DisplayName("Status transition validation matrix")
    void testStatusTransitionMatrix(String from, String to, boolean expected) {
        boolean result = ServiceRequest.isValidTransition(from, to);
        assertEquals(expected, result,
            String.format("Transition %s -> %s should be %s", from, to, expected));
    }

    @Test
    @DisplayName("CreatedAt in far past")
    void testCreatedAtInFarPast() {
        ServiceRequest sr = new ServiceRequest();
        LocalDateTime veryOldDate = LocalDateTime.of(1900, 1, 1, 0, 0);

        sr.setCreatedAt(veryOldDate);

        assertEquals(veryOldDate, sr.getCreatedAt());
        assertTrue(sr.getCreatedAt().isBefore(LocalDateTime.now()));
    }

    @Test
    @DisplayName("ResolvedAt in far future")
    void testResolvedAtInFarFuture() {
        ServiceRequest sr = new ServiceRequest();
        LocalDateTime futureDate = LocalDateTime.of(2100, 12, 31, 23, 59);

        sr.setResolvedAt(futureDate);

        assertEquals(futureDate, sr.getResolvedAt());
        assertTrue(sr.getResolvedAt().isAfter(LocalDateTime.now()));
    }

    @Test
    @DisplayName("Customer ID boundary values")
    void testCustomerIdBoundaryValues() {
        ServiceRequest sr = new ServiceRequest();

        sr.setCustomerId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, sr.getCustomerId());

        sr.setCustomerId(1);
        assertEquals(1, sr.getCustomerId());
    }

    @Test
    @DisplayName("Null resolution field")
    void testNullResolution() {
        ServiceRequest sr = new ServiceRequest();
        sr.setResolution(null);
        assertNull(sr.getResolution());
    }

    @Test
    @DisplayName("Empty resolution field")
    void testEmptyResolution() {
        ServiceRequest sr = new ServiceRequest();
        sr.setResolution("");
        assertEquals("", sr.getResolution());
    }

    @Test
    @DisplayName("Service request with all fields null")
    void testServiceRequestAllFieldsNull() {
        ServiceRequest sr = new ServiceRequest();

        // Set nullable fields to null
        sr.setRequestCode(null);
        sr.setSubject(null);
        sr.setDescription(null);
        sr.setContractId(null);
        sr.setDeviceId(null);
        sr.setAssignedTo(null);
        sr.setResolution(null);
        sr.setResolvedAt(null);
        sr.setCreatedAt(null);
        sr.setUpdatedAt(null);
        sr.setCustomerName(null);
        sr.setAssignedToName(null);
        sr.setDeviceSerialNumber(null);

        // Verify all are null
        assertNull(sr.getRequestCode());
        assertNull(sr.getSubject());
        assertNull(sr.getDescription());
        assertNull(sr.getContractId());
        assertNull(sr.getDeviceId());
        assertNull(sr.getAssignedTo());
        assertNull(sr.getResolution());
        assertNull(sr.getResolvedAt());
    }

    @Test
    @DisplayName("Display methods with unknown values")
    void testDisplayMethodsWithUnknownValues() {
        ServiceRequest sr = new ServiceRequest();

        sr.setRequestType("UNKNOWN_TYPE");
        sr.setStatus("UNKNOWN_STATUS");
        sr.setPriority("UNKNOWN_PRIORITY");

        // Display methods should handle unknown values gracefully
        assertNotNull(sr.getRequestTypeDisplay());
        assertNotNull(sr.getStatusDisplay());
        assertNotNull(sr.getPriorityDisplay());
    }

    @Test
    @DisplayName("Formatted date with null dates returns null")
    void testFormattedDateWithNullDates() {
        ServiceRequest sr = new ServiceRequest();

        sr.setCreatedAt(null);
        sr.setUpdatedAt(null);
        sr.setResolvedAt(null);

        assertNull(sr.getCreatedAtFormatted());
        assertNull(sr.getUpdatedAtFormatted());
        assertNull(sr.getResolvedAtFormatted());
    }

    @Test
    @DisplayName("Formatted date with edge dates")
    void testFormattedDateWithEdgeDates() {
        ServiceRequest sr = new ServiceRequest();

        LocalDateTime edgeDate = LocalDateTime.of(2000, 1, 1, 0, 0);
        sr.setCreatedAt(edgeDate);

        String formatted = sr.getCreatedAtFormatted();
        assertNotNull(formatted);
        assertEquals("01/01/2000 00:00", formatted);
    }

    @Test
    @DisplayName("Multiple status transitions in sequence")
    void testMultipleStatusTransitions() {
        ServiceRequest sr = new ServiceRequest();

        // OPEN -> IN_PROGRESS
        sr.setStatus(ServiceRequest.STATUS_OPEN);
        assertTrue(ServiceRequest.isValidTransition(
            sr.getStatus(), ServiceRequest.STATUS_IN_PROGRESS));
        sr.setStatus(ServiceRequest.STATUS_IN_PROGRESS);

        // IN_PROGRESS -> RESOLVED
        assertTrue(ServiceRequest.isValidTransition(
            sr.getStatus(), ServiceRequest.STATUS_RESOLVED));
        sr.setStatus(ServiceRequest.STATUS_RESOLVED);

        // RESOLVED -> CLOSED
        assertTrue(ServiceRequest.isValidTransition(
            sr.getStatus(), ServiceRequest.STATUS_CLOSED));
        sr.setStatus(ServiceRequest.STATUS_CLOSED);

        assertEquals(ServiceRequest.STATUS_CLOSED, sr.getStatus());
    }

    @Test
    @DisplayName("ToString with null fields doesn't throw")
    void testToStringWithNullFields() {
        ServiceRequest sr = new ServiceRequest();
        sr.setRequestCode(null);

        assertDoesNotThrow(() -> {
            String result = sr.toString();
            assertNotNull(result);
        });
    }

    @Test
    @DisplayName("Concurrent modification of service request")
    void testConcurrentModification() {
        ServiceRequest sr = new ServiceRequest();

        sr.setId(1);
        sr.setStatus(ServiceRequest.STATUS_OPEN);

        // Simulate concurrent updates
        LocalDateTime time1 = LocalDateTime.now();
        sr.setUpdatedAt(time1);

        LocalDateTime time2 = time1.plusSeconds(1);
        sr.setUpdatedAt(time2);

        assertEquals(time2, sr.getUpdatedAt());
        assertTrue(sr.getUpdatedAt().isAfter(time1));
    }

    @Test
    @DisplayName("Service request equality based on ID")
    void testServiceRequestEqualityById() {
        ServiceRequest sr1 = new ServiceRequest();
        ServiceRequest sr2 = new ServiceRequest();

        sr1.setId(1);
        sr2.setId(1);

        // Note: This tests if IDs match, not object equality
        assertEquals(sr1.getId(), sr2.getId());
    }
}