package com.untitled.net;

/**
 * A Consumer.
 */
public interface HttpResponseCallback {
	/**
	 * @param response http request response from the server.
	 */
	void call(String response);
}
