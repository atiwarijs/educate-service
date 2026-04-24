package com.eduservices.profile.keycloak;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;

/**
 * Custom encoder for token requests
 */
public class TokenFormEncoder extends SpringFormEncoder {

//	private final Encoder delegate;
//
//	public TokenFormEncoder(Encoder delegate) {
//		super(delegate);
//		this.delegate = delegate;
//	}
//
//	@Override
//	public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
//		if (object instanceof TokenRequestDto) {
//			Map<String, String> params = ((TokenRequestDto) object).getParameters();
//			MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//
//			params.forEach(formData::add);
//
//			template.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//
//			// Convert the form data to a URL encoded string
//			StringBuilder builder = new StringBuilder();
//			formData.forEach((key, values) -> {
//				values.forEach(value -> {
//					if (builder.length() > 0) {
//						builder.append("&");
//					}
//					builder.append(key).append("=").append(value);
//				});
//			});
//
//			String encodedForm = builder.toString();
//			template.body(encodedForm.getBytes(StandardCharsets.UTF_8));
//		} else {
//			delegate.encode(object, bodyType, template);
//		}
//	}
}
