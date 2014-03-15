
package me.sniperzciinema.cranked.Messages;

public class StringUtil {

	/**
	 * 
	 * @param string
	 * @return the string with a capital first letter and the rest lowercase
	 */
	public static String getWord(String string) {
		if (string != null)
		{
			string = string.replaceAll("_", " ");
			string = string.toLowerCase();
			string = string.replaceFirst(String.valueOf(string.charAt(0)), String.valueOf(string.charAt(0)).toUpperCase());

		}
		return string;
	}
	public static String format(String string){
		return (string.replaceAll("&", "§"));
	}
}
