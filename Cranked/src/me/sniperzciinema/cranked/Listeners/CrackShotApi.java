
package me.sniperzciinema.cranked.Listeners;

import me.sniperzciinema.cranked.GameMechanics.DeathTypes;
import me.sniperzciinema.cranked.GameMechanics.Deaths;
import me.sniperzciinema.cranked.Handlers.Arena.Arena;
import me.sniperzciinema.cranked.Handlers.Arena.ArenaManager;
import me.sniperzciinema.cranked.Handlers.Arena.GameState;
import me.sniperzciinema.cranked.Handlers.Player.CPlayer;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;


public class CrackShotApi implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerGetShot(WeaponDamageEntityEvent e) {

		// Is the victim a player?
		if (e.getVictim() instanceof Player)
		{
			Player victim = (Player) e.getVictim();
			Player killer = null;

			// If they're in the game
			if (ArenaManager.getArena(victim) != null)// Get the attacker
				if (e.getPlayer() instanceof Player)
					killer = e.getPlayer();

			Arena arena = ArenaManager.getArena(victim);

			if (arena.getState() != GameState.Started)
			{
				e.setDamage(0);
				e.setCancelled(true);
			}

			// If the game has fully started
			else
			{
				CPlayer cv = CPlayerManager.getCrankedPlayer(victim);

				// Saves who hit the person last
				cv.setLastDamager(killer);

				// If it was enough to kill the player
				if (victim.getHealth() - e.getDamage() <= 0)
				{
					e.setDamage(0);
					Deaths.playerDies(killer, victim, DeathTypes.Gun);
				}

			}
		}
	}
}