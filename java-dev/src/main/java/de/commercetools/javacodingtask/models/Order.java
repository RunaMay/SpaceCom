package de.commercetools.javacodingtask.models;

import io.sphere.sdk.models.Base;

import java.util.Currency;

/**
 * The order of a customer in an online shop.
 */
public class Order extends Base {
    
    /** The customer id. */
    private String customerId;
    
    /** The id. */
    private String id;
    
    /** The pick. */
    private String pick;
    
    /** The currency. */
    private Currency currency;
    
    /** The cent amount. */
    private long centAmount;

  
    
    /**
     * Instantiates a new order.
     */
    public Order(){}

    /**
     * Instantiates a new order.
     *
     * @param customerId the customer id
     * @param id the id
     * @param pick the pick
     * @param currency the currency
     * @param centAmount the cent amount
     */
    public Order(String customerId, String id, String pick, Currency currency, long centAmount) {
        	this.customerId = customerId;
        	this.id = id;
        	this.pick = pick;
        	this.currency = currency;
        	this.centAmount = centAmount;
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
     * Gets the customer id.
     *
     * @return the customer id
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer id.
     *
     * @param personId the new customer id
     */
    public void setCustomerId(final String personId) {
        this.customerId = personId;
    }

    /**
     * Gets the cent amount.
     *
     * @return the cent amount
     */
    public long getCentAmount() {
        return centAmount;
    }

    /**
     * Sets the cent amount.
     *
     * @param centAmount the new cent amount
     */
    public void setCentAmount(final long centAmount) {
        this.centAmount = centAmount;
    }

    /**
     * Gets the currency.
     *
     * @return the currency
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Sets the currency.
     *
     * @param currency the new currency
     */
    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    /**
     * Gets the pick.
     *
     * @return the pick
     */
    public String getPick() {
        return pick;
    }

    /**
     * Sets the pick.
     *
     * @param pick the new pick
     */
    public void setPick(final String pick) {
        this.pick = pick;
    }
}
