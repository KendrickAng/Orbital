package com.untitled.net;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;

/**
 * POST REST HTTP Request
 */
public class PostRequest extends HttpRequest {
	private String body;

	public PostRequest(String url) {
		super(url);
		this.body = "";
	}

	@Override
	protected Net.HttpRequest httpRequest(HttpRequestBuilder builder) {
		return builder.method(Net.HttpMethods.POST)
				.content(body)
				.build();
	}

	@Override
	public PostRequest setHeader(String key, String value) {
		super.setHeader(key, value);
		return this;
	}

	@Override
	public PostRequest setURLParameter(String key, String value) {
		super.setURLParameter(key, value);
		return this;
	}

	@Override
	public PostRequest setResponse200(HttpResponseCallback success) {
		super.setResponse200(success);
		return this;
	}

	@Override
	public PostRequest setFailedCallback(HttpFailedCallback failedCallback) {
		super.setFailedCallback(failedCallback);
		return this;
	}

	/**
	 * Set a POST request body.
	 * Set the header to reflect the correct Content-Type.
	 * i.e. .setHeader("Content-Type", "application/json")
	 *
	 * @param body the body content
	 * @return this instance
	 */
	public PostRequest setBody(String body) {
		this.body = body;
		return this;
	}
}
