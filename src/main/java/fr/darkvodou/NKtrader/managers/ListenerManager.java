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

	private final Plugin plugin;

	public ListenerManager(Plugin plugin)
	{
		this.plugin = plugin;
	}

	public HashMap<String, Listener> getListeners()
	{
		return listeners;
	}

	public Plugin getPlugin()
	{
		return plugin;
	}

	public void setListeners(HashMap<String, Listener> listeners)
	{
		this.listeners = listeners;
	}

	public void add(Listener listener, String key)
	{
		listeners.put(key, listener);
	}

	public void remove(Listener listener, String key)
	{
		listeners.remove(key);
	}

	public void removeAll()
	{
		listeners.forEach((key, value) -> remove(value, key));
	}

	public void unregisterAll()
	{
		HandlerList.unregisterAll(plugin);
	}

	public void registerAll()
	{
		PluginManager pluginManager = Bukkit.getPluginManager();
		listeners.forEach((key, value) -> pluginManager.registerEvents(value, plugin));
	}
}
