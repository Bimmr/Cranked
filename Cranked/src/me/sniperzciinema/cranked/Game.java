
package me.sniperzciinema.cranked;

import me.sniperzciinema.cranked.GameMechanics.Stats;
import me.sniperzciinema.cranked.Handlers.Arena.Arena;
import me.sniperzciinema.cranked.Handlers.Arena.GameState;
import me.sniperzciinema.cranked.Handlers.Player.CPlayer;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;
import me.sniperzciinema.cranked.Messages.Msgs;
import me.sniperzciinema.cranked.Tools.Settings;
import me.sniperzciinema.cranked.Tools.Sort;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;


public class Game {

	public static void start(Arena arena) {
		// Respawn all the players
		for (Player p : arena.getPlayers())
		{
			CPlayer cp = CPlayerManager.getCrankedPlayer(p);
			cp.respawn(true);
			cp.setTimeJoined(System.currentTimeMillis() / 1000);
		}

		// Start the pregame timer
		arena.getTimer().startPreGameTimer();
	}

	public static void end(Arena arena, Boolean timeRanOut) {

		// Reset the timers and state
		arena.reset();

		Player[] winners = Sort.topStats(arena.getPlayers(), 3);
		// Reset all players, inform them the game ended
		for (Player p : arena.getPlayers())
		{
			int place = 0;
			CPlayer cp = CPlayerManager.getCrankedPlayer(p);
			cp.getTimer().stopTimer();
			if (Stats.getHighestKillStreak(p.getName()) < cp.getKillstreak())
				Stats.setHighestKillStreak(p.getName(), cp.getKillstreak());
			Stats.setPlayingTime(p.getName(), Stats.getPlayingTime(p.getName()) + (System.currentTimeMillis() / 1000 - cp.getTimeJoined()));
			p.sendMessage(Msgs.Format_Line.getString(false));
			p.sendMessage("");
			p.sendMessage(Msgs.Game_Over_Ended.getString(true));
			if (timeRanOut)
				p.sendMessage(Msgs.Game_Over_Times_Up.getString(true));

			p.sendMessage("");
			for (Player winner : winners)
			{
				if (winner != null)
					p.sendMessage(Msgs.Game_Over_Winners.getString(true, "<place>", String.valueOf(place + 1), "<player>", winner.getName() + "(" + CPlayerManager.getCrankedPlayer(winner).getPoints() + ")"));
				place++;
			}

			p.sendMessage("");
			p.sendMessage(Msgs.Arena_Information.getString(true, "<arena>", arena.getName(), "<creator>", arena.getCreator()));
			p.sendMessage(Msgs.Format_Line.getString(false));

			// Set back any blocks that were broken well playing in the
			// arena(This includes chests)
		}
		for (Player p : arena.getPlayers())
			leave(CPlayerManager.getCrankedPlayer(p));
	}

	public static void join(CPlayer cp, final Arena arena) {

		// Get the new arenas settings
		Settings Settings = new Settings(arena);
		Player p = cp.getPlayer();

		// If theres no one in there, then lets start the
		// timer so theres one going for the new player
		if (arena.getPlayers().size() == 0)
			arena.getTimer().startUpdaterTimer();

		// Set the players info
		cp.setInfo();
		cp.setArena(arena);
		cp.getScoreBoard().showStats();

		for (PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());

		// Spawn in the player
		p.setFallDistance(0);
		cp.respawn(false);

		if (arena.getState() == GameState.Started)
		{
			cp.setTimeJoined(System.currentTimeMillis() / 1000);
		}

		// See if autostart happens yet
		else if (arena.getState() == GameState.Waiting && arena.getPlayers().size() >= Settings.getRequiredPlayers())
		{
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cranked.me, new Runnable()
			{
				@Override
				public void run() {
					start(arena);
				}
			}, 100L);
		}
	}

	public static void leave(CPlayer cp) {
		Arena arena = cp.getArena();
		// Reset the player

		cp.reset();
		cp.getScoreBoard().showStats();

		// If there's noone left in the arena, reset it
		if (arena.getState() != GameState.Waiting && arena.getPlayers().size() <= 1)
		{

			arena.reset();
			for (Player p : arena.getPlayers())
			{
				p.sendMessage(Msgs.Game_End_Not_Enough_Players.getString(true));
				CPlayer cpp = CPlayerManager.getCrankedPlayer(p);
				cpp.reset();
				cpp.getScoreBoard().showStats();
			}
		}
	}

}