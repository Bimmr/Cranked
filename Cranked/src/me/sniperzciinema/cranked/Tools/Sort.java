
package me.sniperzciinema.cranked.Tools;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;

import org.bukkit.entity.Player;


public class Sort {

	private static Player getHighest(HashMap<Player, Integer> map) {
		Player highest = null;

		int top = (Collections.max(map.values()));

		for (Entry<Player, Integer> e : map.entrySet())
			if (e.getValue() == top)
			{
				highest = e.getKey();
			}

		return highest;
	}

	public static Player[] topStats(List<Player> list, Integer howMany) {

		HashMap<Player, Integer> players = new HashMap<Player, Integer>();
		// Get all the players and put them and their score in a new hashmap for
		// Player, Score
		Player[] top = { null, null, null, null, null, null, null, null, null, null };
		for (Player p : list)
			players.put(p, CPlayerManager.getCrankedPlayer(p).getPoints());

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
