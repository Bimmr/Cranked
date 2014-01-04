
package me.sniperzciinema.cranked.Tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.sniperzciinema.cranked.Cranked;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class Files {

	public static YamlConfiguration arenas = null;
	public static File arenasFile = null;
	public static YamlConfiguration playerF = null;
	public static File playerFile = null;
	public static YamlConfiguration messages = null;
	public static File messagesFile = null;
	public static YamlConfiguration kits = null;
	public static File kitsFile = null;

	public static void reloadConfig() {
		Cranked.me.reloadConfig();
	}

	public static void saveConfig() {
		Cranked.me.saveConfig();
	}

	public static FileConfiguration getConfig() {
		return Cranked.me.getConfig();
	}

	public static void reloadAll() {
		reloadConfig();
		reloadArenas();
		reloadPlayers();
		reloadMessages();
		reloadKits();
	}

	public static void saveAll() {
		saveConfig();
		saveArenas();
		savePlayers();
		saveMessages();
		saveKits();
	}

	public static void create() {
		getConfig().options().copyDefaults(true);
		getArenas().options().copyDefaults(true);
		getPlayers().options().copyDefaults(true);
		getMessages().options().copyDefaults(true);	
		getKits().options().copyDefaults(true);
	}


	// Reload Abilities File
	public static void reloadKits() {
		if (kitsFile == null)
			kitsFile = new File(
					Bukkit.getPluginManager().getPlugin("Cranked").getDataFolder(),
					"Kits.yml");
		kits = YamlConfiguration.loadConfiguration(kitsFile);
		// Look for defaults in the jar
		InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Cranked").getResource("Kits.yml");
		if (defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			if (!kitsFile.exists() || kitsFile.length() == 0)
				kits.setDefaults(defConfig);
		}
	}

	// Get Abilities file
	public static FileConfiguration getKits() {
		if (kits == null)
			reloadKits();
		return kits;
	}

	// Safe Abilities File
	public static void saveKits() {
		if (kits == null || kitsFile == null)
			return;
		try
		{
			getKits().save(kitsFile);
		} catch (IOException ex)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + kitsFile, ex);
		}
	}
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Reload Arenas File
	public static void reloadArenas() {
		if (arenasFile == null)
			arenasFile = new File(
					Bukkit.getPluginManager().getPlugin("Cranked").getDataFolder(),
					"Arenas.yml");
		arenas = YamlConfiguration.loadConfiguration(arenasFile);
		// Look for defaults in the jar
		InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Cranked").getResource("Arenas.yml");
		if (defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			arenas.setDefaults(defConfig);
		}
	}

	// Get Arenas File
	public static FileConfiguration getArenas() {
		if (arenas == null)
			reloadArenas();
		return arenas;
	}

	// Safe Arenas File
	public static void saveArenas() {
		if (arenas == null || arenasFile == null)
			return;
		try
		{
			getArenas().save(arenasFile);
		} catch (IOException ex)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + arenasFile, ex);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	// MESSAGES

	// Reload Arenas File
	public static void reloadMessages() {
		if (messages == null)
			messagesFile = new File(
					Bukkit.getPluginManager().getPlugin("Cranked").getDataFolder(),
					"Messages.yml");
		messages = YamlConfiguration.loadConfiguration(messagesFile);
		// Look for defaults in the jar
		InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Cranked").getResource("Messages.yml");
		if (defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			messages.setDefaults(defConfig);
		}
	}

	// Get Arenas File
	public static FileConfiguration getMessages() {
		if (messages == null)
			reloadMessages();
		return messages;
	}

	// Safe Arenas File
	public static void saveMessages() {
		if (messages == null || messagesFile == null)
			return;
		try
		{
			getMessages().save(messagesFile);
		} catch (IOException ex)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + messagesFile, ex);
		}
	}

	// ======================================================================================
	// PLAYERS

	// Reload Kills File
	public static void reloadPlayers() {
		if (playerFile == null)
			playerFile = new File(
					Bukkit.getPluginManager().getPlugin("Cranked").getDataFolder(),
					"Players.yml");
		playerF = YamlConfiguration.loadConfiguration(playerFile);
		// Look for defaults in the jar
		InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Cranked").getResource("Players.yml");
		if (defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			playerF.setDefaults(defConfig);
		}
	}

	// Get Kills file
	public static FileConfiguration getPlayers() {
		if (playerF == null)
		{
			reloadPlayers();
			savePlayers();
		}
		return playerF;
	}

	// Save Kills File
	public static void savePlayers() {
		if (playerF == null || playerFile == null)
			return;
		try
		{
			getPlayers().save(playerFile);
		} catch (IOException ex)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + playerFile, ex);
		}
	}


}