package com.mygdx.game.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;

public abstract class HttpRequest {
	private URL url;
	private HttpResponseCallback success;
	private HttpRequestBuilder httpRequestBuilder;

	public HttpRequest(String url) {
		this.url = new URL(url);
		this.httpRequestBuilder = new HttpRequestBuilder();
	}

	protected HttpRequest setURLParameter(String key, String value) {
		url.setParameter(key, value);
		return this;
	}

	protected HttpRequest setSuccessCallback(HttpResponseCallback success) {
		this.success = success;
		return this;
	}

	protected abstract Net.HttpRequest httpRequest(HttpRequestBuilder builder);

	public void call() {
		Net.HttpRequest request = httpRequest(httpRequestBuilder
				.newRequest()
				.url(url.toString()));

		Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
			@Override
			public void handleHttpResponse(Net.HttpResponse httpResponse) {
				if (success != null) {
					success.call(httpResponse.getResultAsString());
				}
			}

			@Override
			public void failed(Throwable t) {

			}

			@Override
			public void cancelled() {

			}
		});
	}
}
