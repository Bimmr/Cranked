
package me.sniperzciinema.cranked.Tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.sniperzciinema.cranked.Cranked;


public class MySQLManager {

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
}
