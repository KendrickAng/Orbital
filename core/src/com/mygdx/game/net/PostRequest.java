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

	public void setBody(String body) {
		this.body = body;
	}
}
