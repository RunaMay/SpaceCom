package de.commercetools.javacodingtask.errors;

/**
 * The server responded with status code 503.
 */
public class ServiceUnavailableException extends ClientException {
    
    /**
     * Instantiates a new service unavailable exception.
     *
     * @param s the s
     */
    public ServiceUnavailableException(String s) {
        super("Please try directly again."+s);
    }
}
