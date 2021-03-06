
package me.sniperzciinema.cranked.Handlers.Player;

import java.util.Random;

import me.sniperzciinema.cranked.Game;
import me.sniperzciinema.cranked.Extras.ScoreBoard;
import me.sniperzciinema.cranked.GameMechanics.Agility;
import me.sniperzciinema.cranked.GameMechanics.Equip;
import me.sniperzciinema.cranked.GameMechanics.Stats;
import me.sniperzciinema.cranked.Handlers.Arena.Arena;
import me.sniperzciinema.cranked.Handlers.Arena.GameState;
import me.sniperzciinema.cranked.Handlers.Arena.ArenaManager.GameType;
import me.sniperzciinema.cranked.Handlers.Kits.Kit;
import me.sniperzciinema.cranked.Handlers.Kits.KitManager;
import me.sniperzciinema.cranked.Handlers.Location.LocationHandler;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager.Team;
import me.sniperzciinema.cranked.Handlers.Potions.PotionEffects;
import me.sniperzciinema.cranked.Messages.Msgs;
import me.sniperzciinema.cranked.Tools.IconMenu;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;


public class CPlayer {

	private Player player;
	String name;
	private int points = 0;
	private int killstreak = 0;
	private CPlayerTimers PlayerTimer = new CPlayerTimers(this);
	private ScoreBoard ScoreBoard = new ScoreBoard(this);
	private long timeJoined;
	private GameMode gamemode;
	private int level;
	private float exp;
	private double health;
	private int food;
	private ItemStack[] armor;
	private ItemStack[] inventory;
	private Location location;
	private Arena arena;
	private String creating;
	private Player lastDamager;
	private int kills = 0;
	private int deaths = 0;
	private Kit kit;
	private Team team = null;

	public CPlayer(Player p)
	{
		name = p.getName();
		player = p;
	}

	public boolean isInGame() {
		return getArena() != null;
	}

	// Set all their info into their CPlayer
	@SuppressWarnings("deprecation")
	public void setInfo() {
		location = player.getLocation();
		name = player.getName();
		gamemode = player.getGameMode();
		level = player.getLevel();
		exp = player.getExp();
		health = player.getHealth();
		food = player.getFoodLevel();
		inventory = player.getInventory().getContents();
		armor = player.getInventory().getArmorContents();
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.updateInventory();

		player.setGameMode(GameMode.ADVENTURE);
		player.setLevel(0);
		player.setExp(0.0F);
		player.setHealth(20);
		player.setFoodLevel(20);
	}

	// Set the last damager for the player
	/**
	 * @param p
	 */
	public void setLastDamager(Player p) {
		lastDamager = p;
	}

	/**
	 * @return last damage
	 */
	public Player getLastDamager() {
		return lastDamager;
	}

	public void unEquip() {
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
	}

	@SuppressWarnings("deprecation")
	public void leave() {
		player.setGameMode(gamemode);
		player.setLevel(level);
		player.setExp(exp);
		player.setHealth(health);
		player.setFireTicks(0);
		player.setFoodLevel(food);
		player.getInventory().setContents(inventory);
		player.getInventory().setArmorContents(armor);
		player.updateInventory();
		player.setFallDistance(0);
		player.teleport(location);
		player.setWalkSpeed(0.2F);

		getTimer().stopTimer();

		Arena a = arena;
		arena.removePlayer(this);

		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());

