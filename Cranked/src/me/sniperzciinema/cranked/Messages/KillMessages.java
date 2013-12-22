
package me.sniperzciinema.cranked.Messages;

import java.util.List;
import java.util.Random;

import me.sniperzciinema.cranked.GameMechanics.DeathTypes;
import me.sniperzciinema.cranked.Handlers.Player.CPlayerManager;
import me.sniperzciinema.cranked.Tools.Files;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class KillMessages {

	public static String getKillMessage(Player killer, Player killed, DeathTypes death) {

		String msg = getKill(death);

		// Replace color codes, and killer and killed names
		msg = ChatColor.translateAlternateColorCodes('&', msg.replaceAll("&x", "&" + String.valueOf(RandomChatColor.getColor().getChar())).replaceAll("&y", "&" + String.valueOf(RandomChatColor.getFormat().getChar())));
		if (killer != null)
			msg = msg.replaceAll("<killer>", killer.getName() + "(" + CPlayerManager.getCrankedPlayer(killer).getPoints() + ")");
		if (killed != null)
			msg = msg.replaceAll("<killed>", killed.getName() + "(" + CPlayerManager.getCrankedPlayer(killed).getPoints() + ")");
		return Msgs.Format_Prefix.getString(false) + msg;
	}

	private static String getKill(DeathTypes death) {
		String message;
		List<String> list;
		if (death == DeathTypes.Other || death == DeathTypes.OutOfTime)
			list = Files.getMessages().getStringList("Suicides");
		else
			list = Files.getMessages().getStringList("Kills." + death.toString());
		Random r = new Random();
		int i = r.nextInt(list.size());
		message = list.get(i);
		return message;
	}
}
