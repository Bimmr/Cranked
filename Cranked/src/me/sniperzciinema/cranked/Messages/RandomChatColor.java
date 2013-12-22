
package me.sniperzciinema.cranked.Messages;

import java.util.Random;

import org.bukkit.ChatColor;


/**
 * If anyone wants to use this class for their own plugin go ahead, this is a
 * class you can also find in my gists(On Github). If you have any
 * suggestions/additions that you think would help Infected feel free to message
 * me or fork the gist
 * 
 * @author Sniperz
 * 
 */

public class RandomChatColor {

	public static ChatColor getColor(ChatColor... chatColors) {
		Random r = new Random();
		ChatColor[] colors;
		if (chatColors.length == 0)
			colors = ChatColor.values();
		else
			colors = chatColors;
		int i = r.nextInt(colors.length);
		while (!colors[i].isColor())
			i = r.nextInt(colors.length);
		ChatColor rc = colors[i];
		return rc;
	}

	public static ChatColor getFormat(ChatColor... chatColors) {
		Random r = new Random();
		ChatColor[] colors;
		if (chatColors.length == 0)
			colors = ChatColor.values();
		else
			colors = chatColors;
		int i = r.nextInt(colors.length);
		while (!colors[i].isFormat())
			i = r.nextInt(colors.length);
		ChatColor rc = colors[i];
		return rc;
	}
}