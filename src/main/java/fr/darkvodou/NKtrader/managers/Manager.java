package fr.darkvodou.NKtrader.managers;

import fr.darkvodou.NKtrader.NKtrader;
import fr.darkvodou.NKtrader.enums.MsgUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class Manager
{
	private ConsoleCommandSender console;
	private QueueManager queueManager;
	private ConfigManager configManager;
	private DatabaseManager databaseManager;
	private TraderManager traderManager;
	private ListenerManager listenerManager;

	public Manager(NKtrader plugin)
	{
		console = Bukkit.getConsoleSender();
		queueManager = new QueueManager();
		configManager = new ConfigManager(plugin.getConfig());
		databaseManager = new DatabaseManager(configManager);
		traderManager = new TraderManager(console, this);
		listenerManager = new ListenerManager(plugin);
	}

	public ListenerManager getListenerManager()
	{
		return listenerManager;
	}

	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
	public DatabaseManager getDatabaseManager()
	{
		return databaseManager;
	}

	public TraderManager getTraderManager()
	{
		return traderManager;
	}

	@SuppressWarnings("unused")
	public void setListenerManager(ListenerManager listenerManager)
	{
		this.listenerManager = listenerManager;
	}

	@SuppressWarnings("unused")
	public void setConsole(ConsoleCommandSender console)
	{
		this.console = console;
	}

	@SuppressWarnings("unused")
	public void setQueueManager(QueueManager queueManager)
	{
		this.queueManager = queueManager;
	}

	@SuppressWarnings("unused")
	public void setConfigManager(ConfigManager configManager)
	{
		this.configManager = configManager;
	}

	@SuppressWarnings("unused")
	public void setDatabaseManager(DatabaseManager databaseManager)
	{
		this.databaseManager = databaseManager;
	}

	@SuppressWarnings("unused")
	public void setTraderManager(TraderManager traderManager)
	{
		this.traderManager = traderManager;
	}

	public void unloadManagers()
	{
		databaseManager.unload();
		traderManager.unload();
	}

	public boolean loadManagers(NKtrader plugin)
	{
		if(!configManager.load())
		{
			console.sendMessage(MsgUtils.PREFIX_ERROR + "Fail to load ConfigManager");
			plugin.disablePlugin();

			return false;
		}

		if(!databaseManager.load())
		{
			console.sendMessage(MsgUtils.PREFIX_ERROR + "Fail to load databaseManager");
			plugin.disablePlugin();

			return false;
		}

		if(!traderManager.load())
		{
			console.sendMessage(MsgUtils.PREFIX_ERROR + "Fail to load traderManager");
			plugin.disablePlugin();

			return false;
		}

		return true;
	}
}
