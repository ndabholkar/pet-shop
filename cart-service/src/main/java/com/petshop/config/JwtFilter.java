package com.petshop.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;


public class JwtFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
		throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String authHeader = request.getHeader("authorization");

		if ("OPTIONS".equals(request.getMethod())) {

			response.setStatus(HttpServletResponse.SC_OK);
			filterChain.doFilter(request, response);

		} else {    // AUTHORIZATION

			RestTemplate restTemplate = new RestTemplate();
			HashMap<String, String> map = new HashMap<>();
			map.put("token", authHeader);
			map.put("keyword", "secretkey");

			Map serviceResponse = restTemplate.postForObject("http://localhost:8090/check", map, Map.class);

			ServletRequestWrapper wrapper = new ServletRequestWrapper(request);
			String isValid = (String) serviceResponse.get("response");
			String userId = (String) serviceResponse.get("user_id");
			String balance = (String) serviceResponse.get("balance");

			switch (isValid) {

				case "200":
					wrapper.addHeader("user_id", userId);
					wrapper.addHeader("balance", balance);
					break;

				case "401":
					throw new ServletException("Invalid token");
				case "510":
					throw new ServletException("JWToken expired. Please refresh your token");
			}

			filterChain.doFilter(wrapper, response);
		}

	}


}
