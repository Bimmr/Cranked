
package me.sniperzciinema.cranked.Extras;

import me.sniperzciinema.cranked.Cranked;
import me.sniperzciinema.cranked.GameMechanics.Equip;
import me.sniperzciinema.cranked.Handlers.Arena.Arena;
import me.sniperzciinema.cranked.Handlers.Arena.ArenaManager;
import me.sniperzciinema.cranked.Handlers.Arena.GameState;
import me.sniperzciinema.cranked.Handlers.Kits.Kit;
import me.sniperzciinema.cranked.Handlers.Kits.KitManager;
import me.sniperzciinema.cranked.Handlers.Player.CPlayer;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;
import me.sniperzciinema.cranked.Messages.Msgs;
import me.sniperzciinema.cranked.Messages.RandomChatColor;
import me.sniperzciinema.cranked.Messages.StringUtil;
import me.sniperzciinema.cranked.Messages.Time;
import me.sniperzciinema.cranked.Tools.IconMenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Menus {

	// Create all the menus
	public IconMenu arenaMenu;
	public IconMenu kitMenu;

	// When the class gets loaded create all the IconMenus and store them for
	// later use
	public Menus()
	{
		arenaMenu = getArenaMenu();
		kitMenu = getKitMenu();
	}

	public void destroyMenu(IconMenu menu) {
		menu.destroy();
	}

	public void destroyAllMenus() {
		arenaMenu.destroy();
		kitMenu.destroy();
	}

	public IconMenu getArenaMenu() {
		IconMenu menu = new IconMenu(ChatColor.GREEN + "Join An Arena",
				((ArenaManager.getArenas().size() / 9) * 9) + 9,
				new IconMenu.OptionClickEventHandler()
				{

					@Override
					public void onOptionClick(final IconMenu.OptionClickEvent event) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(Cranked.me, new Runnable()
						{

							public void run() {
								// Make sure the item(Arena) they choose is a
								// valid arena
								if (ArenaManager.isArenaValid(ChatColor.stripColor(event.getName())))

									event.getPlayer().performCommand("Cranked Join " + ChatColor.stripColor(event.getName()));
								else
									event.getPlayer().sendMessage(Msgs.Error_Arena_No_Spawns.getString(false));
							}
						}, 2);
					}
				}, Cranked.me);
		// Choose a block to represent the arena in the GUI
		int place = 0;

		for (Arena arena : ArenaManager.getArenas())
		{

			// Set the message depending on if the arena is valid(Has spawns)
			if (ArenaManager.isArenaValid(arena.getName()))
				menu.setOption(place, arena.getBlock(), "" + ChatColor.getByChar(String.valueOf(place + 1)) + ChatColor.BOLD + ChatColor.UNDERLINE + arena.getName(), "", ChatColor.GREEN + "Click Here Join This Arena", ChatColor.GRAY + "--------------------------", ChatColor.YELLOW + "Auto Start At: " + arena.getSettings().getRequiredPlayers(), ChatColor.YELLOW + "Playing: " + arena.getPlayers().size() + "/" + arena.getSettings().getMaxPlayers(), ChatColor.YELLOW + "State: " + arena.getGameState(), ChatColor.YELLOW + "Time Left: " + (arena.getGameState() == GameState.Started ? arena.getTimer().getTimeLeft() : "N/A"), ChatColor.GRAY + "--------------------------", ChatColor.RED + "Time Limit: " + Time.getTime((long) arena.getSettings().getGameTime()), ChatColor.RED + "Points To Win: " + arena.getSettings().getPointsToWin(), ChatColor.GRAY + "--------------------------", ChatColor.AQUA + "Creator: " + ChatColor.WHITE + arena.getCreator());
			else
				menu.setOption(place, new ItemStack(Material.REDSTONE_BLOCK), ChatColor.DARK_RED + arena.getName(), "", ChatColor.RED + "This arena isn't playable!", ChatColor.RED + "      It's Missing Spawns!", ChatColor.GRAY + "--------------------------", "" + ChatColor.RED + ChatColor.STRIKETHROUGH + "Auto Start At: " + arena.getSettings().getRequiredPlayers(), "" + ChatColor.RED + ChatColor.STRIKETHROUGH + "Playing: " + arena.getPlayers().size() + "/" + arena.getSettings().getMaxPlayers(), "" + ChatColor.RED + ChatColor.STRIKETHROUGH + "State: " + arena.getGameState(), "" + ChatColor.RED + ChatColor.STRIKETHROUGH + "Time Left: " + (arena.getGameState() == GameState.Started ? arena.getTimer().getTimeLeft() : "N/A"), ChatColor.GRAY + "--------------------------", "" + ChatColor.RED + ChatColor.STRIKETHROUGH + "Time Limit: " + Time.getTime((long) arena.getSettings().getGameTime()), "" + ChatColor.RED + ChatColor.STRIKETHROUGH + "Points To Win: " + arena.getSettings().getPointsToWin(), ChatColor.GRAY + "--------------------------", "" + ChatColor.RED + ChatColor.STRIKETHROUGH + "Creator: " + ChatColor.WHITE + arena.getCreator());

			place++;
		}

		return menu;
	}

	public IconMenu getKitMenu() {
		IconMenu menu = new IconMenu(
				RandomChatColor.getColor(ChatColor.GOLD, ChatColor.GREEN, ChatColor.BLUE, ChatColor.RED, ChatColor.DARK_AQUA, ChatColor.YELLOW) + "Choose a Kit",
				((KitManager.getKits().size() / 9) * 9) + 18,
				new IconMenu.OptionClickEventHandler()
				{

					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						final Player p = event.getPlayer();
						final CPlayer cp = CPlayerManager.getCrankedPlayer(p);
						if (ChatColor.stripColor(event.getName()).equalsIgnoreCase("None"))
						{
							p.sendMessage(Msgs.Kits_None.getString(true));

							if (event.getClickType().isRightClick())
							{
								Bukkit.getScheduler().scheduleSyncDelayedTask(Cranked.me, new Runnable()
								{

									public void run() {
										cp.openMenu(kitMenu);
									}
								}, 2);
							}
						} else if (p.hasPermission("Cranked.Kits") || p.hasPermission("Cranked.Kits." + event.getName()))
						{
							if (KitManager.isRegistered(KitManager.getKit(ChatColor.stripColor(event.getName()))))
							{
								p.sendMessage(Msgs.Kits_Chosen.getString(true, "<kit>", event.getName()));
								cp.setKit(KitManager.getKit(ChatColor.stripColor(event.getName())));

								if (cp.getArena().getGameState() == GameState.Started)
									p.sendMessage("Wait till you respawn");
								if (cp.getArena().getGameState() == GameState.PreGame)
									Equip.equip(p);
								if (event.getClickType().isRightClick())
								{
									Bukkit.getScheduler().scheduleSyncDelayedTask(Cranked.me, new Runnable()
									{

										public void run() {
											cp.openMenu(kitMenu);
										}
									}, 2);
								}
							}

						} else
							p.sendMessage(Msgs.Error_Misc_No_Permission.getString(true));
					}
				}, Cranked.me);
		int i = 0;
		for (Kit kit : KitManager.getKits())
		{
			ItemStack item = kit.getIcon();
			menu.setOption(i, item, kit.getName(), ChatColor.DARK_AQUA + Msgs.Menu_Kits_Click_To_Choose.getString(false), "", ChatColor.GRAY + "Helmet: " + ChatColor.GREEN + StringUtil.getWord(kit.getHelmet().getType().name()), ChatColor.GRAY + "Chestplate: " + ChatColor.GREEN + StringUtil.getWord(kit.getChestplate().getType().name()), ChatColor.GRAY + "Leggings: " + ChatColor.GREEN + StringUtil.getWord(kit.getLeggings().getType().name()), ChatColor.GRAY + "Boots: " + ChatColor.GREEN + StringUtil.getWord(kit.getBoots().getType().name()));
			i++;
		}
		menu.setOption(i, new ItemStack(Material.CAKE), "None", Msgs.Menu_Kits_Click_For_None.getString(false));
		return menu;
	}
}
