
package me.sniperzciinema.cranked.Handlers.Arena;

import java.util.HashMap;
import java.util.List;

import me.sniperzciinema.cranked.Handlers.Items.ItemHandler;
import me.sniperzciinema.cranked.Handlers.Player.CPlayer;
import me.sniperzciinema.cranked.Tools.Files;
import me.sniperzciinema.cranked.Tools.Settings;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class Arena {

	private String name;
	private GameState state = GameState.Waiting;
	private HashMap<Location, Inventory> chests = new HashMap<Location, Inventory>();
	private HashMap<Location, Material> blocks = new HashMap<Location, Material>();

	private Settings Settings = new Settings(this);
	private ArenaTimers Timer = new ArenaTimers(this);

	public Arena(String name)
	{
		this.name = name;
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

	// Get the arenas spawns
	public List<String> getSpawns() {
		List<String> spawns = Files.getArenas().getStringList("Arenas." + name + ".Spawns");
		return spawns;
	}

	// Get the players in this arena
	public List<Player> getPlayers() {
		return ArenaManager.getPlayers(this);
	}
	public void removePlayer(CPlayer cp){
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
		if(Files.getArenas().getString("Arenas." + name + ".Block") == null || Files.getArenas().getString("Arenas." + name + ".Block") == "")
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

}
