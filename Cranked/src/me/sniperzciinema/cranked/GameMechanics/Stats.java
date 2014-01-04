
package me.sniperzciinema.cranked.GameMechanics;

import me.sniperzciinema.cranked.Cranked;
import me.sniperzciinema.cranked.Tools.Files;
import me.sniperzciinema.cranked.Tools.MySQLManager;


public class Stats {

	// Get the kills from the location required
	public static int getKills(String name) {
		if (Cranked.me.getConfig().getBoolean("MySQL.Enable"))
			return Integer.valueOf(getMySQLStats(name, "Kills"));
		else
			return Files.getPlayers().getInt("Players." + name + ".Kills");
	}

	// Set the kills to the location required
	public static void setKills(String name, Integer kills) {
		if (Cranked.me.getConfig().getBoolean("MySQL.Enable"))
			setMySQLStats(name, "Kills", kills);
		else
		{
			Files.getPlayers().set("Players." + name + ".Kills", kills);
			Files.savePlayers();
		}
	}

	// Get the deaths from the location required
	public static int getDeaths(String name) {
		if (Cranked.me.getConfig().getBoolean("MySQL.Enable"))
			return Integer.valueOf(getMySQLStats(name, "Deaths"));
		else
			return Files.getPlayers().getInt("Players." + name + ".Deaths");
	}

	// Set the Deaths to the location required
	public static void setDeaths(String name, Integer deaths) {
		if (Cranked.me.getConfig().getBoolean("MySQL.Enable"))
			setMySQLStats(name, "Deaths", deaths);
		else
		{
			Files.getPlayers().set("Players." + name + ".Deaths", deaths);
			Files.savePlayers();
		}
	}

	// Get the score from the location required
	public static int getScore(String name) {
		if (Cranked.me.getConfig().getBoolean("MySQL.Enable"))
			return Integer.valueOf(getMySQLStats(name, "Score"));
		else
			return Files.getPlayers().getInt("Players." + name + ".Score");
	}

	// Set the Score to the location required
	public static void setScore(String name, Integer score) {
		if (Cranked.me.getConfig().getBoolean("MySQL.Enable"))
			setMySQLStats(name, "Score", score);
		else
		{
			Files.getPlayers().set("Players." + name + ".Score", score);
			Files.savePlayers();
		}
	}

	/**
	 * 
	 * @param name
	 * @return playingTime
	 */
	public static int getPlayingTime(String name) {
		if (Cranked.me.getConfig().getBoolean("MySQL.Enable"))
			return Integer.valueOf(getMySQLStats(name, "Playing Time"));
		else
			return Files.getPlayers().getInt("Players." + name + ".Playing Time");
	}

	/**
	 * 
	 * @param name
	 * @param playingTime
	 */
	public static void setPlayingTime(String name, Long playingTime) {
		if (Cranked.me.getConfig().getBoolean("MySQL.Enable"))
			setMySQLStats(name, "Playing Time", playingTime.intValue());
		else
		{
			Files.getPlayers().set("Players." + name + ".Playing Time", playingTime.intValue());
			Files.savePlayers();
		}
	}

	/**
	 * 
	 * @param name
	 * @return highestKillStreak
	 */
	public static int getHighestKillStreak(String name) {
		if (Cranked.me.getConfig().getBoolean("MySQL.Enable"))
			return Integer.valueOf(getMySQLStats(name, "Highest KillStreak"));
		else
			return Files.getPlayers().getInt("Players." + name + ".Highest KillStreak");
	}

	/**
	 * 
	 * @param name
	 * @param highestKillStreak
	 */
	public static void setHighestKillStreak(String name, int highestKillStreak) {
		if (Cranked.me.getConfig().getBoolean("MySQL.Enable"))
			setMySQLStats(name, "Highest KillStreak", highestKillStreak);
		else
		{
			Files.getPlayers().set("Players." + name + ".Highest KillStreak", highestKillStreak);
			Files.savePlayers();
		}
	}

	private static Integer getMySQLStats(String name, String stat) {
		name = name.toLowerCase();
		return MySQLManager.getInt("Infected", stat, name);
	}

	/**
	 * Attempts to set Stats to MySQL (Untested)
	 * 
	 * @param name
	 * @param stat
	 * @param value
	 */
	private static void setMySQLStats(String name, String stat, int value) {
		name = name.toLowerCase();
		MySQLManager.update("Infected", stat, value, name);
	}
}
