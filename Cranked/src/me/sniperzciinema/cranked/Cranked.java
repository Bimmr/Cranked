
package me.sniperzciinema.cranked;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import me.sniperzciinema.cranked.Extras.Menus;
import me.sniperzciinema.cranked.Handlers.Arena.Arena;
import me.sniperzciinema.cranked.Handlers.Arena.ArenaManager;
import me.sniperzciinema.cranked.Handlers.Kits.KitManager;
import me.sniperzciinema.cranked.Handlers.Player.CPlayer;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;
import me.sniperzciinema.cranked.Listeners.CrackShotApi;
import me.sniperzciinema.cranked.Listeners.DamageEvents;
import me.sniperzciinema.cranked.Listeners.MiscListeners;
import me.sniperzciinema.cranked.Listeners.RankingsToggle;
import me.sniperzciinema.cranked.Listeners.RegisterAndUnRegister;
import me.sniperzciinema.cranked.Listeners.SignListener;
import me.sniperzciinema.cranked.Messages.Msgs;
import me.sniperzciinema.cranked.Messages.RandomChatColor;
import me.sniperzciinema.cranked.Messages.StringUtil;
import me.sniperzciinema.cranked.Tools.Files;
import me.sniperzciinema.cranked.Tools.Metrics;
import me.sniperzciinema.cranked.Tools.Updater;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import code.husky.mysql.MySQL;


public class Cranked extends JavaPlugin {

	public static Plugin me;

	public static MySQL MySQL = null;
	public static Connection connection = null;

	public static boolean update;
	public static String name;
	
	public static Menus Menus;

	public void onEnable() {

		System.out.println(Msgs.Format_Header.getString(false, "<title>", "Cranked Startup"));
		if (getConfig().getBoolean("Check For Updates.Enabled"))
		{
			try
			{
				Updater updater = new Updater(this, 67895, getFile(),
						Updater.UpdateType.NO_DOWNLOAD, false);

				update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
				name = updater.getLatestName();
				if (update)
				{
					for (Player player : Bukkit.getOnlinePlayers())
						if (player.hasPermission("Cranked.Admin"))
							player.sendMessage(RandomChatColor.getColor() + "Update for Cranked Availble: " + name);
				}
			} catch (Exception ex)
			{
				System.out.println("The auto-updater tried to contact dev.bukkit.org, but was unsuccessful.");
			}
		}
		try
		{
			Metrics metrics = new Metrics(this);
			metrics.start();
			System.out.println("Metrics was started!");
		} catch (IOException e)
		{
			System.out.println("Metrics was unable to start...");
		}

		me = this;
		// Register the event listeners

		DamageEvents damage = new DamageEvents();
		getServer().getPluginManager().registerEvents(damage, this);
		SignListener sign = new SignListener();
		getServer().getPluginManager().registerEvents(sign, this);
		RegisterAndUnRegister register = new RegisterAndUnRegister();
		getServer().getPluginManager().registerEvents(register, this);
		MiscListeners MiscListeners = new MiscListeners();
		getServer().getPluginManager().registerEvents(MiscListeners, this);
		RankingsToggle RankingsToggle = new RankingsToggle();
		getServer().getPluginManager().registerEvents(RankingsToggle, this);

		if (getServer().getPluginManager().getPlugin("CrackShot") != null)
		{
			CrackShotApi CrackShotApi = new CrackShotApi();
			getServer().getPluginManager().registerEvents(CrackShotApi, this);
			System.out.println("CrackShot is loaded up and ready for use!");
		}
		getCommand("Cranked").setExecutor(new Commands(this));

		Files.create();
		Files.saveAll();

		for (Player p : Bukkit.getOnlinePlayers())
		{
			CPlayer cp = new CPlayer(p);
			CPlayerManager.loadCrankedPlayer(cp);
		}
		System.out.println("Loading Arenas...");
		if (Files.getArenas().getConfigurationSection("Arenas") != null)
			for (String s : Files.getArenas().getConfigurationSection("Arenas").getKeys(false))
			{
				Arena arena = new Arena(StringUtil.getWord(s));
				ArenaManager.loadArena(arena);
				System.out.println("Loaded Arena: " + arena.getName());
			}
		else
			System.out.println("Couldn't Find Any Arenas");

		KitManager.loadConfigKits();

		if (getConfig().getBoolean("MySQL.Enable"))
		{
			System.out.println("Attempting to connect to MySQL");
			MySQL = new MySQL(this, getConfig().getString("MySQL.Host"),
					getConfig().getString("MySQL.Port"),
					getConfig().getString("MySQL.Database"),
					getConfig().getString("MySQL.User"),
					getConfig().getString("MySQL.Pass"));
			connection = MySQL.openConnection();
			try
			{
				connection = MySQL.openConnection();
				Statement statement = connection.createStatement();

				statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + "Cranked" + " (Player VARCHAR(20), Kills INT(10), Deaths INT(10), Score INT(10), PlayingTime INT(15), HighestKillStreak INT(10));");
				System.out.println("MySQL Table has been loaded");
			} catch (Exception e)
			{
				Files.getConfig().set("MySQL.Enabled", false);
				System.out.println("Unable to connect to MySQL");
			}
		}
		System.out.println("Using Players.yml for stats");
		System.out.println(Msgs.Format_Line.getString(false));

		Menus = new Menus();
	}

	public void onDisable() {
		if (!CPlayerManager.getPlayers().isEmpty())
		{
			for (CPlayer cp : CPlayerManager.getPlayers())
				if (cp.getArena() != null)
					cp.getPlayer().sendMessage(Msgs.Error_Misc_Plugin_Unloaded.getString(true));

			for (CPlayer cp : CPlayerManager.getPlayers())
				if (cp.getArena() != null)
					Game.leave(cp);
		}

		if (getConfig().getBoolean("MySQL.Enable"))
			MySQL.closeConnection();
	}

}
