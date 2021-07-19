package fr.darkvodou.NKtrader.entity;

public class Trader
{
	private String name;
	private double x;
	private double y;
	private double z;

	public Trader(String name, double x, double y, double z)
	{
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
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
}
