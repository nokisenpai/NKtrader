package fr.darkvodou.NKtrader.managers;

import fr.darkvodou.NKtrader.entity.Trader;
import org.bukkit.command.ConsoleCommandSender;

import java.util.HashMap;

import static fr.darkvodou.NKtrader.enums.Msgmanager.*;

public class TraderManager
{
	private HashMap<String, Trader> traders = new HashMap<>();
	public ConsoleCommandSender consoleCommandSender;

	public TraderManager(ConsoleCommandSender consoleCommandSender)
	{this.consoleCommandSender = consoleCommandSender;}

	public boolean hasTrader(String name){return traders.containsKey(name);}
	public void addTrader(String name, Trader trader)
	{
		if(hasTrader(name))
			consoleCommandSender.sendMessage("" + ERROR_TRADER_IS_CONTAINED);
		else
		{
			traders.put(name,trader);
		}
	}
	public void removeTrader(String name)
	{
		if(!hasTrader(name))
		{
			consoleCommandSender.sendMessage("" + ERROR_TRADER_NOT_FOUND + name);
		}
		else
		{
			traders.remove(name);
		}
	}
	public Trader getTrader(String name){return traders.get(name);}
	public HashMap<String, Trader> getTraders(){return this.traders;}

	//TODO Load Traders in bdd
}
