
package me.sniperzciinema.cranked.Tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.sniperzciinema.cranked.Main;

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

	public static void reloadConfig() {
		Main.me.reloadConfig();
	}

	public static void saveConfig() {
		Main.me.saveConfig();
	}

	public static FileConfiguration getConfig() {
		return Main.me.getConfig();
	}

	public static void reloadAll() {
		reloadConfig();
		reloadArenas();
		reloadPlayers();
		reloadMessages();
	}

	public static void saveAll() {
		saveConfig();
		saveArenas();
		savePlayers();
		saveMessages();
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