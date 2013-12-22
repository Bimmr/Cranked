
package me.sniperzciinema.cranked.Listeners;

import me.sniperzciinema.cranked.Game;
import me.sniperzciinema.cranked.Handlers.Player.CPlayer;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class RegisterAndUnRegister implements Listener {

//When a player joins the server, create a CPlayer for them
	@EventHandler(priority = EventPriority.NORMAL)
	public void onJoinCreateCrankedPlayer(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		CPlayer cp = new CPlayer(p);
		CPlayerManager.loadCrankedPlayer(cp);
	}

//When a player leaves the server willingly, delete the CPlayer of them
	@EventHandler(priority = EventPriority.NORMAL)
	public void onLeaveDeleteCrankedPlayer(PlayerQuitEvent e) {
		CPlayer cp = CPlayerManager.getCrankedPlayer(e.getPlayer());
		if (cp.getArena() != null)
			Game.leave(cp);
		CPlayerManager.deleteCrankedPlayer(cp);
	}

	// When a player leaves the server by kick, delete the CPlayer of them
	@EventHandler(priority = EventPriority.NORMAL)
	public void onKickedDeleteCrankedPlayer(PlayerKickEvent e) {
		CPlayer cp = CPlayerManager.getCrankedPlayer(e.getPlayer());
		if (cp.getArena() != null)
			Game.leave(cp);
		CPlayerManager.deleteCrankedPlayer(cp);
	}

}
