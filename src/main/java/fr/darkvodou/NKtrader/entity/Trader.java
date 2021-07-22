package fr.darkvodou.NKtrader.entity;

import org.bukkit.World;

public class Trader
{
	private String name;
	private double x;
	private double y;
	private double z;
	private String id;
	private World world;
	private String type;
	private String entityType;
	private String blockType;

	public Trader(String name, double x, double y, double z, World world)
	{
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = name + x + y + z;
		this.world = world;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setWorld(World world)
	{
		this.world = world;
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

	public void setX(double x)
	{
		this.x = x;
	}

	public void setY(double y)
	{
		this.y = y;
	}

	public void setZ(double z)
	{
		this.z = z;
	}

	public void setCoords(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String getName()
	{
		return name;
	}

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}

	public double getZ()
	{
		return z;
	}

	public String getId()
	{
		return id;
	}

	public World getWorld()
	{
		return world;
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
}
