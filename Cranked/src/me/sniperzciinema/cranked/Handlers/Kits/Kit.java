
package me.sniperzciinema.cranked.Handlers.Kits;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;


public class Kit {

	private String name;
	private ItemStack helmet;
	private ItemStack chestplate;
	private ItemStack leggings;
	private ItemStack boots;
	private ItemStack icon;
	private ArrayList<ItemStack> items;

	private ArrayList<PotionEffect> potionEffects;
	private HashMap<Integer, ItemStack> killstreaks;

	public Kit(String name, ItemStack helmet, ItemStack chestplate,
			ItemStack leggings, ItemStack boots, ArrayList<ItemStack> items,
			ArrayList<PotionEffect> effects,
			HashMap<Integer, ItemStack> killstreaks, ItemStack icon)
	{
		this.name = name;
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;
		this.setPotionEffects(effects);
		this.setIcon(icon);
		this.items = items;
		this.setKillstreaks(killstreaks);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the helmet
	 */
	public ItemStack getHelmet() {
		return helmet;
	}

	/**
	 * @param helmet
	 *            the helmet to set
	 */
	public void setHelmet(ItemStack helmet) {
		this.helmet = helmet;
	}

	/**
	 * @return the chestplate
	 */
	public ItemStack getChestplate() {
		return chestplate;
	}

	/**
	 * @param chestplate
	 *            the chestplate to set
	 */
	public void setChestplate(ItemStack chestplate) {
		this.chestplate = chestplate;
	}

	/**
	 * @return the leggings
	 */
	public ItemStack getLeggings() {
		return leggings;
	}

	/**
	 * @param leggings
	 *            the leggings to set
	 */
	public void setLeggings(ItemStack leggings) {
		this.leggings = leggings;
	}

	/**
	 * @return the boots
	 */
	public ItemStack getBoots() {
		return boots;
	}

	/**
	 * @param boots
	 *            the boots to set
	 */
	public void setBoots(ItemStack boots) {
		this.boots = boots;
	}

	/**
	 * @return the items
	 */
	public ArrayList<ItemStack> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(ArrayList<ItemStack> items) {
		this.items = items;
	}

	/**
	 * @return the killstreaks
	 */
	public HashMap<Integer, ItemStack> getKillstreaks() {
		return killstreaks;
	}

	/**
	 * @param killstreaks
	 *            the killstreaks to set
	 */
	public void setKillstreaks(HashMap<Integer, ItemStack> killstreaks) {
		this.killstreaks = killstreaks;
	}

	/**
	 * @return the icon
	 */
	public ItemStack getIcon() {
		if (icon == null)
			return items.get(0);
		else
			return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(ItemStack icon) {
		this.icon = icon;
	}

	/**
	 * @return the potionEffects
	 */
	public ArrayList<PotionEffect> getPotionEffects() {
		return potionEffects;
	}

	/**
	 * @param potionEffects
	 *            the potionEffects to set
	 */
	public void setPotionEffects(ArrayList<PotionEffect> potionEffects) {
		this.potionEffects = potionEffects;
	}

}
