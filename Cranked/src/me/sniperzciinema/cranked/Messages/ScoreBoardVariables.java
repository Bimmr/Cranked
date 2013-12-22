
package me.sniperzciinema.cranked.Messages;

import me.sniperzciinema.cranked.Handlers.Player.CPlayer;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class ScoreBoardVariables {

	public static String getLine(String string, Player user) {

		CPlayer cp = CPlayerManager.getCrankedPlayer(user);
		String newString = string;
		// Replace all variables we need

		newString = newString.replaceAll("<kills>", String.valueOf(cp.getKills()));
		newString = newString.replaceAll("<deaths>", String.valueOf(cp.getDeaths()));
		newString = newString.replaceAll("<overallkills>", String.valueOf(cp.getOverallKills()));
		newString = newString.replaceAll("<overallDeaths>", String.valueOf(cp.getOverallDeaths()));
		newString = newString.replaceAll("<killstreak>", String.valueOf(cp.getKillstreak()));
		newString = newString.replaceAll("<points>", String.valueOf(cp.getPoints()));
		newString = newString.replaceAll("<players>", String.valueOf(cp.getArena().getPlayers().size()));
		newString = newString.replaceAll("<neededplayers>", String.valueOf(cp.getArena().getSettings().getRequiredPlayers()));
		newString = newString.replaceAll("<creator>", String.valueOf(cp.getArena().getCreator()));
		newString = newString.replaceAll("<state>", String.valueOf(cp.getArena().getState()));

		// TODO: Fix kills and deaths

		// Replace color codes
		newString = ChatColor.translateAlternateColorCodes('&', ChatColor.stripColor(newString));

		// Make sure string isnt to long
		if (newString.length() > 16)
			newString = newString.substring(0, Math.min(newString.length(), 16));

		return newString;
	}
}
