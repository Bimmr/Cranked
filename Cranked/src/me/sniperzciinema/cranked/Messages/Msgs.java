
package me.sniperzciinema.cranked.Messages;

import me.sniperzciinema.cranked.Tools.Files;

import org.bukkit.ChatColor;


public enum Msgs
{
	Format_Header("Format.Header"),	// <title>>
	Format_Line("Format.Line"),
	Format_Prefix("Format.Prefix"),
	Arena_Arenas("Arena.Arenas"),
	Arena_Information("Arena.Information"),
	Command_Arena_Created("Command.Arena.Created")/* <arena> */,
	Command_Arena_Removed("CommandArena.Removed")/* <arena> */,
	Command_Arena_Set("Command.Arena.Set")/* <arena> */,
	Command_Spawn_Set("Command.Spawn.Set")/* <spawn> */,
	Command_Spawn_Tp("Command.Spawn.Tp")/* <spawn> */,
	Command_Spawn_Spawns("Command.Spawn.Spawns")/* <spawns> */,
	Command_Spawn_Deleted("Command.Spawn.Deleted")/* <spawn> */,
	Command_Info_Players("Command.Info.Players")/* <players> */,
	Command_Info_State("Command.Info.State")/* <state> */,
	Error_Misc_No_Permission("Error.Misc.No Permission"),
	Error_Misc_Plugin_Unloaded("Error.Misc.Plugin Unloaded"),
	Error_Misc_Plugin_Disabled("Error.Misc.Plugin Disabled"),
	Error_Misc_Use_Command("Error.Misc.Cant Use Command"),
	Error_Misc_Not_Player("Error.Misc.Not Player"),
	Error_Misc_Unkown_Command("Error.Misc.Unkown Command"),
	Error_Game_Started("Error.Game.Started"),
	Error_Game_Not_Started("Error.Game.Not Started"),
	Error_Game_Not_In("Error.Game.Not In"),
	Error_Game_In("Error.Game.In"),
	Error_Top_Not_Stat("Error.Top.Not Stat")/* <stats> */,
	Error_Arena_Doesnt_Exist("Error.Arena.Doesnt Exist")/* <arena> */,
	Error_Arena_Not_Valid("Error.Arena.Not Valid")/* <arena> */,
	Error_Arena_Already_Exists("Error.Arena.Already Exist"),
	Error_Arena_No_Spawns("Error.Arena.No Spawns"),
	Error_Sign_Not_Valid("Error.Sign.Not Valid"),
	Game_Time_Left("Game.Time Left")/* <time> */,
	Game_Joined_You("Game.Joined.You"),
	Game_Joined_They("Game.Joined.They")/* <player> */,
	Game_Left_You("Game.Left.You"),
	Game_Left_They("Game.Left.They")/* <player> */,
	Game_End_Not_Enough_Players("Game.End.Not Enough Players"),
	Help_Lists("Help.Lists")/* <lists> */,
	Help_TpSpawn("Help.TpSpawn"),
	Help_DelSpawn("Help.DelSpawn"),
	Help_SetSpawn("Help.SetSpawn"),
	Help_Create("Help.Create"),
	Help_Remove("Help.Remove"),
	Help_SetArena("Help.SetArena"),
	Help_Info("Help.Info"),
	Help_Top("Help.Top")/* <stats> */,

	Waiting_Players_Needed("Waiting.Players Needed"),
	Before_Game_Time_Left("Before Game.Time Left"),
	Before_Game_Please_Wait("Before Game.Please Wait"),
	Before_Game_Death("Before Game.Death"),
	Command_Admin_Reload("Command.Admin.Reload"),
	Command_Admin_Stat_Changed("Command.Admin.Stat Changed"),
	Game_Over_Ended("Game Over.Ended"),
	Game_Over_Times_Up("Game Over.Times Up"),
	Game_Over_Winners("Game Over.Winners"),
	Game_Started("Game.Started"), 
	Kits_None("Kits.None"), 
	Kits_Chosen("Kits.Chosen"), 
	Menu_Kits_Click_To_Choose("Menu.Kits.Click To Choose"),
	Menu_Kits_Click_For_None("Menu.Kits.Click For None");

	private String string;

	private Msgs(String s)
	{
		string = s;
	}

	public String getString(boolean usePrefix, String... variables) {
		String prefix = ChatColor.translateAlternateColorCodes('&', Files.getMessages().getString("Format.Prefix").replaceAll("&x", "&" + String.valueOf(RandomChatColor.getColor().getChar())).replaceAll("&y", "&" + String.valueOf(RandomChatColor.getFormat().getChar()))) + " ";
		try
		{
			String message = (usePrefix ? prefix : "") + ChatColor.translateAlternateColorCodes('&', Files.getMessages().getString(string).replaceAll("&x", "&" + String.valueOf(RandomChatColor.getColor().getChar())).replaceAll("&y", "&" + String.valueOf(RandomChatColor.getFormat().getChar()))) + " ";
			int i = 0;
			String replace = null;
			for (String variable : variables)
			{
				if (i == 0)
				{
					replace = variable;
					i++;
				} else
				{
					message = message.replaceAll(replace, variable);
					i = 0;
				}
			}
			return message;
		} catch (NullPointerException npe)
		{
			return prefix + "Unable to find message: " + string;
		}
	}
};
