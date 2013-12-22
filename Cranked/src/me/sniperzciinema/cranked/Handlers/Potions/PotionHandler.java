
package me.sniperzciinema.cranked.Handlers.Potions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


/**
 * If anyone wants to use this class for their own plugin go ahead, this is a
 * class you can also find in my gists(On Github). If you have any
 * suggestions/additions that you think would help Infected feel free to message
 * me or fork the gist
 * 
 * @author Sniperz
 */

@SuppressWarnings("deprecation")
public class PotionHandler {

	public static PotionEffect getPotion(String path) {
		Integer id = 0;
		Integer time = 0;
		Integer power = 0;
		String[] strings = path.split(":");
		id = Integer.valueOf(strings[0]);
		time = Integer.valueOf(strings[1]) * 20;
		power = Integer.valueOf(strings[2]) - 1;
		return new PotionEffect(PotionEffectType.getById(id), time, power);
	}

	public static ArrayList<PotionEffect> getPotions(List<String> list) {
		ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
		if (!list.isEmpty())
			for (String path : list)
			{
				Integer id = 0;
				Integer time = 0;
				Integer power = 0;
				String[] strings = path.split(":");
				id = Integer.valueOf(strings[0]);
				time = Integer.valueOf(strings[1]) * 20;
				power = Integer.valueOf(strings[2]) - 1;
				effects.add(new PotionEffect(PotionEffectType.getById(id),
						time, power));
			}
		return effects;
	}

}
