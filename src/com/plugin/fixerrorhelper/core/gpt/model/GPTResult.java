package com.plugin.fixerrorhelper.core.gpt.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.plugin.fixerrorhelper.constants.SectionHeadersKeyConstants;
import com.plugin.fixerrorhelper.messages.Messages;

public record GPTResult(String cause, String error, String solutions, boolean hasParseError,
		boolean isInsufficientQuota) {

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
	
	public static String formattedNumberedSolutions(String solutions) {
		String regex = "\\d+\\..*?(?=\\d+\\.|$)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(solutions);
        
        StringBuilder formattedText = new StringBuilder();
        
        formattedText.append("\n");
        int count = 1;
        while (matcher.find()) {
        	formattedText.append(matcher.group(0)).append("\n\n");
            count++;
        }
        
        if (count == 1) {
            return solutions;
        }
        
        return formattedText.toString();
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

			var cause = contentObj.getString(SectionHeadersKeyConstants.KEY_CAUSE);
			var error = contentObj.getString(SectionHeadersKeyConstants.KEY_ERROR);
			var solutions = contentObj.getString(SectionHeadersKeyConstants.KEY_POSSIBLE_SOLUTIONS);
			
			solutions = formattedNumberedSolutions(solutions);

			return new GPTResult(cause, error, solutions, false, false);
		} catch (Exception e) {
			return new GPTResult(null, null, null, true, false);
		}
	}

}
