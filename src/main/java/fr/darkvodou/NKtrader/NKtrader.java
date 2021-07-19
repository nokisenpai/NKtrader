package fr.darkvodou.NKtrader;

import org.bukkit.plugin.java.JavaPlugin;

public final class NKtrader extends JavaPlugin
{
	private static NKtrader plugin = null;

	@Override
	public void onEnable()
	{
		// Plugin startup logic

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
