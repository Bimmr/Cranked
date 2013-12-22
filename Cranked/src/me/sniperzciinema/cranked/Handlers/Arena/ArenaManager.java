
package me.sniperzciinema.cranked.Handlers.Arena;

import java.util.ArrayList;
import java.util.List;

import me.sniperzciinema.cranked.Handlers.Location.LocationHandler;
import me.sniperzciinema.cranked.Handlers.Player.CPlayer;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;
import me.sniperzciinema.cranked.Messages.StringUtil;
import me.sniperzciinema.cranked.Tools.Files;

import org.bukkit.Location;
import org.bukkit.entity.Player;


public class ArenaManager {

	private static List<Arena> arenas = new ArrayList<Arena>();

	// Return the list of arenas
	public static List<Arena> getArenas() {
		return arenas;
	}

	// return a list of Valid Areas
	public static ArrayList<Arena> getValidArenas() {

		ArrayList<Arena> possible = new ArrayList<Arena>();
		for (Arena arena : arenas)
		{
			if (ArenaManager.arenaRegistered(arena))
			{
				if (isArenaValid(arena.getName()))
					possible.add(arena);
			}
		}
		return possible;
	}

	// Return a string of valid arenas
	public static String getPossibleArenas() {

		ArrayList<String> possible = new ArrayList<String>();
		for (Arena arena : arenas)
		{
			if (ArenaManager.arenaRegistered(arena))
			{
				if (isArenaValid(arena.getName()))
					possible.add(arena.getName());
			}
		}

		StringBuilder possibleString = new StringBuilder();
		for (String s : possible)
		{
			possibleString.append(s);
			if (possible.size() > 1)
				possibleString.append(", ");
		}
		return possible.toString().replaceAll("\\[", "").replaceAll("\\]", "");
	}

	// Return a list of nonvalid arenas
	public static String getNotPossibleArenas() {

		ArrayList<String> notpossible = new ArrayList<String>();
		for (Arena arena : arenas)
		{
			if (ArenaManager.arenaRegistered(arena))
			{
				if (!isArenaValid(arena.getName()))
					notpossible.add(arena.getName());
			}
		}

		StringBuilder notpossibleString = new StringBuilder();
		for (String s : notpossible)
		{
			notpossibleString.append(s);
			if (notpossible.size() > 1)
				notpossibleString.append(", ");
		}
		return notpossible.toString().replaceAll("\\[", "").replaceAll("\\]", "");
	}

	// Add the arena to arenas
	public static void loadArena(Arena arena) {
		if (!arenaRegistered(arena))
			arenas.add(arena);
	}

	// Create the area
	public static void createArena(String name) {
		name = StringUtil.getWord(name);
		Files.arenas.set("Arenas." + name, null);
		Files.saveArenas();
		loadArena(new Arena(name));
	}

	// unload the arena
	public static void unloadArena(Arena arena) {
		if (arenaRegistered(arena))
		{
			arenas.remove(arena);
		}
	}

	// Delete the arena
	public static void removeArena(String name) {
		name = StringUtil.getWord(name);
		Files.arenas.set("Arenas." + name, null);
		Files.saveArenas();
		unloadArena(getArena(name));
	}

	// Check if the arena is registered
	public static boolean arenaRegistered(Arena arena) {
		return arenas.contains(arena);
	}

	// Check if the arena is registered
	public static boolean arenaRegistered(String name) {
		name = StringUtil.getWord(name);
		return (getArena(name) != null);
	}

	// Check if the arena is avalid
	public static boolean isArenaValid(String name) {
		name = StringUtil.getWord(name);
		return !Files.getArenas().getStringList("Arenas." + name + ".Spawns").isEmpty();
	}

	// Return the players arenas
	public static Arena getArena(String name) {
		name = StringUtil.getWord(name);
		for (Arena arena : arenas)
		{
			if (arena.getName().equals(name))
			{
				return arena;
			}
		}
		return null;
	}

	// Return the Players arena
	public static Arena getArena(CPlayer cp) {
		return cp.getArena();
	}

	// Get the players arena
	public static Arena getArena(Player p) {
		return CPlayerManager.getCrankedPlayer(p).getArena();
	}

	// Set a spawn for the arena
	public static void setSpawn(String arena, Location loc) {
		arena = StringUtil.getWord(arena);
		List<String> spawns = Files.getArenas().getStringList("Arenas." + arena + ".Spawns");
		spawns.add(LocationHandler.getLocationToString(loc));
		Files.getArenas().set("Arenas." + arena + ".Spawns", spawns);
		Files.saveArenas();
	}

	// Get the creator for a arena
	public static String getArenaCreator(Arena arena) {
		if (arena.getCreator() == null)
			arena.setCreator("Unkown");
		return arena.getCreator();
	}

	// Return all users in that arena
	public static List<Player> getPlayers(Arena arena) {
		List<Player> players = new ArrayList<Player>();
		for (CPlayer cp : CPlayerManager.getPlayers())
		{
			if (cp.getArena() == arena)
				players.add(cp.getPlayer());
		}
		return players;
	}

}
