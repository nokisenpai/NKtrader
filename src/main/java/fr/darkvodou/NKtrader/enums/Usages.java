package fr.darkvodou.NKtrader.enums;

import static fr.darkvodou.NKtrader.enums.MsgUtils.*;

public enum Usages
{
	PLUGIN_NAME("NKtrader"),


	//#########################
	//Usages for commands
	//#########################

	NKT_USAGE_ADDTRADER(PREFIX_USAGE + "/addtrader <entity|block> [-n <name>] [-w <world> <x> <y> <z> ]"),


	;

	private String name;

	Usages(String name)
	{
		this.name = name;
	}

	public String toString()
	{
		return name;
	}
}
