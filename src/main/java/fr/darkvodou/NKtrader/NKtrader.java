package fr.darkvodou.NKtrader;

import fr.darkvodou.NKtrader.cmds.addTraderCmd;
import org.bukkit.plugin.java.JavaPlugin;

public final class NKtrader extends JavaPlugin
{
	private static NKtrader plugin = null;

	@Override
	public void onEnable()
	{
		//Register commands
		getCommand("addtrader").setExecutor(new addTraderCmd());
	}

	@Override
	public void onDisable()
	{
		// Plugin shutdown logic
	}

	public static NKtrader getPlugin(){return plugin;}
	public void disablePlugin()
	{
		getServer().getPluginManager().disablePlugin(this);
	}
}
