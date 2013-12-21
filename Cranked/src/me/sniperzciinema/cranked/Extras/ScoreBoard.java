
package me.sniperzciinema.cranked.Extras;

import java.util.List;

import me.sniperzciinema.cranked.Messages.ScoreBoardVariables;
import me.sniperzciinema.cranked.PlayerHandlers.CPlayer;
import me.sniperzciinema.cranked.PlayerHandlers.CPlayerManager;
import me.sniperzciinema.cranked.Tools.Sort;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;


public class ScoreBoard {

	CPlayer cp;

	public ScoreBoard(CPlayer cp)
	{
		this.cp = cp;
	}

	public enum ScoreBoards{Rankings, Stats};
	private ScoreBoards showing;
	
	public ScoreBoards getShowing(){
		return showing;
	}
	public void switchScoreboards(){
		if(getShowing() == ScoreBoards.Rankings)
			showStats();
		else
			showRankings();
	}
	public void showProper(){
		if(getShowing() == ScoreBoards.Rankings)
			showRankings();
		else
			showStats();
	}
	public void showRankings(){
		showing = ScoreBoards.Rankings;
		Player player = cp.getPlayer();

		// Make sure the player is in an arena before setting
		// If they aren't clear their scoreboard, because they just left
		if (cp.getArena() == null)
		{
			player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		} else
		{
			// Create a new scoreboard
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard sb = manager.getNewScoreboard();
			Objective ob = sb.registerNewObjective("CrankedBoard", "dummy");
			ob.setDisplaySlot(DisplaySlot.SIDEBAR);

			// Now set all the scores and the title
			ob.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "Rankings");

			for(Player p : Sort.topStats(cp.getArena().getPlayers(), 10))
			{
				if(p != null){
					Score score = ob.getScore(Bukkit.getOfflinePlayer(p.getName()));
					score.setScore(CPlayerManager.getCrankedPlayer(p).getPoints());
				}
			}
			player.setScoreboard(sb);
		}
	}
	public void showStats() {
		showing = ScoreBoards.Stats;
		Player player = cp.getPlayer();

		// Make sure the player is in an arena before setting
		// If they aren't clear their scoreboard, because they just left
		if (cp.getArena() == null)
		{
			player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		} else
		{
			// Create a new scoreboard
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard sb = manager.getNewScoreboard();
			Objective ob = sb.registerNewObjective("CrankedBoard", "dummy");
			ob.setDisplaySlot(DisplaySlot.SIDEBAR);

			// Now set all the scores and the title
			ob.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + ChatColor.UNDERLINE + cp.getArena().getName());

			int row = 0;
			int spaces = 0;

			List<String> list = cp.getArena().getSettings().getScoreBoardRows();

			for (@SuppressWarnings("unused")
			// loop through all the list
			String s : list)
			{
				Score score = null;

				// Get the string my using the row
				String line = list.get(row);

				// If the line is just a space, set the offline player to a
				// color code
				// This way it'll show as a blank line, and not be merged with
				// similar color codes
				if (ScoreBoardVariables.getLine(line, player).equalsIgnoreCase(" "))
				{
					String space = "&" + spaces;
					spaces++;
					score = ob.getScore(Bukkit.getOfflinePlayer(ScoreBoardVariables.getLine(space, player)));
				} else
				{
					// If its just a regular message, just set it
					score = ob.getScore(Bukkit.getOfflinePlayer(ScoreBoardVariables.getLine(line, player)));

				}
				score.setScore(list.size() - 1 - row);
				row++;
			}
			player.setScoreboard(sb);
		}
	}
}
