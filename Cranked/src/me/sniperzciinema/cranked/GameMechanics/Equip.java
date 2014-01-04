
package me.sniperzciinema.cranked.GameMechanics;

import me.sniperzciinema.cranked.Handlers.Kits.Kit;
import me.sniperzciinema.cranked.Handlers.Player.CPlayer;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Equip {

	@SuppressWarnings("deprecation")
	public static void equip(Player p) {

		CPlayer CP = CPlayerManager.getCrankedPlayer(p);
		Kit kit = CP.getKit();
		p.playSound(p.getLocation(), Sound.ANVIL_USE, 1, 1);

		// Reset their inventory by: Going through and removing any old items
		// from the class
		// and add the new ones, this way we don't remove purchased/grenades
		if (!kit.getItems().isEmpty())
			for (ItemStack is : kit.getItems())
			{
				if (p.getInventory().contains(is.getType()))
					p.getInventory().remove(is);
				if (!p.getInventory().contains(is.getType()))
					p.getInventory().addItem(is);
			}

		// Only replace the armor if the player hasn't changed it(So if
		// its none, or if it is the same as default)
		if (p.getInventory().getHelmet() == kit.getHelmet() || p.getInventory().getHelmet() == null)
			p.getInventory().setHelmet(kit.getHelmet());
		if (p.getInventory().getChestplate() == kit.getChestplate() || p.getInventory().getChestplate() == null)
			p.getInventory().setChestplate(kit.getChestplate());
		if (p.getInventory().getLeggings() == kit.getLeggings() || p.getInventory().getLeggings() == null)
			p.getInventory().setLeggings(kit.getLeggings());
		if (p.getInventory().getBoots() == kit.getBoots() || p.getInventory().getBoots() == null)
			p.getInventory().setBoots(kit.getBoots());

		p.updateInventory();

	}
}
