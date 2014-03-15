
package me.sniperzciinema.cranked.Tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import me.sniperzciinema.cranked.Cranked;


public class MySQLManager {

	/**
	 * 
	 * @param tableName
	 *            - Always put "Cranked"
	 * @param columnName
	 *            - The stats name
	 * @param playerName
	 * @return the players stats
	 */
	public static int getInt(String tableName, String columnName, String playerName) {
		try
		{
			Statement statement = Cranked.connection.createStatement();
			ResultSet set = statement.executeQuery("SELECT " + columnName + " FROM " + tableName + " WHERE Player = '" + playerName + "';");
			int i = 0;
			set.next();
			i = set.getInt(columnName);
			set.close();
			return i;
		} catch (SQLException e)
		{
			setInt(tableName, columnName, 0, playerName);
			return 0;
		}
	}

	/**
	 * 
	 * Safley update/set the value
	 * 
	 * Will set the value only if the table doesn't have the player already,
	 * otherwise it'll just update the players values
	 * 
	 * @param tableName
	 *            - Always put "Cranked"
	 * @param columnName
	 *            - The stats name
	 * @param value
	 * @param playerName
	 */
	public static void update(String tableName, String columnName, int value, String playerName) {
		try
		{
			Statement statement = Cranked.connection.createStatement();
			statement.execute("UPDATE " + tableName + " SET " + columnName + "=" + value + " WHERE Player ='" + playerName + "';");
			statement.close();
		} catch (SQLException e)
		{
			setInt(tableName, columnName, value, playerName);
		}
	}

	/**
	 * 
	 * Force the setting of the players value
	 * 
	 * @param tableName
	 *            - Always put "Cranked"
	 * @param columnName
	 *            - The stats name
	 * @param value
	 * @param playerName
	 */
	private static void setInt(String tableName, String columnName, int value, String playerName) {
		try
		{
			Statement statement = Cranked.connection.createStatement();
			statement.execute("INSERT INTO " + tableName + " (`Player`, `" + columnName + "`) VALUES ('" + playerName + "', '" + value + "');");

			statement.close();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param tableName
	 *            - Always put "Cranked"
	 * @return All the players in the Cranked table
	 */
	public static ArrayList<String> getPlayers(String tableName) {
		try
		{
			Statement statement = Cranked.connection.createStatement();
			ResultSet set = statement.executeQuery("SELECT * FROM `infected` ");
			ArrayList<String> players = new ArrayList<String>();
			while (true)
			{
				set.next();
				players.add(set.getString("Player"));
				if (set.isLast())
					break;
			}
			set.close();
			return players;
		} catch (SQLException e)
		{
			ArrayList<String> nope = new ArrayList<String>();
			return nope;
		}
	}
}
