package com.colorlaboratory.serviceportalbackend.model.entity.insights;

public enum InsightsMetricType {

    // Issue stats
    TOTAL_ISSUES,
    OPEN_ISSUES,
    IN_PROGRESS_ISSUES,
    RESOLVED_ISSUES,
    CLOSED_ISSUES,

    // User stats
    TOTAL_USERS,
    ADMINS,
    SERVICE_MANAGERS,
    TECHNICIANS,
    CLIENTS,

    // Staff-specific
    STAFF_ASSIGNED,
    STAFF_OPEN,
    STAFF_IN_PROGRESS,
    STAFF_RESOLVED,
    STAFF_CLOSED,

    // Client-specific
    CLIENT_TOTAL,
    CLIENT_OPEN,
    CLIENT_IN_PROGRESS,
    CLIENT_RESOLVED
}

