package com.untitled.net;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;

/**
 * GET HTTP REST Request
 */
public class GetRequest extends HttpRequest {
	public GetRequest(String url) {
		super(url);
	}

	@Override
	protected Net.HttpRequest httpRequest(HttpRequestBuilder builder) {
		return builder.method(Net.HttpMethods.GET)
				.build();
	}

	@Override
	public GetRequest setResponse200(HttpResponseCallback success) {
		super.setResponse200(success);
		return this;
	}
}
