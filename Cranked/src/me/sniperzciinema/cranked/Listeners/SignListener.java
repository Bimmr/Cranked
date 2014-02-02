
package me.sniperzciinema.cranked.Listeners;

import me.sniperzciinema.cranked.Game;
import me.sniperzciinema.cranked.Handlers.Arena.Arena;
import me.sniperzciinema.cranked.Handlers.Arena.ArenaManager;
import me.sniperzciinema.cranked.Handlers.Arena.GameState;
import me.sniperzciinema.cranked.Handlers.Player.CPlayer;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;
import me.sniperzciinema.cranked.Messages.Msgs;
import me.sniperzciinema.cranked.Messages.Time;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;


public class SignListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerClickSign(PlayerInteractEvent event) {
		if (!event.isCancelled())
		{
			Player p = event.getPlayer();
			if (p.getItemInHand().getType() == Material.BOW)
			{
				event.setUseItemInHand(Result.DEFAULT);
				event.setCancelled(false);
			} else
			{
				if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)
				{
					Block b = event.getClickedBlock();
					if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN)
					{
						Sign sign = ((Sign) b.getState());
						if (sign.getLine(0).contains("[Cranked]"))
						{
							String arenaName = ChatColor.stripColor(sign.getLine(1));
							Arena arena = ArenaManager.getArena(arenaName);
							CPlayer cp = CPlayerManager.getCrankedPlayer(p);
							Game.join(cp, arena);

							// Info the players of their current situation
							p.sendMessage(Msgs.Format_Line.getString(false));
							p.sendMessage("");
							p.sendMessage(Msgs.Game_Joined_You.getString(true, "<arena>", cp.getArena().getName()));
							p.sendMessage(Msgs.Arena_Creator.getString(true, "<creator>", arena.getCreator()));
							p.sendMessage("");
							p.sendMessage("");
							if (arena.getGameState() == GameState.Waiting)
							{
								p.sendMessage(Msgs.Waiting_Players_Needed.getString(true, "<current>", String.valueOf(arena.getPlayers().size()), "<needed>", String.valueOf(arena.getSettings().getRequiredPlayers())));
							} else if (arena.getGameState() == GameState.PreGame)
							{
								p.sendMessage(Msgs.Before_Game_Time_Left.getString(true, "<time>", Time.getTime((long) arena.getTimer().getTimeLeft())));
							} else if (arena.getGameState() == GameState.Started)
							{
								p.sendMessage(Msgs.Game_Time_Left.getString(true, "<time>", Time.getTime((long) arena.getTimer().getTimeLeft())));
							}
							p.sendMessage("");
							p.sendMessage(Msgs.Format_Line.getString(true));
							for (Player ppl : cp.getArena().getPlayers())
								if (ppl != cp.getPlayer())
									ppl.sendMessage(Msgs.Game_Joined_They.getString(true, "<player>", p.getName(), "<arena>", cp.getArena().getName()));

						}
					}
				}
			}
		}
	}

	// ======================================================================================================================

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCreateSign(SignChangeEvent event) {
		if (!event.isCancelled())
		{
			Player p = event.getPlayer();
			if (event.getLine(0).equalsIgnoreCase("[Cranked]"))
			{
				if (!p.hasPermission("Infected.Setup"))
				{
					p.sendMessage(Msgs.Error_Misc_No_Permission.getString(true));
					event.setCancelled(true);
				} else if (ArenaManager.arenaRegistered(event.getLine(1)))
				{
					Arena arena = ArenaManager.getArena(event.getLine(1));
					event.setLine(0, ChatColor.DARK_RED + "" + "[Cranked]");
					event.setLine(1, ChatColor.GREEN + arena.getName());
					event.setLine(2, ChatColor.GOLD + arena.getGameState().toString());
					event.setLine(3, ChatColor.GOLD + "" + arena.getPlayers().size() + "/" + arena.getSettings().getMaxPlayers());
				} else
				{
					p.sendMessage(Msgs.Error_Arena_Doesnt_Exist.getString(true));
				}
			}
		}
	}

}