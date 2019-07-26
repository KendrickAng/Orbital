package com.untitled.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP REST Request
 *
 * Able to make http requests with the internet.
 */
public abstract class HttpRequest {
	private URL url;
	private HashMap<String, String> headers;
	private HttpRequestBuilder httpRequestBuilder;

	private HttpResponseCallback response200;
	private HttpFailedCallback failedCallback;

	/**
	 * @param url base url of the HTTP Request.
	 */
	public HttpRequest(String url) {
		this.url = new URL(url);
		this.headers = new HashMap<>();
		this.httpRequestBuilder = new HttpRequestBuilder();
	}

	/**
	 * Set a key value pair of url parameters. Can set multiple parameters.
	 * Illustration: https://base_url?key1=value1{@literal &}key2=value2
	 *
	 * @param key the key
	 * @param value the value
	 * @return this instance
	 */
	protected HttpRequest setURLParameter(String key, String value) {
		url.setParameter(key, value);
		return this;
	}

	/**
	 * Set a key value pair of header content. Can set multiple headers.
	 *
	 * @param key the key
	 * @param value the value
	 * @return this instance
	 */
	protected HttpRequest setHeader(String key, String value) {
		headers.put(key, value);
		return this;
	}

	/**
	 * @param success Called after recieving a 200 response from the server.
	 * @return this instance
	 */
	protected HttpRequest setResponse200(HttpResponseCallback success) {
		this.response200 = success;
		return this;
	}

	/**
	 * @param failedCallback Called after failing to send a http request to the server.
	 * @return this instance
	 */
	protected HttpRequest setFailedCallback(HttpFailedCallback failedCallback) {
		this.failedCallback = failedCallback;
		return this;
	}

	/**
	 * Child classes must return a {@link Net.HttpRequest} from {@link HttpRequestBuilder}
	 * @param builder the builder
	 * @return a {@link Net.HttpRequest}
	 */
	protected abstract Net.HttpRequest httpRequest(HttpRequestBuilder builder);

	/**
	 * Builds a HttpRequest, then execute the request.
	 */
	public void call() {
		HttpRequestBuilder builder = httpRequestBuilder
				.newRequest()
				.url(url.toString());

		// Add Headers
		for (Map.Entry<String, String> e : headers.entrySet()) {
			builder.header(e.getKey(), e.getValue());
		}

		// Allow subclasses to modify builder
		Net.HttpRequest request = httpRequest(builder);

		// Actual Http Request
		Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
			@Override
			public void handleHttpResponse(Net.HttpResponse httpResponse) {
				int status = httpResponse.getStatus().getStatusCode();
				String statusCode = String.valueOf(status);
				String result = httpResponse.getResultAsString();

				switch (status) {
					case HttpStatus.SC_OK:
						if (response200 != null) {
							response200.call(result);
						}
						break;
					case HttpStatus.SC_FORBIDDEN:
						// Incorrect permissions.
						// Either not authenticated, or submitted data is invalid.
						Gdx.app.error(statusCode, result);
						break;
					case HttpStatus.SC_BAD_REQUEST:
						// Submitted data is invalid, or query requires index.
						Gdx.app.error(statusCode, result);
						break;
					default:
						// Unhandled Status
						Gdx.app.log(statusCode, result);
						break;
				}
			}

			@Override
			public void failed(Throwable t) {
				Gdx.app.log("HttpRequest.java", "Failed");
				if (failedCallback != null) {
					failedCallback.call();
				}
			}

			@Override
			public void cancelled() {

			}
		});
	}
}
