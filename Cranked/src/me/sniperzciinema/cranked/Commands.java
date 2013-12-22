
package me.sniperzciinema.cranked;

import java.util.List;

import me.sniperzciinema.cranked.Extras.Menus;
import me.sniperzciinema.cranked.GameMechanics.Agility;
import me.sniperzciinema.cranked.GameMechanics.DeathTypes;
import me.sniperzciinema.cranked.GameMechanics.Deaths;
import me.sniperzciinema.cranked.GameMechanics.Stats;
import me.sniperzciinema.cranked.Handlers.Arena.Arena;
import me.sniperzciinema.cranked.Handlers.Arena.ArenaManager;
import me.sniperzciinema.cranked.Handlers.Arena.GameState;
import me.sniperzciinema.cranked.Handlers.Items.ItemHandler;
import me.sniperzciinema.cranked.Handlers.Location.LocationHandler;
import me.sniperzciinema.cranked.Handlers.Player.CPlayer;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;
import me.sniperzciinema.cranked.Messages.Msgs;
import me.sniperzciinema.cranked.Messages.StringUtil;
import me.sniperzciinema.cranked.Messages.Time;
import me.sniperzciinema.cranked.Tools.Files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commands implements CommandExecutor {

	Main plugin;

	public Commands(Main plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("Cranked"))
		{
			Player p = null;
			CPlayer cp = null;
			if (sender instanceof Player)
			{
				p = (Player) sender;
				cp = CPlayerManager.getCrankedPlayer(p);
			}

			// //////////////////////////////JOIN///////////////////////////////////
			if (args.length > 0 && args[0].equalsIgnoreCase("Join"))
			{
				if (p == null)
					sender.sendMessage("Expected a player!");

				else if (!sender.hasPermission("Cranked.Join") && !sender.hasPermission("Cranked.Join." + args[1]))
					sender.sendMessage(Msgs.Error_Misc_No_Permission.getString(true));

				else if (cp.getArena() != null)

					sender.sendMessage(Msgs.Error_Game_Not_In.getString(true));

				else if (args.length >= 2)
				{
					String arenaName = args[1];
					if (ArenaManager.arenaRegistered(arenaName))
					{
						if (ArenaManager.isArenaValid(arenaName))
						{
							Game.join(cp, ArenaManager.getArena(StringUtil.getWord(arenaName)));
							Arena arena = cp.getArena();
							// Info the players of their current situation
							p.sendMessage(Msgs.Format_Line.getString(false));
							p.sendMessage("");
							p.sendMessage(Msgs.Game_Joined_You.getString(true, "<arena>", cp.getArena().getName()));
							p.sendMessage(Msgs.Arena_Information.getString(true, "<arena>", arena.getName(), "<creator>", arena.getCreator()));
							p.sendMessage("");
							if (arena.getState() == GameState.Waiting)
								p.sendMessage(Msgs.Waiting_Players_Needed.getString(true, "<current>", String.valueOf(arena.getPlayers().size()), "<needed>", String.valueOf(arena.getSettings().getRequiredPlayers())));
							p.sendMessage("");
							p.sendMessage(Msgs.Format_Line.getString(false));

							for (Player ppl : cp.getArena().getPlayers())
								if (ppl != cp.getPlayer()){
									ppl.sendMessage(Msgs.Game_Joined_They.getString(true, "<player>", p.getName(), "<arena>", cp.getArena().getName()));
									CPlayerManager.getCrankedPlayer(ppl).getScoreBoard().showProper();
								}
							cp.speed();
						} else
							sender.sendMessage(Msgs.Error_Arena_No_Spawns.getString(true, "<arena>", arenaName));
					} else
						sender.sendMessage(Msgs.Error_Arena_Doesnt_Exist.getString(true, "<arena>", arenaName));
				} else
					Menus.chooseArena(p);
			}
			// //////////////////////////////LEAVE///////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Leave"))
			{
				if (p == null)
					sender.sendMessage(Msgs.Error_Misc_Not_Player.getString(false));

				else if (!p.hasPermission("Cranked.Leave"))
					p.sendMessage(Msgs.Error_Misc_No_Permission.getString(true));

				else if (cp.getArena() == null)
					p.sendMessage(Msgs.Error_Game_Not_In.getString(true));

				else
				{
					Arena arena = cp.getArena();
					Game.leave(cp);

					// Tell the player they left
					p.sendMessage(Msgs.Format_Line.getString(false));
					p.sendMessage("");
					p.sendMessage(Msgs.Game_Left_You.getString(true, "<arena>", arena.getName()));
					p.sendMessage("");
					p.sendMessage(Msgs.Format_Line.getString(false));
					// Update the other players on the situation
					for (Player ppl : arena.getPlayers())
					{
						CPlayerManager.getCrankedPlayer(ppl).getScoreBoard().showProper();
						ppl.sendMessage(Msgs.Game_Left_They.getString(true, "<player>", cp.getName(), "<arena>", arena.getName()));
					}

					Agility.resetSpeed(p);
				}
			}

			// //////////////////////////////CREATE///////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Create"))
			{

				if (p == null)
					sender.sendMessage(Msgs.Error_Misc_Not_Player.getString(false));

				if (!sender.hasPermission("Cranked.Create"))
					sender.sendMessage(Msgs.Error_Misc_No_Permission.getString(true));

				else if (args.length >= 2)
				{
					String arena = StringUtil.getWord(args[1]);
					if (!ArenaManager.arenaRegistered(arena))
					{

						ArenaManager.createArena(arena);
						p.sendMessage(Msgs.Format_Line.getString(false));
						p.sendMessage(Msgs.Command_Arena_Created.getString(true, "<arena>", arena));
						p.sendMessage(Msgs.Help_SetSpawn.getString(true));
						cp.setCreating(arena);

						if (args.length == 3)
							ArenaManager.getArena(arena).setCreator(args[2]);

						else
							ArenaManager.getArena(StringUtil.getWord(arena)).setCreator("Unkown");

						p.sendMessage(Msgs.Format_Line.getString(false));

					} else
						p.sendMessage(Msgs.Error_Game_In.getString(true, "<arena>", arena));

				} else
					p.sendMessage(Msgs.Help_Create.getString(true));

			}

			// //////////////////////////////REMOVE///////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Remove"))
			{
				if (!sender.hasPermission("Cranked.Remove"))
					sender.sendMessage(Msgs.Error_Misc_No_Permission.getString(true));

				else if (args.length >= 2)
				{
					String arena = StringUtil.getWord(args[1]);

					if (ArenaManager.arenaRegistered(arena))
					{
						ArenaManager.removeArena(args[1]);

						sender.sendMessage(Msgs.Command_Arena_Removed.getString(true, "<arena>", arena));
					} else
						sender.sendMessage(Msgs.Error_Arena_Doesnt_Exist.getString(true, "<arena>", arena));

				} else
					sender.sendMessage(Msgs.Help_Remove.getString(true));
			}
			// //////////////////////////////ARENAS///////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Arenas"))
			{
				if (!sender.hasPermission("Cranked.Arenas"))
					sender.sendMessage(Msgs.Error_Misc_No_Permission.getString(true));
				else
				{
					sender.sendMessage(Msgs.Format_Header.getString(false, "<title>", "Arenas"));
					sender.sendMessage(Msgs.Arena_Arenas.getString(true, "<validarenas>", ArenaManager.getPossibleArenas(), "<notvalidarenas>", ArenaManager.getNotPossibleArenas()));
					sender.sendMessage(Msgs.Format_Line.getString(false));
				}
			}
			// //////////////////////////////SETSPAWN///////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("SetSpawn"))
			{
				if (p == null)
					sender.sendMessage(Msgs.Error_Misc_Not_Player.getString(true));

				else if (!p.hasPermission("Cranked.SetSpawn"))
					p.sendMessage(Msgs.Error_Misc_No_Permission.getString(true));

				else
				{
					String arena = cp.getCreating();
					System.out.println(arena);
					if (ArenaManager.arenaRegistered(arena))
					{
						ArenaManager.setSpawn(arena, p.getLocation());
						p.sendMessage(Msgs.Command_Spawn_Set.getString(true, "<spawn>", String.valueOf(ArenaManager.getArena(arena).getSpawns().size())));
					} else
						p.sendMessage(Msgs.Help_SetArena.getString(true));
				}
			}
			// //////////////////////////////SETARENA///////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("SetArena"))
			{
				if (p == null)
					sender.sendMessage(Msgs.Error_Misc_Not_Player.getString(true));

				else if (!p.hasPermission("Cranked.SetArena"))
					p.sendMessage(Msgs.Error_Misc_No_Permission.getString(true));

				else
				{
					if (args.length == 2)
					{
						String arenaName = StringUtil.getWord(args[1]);
						if (ArenaManager.arenaRegistered(args[1]))
						{
							cp.setCreating(arenaName);
							p.sendMessage(Msgs.Command_Arena_Set.getString(true, "<arena>", arenaName));
						} else
							sender.sendMessage(Msgs.Error_Arena_Doesnt_Exist.getString(true, "<arena>", arenaName));

					} else
						p.sendMessage(Msgs.Help_SetArena.getString(true));
				}
			}
			// //////////////////////////////ADMIN///////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Admin"))
			{

				if (!sender.hasPermission("Infected.Admin"))
					sender.sendMessage(Msgs.Error_Misc_No_Permission.getString(true));

				else
				{
					if (args.length == 2)
					{
						// RELOAD
						if (args[1].equalsIgnoreCase("Reload"))
						{
							Files.reloadConfig();
							Files.reloadMessages();
							Files.reloadPlayers();
							sender.sendMessage(Msgs.Command_Admin_Reload.getString(true));
						}
						// CODE
						else if (args[1].equalsIgnoreCase("Code"))
						{
							p.sendMessage(Msgs.Format_Prefix.getString(false) + "Code: " + ChatColor.WHITE + ItemHandler.getItemStackToString(((Player) sender).getItemInHand()));
							p.sendMessage(Msgs.Format_Prefix.getString(false) + "This code has also been sent to your console to allow for copy and paste!");
							System.out.println(ItemHandler.getItemStackToString(((Player) sender).getItemInHand()));
						} else
							p.sendMessage(Msgs.Error_Misc_Unkown_Command.getString(true));
					} else if (args.length == 3)
					{
						if (args[1].equalsIgnoreCase("Kick"))
						{
							Player user = Bukkit.getPlayer(args[2]);
							if (user == null || !CPlayerManager.isInArena(user))
							{
								p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.RED + "The player must be playing Cranked");
							} else
							{
								user.performCommand("Cranked Leave");
								user.sendMessage("You been kicked");
								p.sendMessage(Msgs.Format_Prefix.getString(false) + "You have kicked " + user.getName() + " from Cranked");
							}
						} else
							p.sendMessage(Msgs.Error_Misc_Unkown_Command.getString(true));
					} else if (args.length == 4)
					{
						String user = args[2];

						int i = Integer.parseInt(args[3]);

						if (args[1].equalsIgnoreCase("Score"))
						{
							Stats.setScore(user, Stats.getScore(user) + i);
							p.sendMessage(Msgs.Command_Admin_Stat_Changed.getString(true, "<stat>", "Score", "<value>", String.valueOf(Stats.getScore(user))));
						} else if (args[1].equalsIgnoreCase("kStats"))
						{
							Stats.setKills(user, Stats.getKills(user) + i);
							p.sendMessage(Msgs.Command_Admin_Stat_Changed.getString(true, "<stat>", "Kills", "<value>", String.valueOf(Stats.getKills(user))));
						} else if (args[1].equalsIgnoreCase("DStats"))
						{
							Stats.setDeaths(user, Stats.getDeaths(user) + i);
							p.sendMessage(Msgs.Command_Admin_Stat_Changed.getString(true, "<stat>", "Deaths", "<value>", String.valueOf(Stats.getDeaths(user))));
						} else
							p.sendMessage(Msgs.Error_Misc_Unkown_Command.getString(true));

					} else
					{
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.GREEN + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "======" + ChatColor.GOLD + " Admin Menu " + ChatColor.GREEN + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "======");
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.AQUA + "/Inf Admin Points <Player> <#>");
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add points to a p(Also goes negative)");
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.BLUE + "/Inf Admin Score <Player> <#>");
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add score to a p(Also goes negative)");
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.DARK_AQUA + "/Inf Admin KStats <Player> <#>");
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add kills to a p(Also goes negative)");
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.DARK_BLUE + "/Inf Admin DStats <Player> <#>");
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add deaths to a p(Also goes negative)");
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.DARK_GRAY + "/Inf Admin Kick <Player>");
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Kick a p out of Cranked");
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.DARK_GREEN + "/Inf Admin Reset <Player>");
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Reset a p's stats");
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.DARK_PURPLE + "/Inf Admin Shutdown");
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Prevent joining Cranked");
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.DARK_RED + "/Inf Admin Reload");
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Reload the config");
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.GOLD + "/Inf Admin Code");
						p.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "See Cranked's item code for the item in hand");
					}
				}
			}

			// //////////////////////////////////////INFO///////////////////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Info"))
			{

				if (!sender.hasPermission("Cranked.Info"))
					sender.sendMessage(Msgs.Error_Misc_No_Permission.getString(true));

				else if (args.length == 2)
				{
					if (ArenaManager.arenaRegistered(args[1]))
					{
						Arena arena = ArenaManager.getArena(args[1]);
						sender.sendMessage("");
						sender.sendMessage(Msgs.Format_Header.getString(false, "<title>", arena.getName() + " Information"));
						sender.sendMessage(Msgs.Command_Info_Players.getString(true, "<current>", String.valueOf(arena.getPlayers().size()), "<max>", String.valueOf(arena.getSettings().getMaxPlayers())));
						sender.sendMessage(Msgs.Command_Info_State.getString(true, "<state>", String.valueOf(arena.getState())));
						sender.sendMessage(Msgs.Game_Time_Left.getString(true, "<time>", String.valueOf(arena.getState() == GameState.Started ? Time.getTime((long) arena.getTimer().getTimeLeft()) : "N/A")));
						sender.sendMessage(Msgs.Arena_Creator.getString(true, "<creator>", String.valueOf(arena.getCreator())));
					} else
						sender.sendMessage(Msgs.Error_Arena_Doesnt_Exist.getString(true, "<arena>", args[1]));
				} else
					sender.sendMessage(Msgs.Help_Info.getString(true));

			}

			// ////////////////////////////////////////////////SUICIDE///////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Suicide"))
			{
				if (!(sender instanceof Player))
					sender.sendMessage(Msgs.Error_Misc_Not_Player.getString(false));

				else if (!sender.hasPermission("Cranked.Suicide"))
					sender.sendMessage(Msgs.Error_Misc_No_Permission.getString(true));

				else if (cp.getArena() != null)
					Deaths.playerDies(null, cp.getPlayer(), DeathTypes.Other);

				else
					p.sendMessage(Msgs.Error_Game_Not_In.getString(true));
			}
			// /////////////////////////////////////////////HELP//////////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Help"))
			{
				if (!sender.hasPermission("Cranked.Help"))
					sender.sendMessage(Msgs.Error_Misc_No_Permission.getString(false));
				else
				{
					sender.sendMessage("");
					sender.sendMessage(Msgs.Format_Header.getString(false, "<title>", "Cranked Help"));
					sender.sendMessage("Put help things here");
				}
			}
			// //////////////////////////////////////////////STATS/////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Stats"))
			{

				if (!(sender instanceof Player))
					sender.sendMessage(Msgs.Error_Misc_Not_Player.getString(false));

				if (!p.hasPermission("Cranked.Stats"))
					p.sendMessage(Msgs.Error_Misc_No_Permission.getString(true));

//				if (args.length != 1)
//				{
//					if (!p.hasPermission("Cranked.Stats.Other"))
//						p.sendMessage(Msgs.Error_Misc_No_Permission.getString(true));
//					
//					String user = args[1].toLowerCase();
//					p.sendMessage("");
//					p.sendMessage(Msgs.Format_Header.getString(false, "<title>", user));
//					p.sendMessage(Msgs.getString("<value>", String.valueOf(Stats.getScore(user))));
//					p.sendMessage(Msgs.Stats_Highest_KillStreak.getString("<value>", String.valueOf(Stats.getHighestKillStreak(user))));
//					p.sendMessage(Msgs.Stats_Kills.getString("<value>", String.valueOf(Stats.getKills(user))));
//					p.sendMessage(Msgs.Stats_Deaths.getString("<value>", String.valueOf(Stats.getDeaths(user))));
//					p.sendMessage(Msgs.Stats_Playing_Time.getString("<value>", String.valueOf(Time.getTime((long) Stats.getPlayingTime(user)))));
//				} else
//				{
//					String user = p.getName().toLowerCase();
//					p.sendMessage("");
//					p.sendMessage(Msgs.Format_Header.getString("<title>", user));
//					p.sendMessage(Msgs.Stats_Score.getString("<value>", String.valueOf(Stats.getScore(user))));
//					p.sendMessage(Msgs.Stats_Highest_KillStreak.getString("<value>", String.valueOf(Stats.getHighestKillStreak(user))));
//					p.sendMessage(Msgs.Stats_Kills.getString("<value>", String.valueOf(Stats.getKills(user))));
//					p.sendMessage(Msgs.Stats_Deaths.getString("<value>", String.valueOf(Stats.getDeaths(user))));
//					p.sendMessage(Msgs.Stats_Playing_Time.getString("<value>", String.valueOf(Time.getTime((long) Stats.getPlayingTime(user)))));
//				}
			}

			// //////////////////////////////////////////////TPSPAWN////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("TpSpawn"))
			{
				if (!(sender instanceof Player))
					sender.sendMessage(Msgs.Error_Misc_Not_Player.getString(false));

				if (!p.hasPermission("Cranked.SetUp"))
					p.sendMessage(Msgs.Error_Misc_No_Permission.getString(true));

				if (cp.getCreating() == null)
					p.sendMessage(Msgs.Help_SetArena.getString(true));

				if (args.length != 1)
				{
					try
					{
						int spawn = Integer.valueOf(args[1]);
						List<String> list = ArenaManager.getArena(cp.getCreating()).getSpawns();
						Location loc = LocationHandler.getPlayerLocation(list.get(spawn - 1));
						p.teleport(loc);
						p.sendMessage(Msgs.Command_Spawn_Tp.getString(true, "<spawn>", String.valueOf(spawn)));
					} catch (NumberFormatException NFE)
					{
						p.sendMessage(Msgs.Help_TpSpawn.getString(true));
					}
				} else
					p.sendMessage(Msgs.Help_TpSpawn.getString(true));

			}
			// ////////////////////////////////////////DELSPAWN/////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("DelSpawn"))
			{
				if (!(sender instanceof Player))
					sender.sendMessage(Msgs.Error_Misc_Not_Player.getString(true));

				if (!p.hasPermission("Cranked.SetUp"))
					p.sendMessage(Msgs.Error_Misc_No_Permission.getString(true));

				if (cp.getCreating() == null)
					p.sendMessage(Msgs.Help_SetArena.getString(true));

				if (args.length != 1)
				{
					try
					{
						int spawn = Integer.valueOf(args[1]);
						String arenaName = cp.getCreating();
						List<String> list = ArenaManager.getArena(arenaName).getSpawns();
						list.remove(spawn - 1);
						Files.getArenas().set("Arenas." + arenaName + ".Spawns", list);
						Files.saveArenas();
						p.sendMessage(Msgs.Command_Spawn_Deleted.getString(true, "<spawn>", String.valueOf(spawn)));
					} catch (NumberFormatException NFE)
					{
						p.sendMessage(Msgs.Help_DelSpawn.getString(true));
					}
				} else
					p.sendMessage(Msgs.Help_DelSpawn.getString(true));
			}

			// //////////////////////////////////SPAWNS/////////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Spawns"))
			{

				if (!(sender instanceof Player))
					sender.sendMessage(Msgs.Error_Misc_Not_Player.getString(true));

				if (!p.hasPermission("Cranked.Spawns"))
					p.sendMessage(Msgs.Error_Misc_No_Permission.getString(true));

				if (cp.getCreating() == null)
					p.sendMessage(Msgs.Help_SetArena.getString(true));

				List<String> list = ArenaManager.getArena(cp.getCreating()).getSpawns();
				p.sendMessage(Msgs.Command_Spawn_Spawns.getString(true, "<spawns>", String.valueOf(list.size())));
			} else
			{
				CommandSender player = sender;
				player.sendMessage("");
				player.sendMessage(Msgs.Format_Header.getString(false, "<title>", "Cranked"));
				if (Main.update)
					player.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.RED + ChatColor.BOLD + "Update Available: " + ChatColor.WHITE + ChatColor.BOLD + Main.name);
				player.sendMessage("");
				player.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.GRAY + "Author: " + ChatColor.GREEN + ChatColor.BOLD + "SniperzCiinema");
				player.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.GRAY + "Version: " + ChatColor.GREEN + ChatColor.BOLD + plugin.getDescription().getVersion());
				player.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.GRAY + "BukkitDev: " + ChatColor.GREEN + ChatColor.BOLD + "Link to come...");
				player.sendMessage(Msgs.Format_Prefix.getString(false) + ChatColor.YELLOW + "For Help type: /Cranked Help");

			}
		}
		return true;
	}
}