
package me.sniperzciinema.cranked.GameMechanics;

import me.sniperzciinema.cranked.Game;
import me.sniperzciinema.cranked.Handlers.Arena.Arena;
import me.sniperzciinema.cranked.Handlers.Arena.ArenaManager.GameType;
import me.sniperzciinema.cranked.Handlers.Player.CPlayer;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager.Team;
import me.sniperzciinema.cranked.Messages.KillMessages;

import org.bukkit.entity.Player;


public class Deaths {

	// Method that handles all deaths in the arenas
	public static void playerDies(Player killer, Player killed, DeathTypes death) {

		CPlayer cKiller = null;
		CPlayer cKilled = null;
		Arena arena = null;
		// Only do the killers parts if the killer isn't here, this way we can
		// still make them suicide safely
		if (killer != null)
		{
			cKiller = CPlayerManager.getCrankedPlayer(killer);
			arena = cKiller.getArena();
			// Restart their last kill timer
			cKiller.getTimer().restartTimer();
			// Update their overall stats
			cKiller.updateStats(1, 0, cKiller.getScore() + cKiller.getArena().getSettings().getScorePerKill());
			// Update their current game kills
			cKiller.setKills(cKiller.getKills() + 1);
			// Set the players Points accordingly
			cKiller.setPoints(cKiller.getPoints() + 1 + (cKiller.getKillstreak() >= 1 ? 1 : 0));
			// Set the players killstreak to add one
			cKiller.setKillstreak(cKiller.getKillstreak() + 1);
			// Update their speed depending on their new killStreak total
			cKiller.speed();
			cKiller.getScoreBoard().showProper();
		}
		if (killed != null)
		{
			if (death == DeathTypes.OutOfTime)
				killed.getWorld().createExplosion(killed.getLocation(), -1);

			cKilled = CPlayerManager.getCrankedPlayer(killed);
			arena = cKilled.getArena();
			if (Stats.getHighestKillStreak(killed.getName()) < cKilled.getKillstreak())
				Stats.setHighestKillStreak(killed.getName(), cKilled.getKillstreak());
			cKilled.setKillstreak(0);
			cKilled.resetSpeed();
			cKilled.getTimer().stopTimer();
			cKilled.updateStats(0, 1, 0);
			cKilled.setDeaths(cKilled.getDeaths() + 1);
			// Respawn the player
			cKilled.respawn(true);
			cKilled.getScoreBoard().showProper();
		}

		// Tell players that <killer> killed <killed>
		for (Player p : arena.getPlayers())
			p.sendMessage(KillMessages.getKillMessage(killer, killed, death));

		// Check if they reached the max kills, if so end the game
		if (cKiller != null)
		{
			if ((arena.getGameType() == GameType.TDM && (arena.getTeamPoints(Team.A) >= cKiller.getArena().getSettings().getPointsToWin() || arena.getTeamPoints(Team.B) >= cKiller.getArena().getSettings().getPointsToWin()))
					&& (arena.getGameType() == GameType.FFA && cKiller.getPoints() >= cKiller.getArena().getSettings().getPointsToWin()))
				Game.end(cKiller.getArena(), false);
		}
	}

}
