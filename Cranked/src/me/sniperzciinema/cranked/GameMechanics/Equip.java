
package me.sniperzciinema.cranked.GameMechanics;

import me.sniperzciinema.cranked.Handlers.Arena.ArenaManager;
import me.sniperzciinema.cranked.Tools.Settings;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Equip {

	// Method to equip the player(Normally used with respawning)
	@SuppressWarnings("deprecation")
	public static void equipPlayer(Player p) {
		Settings Settings = new Settings(ArenaManager.getArena(p));
		p.playSound(p.getLocation(), Sound.ANVIL_USE, 1, 1);

		p.getInventory().setHelmet(Settings.getDefaultHead());
		p.getInventory().setChestplate(Settings.getDefaultChest());
		p.getInventory().setLeggings(Settings.getDefaultLegs());
		p.getInventory().setBoots(Settings.getDefaultFeet());
		p.getInventory().clear();
		for (ItemStack is : Settings.getDefaultItems())
			p.getInventory().addItem(is);
		p.updateInventory();
	}
}
