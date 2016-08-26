package de.commercetools.javacodingtask.client;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import de.commercetools.javacodingtask.models.Customer;
import de.commercetools.javacodingtask.models.Order;

public class ClientTest implements Client {

	@Test
	public void testImportCustomer() throws Exception {

	}

	@Test
	public void testImportOrders() throws Exception {

	}

	@Override
	public void close() throws IOException {

		
	}

	@Override
	public ImportResults importCustomer(List<Customer> customers) {

		return null;
	}

	@Override
	public ImportResults importOrders(List<Order> orders) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImportResults importCustomerTimeout(List<Customer> customers) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
