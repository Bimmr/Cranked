package me.sniperzciinema.cranked.Tools;


public class StringUtil {

	public static String getWord(String string){
		String s = string;
		s = s.replaceFirst(String.valueOf(s.charAt(0)), String.valueOf(s.charAt(0)).toUpperCase());
		return s;
		
	}
}