package dev.golgolex.golgocloud.common.service;

/*
 * Copyright 2023-2024 golgocloud contributors
 */

/**
 * This enumeration represents the possible life cycle states of a service.
 */
public enum ServiceLifeCycle {
    PREPARED,
    EXECUTED,
    READY,
    TERMINATED
}