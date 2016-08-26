package de.commercetools.javacodingtask.client;

import java.io.Closeable;
import java.util.List;

import de.commercetools.javacodingtask.models.Customer;
import de.commercetools.javacodingtask.models.Order;

/**
 * Client for importing customer and order data.
 */
public interface Client extends Closeable {

    /**
     * Imports a list of customers.
     * @param customers the customers which should be imported
     * @return the result of the import
     */
    ImportResults importCustomer(final List<Customer> customers);

    /**
     * imports a list of orders.
     * @param orders the orders to import
     * @return the result of the import
     */
    ImportResults importOrders(final List<Order> orders);
    

	/**
	 * Import customer timeout.
	 *
	 * @param customers the customers
	 * @return the import results
	 */
	ImportResults importCustomerTimeout(final List<Customer> customers);

}
