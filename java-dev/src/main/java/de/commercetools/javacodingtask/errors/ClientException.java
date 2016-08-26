package de.commercetools.javacodingtask.errors;

/**
 * Base class for all exceptions {@link de.commercetools.javacodingtask.client.Client} should throw.
 */
public class ClientException extends RuntimeException {

    /**
     * Instantiates a new client exception.
     *
     * @param e the e
     */
    public ClientException(final Exception e) {
        super(e);
    }

    /**
     * Instantiates a new client exception.
     *
     * @param message the message
     */
    public ClientException(final String message) {
        super(message);
    }
}
