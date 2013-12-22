
package me.sniperzciinema.cranked.GameMechanics;

import me.sniperzciinema.cranked.Handlers.Arena.GameState;
import me.sniperzciinema.cranked.Handlers.Player.CPlayer;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class Agility {

	// Method that speeds up the player
	public static void speed(Player p) {
		CPlayer cp = CPlayerManager.getCrankedPlayer(p);
		p.getWorld().playEffect(p.getLocation(), Effect.POTION_BREAK, 16462);
		p.getWorld().playEffect(p.getLocation().add(0, 1, 0), Effect.POTION_BREAK, 16462);
		if (cp.getArena().getState() == GameState.Started)
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999,
					1));
		else
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999,
					3));
	}

	public static void resetSpeed(Player p) {
		// Reset the players walking speed
		p.removePotionEffect(PotionEffectType.SPEED);
	}

}
