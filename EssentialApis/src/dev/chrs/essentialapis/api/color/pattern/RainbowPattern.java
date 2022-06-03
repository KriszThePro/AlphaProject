package dev.chrs.essentialapis.api.color.pattern;

import java.util.regex.Matcher;

import dev.chrs.essentialapis.api.color.ColorApi;

public class RainbowPattern implements Pattern
{
	/**
	 * Applies a rainbow pattern to the provided String.
	 * Output might me the same as the input if this pattern is not present.
	 *
	 * @param string The String to which this pattern should be applied to
	 * @return The new String with applied pattern
	 */
	public String process(String string)
	{
		Matcher matcher = pattern.matcher(string);
		
		while (matcher.find())
		{
			String saturation = matcher.group(1);
			String content = matcher.group(2);
			string = string.replace(matcher.group(), ColorApi.rainbow(content, Float.parseFloat(saturation)));
		}
		
		return string;
	}

	
	private java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("<RAINBOW([0-9]{1,3})>(.*?)</RAINBOW>");
}
