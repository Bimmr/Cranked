
package me.sniperzciinema.cranked.Handlers.Arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.sniperzciinema.cranked.Handlers.Arena.ArenaManager.GameType;
import me.sniperzciinema.cranked.Handlers.Items.ItemHandler;
import me.sniperzciinema.cranked.Handlers.Player.CPlayer;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager.Team;
import me.sniperzciinema.cranked.Tools.Files;
import me.sniperzciinema.cranked.Tools.Settings;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class Arena {

	private String name;
	private GameType gameType;
	private GameState state = GameState.Waiting;
	private HashMap<Location, Inventory> chests = new HashMap<Location, Inventory>();
	private HashMap<Location, Material> blocks = new HashMap<Location, Material>();

	private Settings Settings = new Settings(this);
	private ArenaTimers Timer = new ArenaTimers(this);

	public Arena(String name)
	{
		this.name = name;
		gameType = getGameType();
	}

	// Get the arenas settings
	public Settings getSettings() {
		return Settings;
	}

	// Get the arenas name
	public String getName() {
		return name;
	}

	// Get the arenas creator
	public String getCreator() {
		return Files.getArenas().getString("Arenas." + name + ".Creator");
	}

	// Set the arenas creator
	public void setCreator(String maker) {
		Files.getArenas().set("Arenas." + name + ".Creator", maker);
		Files.saveArenas();
	}

	// Get the arenas state
	public GameState getGameState() {
		return state;
	}

	// Set the arenas state
	public void setGameState(GameState state) {
		this.state = state;
	}

	public void setSpawns(List<String> spawns, Team team) {
		if (team == Team.A || team == Team.B)
			Files.getArenas().set("Arenas." + name + "." + team.toString() + " Spawns", spawns);
		else
			Files.getArenas().set("Arenas." + name + ".Spawns", spawns);

		Files.saveArenas();
	}

	/**
	 * Returns the spawns from the config
	 * 
	 * @return the spawns
	 */
	public List<String> getSpawns(Team team) {
		List<String> spawns = Files.getArenas().getStringList("Arenas." + name + ".Spawns");

		if (team != null)
			spawns.addAll(Files.getArenas().getStringList("Arenas." + name + "." + team.toString() + " Spawns"));

		return spawns;
	}

	public List<String> getExactSpawns(Team team) {
		List<String> spawns = new ArrayList<String>();
		if (team == null)
			spawns.addAll(Files.getArenas().getStringList("Arenas." + name + ".Spawns"));
		else
			spawns.addAll(Files.getArenas().getStringList("Arenas." + name + "." + team.toString() + " Spawns"));

		return spawns;
	}

	// Get the players in this arena
	public List<Player> getPlayers() {
		return ArenaManager.getPlayers(this);
	}

	public void removePlayer(CPlayer cp) {
		cp.setArena(null);
	}

	// Get the saved chests
	public HashMap<Location, Inventory> getChests() {
		return chests;
	}

	// Get the chest at Loc
	public Inventory getChest(Location loc) {
		return chests.get(loc);
	}

	// Set a chest at Loc
	public void setChest(Location loc, Inventory inv) {
		chests.put(loc, inv);
	}

	// Get the saved Blocks
	public Material getBlock(Location loc) {
		return blocks.get(loc);
	}

	// Get the block at Loc
	public HashMap<Location, Material> getBlocks() {
		return blocks;
	}

	// Set the block at Loc
	public void setBlock(Location loc, Material mat) {
		blocks.put(loc, mat);
	}

	// Get the arenas timers
	public ArenaTimers getTimer() {
		return Timer;
	}

	/**
	 * @return the block
	 */
	public ItemStack getBlock() {
		if (Files.getArenas().getString("Arenas." + name + ".Block") == null || Files.getArenas().getString("Arenas." + name + ".Block") == "")
			return new ItemStack(Material.EMPTY_MAP, 1);
		else
			return ItemHandler.getItemStack(Files.getArenas().getString("Arenas." + name + ".Block"));
	}

	/**
	 * @param block
	 *            the block to set
	 */
	@SuppressWarnings("deprecation")
	public void setBlock(ItemStack is) {
		if (is.getType() == null || is.getType().getId() == 0)
			Files.getArenas().set("Arenas." + name + ".Block", "id:395");
		else
			Files.getArenas().set("Arenas." + name + ".Block", ItemHandler.getItemStackToString(is));
		Files.saveArenas();
	}

	public void reset() {
		getTimer().stopGameTimer();
		getTimer().stopPreGameTimer();
		getTimer().stopUpdaterTimer();
		setGameState(GameState.Waiting);
		// Clear blocks
		if (!this.getBlocks().isEmpty())
			for (Location loc : this.getBlocks().keySet())
				this.setBlock(loc, this.getBlock(loc));

		this.getBlocks().clear();

		// Clear Chests too
		if (!this.getChests().isEmpty())
			for (Location loc : this.getChests().keySet())
				if (loc.getBlock().getType() == Material.CHEST)
					this.setChest(loc, this.getChest(loc));
		this.getChests().clear();
	}

	/**
	 * @return the gameType
	 */
	public GameType getGameType() {
		if (gameType == null)
			if (Files.getArenas().getString("Arenas." + name + ".Game Type") != null)
				gameType = GameType.valueOf(Files.getArenas().getString("Arenas." + name + ".Game Type"));
			else
			{
				Files.getArenas().set("Arenas." + name + ".Game Type", "FFA");
				Files.saveArenas();
				gameType = GameType.FFA;
			}

		return gameType;
	}

	/**
	 * @param gameType
	 *            the gameType to set
	 */
	public void setGameType(GameType gameType) {
		Files.getArenas().set("Arenas." + name + ".Game Type", gameType.toString());
		Files.saveArenas();
		this.gameType = gameType;
	}
	/**
	 * Look at the other teams and add the player to the proper team
	 * 
	 * @param cp
	 */
	public void addTeamPlayer(CPlayer cp) {
		ArrayList<Player> teamA = getTeam(Team.A);
		ArrayList<Player> teamB = getTeam(Team.B);
		
		if (teamA.size() <= teamB.size())
			cp.setTeam(Team.A);
		else
			cp.setTeam(Team.B);
	}

	/**
	 * @return the teamA
	 */
	public ArrayList<Player> getTeam(Team team) {
		ArrayList<Player> teamP = new ArrayList<Player>();
		for (Player player : getPlayers())
		{
			CPlayer cpp = CPlayerManager.getCrankedPlayer(player);
			if (cpp.getTeam() == team)
				teamP.add(player);
		}
		return teamP;
	}
	
	/**
	 * Set the CPlayer's team
	 * @param cp
	 * @param team
	 */
	public void setTeam(CPlayer cp, Team team){
		cp.setTeam(team);
	}

	public int getTeamPoints(Team team){
		int i = 0;
		for(Player p : getTeam(team))
			i += CPlayerManager.getCrankedPlayer(p).getPoints();
		
		return i;
	}
}
