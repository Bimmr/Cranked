
package me.sniperzciinema.cranked.Handlers.Arena;

import me.sniperzciinema.cranked.Game;
import me.sniperzciinema.cranked.Cranked;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;
import me.sniperzciinema.cranked.Messages.Msgs;
import me.sniperzciinema.cranked.Messages.Time;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class ArenaTimers {

	private Arena arena;
	private int timeLeft;
	private int pregame;
	private int game;
	private int updater;
	private int updateTime;

	public ArenaTimers(Arena arena)
	{
		this.arena = arena;
	}

	public Arena getArena() {
		return arena;
	}

	// Get the time left for the current stage
	public int getTimeLeft() {
		return timeLeft;
	}

	// Set the time left for the current stage
	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	// Stop the pregame timer
	public void stopPreGameTimer() {
		Bukkit.getScheduler().cancelTask(pregame);
	}

	// stop the game timer
	public void stopGameTimer() {
		Bukkit.getScheduler().cancelTask(game);
	}

	// Stop the Waiting update timer
	public void stopUpdaterTimer() {
		Bukkit.getScheduler().cancelTask(updater);
	}

	// Get the pregame time limit
	public int getTimePreGame() {
		return arena.getSettings().getPregameTime();
	}

	// Get the waiting Status Update time delays
	public int getWaitingStatusUpdateTime() {
		return arena.getSettings().getWaitingStatusUpdateTime();
	}

	// Restart the waiting status timer
	public void restartUpdaterTimer() {
		stopUpdaterTimer();
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Cranked.me, new Runnable()
		{

			@Override
			public void run() {

				startUpdaterTimer();
			}
		}, 1L);
	}

	// The waiting status updater
	public void startUpdaterTimer() {
		updateTime = getWaitingStatusUpdateTime();
		updater = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Cranked.me, new Runnable()
		{

			@Override
			public void run() {

				// Check the time, if it's not 0, subtract 1
				if (updateTime != 0)
				{
					updateTime -= 1;

				}

				// Send current game status
				else if (updateTime == 0)
				{
					for (Player player : arena.getPlayers())
					{
						player.sendMessage(Msgs.Waiting_Players_Needed.getString(true, "<current>", String.valueOf(arena.getPlayers().size()), "<needed>", String.valueOf(arena.getSettings().getRequiredPlayers())));
					}
					restartUpdaterTimer();
				}
			}
		}, 0L, 20L);
	}

	// Get the game time limit
	public int getGameTime() {
		return arena.getSettings().getGameTime();
	}

	// Reset the game
	public void resetGame() {
		stopPreGameTimer();
		stopGameTimer();
		timeLeft = getTimePreGame();
	}

	// Start the pregame timer(Holds them in place)
	public void startPreGameTimer() {

		// Set the info
		stopUpdaterTimer();
		timeLeft = getTimePreGame();
		arena.setState(GameState.PreGame);

		// Apply potions
		for (Player player : arena.getPlayers())
		{
			CPlayerManager.getCrankedPlayer(player).respawn(true);
			CPlayerManager.getCrankedPlayer(player).getScoreBoard().showStats();
			player.sendMessage(Msgs.Before_Game_Please_Wait.getString(true));
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,
					Integer.MAX_VALUE, 128));
			player.setWalkSpeed(0.0F);
		}
		pregame = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Cranked.me, new Runnable()
		{

			@Override
			public void run() {
				// Check the time, if it's not 0, subtract 1
				if (timeLeft != 0)
				{
					timeLeft -= 1;
					for (Player player : arena.getPlayers())
					{
						// For some reason i need to we the walk time in here,
						// as if i don't it bugs out...
						player.setLevel(timeLeft);
						// Tell the player how much time is left if it's a
						// certain value
						if (timeLeft == (getGameTime() / 4) * 3 || timeLeft == getGameTime() / 2 || timeLeft == getGameTime() / 4 || timeLeft == 5 || timeLeft == 4 || timeLeft == 3 || timeLeft == 2 || timeLeft == 1)
							player.sendMessage(Msgs.Before_Game_Time_Left.getString(true, "<time>", Time.getTime((long) timeLeft)));
					}
				}
				// GAME STARTS
				else if (timeLeft == 0)
				{
					startGameTimer();
				}
			}
		}, 0L, 20L);
	}

	// Start Game timer
	public void startGameTimer() {
		// Set info
		stopPreGameTimer();
		arena.setState(GameState.Started);

		timeLeft = getGameTime();
		for (Player p : arena.getPlayers())
		{
			if (!p.isSneaking())
				CPlayerManager.getCrankedPlayer(p).getScoreBoard().showStats();
			p.setWalkSpeed(0.2F);
			for (PotionEffect effect : p.getActivePotionEffects())
				p.removePotionEffect(effect.getType());
			p.sendMessage(Msgs.Game_Started.getString(true));
		}
		game = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Cranked.me, new Runnable()
		{

			@Override
			public void run() {
				// Check the time, if it's not 0, subtract 1
				if (timeLeft != 0)
				{
					timeLeft -= 1;
					for (Player player : arena.getPlayers())
					{
						player.setLevel(timeLeft);
					}
					// Display time if it's a certain value
					if (timeLeft == (getGameTime() / 4) * 3 || timeLeft == getGameTime() / 2 || timeLeft == getGameTime() / 4 || timeLeft == 60 || timeLeft == 10 || timeLeft == 9 || timeLeft == 8 || timeLeft == 7 || timeLeft == 6 || timeLeft == 5 || timeLeft == 4 || timeLeft == 3 || timeLeft == 2 || timeLeft == 1)
						for (Player player : arena.getPlayers())
						{
							player.sendMessage(Msgs.Game_Time_Left.getString(true, "<time>", Time.getTime((long) timeLeft)));
						}

				}
				// GAME ENDS
				else if (timeLeft == 0)
				{
					Game.end(arena, true);
				}
			}
		}, 0L, 20L);
	}
}
