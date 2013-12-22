
package me.sniperzciinema.cranked.Handlers.Player;

import me.sniperzciinema.cranked.Main;
import me.sniperzciinema.cranked.GameMechanics.DeathTypes;
import me.sniperzciinema.cranked.GameMechanics.Deaths;
import me.sniperzciinema.cranked.Handlers.Arena.GameState;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class CPlayerTimers {

	private CPlayer cp;
	private int timeSinceLastKill = 0;
	private int timer;

	public CPlayerTimers(CPlayer cp)
	{
		this.cp = cp;
	}

	// Return the CPlayer
	public CPlayer getCrankedPlayer() {
		return cp;
	}

	public boolean isCranked() {
		return timeSinceLastKill != 0;
	}

	// Return the time since the players last kill
	public int getTimeSinceLastKill() {
		return timeSinceLastKill;
	}

	// Method to stop the timer
	public void stopTimer() {
		Bukkit.getScheduler().cancelTask(timer);
		reset();
	}

	// Reset the time since last kill
	public void reset() {
		timeSinceLastKill = 0;
	}

	// Method to restart the timer
	public void restartTimer() {
		stopTimer();
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
		{

			@Override
			public void run() {

				startTimer();
			}
		}, 1L);
	}

	// The timer it's self
	public void startTimer() {

		// Set all the basics
		timeSinceLastKill = 0;
		final Player player = getCrankedPlayer().getPlayer();
		player.setExp(0.99F);

		// Create the schedular
		timer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.me, new Runnable()
		{

			@Override
			public void run() {
				// Check the time, if it's not add, subtract 1, editing the
				// players exp as we go
				if (timeSinceLastKill != 30)
				{
					timeSinceLastKill += 1;

					if (getCrankedPlayer().isInGame())
						player.setExp(player.getExp() - (float) 1 / 30);
				} else if (timeSinceLastKill == 30)
				{
					// Times up and they didnt get a kill, so we'll kill them
					// with a bang(That doesn't break blocks)!
					if (getCrankedPlayer().isInGame())
						if (getCrankedPlayer().getArena().getState() == GameState.Started)
							Deaths.playerDies(null, player, DeathTypes.OutOfTime);
				}
			}
		}, 0L, 20L);
	}
}
