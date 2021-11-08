package fr.darkvodou.NKtrader;

import fr.darkvodou.NKtrader.cmds.AddTraderCmd;
import fr.darkvodou.NKtrader.cmds.RemoveTraderCmd;
import fr.darkvodou.NKtrader.enums.MsgUtils;
import fr.darkvodou.NKtrader.listeners.LeftClick;
import fr.darkvodou.NKtrader.managers.ListenerManager;
import fr.darkvodou.NKtrader.managers.Manager;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import static fr.darkvodou.NKtrader.enums.MsgUtils.PREFIX_ERROR;
import static fr.darkvodou.NKtrader.enums.MsgUtils.PREFIX_SUCCESS;

public class NKtrader extends JavaPlugin
{
	private static NKtrader plugin = null;
	private Manager manager = null;

	@Override
	public void onEnable()
	{
		ConsoleCommandSender console = Bukkit.getConsoleSender();
		plugin = this;
		this.saveDefaultConfig();
		manager = new Manager(this);

		//Load all managers
		if(!manager.loadManagers(plugin))
		{
			disablePlugin();

			return;
		}

		//events
		ListenerManager listenerManager = manager.getListenerManager();
		listenerManager.add(new LeftClick(manager.getTraderManager()), "leftClick");

		listenerManager.registerAll();

		//Register commands
		PluginCommand addTraderCommand = getCommand("addtrader");
		PluginCommand removeTraderCommand = getCommand("removetrader");

		if(addTraderCommand != null)
		{
			addTraderCommand.setExecutor(new AddTraderCmd(manager.getTraderManager()));
		}
		else
		{
			console.sendMessage(MsgUtils.ERROR_REGISTER_COMMAND + "addtrader");
			console.sendMessage(PREFIX_ERROR + "disabling plugin");
			disablePlugin();
		}

		if(removeTraderCommand != null)
		{
			removeTraderCommand.setExecutor(new RemoveTraderCmd(manager.getTraderManager(), manager.getConfigManager()));
		}
		else
		{
			console.sendMessage(MsgUtils.ERROR_REGISTER_COMMAND + "removeTrader");
			console.sendMessage(PREFIX_ERROR + "disabling plugin");
			disablePlugin();
		}

		console.sendMessage(PREFIX_SUCCESS + ": Ready !");
	}

	@Override
	public void onDisable()
	{
		manager.unloadManagers();
	}

	public static NKtrader getPlugin()
	{
		return plugin;
	}

	public void disablePlugin()
	{
		getServer().getPluginManager().disablePlugin(this);
	}
}
