package fr.darkvodou.NKtrader.entity;

import org.bukkit.Location;

public class Trader
{
	private String id;
	private Location location;
	private String name;
	private String type;
	private String entityType;
	private String blockType;

	public Trader(Location location)
	{
		this.id = location.getWorld().getName() + location.getBlockX() + location.getBlockY() + location.getBlockZ();
		this.location = location;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setEntityType(String entityType)
	{
		this.entityType = entityType;
	}

	public void setBlockType(String blockType)
	{
		this.blockType = blockType;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setLocation(Location location)
	{
		this.location = location;
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

	public String getEntityType()
	{
		return entityType;
	}

	public String getBlockType()
	{
		return blockType;
	}

	public Location getLocation()
	{
		return location;
	}
}
