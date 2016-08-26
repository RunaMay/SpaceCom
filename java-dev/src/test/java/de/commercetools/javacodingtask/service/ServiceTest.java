package de.commercetools.javacodingtask.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.commercetools.javacodingtask.client.Client;
import de.commercetools.javacodingtask.client.ClientFactory;
import de.commercetools.javacodingtask.client.ImportResults;
import de.commercetools.javacodingtask.models.Customer;
import de.commercetools.javacodingtask.models.Order;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTest {

	public ArrayList<Customer> customersTest = new ArrayList<Customer>();

	public Logger log;

	public boolean result = false;

	public ArrayList<Order> ordersTest = new ArrayList<Order>();

	public ArrayList<Customer> uniqueCustomersListTest = new ArrayList<Customer>();

	public ArrayList<Customer> uniqueCustomersPackTest = new ArrayList<Customer>();
	@InjectMocks
	private Service service;

	Client client = ClientFactory.getInstance().create(
			"julianna.bondarchuk@gmail.com");

	@Test
	public void testMain() throws Exception {

	}

	@Test
	public void testDoService() throws Exception {
		testParse();
		String[] tokens = new String[] { "5", "Aaron", "Williamson", "79",
				"Vonzo Drive", "Fegortow", "ND", "99984", "$223.38", "GREEN" };
		String id = tokens[1] + "_" + tokens[2];

		service.parse(tokens, 0);
		assertEquals(customersTest.get(0), service.map.get(id));
	}

	@Test
	public void testParse() throws Exception {
		String[] tokens = new String[] { "5", "Aaron", "Williamson", "79",
				"Vonzo Drive", "Fegortow", "ND", "99984", "$223.38", "GREEN" };
		String id = tokens[1] + "_" + tokens[2];
		Customer c = new Customer(id, tokens[1], tokens[2],
				Integer.parseInt(tokens[3].replaceAll("\\D+", "")), tokens[4],
				tokens[5], tokens[6], tokens[7]);
		customersTest.add(c);
		String price = tokens[8].replaceAll("\\D+", "");
		Order o = new Order(id, tokens[0], tokens[9],
				Currency.getInstance(Locale.US), Long.parseLong(price));
		ordersTest.add(o);
	}

	@Test
	public void testDoImportCustomers() throws Exception {
		testDoImportUnicCustomers();
		assertEquals(service.doImportUniqueCustomers(client), result);
	}

	@Test
	public void testDoImportUnicCustomers() throws Exception {
		int i = 0;
		ImportResults res = null;

		while (res == null) {
			res = client.importCustomer(customersTest);
			i++;

			if (res != null) {
				customersTest.clear();
				i = 0;
			}
			if (i == 5) {
				i = 0;
				try {
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (res != null) 
			result=true;
	}

	@Test
	public void testDoImportOrders() throws Exception {

	}

}
