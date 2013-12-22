
package me.sniperzciinema.cranked.Tools;

import java.util.List;

import me.sniperzciinema.cranked.Handlers.Arena.ArenaManager;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;


public class TeleportFix implements Listener {

	private Server server;
	private Plugin plugin;

	private final int TELEPORT_FIX_DELAY = 15; // ticks

	public TeleportFix(Plugin plugin)
	{
		this.plugin = plugin;
		this.server = plugin.getServer();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event) {

		final Player player = event.getPlayer();
		// Fix the visibility issue one tick later
		server.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
		{

			@Override
			public void run() {
				// Refresh nearby clients
				final List<Player> nearby = ArenaManager.getArena(player).getPlayers();

				// Hide every player
				updateEntities(player, nearby);

				// Then show them again
				server.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
				{

					@Override
					public void run() {
						updateEntities(player, nearby);
					}
				}, 1);
			}
		}, TELEPORT_FIX_DELAY);
	}

	public void updateEntities(Player tpedPlayer, List<Player> players) {
		// Hide or show every player to tpedPlayer
		// and hide or show tpedPlayer to every player.
		for (Player player : players)
		{
			if (ArenaManager.getArena(player) != null && ArenaManager.getArena(tpedPlayer) != null)
			{
				if (!tpedPlayer.canSee(player))
					tpedPlayer.showPlayer(player);
				else
					tpedPlayer.hidePlayer(player);
				if (!player.canSee(tpedPlayer))
					player.showPlayer(tpedPlayer);
				else
					player.hidePlayer(tpedPlayer);

			}
		}
	}
}