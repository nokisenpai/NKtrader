package fr.darkvodou.NKtrader.enums;

public enum Permissions
{
	PLUGIN_NAME("NKtrader"),


	//#########################
	//Permissions admins
	//#########################

	NKT_ADMIN(PLUGIN_NAME + ".admin"),
	NKT_ALL(PLUGIN_NAME + ".*"),

	//#########################
	//Permissions for commands
	//#########################

	NKT_ADD_TRADER(PLUGIN_NAME + ".addtrader");

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
