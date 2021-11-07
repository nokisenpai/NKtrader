package fr.darkvodou.NKtrader.enums;

import static fr.darkvodou.NKtrader.enums.MsgUtils.PREFIX_USAGE;

/**
 *
 */
public enum Usages
{
	NKT_USAGE_ADDTRADER(PREFIX_USAGE + "/addtrader <entity | block> [-n <name>] [-w <world> <x> <y> <z>]"),
	NKT_USAGE_REMOVETRADER(PREFIX_USAGE + "/removetrader :\n Remove the trader at the cursor position");

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
