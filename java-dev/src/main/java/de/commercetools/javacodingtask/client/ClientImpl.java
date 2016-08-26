package de.commercetools.javacodingtask.client;

import io.sphere.sdk.models.Base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpRequestExecutor;
import org.json.simple.JSONArray;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.regexp.internal.recompile;

import de.commercetools.javacodingtask.errors.ClientException;
import de.commercetools.javacodingtask.errors.ServiceUnavailableException;
import de.commercetools.javacodingtask.models.Customer;
import de.commercetools.javacodingtask.models.Order;
import de.commercetools.javacodingtask.service.Service;

/**
 * The Class ClientImpl.
 */
final class ClientImpl extends Base implements Client {
	
	public static Logger log = Logger.getLogger(ClientImpl.class.getName());

	
	/** The Constant BASE_URL. */
	private static final String BASE_URL = "http://limitless-escarpment-3158.herokuapp.com/";
	
	/** The Constant objectMapper. */
	private static final ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	/** The Constant ORDERS. */
	private static final String ORDERS = "/orders";
	
	/** The Constant CUSTOMERS. */
	private static final String CUSTOMERS = "/customers";

	/** The http client. */
	private final CloseableHttpClient httpClient = HttpClients.createDefault();
	
	/** The applicant email. */
	private final String applicantEmail;

	/**
	 * Instantiates a new client impl.
	 *
	 * @param applicantEmail the applicant email
	 */
	public ClientImpl(final String applicantEmail) {
		this.applicantEmail = applicantEmail;
	}

	/* (non-Javadoc)
	 * @see de.commercetools.javacodingtask.client.Client#importOrders(java.util.List)
	 */
	@Override
	public ImportResults importOrders(final List<Order> orders) {
		return importEntity(orders, ORDERS);
	}

	/* (non-Javadoc)
	 * @see de.commercetools.javacodingtask.client.Client#importCustomer(java.util.List)
	 */
	@Override
	public ImportResults importCustomer(final List<Customer> customers) {
		return importEntity(customers, CUSTOMERS);
	}
	
	/* (non-Javadoc)
	 * @see de.commercetools.javacodingtask.client.Client#importCustomerTimeout(java.util.List)
	 */
	@Override
	public ImportResults importCustomerTimeout(final List<Customer> customers) {
		return importEntityTimeout(customers, CUSTOMERS);
	}

	/**
	 * Import entity timeout.
	 *
	 * @param <T> the generic type
	 * @param elements the elements
	 * @param path the path
	 * @return the import results
	 */
	private <T> ImportResults importEntityTimeout(final List<T> elements,
			final String path) {
		return executeRequestTimeout("POST", BASE_URL + path, toJsonString(elements),
				ImportResults.class);
	}

	/* (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		IOUtils.closeQuietly(httpClient);
	}

	/**
	 * To json string.
	 *
	 * @param <T> the generic type
	 * @param object the object
	 * @return the string
	 */
	private <T> String toJsonString(final T object) {

		try {
			return objectMapper.writer().writeValueAsString(object);
		} catch (final JsonProcessingException e) {

			throw new ClientException(e);
		}
	}

	/**
	 * Import entity.
	 *
	 * @param <T> the generic type
	 * @param elements the elements
	 * @param path the path
	 * @return the import results
	 */
	private <T> ImportResults importEntity(final List<T> elements,
			final String path) {
		return executeRequest("POST", BASE_URL + path, toJsonString(elements),
				ImportResults.class);
	}

	/**
	 * Execute request.
	 *
	 * @param <T> the generic type
	 * @param method the method
	 * @param uri the uri
	 * @param body the body
	 * @param clazz the clazz
	 * @return the t
	 */
	private <T> T executeRequest(final String method, final String uri,
			final String body, final Class<T> clazz) {
		try {
			RequestConfig requestConfig = RequestConfig.custom().build();

			final RequestBuilder requestBuilder = RequestBuilder.create(method)
					.setConfig(requestConfig).setUri(uri)
					.setHeader("User-Agent", applicantEmail);
			if (body != null) {
				requestBuilder.setEntity(new StringEntity(body));
			}

			final HttpUriRequest httpUriRequest = requestBuilder.build();

			return httpClient.execute(httpUriRequest,
					new InternalResponseHandler<T>(clazz));
		} catch (final ServiceUnavailableException e) {
			throw e;
		} catch (final Exception e) {
			throw new ClientException(e);
		}
	}

	/**
	 * The Class InternalResponseHandler.
	 *
	 * @param <T> the generic type
	 */
	private static class InternalResponseHandler<T> implements
			ResponseHandler<T> {
		
		/** The value type. */
		final Class<T> valueType;

		/**
		 * Instantiates a new internal response handler.
		 *
		 * @param valueType the value type
		 */
		public InternalResponseHandler(final Class<T> valueType) {
			this.valueType = valueType;
		}

		/* (non-Javadoc)
		 * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http.HttpResponse)
		 */
		@Override
		public T handleResponse(final HttpResponse httpResponse)
				throws IOException {
			final int statusCode = httpResponse.getStatusLine().getStatusCode();
			T output = null;

			if (statusCode != 200) {
				output = null;
				//log.log(Level.INFO,"response---fail---"+statusCode);

			} else {
				objectMapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
				//log.log(Level.INFO,"------response--------" + statusCode);
				try {
					output = objectMapper.readValue(httpResponse.getEntity()
							.getContent(), valueType);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return output;
		}
	}
	
	/**
	 * Execute request timeout.
	 *
	 * @param <T> the generic type
	 * @param method the method
	 * @param uri the uri
	 * @param body the body
	 * @param clazz the clazz
	 * @return the t
	 */
	private <T> T executeRequestTimeout(final String method, final String uri,
			final String body, final Class<T> clazz) {
		try {
			
			int timeout = 20;
			RequestConfig config = RequestConfig.custom().
			  setConnectTimeout(timeout * 1000).
			  setConnectionRequestTimeout(timeout * 1000).
			  setSocketTimeout(timeout * 1000).build();
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
			 
			HttpPost httpPost  = new HttpPost(uri);
			httpPost.setHeader("User-Agent", applicantEmail);
			if (body != null) {
				httpPost.setEntity(new StringEntity(body));
			}
			return client.execute(httpPost, new InternalResponseHandler<T>(clazz));
		} catch (final ServiceUnavailableException e) {
			throw e;
		} catch (final Exception e) {
			throw new ClientException(e);
		}
	}
}
