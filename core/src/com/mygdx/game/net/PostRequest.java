package com.mygdx.game.net;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;

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
	public PostRequest setSuccessCallback(HttpResponseCallback success) {
		super.setSuccessCallback(success);
		return this;
	}

	public PostRequest setBody(String body) {
		this.body = body;
		return this;
	}
}
