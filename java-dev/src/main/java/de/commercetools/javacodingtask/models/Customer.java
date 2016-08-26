/*
 * 
 */
package de.commercetools.javacodingtask.models;

import io.sphere.sdk.models.Base;

/**
 * A customer of an online shop.
 */
public final class Customer extends Base {
    
    /** The id. */
    private String id;
    
    /** The first name. */
    private String firstName;
    
    /** The last name. */
    private String lastName;
    
    /** The age. */
    private int age;
    
    /** The street. */
    private String street;
    
    /** The city. */
    private String city;
    
    /** The state. */
    private String state;
    
    /** The zip. */
    private String zip;

    /**
     * Instantiates a new customer.
     */
    public Customer() {
    }
    
    /**
     * Instantiates a new customer.
     *
     * @param id the id
     * @param firstName the first name
     * @param lastName the last name
     * @param age the age
     * @param street the street
     * @param city the city
     * @param state the state
     * @param zip the zip
     */
    public Customer(String id, String firstName, String lastName, int age, String street, String city, 
    		String state, String zip) {
    	this.id = id;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.age = age;
    	this.street = street;
    	this.city = city;
    	this.state = state;
    	this.zip = zip;    	
    }

    /**
     * Gets the age.
     *
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age.
     *
     * @param age the new age
     */
    public void setAge(final int age) {
        this.age = age;
    }

    /**
     * Gets the city.
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city.
     *
     * @param city the new city
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * Gets the first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName the new first name
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
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
     * Gets the last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName the new last name
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state.
     *
     * @param state the new state
     */
    public void setState(final String state) {
        this.state = state;
    }

    /**
     * Gets the street.
     *
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street.
     *
     * @param street the new street
     */
    public void setStreet(final String street) {
        this.street = street;
    }

    /**
     * Gets the zip.
     *
     * @return the zip
     */
    public String getZip() {
        return zip;
    }

    /**
     * Sets the zip.
     *
     * @param zip the new zip
     */
    public void setZip(final String zip) {
        this.zip = zip;
    }
}
