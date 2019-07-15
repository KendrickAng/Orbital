package com.mygdx.game.net;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

public class URL {
	private String url;
	private HashMap<String, String> parameters;

	public URL(String url) {
		this.url = url;
		this.parameters = new HashMap<>();
	}

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
