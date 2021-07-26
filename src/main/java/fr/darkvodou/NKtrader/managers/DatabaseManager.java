package fr.darkvodou.NKtrader.managers;

import fr.darkvodou.NKtrader.NKtrader;
import fr.darkvodou.NKtrader.utils.SQLConnect;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.sql.*;

import static fr.darkvodou.NKtrader.enums.MsgUtils.*;
import static fr.darkvodou.NKtrader.managers.ConfigManager.PREFIX_CONFIG;

public class DatabaseManager
{
	private static Connection bdd = null;

	private ConsoleCommandSender console = null;
	private fr.darkvodou.NKtrader.managers.ConfigManager configManager = null;

	public DatabaseManager(fr.darkvodou.NKtrader.managers.ConfigManager configManager)
	{
		this.console = Bukkit.getConsoleSender();
		this.configManager = configManager;
	}

	public enum table
	{
		TRADER(PREFIX_CONFIG + "trader");

		private String name = "";

		table(String name)
		{
			this.name = name;
		}

		public String toString()
		{
			return name;
		}

		public static int size()
		{
			return table.values().length;
		}
	}

	public boolean load()
	{
		SQLConnect.setInfo(configManager.getDbHost(), configManager.getDbPort(), configManager.getDbName(), configManager.getDbUser(), configManager.getDbPassword());

		try
		{
			bdd = SQLConnect.getHikariDS().getConnection();
		}
		catch(SQLException e)
		{
			bdd = null;
			console.sendMessage(PREFIX_ERROR + " Error while attempting database connexion. Verify your access informations in config.yml");
			e.printStackTrace();

			return false;
		}

		try
		{
			if(!existTables())
			{
				createTable();
			}
		}
		catch(SQLException e)
		{
			console.sendMessage(PREFIX_ERROR + " Error while creating database structure. (Error#A.2.002)");

			return false;
		}

		return true;
	}

	public void unload()
	{
		if(bdd != null)
		{
			try
			{
				bdd.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	private boolean existTables() throws SQLException
	{
		// Select all tables beginning with the prefix
		String req = "SHOW TABLES FROM " + configManager.getDbName() + " LIKE '" + PREFIX_CONFIG + "%'";
		ResultSet resultat = null;
		PreparedStatement ps = null;

		try
		{
			ps = bdd.prepareStatement(req);
			resultat = ps.executeQuery();
			int count = 0;
			while(resultat.next())
			{
				count++;
			}

			// if all tables are missing
			if(count == 0)
			{
				resultat.close();
				ps.close();
				console.sendMessage(PREFIX_ERROR + " Missing table(s). First start.");

				return false;
			}
			resultat.close();
			ps.close();

			// if 1 or more tables are missing
			if(count < table.size())
			{
				console.sendMessage(PREFIX_ERROR + " Missing table(s). Please don't alter tables name or structure in database. (Error#main.Storage.002)");

				return false;
			}
		}
		catch(SQLException e1)
		{
			console.sendMessage(PREFIX_ERROR+ " Error while testing existance of tables. (Error#main.Storage.003)");
			NKtrader.getPlugin().disablePlugin();
		}
		finally
		{
			if(ps != null)
			{
				ps.close();
			}

			if(resultat != null)
			{
				resultat.close();
			}
		}

		return true;
	}

	private void createTable() throws SQLException
	{
		try
		{
			bdd = getConnection();

			String req = null;
			Statement s = null;

			console.sendMessage(PREFIX_SUCCESS + " Creating Database structure ...");

			try
			{
				req = "CREATE TABLE IF NOT EXISTS `"
						+ table.TRADER
						+ "`( `id` VARCHAR(100) NOT NULL ,"
						+ "`x` DOUBLE NOT NULL ,"
						+ "`y` DOUBLE NOT NULL ,"
						+ "`z` DOUBLE NOT NULL ,"
						+ "`name` VARCHAR NOT NULL ,"
						+ "`type` VARCHAR NOT NULL ,"
						+ "`data_type` VARCHAR NULL ,"
						+ "`world_name` VARCHAR NOT NULL ,"
						+ "PRIMARY KEY (`id`))"
						+ "ENGINE = InnoDB";

				s = bdd.createStatement();
				s.execute(req);
				s.close();

				console.sendMessage(PREFIX_SUCCESS + " Database structure created.");
			}
			catch(SQLException e)
			{
				console.sendMessage(PREFIX_ERROR + " Error while creating database structure. (Error#main.Storage.000)");
				e.printStackTrace();
			}
			finally
			{
				if(s != null)
				{
					s.close();
				}
			}
		}
		catch(SQLException e)
		{
			console.sendMessage(PREFIX_ERROR + " Error while creating database structure. (Error#main.Storage.001)");
		}
	}

	// Getter 'bdd'
	public static Connection getConnection()
	{
		try
		{
			if(!bdd.isValid(1))
			{
				if(!bdd.isClosed())
				{
					bdd.close();
				}
				bdd = SQLConnect.getHikariDS().getConnection();
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return bdd;
	}
}
