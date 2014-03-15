
package me.sniperzciinema.cranked.Listeners;

import me.sniperzciinema.cranked.GameMechanics.DeathTypes;
import me.sniperzciinema.cranked.GameMechanics.Deaths;
import me.sniperzciinema.cranked.Handlers.Arena.Arena;
import me.sniperzciinema.cranked.Handlers.Arena.ArenaManager;
import me.sniperzciinema.cranked.Handlers.Arena.GameState;
import me.sniperzciinema.cranked.Handlers.Player.CPlayer;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;
import me.sniperzciinema.cranked.Messages.Msgs;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;


public class DamageEvents implements Listener {

	// Player is Damaged, User is Damager
	// When entity is damaged
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player)
		{
			Player victim = (Player) e.getEntity();

			if (ArenaManager.getArena(victim) != null)// Get the attacker
			{
				if (e.getCause() != DamageCause.ENTITY_ATTACK && e.getCause() != DamageCause.PROJECTILE)
				{
					Arena arena = ArenaManager.getArena(victim);

					// If the attack happened before the game started
					if (arena.getGameState() != GameState.Started)
					{

						if (victim.getHealth() - e.getDamage() <= 0)
						{

							CPlayer cv = CPlayerManager.getCrankedPlayer(victim);
							cv.respawn(true);
							e.setDamage(0);
							victim.sendMessage(Msgs.Before_Game_Death.getString(true));
						}
					}

					// If the game has fully started
					else
					{
						Player killer = null;
						CPlayer cv = CPlayerManager.getCrankedPlayer(victim);
						if (cv.getLastDamager() != null)
							killer = cv.getLastDamager();

						if ((victim != null) && (killer != null))
						{

							// If it was enough to kill the player
							if (victim.getHealth() - e.getDamage() <= 0)
							{
								Deaths.playerDies(killer, victim, DeathTypes.Melee);
								e.setDamage(0);
							}
						}

					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDamage(EntityDamageByEntityEvent e) {

		// Is the victim a player?
		if (e.getEntity() instanceof Player)
		{
			Player victim = (Player) e.getEntity();
			Player killer = null;

			// If they're in the game
			if (ArenaManager.getArena(victim) != null)
			{
				DeathTypes death = DeathTypes.Melee;

				// Get the attacker
				if (e.getDamager() instanceof Player)
					killer = (Player) e.getDamager();

				else if (e.getDamager() instanceof Arrow)
				{
					victim = (Player) e.getEntity();
					Arrow arrow = (Arrow) e.getDamager();

					if (arrow.getShooter() instanceof Player)
						killer = (Player) arrow.getShooter();
					death = DeathTypes.Arrow;
				}

				if (killer instanceof Player)
				{
					Arena arena = ArenaManager.getArena(victim);

					if (arena.getGameState() != GameState.Started)
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
							Deaths.playerDies(killer, victim, death);
						}

					}
				}
			}

		}
	}

}
