
package me.sniperzciinema.cranked.Handlers.Player;

import java.util.ArrayList;
import java.util.List;

import me.sniperzciinema.cranked.Game;

import org.bukkit.entity.Player;


public class CPlayerManager {

	// An list to hold all of the arenas
	private static List<CPlayer> players = new ArrayList<CPlayer>();

	public static List<CPlayer> getPlayers() {
		return players;
	}

	// Method to register an arena if not registered, just adds it to the list
	public static CPlayer getCrankedPlayer(Player player) {
		for (CPlayer p : players)
		{
			if (p.name.equalsIgnoreCase(player.getName()))
				return p;
		}
		return null;
	}

	// Add the Cranked player to the players list
	public static void loadCrankedPlayer(CPlayer cp) {
		if (!isCrankedPlayer(cp.getPlayer()))
		{
			players.add(cp);
		}
	}

	// Create a CPlayer for a player
	public static void createCrankedPlayer(Player player) {
		CPlayer cp = new CPlayer(player);
		players.add(cp);
	}

	// Delete the Cranked player
	public static void deleteCrankedPlayer(CPlayer cp) {
		players.remove(cp);
	}

	// Check if the player is a Cranked player
	public static boolean isCrankedPlayer(Player p) {
		return players.contains(getCrankedPlayer(p));
	}

	// Set the CPlayer's info
	public static void setInfo(CPlayer cp) {
		cp.setInfo();
	}

	// Reset the CPlayer
	public static void leave(CPlayer cp) {
		Game.leave(cp);
	}

	// Check if a player is in an arena
	public static boolean isInArena(Player player) {
		return (getCrankedPlayer(player).getArena() != null);
	}

	public static CPlayer getCrankedPlayer(String player) {
		for (CPlayer p : players)
		{
			if (p.name.equalsIgnoreCase(player))
				return p;
		}
		return null;
	}
}
