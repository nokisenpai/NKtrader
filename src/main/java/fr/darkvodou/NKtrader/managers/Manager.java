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


	public Manager(NKtrader instance)
	{
		console = Bukkit.getConsoleSender();
		queueManager = new QueueManager();
		configManager = new ConfigManager(instance.getConfig());
		databaseManager = new DatabaseManager(configManager);
	}

	// ######################################
	// Getters & Setters
	// ######################################

	// Console
	public ConsoleCommandSender getConsole()
	{
		return console;
	}

	// QueueManager
	public fr.darkvodou.NKtrader.managers.QueueManager getQueueManager()
	{
		return queueManager;
	}

	// PluginManager
	public fr.darkvodou.NKtrader.managers.ConfigManager getConfigManager()
	{
		return configManager;
	}

	// DatabaseManager
	public fr.darkvodou.NKtrader.managers.DatabaseManager getDatabaseManager()
	{
		return databaseManager;
	}
}
