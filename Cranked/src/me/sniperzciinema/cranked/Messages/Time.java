
package me.sniperzciinema.cranked.Messages;

public class Time {

	/**
	 * 
	 * @param Time
	 * @return the Time in the game format
	 */
	public static String getTime(Long Time) {
		String times = null;
		Long time = Time;
		Long seconds = time;
		long minutes = seconds / 60;
		seconds %= 60;
		if (seconds == 0)
		{
			if (minutes == 0)
				times = "N/A";
			else if (minutes == 1)
				times = minutes + " Minute";
			else
				times = minutes + " Minutes";
		} else if (minutes == 0)
		{
			if (seconds <= 1)
				times = seconds + " Second";
			else
				times = seconds + " Seconds";
		} else
		{
			times = minutes + " Minutes " + seconds + " Seconds";
		}
		return times;
	}

	/**
	 * 
	 * @param time
	 * @return the time in a stats format
	 */
	public static String getOnlineTime(Long time) {
		Long seconds = time;
		long minutes = seconds / 60;
		seconds %= 60;
		long hours = minutes / 60;
		minutes %= 60;
		long days = hours / 24;
		hours %= 24;
		String times = days + "D, " + hours + "H, " + minutes + "M " + seconds + "S";
		return times;
	}
}
