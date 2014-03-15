
package me.sniperzciinema.cranked.Handlers.Potions;

import me.sniperzciinema.cranked.Handlers.Player.CPlayer;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;


public class PotionEffects {

	public static void applyKitEffects(Player p) {
		CPlayer cp = CPlayerManager.getCrankedPlayer(p);

		if (!cp.getKit().getPotionEffects().isEmpty())
			for (PotionEffect PE : cp.getKit().getPotionEffects())
				p.addPotionEffect(PE);
	}

	/* public static void addEffectOnContact(Player p, Player u) { CPlayer cp =
	 * CPlayerManager.getCrankedPlayer(p);
	 * 
	 * if (!cp.getKit().getTransferEffects().isEmpty()) for (PotionEffect PE :
	 * cp.getKit().getTransferEffects()) u.addPotionEffect(PE); } */
}
