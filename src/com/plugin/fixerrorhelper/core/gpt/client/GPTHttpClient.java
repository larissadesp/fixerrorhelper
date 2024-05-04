package com.plugin.fixerrorhelper.core.gpt.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.plugin.fixerrorhelper.Activator;
import com.plugin.fixerrorhelper.constants.PreferenceConstants;
import com.plugin.fixerrorhelper.core.gpt.GPTClient;

public class GPTHttpClient implements GPTClient {

	private static final String URL = "https://api.openai.com/v1/chat/completions";
	private static final String MODEL = "gpt-3.5-turbo-0125";
	private static final String EMPTY_REPLY_MESSAGE = "Empty response from ChatGPT.";
	private static final String COMUNICATION_ERROR_MESSAGE = "Error communicating with ChatGPT: ";

	private String KEY = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.FIELD_API_KEY);

	@Override
	public JSONObject call(JSONArray instruction) {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPost request = new HttpPost(URL);
			request.addHeader("Authorization", "Bearer " + KEY);
			request.addHeader("Content-Type", "application/json");

			JSONObject requestBody = new JSONObject();
			requestBody.put("model", MODEL);
			requestBody.put("messages", instruction);

			request.setEntity(new StringEntity(requestBody.toString(), StandardCharsets.UTF_8));

			try (CloseableHttpResponse response = httpClient.execute(request)) {
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					String responseBody = EntityUtils.toString(entity);
					JSONObject jsonResponse = new JSONObject(responseBody);

					return jsonResponse;
				} else {
					throw new RuntimeException(EMPTY_REPLY_MESSAGE);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(COMUNICATION_ERROR_MESSAGE + e.getMessage(), e);
		}
	}

}
