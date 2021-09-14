package fr.darkvodou.NKtrader;

import fr.darkvodou.NKtrader.cmds.addTraderCmd;
import fr.darkvodou.NKtrader.enums.MsgUtils;
import fr.darkvodou.NKtrader.listeners.LeftClick;
import fr.darkvodou.NKtrader.managers.Manager;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import static fr.darkvodou.NKtrader.enums.MsgUtils.PREFIX_SUCCESS;

public class NKtrader extends JavaPlugin
{
	private static NKtrader plugin = null;
	private Manager manager = null;
	private final List<Listener> listeners = new ArrayList<>();

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

		//create event
		listeners.add(new LeftClick(manager.getTraderManager()));

		//register event
		for(Listener event : listeners)
		{
			getServer().getPluginManager().registerEvents(event, this);
		}

		//Register commands
		PluginCommand addTraderCommand = getCommand("addtrader");

		if( addTraderCommand !=null)
		{
			addTraderCommand.setExecutor(new addTraderCmd(manager.getTraderManager()));
		}
		else
		{
			console.sendMessage(MsgUtils.ERROR_REGISTER_COMMAND + "addtrader");
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
