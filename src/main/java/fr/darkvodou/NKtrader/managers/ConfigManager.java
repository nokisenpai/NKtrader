package fr.darkvodou.NKtrader.managers;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import static fr.darkvodou.NKtrader.enums.Msgmanager.*;

public class ConfigManager
{
	private ConsoleCommandSender console = null;
	private FileConfiguration config = null;

	private String dbHost = null;
	private int dbPort = 3306;
	private String dbName = null;
	private String dbUser = null;
	private String dbPassword = null;

	public static String PREFIXTABLE = null;
	public static String SERVERNAME = null;

	// Constructor
	public ConfigManager(FileConfiguration config)
	{
		this.console = Bukkit.getConsoleSender();
		this.config = config;
	}

	public boolean load()
	{
		if(!config.getBoolean("use-mysql"))
		{
			console.sendMessage("" + ERROR_CONFIG_USE_SQL_IS_FALSE);

			return false;
		}

		// Get database access informations
		dbHost = config.getString("host");
		dbPort = config.getInt("port");
		dbName = config.getString("dbName");
		dbUser = config.getString("user");
		dbPassword = config.getString("password");

		// Get prefix used for table name on database
		PREFIXTABLE = config.getString("table-prefix", "nktrader_");

		// Get server name gave to bungeecord config
		SERVERNAME = config.getString("server-name", "world");

		return true;
	}

	public String getDbHost()
	{
		return dbHost;
	}

	public int getDbPort()
	{
		return dbPort;
	}

	public String getDbName()
	{
		return dbName;
	}

	public String getDbUser()
	{
		return dbUser;
	}

	public String getDbPassword()
	{
		return dbPassword;
	}
}
