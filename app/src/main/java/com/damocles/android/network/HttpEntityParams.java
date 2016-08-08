package com.damocles.android.network;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

/**
 * http请求参数
 * 
 * @author Daniel
 * 
 */
public class HttpEntityParams {

	private List<BasicNameValuePair> params = null;

	public HttpEntityParams() {
		if (params == null) {
			params = new ArrayList<BasicNameValuePair>();
		} else {
			params.clear();
		}
	}

	public void put(String key, String value) {
		params.add(new BasicNameValuePair(key, value));
	}

	public void put(String key, boolean value) {
		params.add(new BasicNameValuePair(key, String.valueOf(value)));
	}

	public void put(String key, char value) {
		params.add(new BasicNameValuePair(key, String.valueOf(value)));
	}

	public void put(String key, char[] data) {
		params.add(new BasicNameValuePair(key, String.valueOf(data)));
	}

	public void put(String key, double value) {
		params.add(new BasicNameValuePair(key, String.valueOf(value)));
	}

	public void put(String key, float value) {
		params.add(new BasicNameValuePair(key, String.valueOf(value)));
	}

	public void put(String key, int value) {
		params.add(new BasicNameValuePair(key, String.valueOf(value)));
	}

	public void put(String key, long value) {
		params.add(new BasicNameValuePair(key, String.valueOf(value)));
	}

	public void put(String key, Object value) {
		params.add(new BasicNameValuePair(key, String.valueOf(value)));
	}

	public void put(String key, char[] data, int start, int length) {
		params.add(new BasicNameValuePair(key, String.valueOf(data, start,
				length)));
	}

	public List<BasicNameValuePair> getHttpEntityParams() {
		if (params == null) {
			params = new ArrayList<BasicNameValuePair>();
		}
		return params;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		if (params == null || params.size() == 0) {
			return null;
		}
		for (int i = 0, len = params.size(); i < len; i++) {
			if (i > 0) {
				buffer.append("&");
			}
			buffer.append(params.get(i).getName());
			buffer.append("=");
			buffer.append(params.get(i).getValue());
		}
		return buffer.toString();
	}

}
