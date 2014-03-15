
package me.sniperzciinema.cranked.Handlers.Kits;

import java.util.ArrayList;
import java.util.HashMap;

import me.sniperzciinema.cranked.Handlers.Items.ItemHandler;
import me.sniperzciinema.cranked.Handlers.Potions.PotionHandler;
import me.sniperzciinema.cranked.Tools.Files;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;


public class KitManager {

	private static ArrayList<Kit> kits = new ArrayList<Kit>();
	private static Kit defaultKit;

	public static void addClass(String name, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ArrayList<ItemStack> items, ArrayList<PotionEffect> effects, HashMap<Integer, ItemStack> killstreaks, ItemStack icon) {
		Kit kit = new Kit(name, helmet, chestplate, leggings, boots, items,
				effects, killstreaks, icon);

		kits.add(kit);
	}

	/**
	 * 
	 * @return kits
	 */
	public static ArrayList<Kit> getKits() {
		return kits;
	}

	/**
	 * 
	 * @param kit
	 * @return if the kit exists
	 */
	public static boolean isRegistered(Kit kit) {
		return kits.contains(kit);
	}

	/**
	 * Adds the kit
	 * 
	 * @param kit
	 */
	public static void addClass(Kit kit) {
		kits.add(kit);
	}

	/**
	 * 
	 * @param kitName
	 * @return kit if it exists
	 */
	public static Kit getKit(String kitName) {
		for (Kit kit : kits)
			if (kit.getName().equalsIgnoreCase(kitName))
				return kit;

		return null;
	}

	/**
	 * @param kit
	 */
	public static void removeKit(Kit kit) {
		kits.remove(kit);
	}

	/**
	 * @param team
	 * @return The default kit
	 */
	public static Kit getDefaultKit() {
		return defaultKit;
	}

	/**
	 * Load the default kit from the config.yml
	 */
	public static void loadDefaultKit() {
		defaultKit = getKit(Files.getConfig().getString("Settings.Global.Default Kit"));
		if (defaultKit == null)
		{
			defaultKit = getKits().get(0);
			Files.getConfig().set("Settings.Global.Default Kits", defaultKit.getName());
			Files.saveConfig();
			System.out.println("Invalid default human class. Changed to: " + defaultKit.getName());
		} else
			defaultKit = getKit(Files.getConfig().getString("Settings.Global.Default Kit"));

	}

	/**
	 * Load all the kits from the Kits.yml
	 */
	public static void loadConfigKits() {
		kits = new ArrayList<Kit>();
		for (String s : Files.getKits().getConfigurationSection("Kits").getKeys(true))
		{
			String name = "0";
			String helmet = "0";
			String chestplate = "0";
			String leggings = "0";
			String boots = "0";
			String icon = "0";
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
			HashMap<Integer, ItemStack> killstreaks = new HashMap<Integer, ItemStack>();
			if (!s.contains("."))
			{
				name = s;
				helmet = Files.getKits().getString("Kits." + s + ".Helmet");
				chestplate = Files.getKits().getString("Kits." + s + ".Chestplate");
				leggings = Files.getKits().getString("Kits." + s + ".Leggings");
				boots = Files.getKits().getString("Kits." + s + ".Boots");
				icon = Files.getKits().getString("Kits." + s + ".Icon");
				items = ItemHandler.getItemStackList(Files.getKits().getStringList("Kits." + s + ".Items"));
				effects = PotionHandler.getPotions(Files.getKits().getStringList("Kits." + s + ".Potion Effects"));
				killstreaks = ItemHandler.getItemHashMap(Files.getKits(), "Kits." + s + ".KillStreaks");
				Kit kit = new Kit(name, ItemHandler.getItemStack(helmet),
						ItemHandler.getItemStack(chestplate),
						ItemHandler.getItemStack(leggings),
						ItemHandler.getItemStack(boots), items, effects,
						killstreaks, ItemHandler.getItemStack(icon));

				System.out.println("Loaded kit: " + kit.getName());
				if (!isRegistered(kit))
					addClass(kit);
			}
		}
		loadDefaultKit();
	}

}
