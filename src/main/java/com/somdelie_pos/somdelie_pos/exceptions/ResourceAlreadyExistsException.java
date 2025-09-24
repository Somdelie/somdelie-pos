package com.somdelie_pos.somdelie_pos.exceptions;

import lombok.Getter;

/**
 * Generic exception thrown when attempting to create or update a resource
 * with a field value that already exists and must be unique.
 *
 * This exception can be used across all entities (Product, Category, Store, User, etc.)
 * for any uniqueness constraint violations.
 */
@Getter
public class ResourceAlreadyExistsException extends Exception {

    /**
     * -- GETTER --
     *  Get the resource type that caused the conflict
     *
     * @return The resource type (e.g., "Product", "Category")
     */
    private final String resourceType;
    /**
     * -- GETTER --
     *  Get the field name that caused the conflict
     *
     * @return The field name (e.g., "sku", "name", "email")
     */
    private final String fieldName;
    /**
     * -- GETTER --
     *  Get the field value that caused the conflict
     *
     * @return The conflicting field value
     */
    private final Object fieldValue;
    /**
     * -- GETTER --
     *  Get the context ID where the conflict occurred (if applicable)
     *
     * @return The context ID or null if no context
     */
    private final Object contextId;
    /**
     * -- GETTER --
     *  Get the context name where the conflict occurred (if applicable)
     *
     * @return The context name or null if no context
     */
    private final String contextName;

    /**
     * Constructor for simple uniqueness violation
     *
     * @param resourceType The type of resource (e.g., "Product", "Category", "User")
     * @param fieldName The field name that must be unique (e.g., "sku", "name", "email")
     * @param fieldValue The value that already exists
     */
    public ResourceAlreadyExistsException(String resourceType, String fieldName, Object fieldValue) {
        super(String.format("%s with %s '%s' already exists", resourceType, fieldName, fieldValue));
        this.resourceType = resourceType;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.contextId = null;
        this.contextName = null;
    }

    /**
     * Constructor for uniqueness violation within a specific context
     *
     * @param resourceType The type of resource (e.g., "Product", "Category")
     * @param fieldName The field name that must be unique (e.g., "sku", "name")
     * @param fieldValue The value that already exists
     * @param contextName The context name (e.g., "store", "company")
     * @param contextId The context identifier
     */
    public ResourceAlreadyExistsException(String resourceType, String fieldName, Object fieldValue,
                                          String contextName, Object contextId) {
        super(String.format("%s with %s '%s' already exists in %s ID: %s",
                resourceType, fieldName, fieldValue, contextName, contextId));
        this.resourceType = resourceType;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.contextId = contextId;
        this.contextName = contextName;
    }

    /**
     * Constructor with custom message
     *
     * @param message Custom error message
     * @param resourceType The type of resource
     * @param fieldName The field name that must be unique
     * @param fieldValue The value that already exists
     */
    public ResourceAlreadyExistsException(String message, String resourceType, String fieldName, Object fieldValue) {
        super(message);
        this.resourceType = resourceType;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.contextId = null;
        this.contextName = null;
    }

    /**
     * Constructor with custom message and context
     *
     * @param message Custom error message
     * @param resourceType The type of resource
     * @param fieldName The field name that must be unique
     * @param fieldValue The value that already exists
     * @param contextName The context name
     * @param contextId The context identifier
     */
    public ResourceAlreadyExistsException(String message, String resourceType, String fieldName,
                                          Object fieldValue, String contextName, Object contextId) {
        super(message);
        this.resourceType = resourceType;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.contextId = contextId;
        this.contextName = contextName;
    }

    /**
     * Constructor with custom message, context and cause
     *
     * @param message Custom error message
     * @param resourceType The type of resource
     * @param fieldName The field name that must be unique
     * @param fieldValue The value that already exists
     * @param contextName The context name
     * @param contextId The context identifier
     * @param cause The underlying cause of the exception
     */
    public ResourceAlreadyExistsException(String message, String resourceType, String fieldName,
                                          Object fieldValue, String contextName, Object contextId, Throwable cause) {
        super(message, cause);
        this.resourceType = resourceType;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.contextId = contextId;
        this.contextName = contextName;
    }

    /**
     * Check if this exception has context information
     *
     * @return true if context information is available, false otherwise
     */
    public boolean hasContext() {
        return contextId != null && contextName != null;
    }
}