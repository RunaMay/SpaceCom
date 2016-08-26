package de.commercetools.javacodingtask.client;

import io.sphere.sdk.models.Base;

/**
 * Creates a client.
 */
public final class ClientFactory extends Base {
	
	/** The client. */
	private static ClientFactory client;
	
    /**
     * Instantiates a new client factory.
     */
    private ClientFactory() {
        //only static factory method should be used
    }
    
    /**
     * Gets the single instance of ClientFactory.
     *
     * @return single instance of ClientFactory
     */
    public static ClientFactory getInstance(){
    	
    	if (client==null){
    		client = new ClientFactory();
    	}
        	
    	return client;
    }

    /**
     * Creates a client which uses applicantEmail as user agent.
     * @param applicantEmail the email of YOU
     * @return a new client
     */
    public Client create(final String applicantEmail) {
        return new ClientImpl(applicantEmail);
    }
}
