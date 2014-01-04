
package me.sniperzciinema.cranked.Tools;

import java.util.Arrays;

import me.sniperzciinema.cranked.Cranked;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;


public class IconMenu implements Listener {

	private String name;
	private int size;
	private OptionClickEventHandler handler;
	private Plugin plugin;

	private String[] optionNames;
	private ItemStack[] optionIcons;

	public IconMenu(String name, int size, OptionClickEventHandler handler,
			Plugin plugin)
	{
		this.name = name;
		this.size = size;
		this.handler = handler;
		this.plugin = plugin;
		this.optionNames = new String[size];
		this.optionIcons = new ItemStack[size];
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public IconMenu setOption(int position, ItemStack icon, String name, String... info) {
		optionNames[position] = name;
		optionIcons[position] = setItemNameAndLore(icon, name, info);
		return this;
	}

	public void open(Player player) {
		Inventory inventory = Bukkit.createInventory(player, size, name);
		for (int i = 0; i < optionIcons.length; i++)
		{
			if (optionIcons[i] != null)
			{
				inventory.setItem(i, optionIcons[i]);
			}
		}
		player.openInventory(inventory);
	}

	public void destroy() {
		HandlerList.unregisterAll(this);
		this.name = "";
		this.handler = null;
		this.optionNames = null;
		this.optionIcons = null;

		// To fix the fact that the Menu being unregistered would prevent others
		// from using a different menu, i made it just re-register the event
		Bukkit.getServer().getPluginManager().registerEvents(this, Cranked.me);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	void onInventoryClose(InventoryCloseEvent event) {
		if (CPlayerManager.isInArena((Player) event.getPlayer()) && event.getInventory().getTitle().contains(event.getPlayer().getName()))
		{
			event.getInventory().clear();
			destroy();
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	void onInventoryClick(InventoryClickEvent event) {
		if (event.getInventory().getTitle().equals(name) && event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR)
		{
			event.setCancelled(true);
			int slot = event.getRawSlot();
			if (slot >= 0 && slot < size && optionNames[slot] != null)
			{
				Plugin plugin = this.plugin;
				final OptionClickEvent e = new OptionClickEvent(
						(Player) event.getWhoClicked(), slot,
						optionNames[slot], event.getClick());
				handler.onOptionClick(e);
				final Player p = (Player) event.getWhoClicked();
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
				{

					public void run() {
						if (e.willClose())
							p.closeInventory();
					}
				}, 1);
				if (e.willClose())
					destroy();
			}
		}
	}

	public interface OptionClickEventHandler {

		public void onOptionClick(OptionClickEvent event);
	}

	public class OptionClickEvent {

		private Player player;
		private int position;
		private String name;
		private boolean close;
		private ClickType click;

		public OptionClickEvent(Player player, int position, String name,
				ClickType clickType)
		{
			this.player = player;
			this.position = position;
			this.name = name;
			this.close = true;
			this.click = clickType;
		}

		public Player getPlayer() {
			return player;
		}

		public int getPosition() {
			return position;
		}

		public String getName() {
			return name;
		}

		public boolean willClose() {
			return close;
		}

		public void setWillClose(boolean close) {
			this.close = close;
		}

		/**
		 * @return the click
		 */
		public ClickType getClick() {
			return click;
		}

	}

	private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + name);
		im.setLore(Arrays.asList(lore));
		item.setItemMeta(im);
		return item;
	}

}