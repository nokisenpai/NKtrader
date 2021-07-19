package fr.darkvodou.NKtrader.enums;

public enum Permissions
{
	PLUGIN_NAME("NKtrader"),

	NKT_ADMIN(PLUGIN_NAME + ".admin"),
	NKT_ALL(PLUGIN_NAME + ".*");

	private String name;

	Permissions(String name)
	{
		this.name = name;
	}

	public String toString()
	{
		return name;
	}
}
