package fr.darkvodou.NKtrader.managers;

import fr.darkvodou.NKtrader.NKtrader;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class Manager
{
	private ConsoleCommandSender console = null;
	private QueueManager queueManager = null;
	private ConfigManager configManager = null;
	private DatabaseManager databaseManager = null;
	private TraderManager traderManager = null;

	public Manager(NKtrader instance)
	{
		console = Bukkit.getConsoleSender();
		queueManager = new QueueManager();
		configManager = new ConfigManager(instance.getConfig());
		databaseManager = new DatabaseManager(configManager);
		traderManager = new TraderManager(console, this);
	}

	public ConsoleCommandSender getConsole()
	{
		return console;
	}

	public QueueManager getQueueManager()
	{
		return queueManager;
	}

	public ConfigManager getConfigManager()
	{
		return configManager;
	}

	public DatabaseManager getDatabaseManager()
	{
		return databaseManager;
	}

	public TraderManager getTraderManager()
	{
		return traderManager;
	}

	public void unloadManagers()
	{
		databaseManager.unload();
		traderManager.unload();
	}

	public boolean loadManagers(NKtrader instance)
	{
		if(!configManager.load())
		{
			instance.disablePlugin();

			return false;
		}

		if(!databaseManager.load())
		{
			instance.disablePlugin();

			return false;
		}

		if(!traderManager.load())
		{
			instance.disablePlugin();

			return false;
		}

		return true;
	}
}
