package fr.darkvodou.NKtrader.enums;

import static fr.darkvodou.NKtrader.enums.MsgUtils.*;

public enum Msgmanager
{
	ERROR_CONFIG_USE_SQL_IS_FALSE(PREFIX_ERROR + " Disabled because this plugin only use MySQL database. Please set to true the 'use-mysql' field in config.yml"),
	ERROR_TRADER_IS_CONTAINED(PREFIX_ERROR + "The trader is already set in the TraderManager"),
	ERROR_TRADER_IS_SET(PREFIX_ERROR + "A trader is already here"),
	ERROR_TRADER_NOT_FOUND(PREFIX_ERROR + "The following trader isn't register : ");

	private final String name;
	Msgmanager(String name)
	{
		this.name = name;
	}
	public String toString(){return name;}
}




