package fr.darkvodou.NKtrader.managers;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;

public class ListenerManager
{
	private HashMap<String, Listener> listeners = new HashMap<>();

	private Plugin plugin;

	@SuppressWarnings("unused")
	public ListenerManager(Plugin plugin)
	{
		this.plugin = plugin;
	}

	@SuppressWarnings("unused")
	public HashMap<String, Listener> getListeners()
	{
		return listeners;
	}

	@SuppressWarnings("unused")
	public Plugin getPlugin()
	{
		return plugin;
	}

	@SuppressWarnings("unused")
	public void setListeners(HashMap<String, Listener> listeners)
	{
		this.listeners = listeners;
	}

	@SuppressWarnings("unused")
	public void setPlugin(Plugin plugin)
	{
		this.plugin = plugin;
	}

	@SuppressWarnings("unused")
	public void add(Listener listener, String key)
	{
		listeners.put(key, listener);
	}

	public void remove(Listener listener, String key)
	{
		listeners.remove(key);
		unregister(listener);
	}

	@SuppressWarnings("unused")
	public void removeAll()
	{
		listeners.forEach((key, value) -> remove(value, key));
	}

	public void unregister(Listener listener)
	{
		HandlerList.unregisterAll(listener);
	}

	@SuppressWarnings("unused")
	public void unregisterAll()
	{
		HandlerList.unregisterAll();
	}

	@SuppressWarnings("unused")
	public void registerAll()
	{
		PluginManager pluginManager = Bukkit.getPluginManager();
		listeners.forEach((key, value) -> pluginManager.registerEvents(value, plugin));
	}
}
