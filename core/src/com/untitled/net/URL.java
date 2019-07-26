package com.untitled.net;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

/**
 * A URL which stores a baseURL and its parameters.
 */
public class URL {
	private String url;
	private HashMap<String, String> parameters;

	/**
	 * @param url baseURL, i.e. https://domain/index.html
	 */
	public URL(String url) {
		this.url = url;
		this.parameters = new HashMap<>();
	}

	/**
	 * Set a key value pair of url parameters. Can set multiple parameters.
	 * Illustration: https://base_url?key1=value1{@literal &}key2=value2
	 *
	 * @param key   the key
	 * @param value the value
	 */
	public void setParameter(String key, String value) {
		parameters.put(key, value);
	}

	@Override
	public String toString() {
		if (parameters.isEmpty()) {
			return url;
		} else {
			Array<String> params = new Array<>();
			for (Map.Entry<String, String> e : parameters.entrySet()) {
				params.add(e.getKey() + "=" + e.getValue());
			}
			return url + "?" + params.toString("&");
		}
	}
}
