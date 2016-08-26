package de.commercetools.javacodingtask.client;

import io.sphere.sdk.models.Base;

import java.util.List;

/**
 * The result of an import for multiple entities.
 */
public final class ImportResults extends Base {
    
    /** The results. */
    private List<ImportResult> results;

    /**
     * Instantiates a new import results.
     */
    public ImportResults() {
    }

    /**
     * Instantiates a new import results.
     *
     * @param results the results
     */
    public ImportResults(final List<ImportResult> results) {
        this.results = results;
    }

    /**
     * Gets the results.
     *
     * @return the results
     */
    public List<ImportResult> getResults() {
        return results;
    }

    /**
     * Sets the results.
     *
     * @param results the new results
     */
    public void setResults(final List<ImportResult> results) {
        this.results = results;
    }
}
