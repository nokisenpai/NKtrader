package fr.darkvodou.NKtrader.managers;

import fr.darkvodou.NKtrader.NKtrader;
import fr.darkvodou.NKtrader.utils.SQLConnect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import java.sql.*;

import static fr.darkvodou.NKtrader.enums.MsgUtils.*;
import static fr.darkvodou.NKtrader.enums.MsgUtils.PREFIX_ERROR;

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
		PT(fr.darkvodou.NKtrader.managers.ConfigManager.PREFIX + "pt"), PLAYERS("DV_players"), PLAYER_DATA(
			fr.darkvodou.NKtrader.managers.ConfigManager.PREFIX + "player_data"), MAIL(
			fr.darkvodou.NKtrader.managers.ConfigManager.PREFIX + "mail");

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

	public boolean loadDatabase()
	{
		// Setting database informations
		SQLConnect.setInfo(configManager.getDbHost(), configManager.getDbPort(), configManager.getDbName(), configManager.getDbUser(), configManager.getDbPassword());

		// Try to connect to database
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
			// Check if tables already exist on database
			if(!existTables())
			{
				// Create database structure if not exist
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

	public void unloadDatabase()
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
		String req = "SHOW TABLES FROM " + configManager.getDbName() + " LIKE '" + fr.darkvodou.NKtrader.managers.ConfigManager.PREFIX + "%'";
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
				console.sendMessage(PREFIX_ERROR + " Missing table(s). First start.");
				resultat.close();
				ps.close();
				return false;
			}
			resultat.close();
			ps.close();

			req = "SHOW TABLES FROM " + configManager.getDbName() + " LIKE 'DV_Players'";
			ps = bdd.prepareStatement(req);
			resultat = ps.executeQuery();
			if(resultat.next())
			{
				count++;
			}

			// if 1 or more tables are missing
			if(count < table.size())
			{
				console.sendMessage(PREFIX_ERROR
						+ " Missing table(s). Please don't alter tables name or structure in database. (Error#main.Storage.002)");
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
				/*// Creating players table
				req = "CREATE TABLE IF NOT EXISTS `" + table.PLAYERS + "` (`id` int(11) NOT NULL AUTO_INCREMENT,"
						+ "`uuid` varchar(40) NOT NULL,`pseudo` varchar(40) NOT NULL,`server` varchar(40) ,PRIMARY KEY (`id`),"
						+ "UNIQUE KEY `uuid_unique` (`uuid`) USING BTREE) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
				s = bdd.createStatement();
				s.execute(req);
				s.close();

				// Creating pt table
				req = "CREATE TABLE IF NOT EXISTS `" + table.PT + "` (`id` int(11) NOT NULL AUTO_INCREMENT,"
						+ "`player_id` int(11) NOT NULL,`cmd` VARCHAR(1000) NULL,`item` VARCHAR(100) NULL, PRIMARY KEY (`id`),"
						+ "UNIQUE INDEX `pt_player_id_item_UNIQUE` (`player_id` ASC, `item` ASC) USING BTREE) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
				s = bdd.createStatement();
				s.execute(req);
				s.close();

				// Creating player_data table
				req = "CREATE TABLE IF NOT EXISTS `" + table.PLAYER_DATA + "` ( `player_id` INT NOT NULL ,"
						+ " `bettermending` BOOLEAN NOT NULL DEFAULT FALSE ," + " `fly_speed` FLOAT NOT NULL ," + " `walk_speed` FLOAT NOT NULL ,"
						+ " UNIQUE (`player_id`)) ENGINE = InnoDB;";
				s = bdd.createStatement();
				s.execute(req);
				s.close();

				*/

				// Creating mail table
				req = "CREATE TABLE IF NOT EXISTS `" + table.MAIL
						+ "` ( `id` INT NOT NULL AUTO_INCREMENT, `receiver` VARCHAR(40) NOT NULL, `sender` VARCHAR(40) "
						+ "NOT NULL, `date_of_receipt` DATE NOT NULL, `mail` TEXT NOT NULL, INDEX(`id`)) ENGINE = InnoDB";
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
