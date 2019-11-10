package com.petshop.config;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


class ServletRequestWrapper extends HttpServletRequestWrapper {

	private Map<String, String> headerMap = new HashMap<>();

	ServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	// Add new header
	void addHeader(String name, String value) {
		headerMap.put(name, value);
	}


	@Override
	public String getHeader(String name) {

		String headerValue = super.getHeader(name);
		if (headerMap.containsKey(name)) {
			headerValue = headerMap.get(name);
		}

		return headerValue;
	}

	@Override
	public Enumeration<String> getHeaderNames() {

		List<String> names = Collections.list(super.getHeaderNames());
		names.addAll(headerMap.keySet());

		return Collections.enumeration(names);
	}

	@Override
	public Enumeration<String> getHeaders(String name) {

		List<String> values = Collections.list(super.getHeaders(name));
		if (headerMap.containsKey(name)) {
			values.add(headerMap.get(name));
		}

		return Collections.enumeration(values);
	}

}
