package fr.darkvodou.NKtrader.entity;

import fr.darkvodou.NKtrader.managers.TraderManager;
import org.bukkit.Location;

public class Trader
{
	private String id;
	private Location location;
	private String name;
	private String type;
	private String dataType;

	public Trader(Location location, String type, String dataType)
	{
		this.id = TraderManager.locationToId(location);
		this.location = location;
		this.type = type;
		this.dataType = dataType;
	}

	public Trader(Location location, String type, String dataType, String name)
	{
		this.id = location.getWorld().getName() + location.getBlockX() + location.getBlockY() + location.getBlockZ();
		this.location = location;
		this.type = type;
		this.dataType = dataType;
		this.name = name;
	}

	@SuppressWarnings("unused")
	public Trader setId(String id)
	{
		this.id = id;

		return this;
	}

	@SuppressWarnings("unused")
	public Trader setType(String type)
	{
		this.type = type;

		return this;
	}

	@SuppressWarnings("unused")
	public Trader setDataType(String dataType)
	{
		this.dataType = dataType;

		return this;
	}

	public Trader setName(String name)
	{
		this.name = name;

		return this;
	}

	@SuppressWarnings("unused")
	public Trader setLocation(Location location)
	{
		this.location = location;

		return this;
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	public String getType()
	{
		return type;
	}

	public String getDataType()
	{
		return dataType;
	}

	public Location getLocation()
	{
		return location;
	}
}
