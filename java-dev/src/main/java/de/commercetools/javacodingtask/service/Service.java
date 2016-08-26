package de.commercetools.javacodingtask.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import de.commercetools.javacodingtask.client.Client;
import de.commercetools.javacodingtask.client.ClientFactory;
import de.commercetools.javacodingtask.client.ImportResults;
import de.commercetools.javacodingtask.models.Customer;
import de.commercetools.javacodingtask.models.Order;

/**
 * The Class Application main class.
 */
@SpringBootApplication
public class Service {

	/** The log. */
	public static Logger log = Logger.getLogger(Service.class.getName());

	/** The unique customers list. */
	public static ArrayList<Customer> uniqueCustomersList = new ArrayList<Customer>();

	/** The unique customers pack. */
	public static ArrayList<Customer> uniqueCustomersPack = new ArrayList<Customer>();

	/** The orders. */
	public static ArrayList<Order> orders = new ArrayList<Order>();

	/** The map. */
	public static Map<String, Customer> map = new HashMap<String, Customer>();

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(Service.class, args);
		doService();

	}

	/**
	 * Do service reading csv file.
	 */
	public static void doService() {
		Client client = ClientFactory.getInstance().create(
				"julianna.bondarchuk@gmail.com");

		String fileToParse = "C:\\Users\\Nazar\\Downloads\\data.csv";
		BufferedReader fileReader = null;

		try {
			String line = "";
			fileReader = new BufferedReader(new FileReader(fileToParse));
			int i = 0;
			boolean a = true;
			int packege = 0;
			int k = 0;

			while (fileReader != null) {
				while ((line = fileReader.readLine()) != null) {
					String[] tokens = line.split(",");
					if (tokens.length > 2) {
						if (!tokens[0].equalsIgnoreCase("seq")) {
							parse(tokens, packege);

							if (k - 1 == 1024) {
								k = 0;
								doImportOrders(client);
								packege++;
							}
						}
					}
					k++;

				}
				doImportOrders(client);
				break;
			}
			log.log(Level.INFO, "---------imposrt Customers------START----");
			doImportCustomers(client);
			log.log(Level.INFO, "---------imposrt Customers------END----");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Parses the line and create Order object and Customer Object. Fills
	 * customers and orders lists
	 *
	 * @param tokens
	 *            the tokens
	 * @param packege
	 *            the packege
	 */
	public static void parse(String[] tokens, int packege) {

		String id = tokens[1] + "_" + tokens[2];
		Customer c = new Customer(id, tokens[1], tokens[2],
				Integer.parseInt(tokens[3].replaceAll("\\D+", "")), tokens[4],
				tokens[5], tokens[6], tokens[7]);
		if (!map.containsKey(id)) {
			map.put(id, c);
		}
		String price = tokens[8].replaceAll("\\D+", "");
		Order o = new Order(id + "_" + packege, tokens[0], tokens[9],
				Currency.getInstance(Locale.US), Long.parseLong(price));
		orders.add(o);

	}

	/**
	 * Do import customers method to get 1000 customers (unique customers).
	 *
	 * @param client
	 *            the client
	 */
	public static void doImportCustomers(Client client) {

		int k = 0;
		int packegeC = 0;
		int sizePackege = 512;
		ArrayList<Customer> uniqueCustomers = new ArrayList<Customer>(
				map.values());
		uniqueCustomersList.addAll(uniqueCustomers);

		for (Customer customer : uniqueCustomersList) {
			uniqueCustomersPack.add(customer);
			if (k - 1 == sizePackege) {
				k = 0;
				doImportUniqueCustomers(client);
				packegeC++;

			}
			k++;
		}
		doImportUniqueCustomers(client);

	}

	/**
	 * Do import unique customers to post pack of unique 1000 customers
	 *
	 * @param client
	 *            the client
	 */
	public static boolean doImportUniqueCustomers(Client client) {
		int i = 0;
		boolean result = false;
		ImportResults res = null;
		res = client.importCustomer(uniqueCustomersPack);

		while (res == null) {
			res = client.importCustomer(uniqueCustomersPack);
			i++;
			if (i == 5) {
				i = 0;
				try {
					delayTimer();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		if (res != null) {
			uniqueCustomersPack.clear();
			i = 0;
			result = true;
		}

		return result;
	}

	/**
	 * Do import orders to post pack of 1000 orders
	 *
	 * @param client
	 *            the client
	 */
	public static void doImportOrders(Client client) {

		int i = 0;
		ImportResults res = null;

		while (res == null) {
			res = client.importOrders(orders);
			i++;

			if (res != null) {
				orders.clear();
				i = 0;
			}
			if (i == 5) {
				i = 0;
				try {
					delayTimer();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void delayTimer() throws InterruptedException {
		TimeUnit.SECONDS.sleep(10);
	}

}
