package de.commercetools.javacodingtask.client;

import io.sphere.sdk.models.Base;

/**
 * The result of the import for a single entity.
 */
public class ImportResult extends Base {
    
    /** The id. */
    private String id;
    
    /** The error code. */
    private String errorCode;
    
    /** The success. */
    private boolean success;

    /**
     * Instantiates a new import result.
     */
    public ImportResult() {
    }

    /**
     * Instantiates a new import result.
     *
     * @param id the id of the imported object
     * @param success true if import is done correctly
     * @param errorCode error code, nullable
     */
    public ImportResult(final String id, final boolean success, final String errorCode) {
        this.errorCode = errorCode;
        this.id = id;
        this.success = success;
    }

    /**
     * Gets the error code.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the error code.
     *
     * @param errorCode the new error code
     */
    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Checks if is success.
     *
     * @return true, if is success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the success.
     *
     * @param success the new success
     */
    public void setSuccess(final boolean success) {
        this.success = success;
    }
}
