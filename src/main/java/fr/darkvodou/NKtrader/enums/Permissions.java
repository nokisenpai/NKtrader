package fr.darkvodou.NKtrader.enums;

public enum Permissions
{
	PLUGIN_NAME("NKtrader"),


	//#########################
	//Permissions admins
	//#########################

	ALL("*"),
	ADMIN(PLUGIN_NAME + "admin"),
	NKT_ALL(PLUGIN_NAME + ".*"),

	//#########################
	//Permissions for commands
	//#########################

	NKT_ADD_TRADER(PLUGIN_NAME + ".addtrader"),
	NKT_REMOVE_TRADER(PLUGIN_NAME + ".removetrader");

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
