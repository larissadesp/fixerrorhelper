package com.plugin.fixerrorhelper.core.gpt.model;

import org.json.JSONObject;

import com.plugin.fixerrorhelper.constants.SectionHeadersKeyConstants;
import com.plugin.fixerrorhelper.messages.Messages;

public record GPTResult(String cause, String error, String solutions, boolean hasParseError, 
		String isErrorOfType) {

	public String formattedMessage() {
		StringBuilder formattedResponse = new StringBuilder();

		try {
			formattedResponse.append(Messages.cause + " ")
							 .append(this.cause)
							 .append("\n\n");
			
			formattedResponse.append(Messages.error + " ")
							 .append(this.error)
							 .append("\n\n");
			
			formattedResponse.append(Messages.possibleSolutions + " ")
							 .append(this.solutions)
							 .append("\n\n");
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
			var errorType = GPTMessageHelper.isErrorOfType(json);
			if (errorType != "") {
				return new GPTResult(null, null, null, false, errorType);
			}
			
			JSONObject messageObj = json.getJSONArray("choices").getJSONObject(0).getJSONObject("message");
			String content = messageObj.getString("content");
			JSONObject contentObj = new JSONObject(content);
			
			var cause = contentObj.getString(SectionHeadersKeyConstants.KEY_CAUSE);
			var error = contentObj.getString(SectionHeadersKeyConstants.KEY_ERROR);
			var solutions = contentObj.getString(SectionHeadersKeyConstants.KEY_POSSIBLE_SOLUTIONS);
			solutions = formatSolutions(solutions);
			
			return new GPTResult(cause, error, solutions, false, "");
			
		} catch (Exception e) {
			return new GPTResult(null, null, null, true, "");
		}
	}
	
	private static String formatSolutions(String solutions) {
		StringBuilder formattedSolutions = new StringBuilder();

		if (solutions.contains("1. ") && solutions.contains("2. ")) {
			if (!solutions.startsWith("\\n")) {
				solutions = "\\n" + solutions;
			}

			String[] solutionItems = solutions.split("\\\\n");

			for (String item : solutionItems) {
				formattedSolutions.append(item.trim()).append("\n");
			}

			if (formattedSolutions.length() > 0) {
				formattedSolutions.setLength(formattedSolutions.length() - 1);
			}

			return formattedSolutions.toString();
		}

		return solutions;
	}
	
}
