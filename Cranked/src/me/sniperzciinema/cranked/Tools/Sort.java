
package me.sniperzciinema.cranked.Tools;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import me.sniperzciinema.cranked.GameMechanics.Stats;
import me.sniperzciinema.cranked.GameMechanics.Stats.StatType;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class Sort {

	private static String getHighest(HashMap<String, Integer> map) {
		String highest = null;

		int top = (Collections.max(map.values()));

		for (Entry<String, Integer> e : map.entrySet())
			if (e.getValue() == top)
			{
				highest = e.getKey();
			}

		return highest;
	}
	public static String[] topStats(StatType type, Integer howMany) {

		HashMap<String, Integer> players = new HashMap<String, Integer>();
		// Get all the players and put them and their score in a new hashmap for
		// Player, Score
		String[] top = { " ", " ", " ", " ", " " };

		for (Player p : Bukkit.getOnlinePlayers())
			players.put(p.getName(), Stats.getStat(type, p.getName()));

		if (Settings.MySQLEnabled())
			for (String playerName : MySQLManager.getPlayers("Cranked"))
				players.put(playerName, Stats.getStat(type, playerName));

		else
			for (String playerName : Files.getPlayers().getConfigurationSection("Players").getKeys(false))
			{
				players.put(playerName, Stats.getStat(type, playerName));

			}
		int place = 0;
		while (place != howMany)
		{
			// If the list still has players in it, find the top player

			try
			{
				top[place] = getHighest(players);

				players.remove(top[place]);
			} catch (NoSuchElementException ne)
			{

			}
			place++;
		}

		return top;
	}
	public static String[] topPoints(List<Player> list, Integer howMany) {

		HashMap<String, Integer> players = new HashMap<String, Integer>();
		// Get all the players and put them and their score in a new hashmap for
		// Player, Score
		String[] top = { null, null, null, null, null, null, null, null, null, null };
		for (Player p : list)
			players.put(p.getName(), CPlayerManager.getCrankedPlayer(p).getPoints());

		int place = 0;
		while (place != howMany - 1)
		{
			// If the list still has players in it, find the top player

			try
			{
				top[place] = getHighest(players);

				players.remove(top[place]);
			} catch (NoSuchElementException ne)
			{

			}
			place++;
		}

		return top;
	}
}
