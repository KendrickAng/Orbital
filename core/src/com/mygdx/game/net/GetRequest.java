package com.mygdx.game.net;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;

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
	public GetRequest setSuccessCallback(HttpResponseCallback success) {
		super.setSuccessCallback(success);
		return this;
	}
}
