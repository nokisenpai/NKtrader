package fr.darkvodou.NKtrader.enums;

import static fr.darkvodou.NKtrader.enums.MsgUtils.PREFIX;

public enum Msgmanager
{

	ERROR_CONFIG_USE_SQL_IS_FALSE(PREFIX + " Disabled because this plugin only use MySQL database. Please set to true the 'use-mysql' field in config.yml");

	private String name;
	Msgmanager(String name)
	{
		this.name = name;
	}
	public String toString(){return name;}
}




