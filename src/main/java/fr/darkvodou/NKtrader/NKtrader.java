package fr.darkvodou.NKtrader;

import fr.darkvodou.NKtrader.cmds.addTraderCmd;
import fr.darkvodou.NKtrader.managers.Manager;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import static fr.darkvodou.NKtrader.enums.MsgUtils.PREFIX_SUCCESS;

public final class NKtrader extends JavaPlugin
{
	private static NKtrader plugin = null;
	private ConsoleCommandSender console = null;
	private Manager manager = null;

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

		//Register commands
		getCommand("addtrader").setExecutor(new addTraderCmd());

		console.sendMessage(PREFIX_SUCCESS + " :Ready !");
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
