
package me.sniperzciinema.cranked.Listeners;

import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager.Team;
import me.sniperzciinema.cranked.Messages.RandomChatColor;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;


public class NameTags implements Listener {

	@EventHandler
	public void onNameTag(AsyncPlayerReceiveNameTagEvent event) {
		if (CPlayerManager.isInArena(event.getNamedPlayer()))
		{
			if (CPlayerManager.getCrankedPlayer(event.getNamedPlayer()).getTeam() == Team.A)
				event.setTag(ChatColor.RED + event.getNamedPlayer().getName());
			else if (CPlayerManager.getCrankedPlayer(event.getNamedPlayer()).getTeam() == Team.B)
				event.setTag(ChatColor.BLUE + event.getNamedPlayer().getName());
			else
				event.setTag(RandomChatColor.getColor() + event.getNamedPlayer().getName());
				
		}
	}

}
