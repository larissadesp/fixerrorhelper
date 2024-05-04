package com.plugin.fixerrorhelper.core.gpt.model;

import org.json.JSONObject;

public record GPTResult(String cause, 
						String error, 
						String solutions, 
						boolean hasParseError, 
						boolean isInsufficientQuota) {

	public String formattedMessage() {
		StringBuilder formattedResponse = new StringBuilder();

		try {
			formattedResponse.append("Cause: ").append(this.cause).append("\n\n");
			formattedResponse.append("Error: ").append(this.error).append("\n\n");
			formattedResponse.append("Possible solution(s): ").append(this.solutions).append("\n\n");
		} catch (Exception e) {
			formattedResponse.append("Error formatting response: ").append(e.getMessage());
		}

		return formattedResponse.toString();
	}

	public boolean isComplete() {
		return GPTMessageHelper.checkIfCompleteAnswer(formattedMessage());
	}

	public static GPTResult fromJson(JSONObject json) {
		try {
			if (GPTMessageHelper.isInsufficientQuota(json)) {
				return new GPTResult(null, null, null, false, true);
			}

			JSONObject messageObj = json.getJSONArray("choices").getJSONObject(0).getJSONObject("message");
			String content = messageObj.getString("content");
			JSONObject contentObj = new JSONObject(content);

			var cause = contentObj.getString(GPTMessageHelper.KEY_CAUSE);
			var error = contentObj.getString(GPTMessageHelper.KEY_ERROR);
			var solutions = contentObj.getString(GPTMessageHelper.KEY_POSSIBLE_SOLUTIONS);

			return new GPTResult(cause, error, solutions, false, false);
		} catch (Exception e) {
			return new GPTResult(null, null, null, true, false);
		}
	}
	
}