		player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);

		if (a.getGameType() == GameType.FFA && (a.getGameState() != GameState.Waiting && a.getPlayers().size() <= 1))
		{
			if (!a.getPlayers().isEmpty())
				for (Player p : a.getPlayers())
				{
					p.sendMessage(Msgs.Game_End_Not_Enough_Players.getString(true));
					CPlayer cpp = CPlayerManager.getCrankedPlayer(p);
					Game.leave(cpp);
				}
			a.reset();
		}

		else if (a.getGameType() == GameType.TDM && (a.getTeam(Team.A).isEmpty() || a.getTeam(Team.B).isEmpty()))
		{
			if (!a.getPlayers().isEmpty())
				for (Player p : a.getPlayers())
				{
					p.sendMessage(Msgs.Game_End_Not_Enough_Players.getString(true));
					CPlayer cpp = CPlayerManager.getCrankedPlayer(p);
					Game.leave(cpp);
				}
			a.reset();
		}

		if (player.getOpenInventory() != null)
			player.closeInventory();

		kills = 0;
		deaths = 0;
		killstreak = 0;
		points = 0;
		location = null;
		gamemode = null;
		level = 0;
		exp = 0;
		health = 20;
		food = 20;
		inventory = null;
		armor = null;
		arena = null;
		timeJoined = 0;
	}

	// Respawn the player
	@SuppressWarnings("deprecation")
	public void respawn(boolean equip) {
		Player p = getPlayer();
		p.setHealth(20.0);
		p.setFoodLevel(20);
		p.setFireTicks(0);
		p.setExp(1.0F);
		p.setFallDistance(0F);
		Random r = new Random();

		int i = r.nextInt(getArena().getSpawns(team).size());
		String loc = getArena().getSpawns(team).get(i);
		p.teleport(LocationHandler.getPlayerLocation(loc));
		p.setFallDistance(0F);

		for (PotionEffect reffect : player.getActivePotionEffects())
			player.removePotionEffect(reffect.getType());

		PotionEffects.applyKitEffects(player);

		if (equip)
			Equip.equip(p);

		p.updateInventory();
	}

	// Get the players current killStreak
	public int getKillstreak() {
		return killstreak;
	}

	// Set the players current killStreak
	public void setKillstreak(int killstreak) {
		this.killstreak = killstreak;
	}

	// Get the players saved gamemode
	public GameMode getGamemode() {
		return gamemode;
	}

	// Set the players saved gamemode
	public void setGamemode(GameMode gamemode) {
		this.gamemode = gamemode;
	}

	// Set the players saved level
	public int getLevel() {
		return level;
	}

	// Set the players saved level
	public void setLevel(int level) {
		this.level = level;
	}

	// Get the players saved exp
	public float getExp() {
		return exp;
	}

	// Set the players saved exp
	public void setExp(float exp) {
		this.exp = exp;
	}

	// Get the players saved health
	public double getHealth() {
		return health;
	}

	// Set the players saved health
	public void setHealth(double health) {
		this.health = health;
	}

	// Get the players saved food
	public int getFood() {
		return food;
	}

	// Set the players saved food
	public void setFood(int food) {
		this.food = food;
	}

	// Get the players saved armor
	public ItemStack[] getArmor() {
		return armor;
	}

	// Set the players saved armor
	public void setArmor(ItemStack[] armor) {
		this.armor = armor;
	}

	// Get the players saved inventory
	public ItemStack[] getInventory() {
		return inventory;
	}

	// Set the players saved inventory
	public void setInventory(ItemStack[] inventory) {
		this.inventory = inventory;
	}

	// Get the players saved location
	public Location getLocation() {
		return location;
	}

	// Set the players saved location
	public void setLocation(Location location) {
		this.location = location;
	}

	// Get the players arena
	public Arena getArena() {
		return arena;
	}

	// Set the players arena
	public void setArena(Arena arena) {
		this.arena = arena;
	}

	// Get the arena the players editing
	public String getCreating() {
		return creating;
	}

	// Set the arena the players editing
	public void setCreating(String creating) {
		this.creating = creating;
	}

	// Get the Bukkit player
	public Player getPlayer() {
		return player;
	}

	// Get the players name
	public String getName() {
		return name;
	}

	// Get the players Points
	public int getPoints() {
		return points;
	}

	// Set the players points
	public void setPoints(int points) {
		this.points = points;
	}

	// Get the players score
	public int getScore() {
		return Stats.getScore(getName());
	}

	// get the players kills this game
	public int getKills() {
		return kills;
	}

	// Set the players kills this game
	public void setKills(int kills) {
		this.kills = kills;
	}

	// Get the players deaths this game
	public int getDeaths() {
		return deaths;
	}

	// Set the players deaths
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	// Get the players overall Kills
	public int getOverallKills() {
		return Stats.getKills(getName());
	}

	// Get the players overall deaths
	public int getOverallDeaths() {
		return Stats.getDeaths(getName());
	}

	// Update the players speed depending on their killstreak
	public void speed() {
		Agility.speed(player);
	}

	// Reset the players speed to default
	public void resetSpeed() {
		Agility.resetSpeed(player);
	}

	// Update the kills and deaths and score that are saved in file
	public void updateStats(int kills, int deaths, int score) {
		if (kills != 0)
			Stats.setKills(getName(), getOverallKills() + kills);
		if (deaths != 0)
			Stats.setDeaths(getName(), getOverallDeaths() + deaths);
		if (score != 0)
			Stats.setScore(getName(), score);
	}

	// Get the players scoreboard
	public ScoreBoard getScoreBoard() {
		return ScoreBoard;
	}

	// Get the players timers
	public CPlayerTimers getTimer() {
		return PlayerTimer;
	}

	public boolean isCranked() {
		return getTimer().isCranked();
	}

	/**
	 * @return the timeJoined
	 */
	public long getTimeJoined() {
		return timeJoined;
	}

	/**
	 * @param timeJoined
	 *            the timeJoined to set
	 */
	public void setTimeJoined(long timeJoined) {
		this.timeJoined = timeJoined;
	}

	public Kit getKit() {
		if (kit == null)
			return KitManager.getDefaultKit();
		else
			return kit;
	}

	/**
	 * @param kit
	 *            the kit to set
	 */
	public void setKit(Kit kit) {
		this.kit = kit;
	}

	/**
	 * Opens the menu for the player
	 * 
	 * @param menu
	 */
	public void openMenu(IconMenu menu) {
		menu.open(player);
	}

	/**
	 * @return the team
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * @param team
	 *            the team to set
	 */
	public void setTeam(Team team) {
		this.team = team;
	}
}
