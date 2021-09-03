package fr.darkvodou.NKtrader;

import fr.darkvodou.NKtrader.cmds.addTraderCmd;
import fr.darkvodou.NKtrader.listeners.LeftClick;
import fr.darkvodou.NKtrader.managers.Manager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

import static fr.darkvodou.NKtrader.enums.MsgUtils.PREFIX_SUCCESS;

public final class NKtrader extends JavaPlugin
{
	private static NKtrader plugin = null;
	private ConsoleCommandSender console = null;
	private Manager manager = null;
	private ArrayList<Listener> listeners = new ArrayList<>();

	@Override
	public void onEnable()
	{
		plugin = this;
		this.saveDefaultConfig();
		console = Bukkit.getConsoleSender();
		manager = new Manager(this);

		//Load all managers
		if(!manager.loadManagers(plugin))
		{
			disablePlugin();

			return;
		}

		//create event

		Listener LeftClick = new LeftClick(manager.getTraderManager());
		listeners.add(LeftClick);

		//register event

		for(Listener event : listeners)
		{
			getServer().getPluginManager().registerEvents(event,this);
		}

		//Register commands
		getCommand("addtrader").setExecutor(new addTraderCmd(manager));

		console.sendMessage(PREFIX_SUCCESS + ": Ready !");
		console.sendMessage(PREFIX_SUCCESS + ": " + Material.CHEST.toString());
	}

	@Override
	public void onDisable()
	{
		manager.unloadManagers();
	}

	public static NKtrader getPlugin(){return plugin;}
	public void disablePlugin()
	{
		getServer().getPluginManager().disablePlugin(this);
	}
}
