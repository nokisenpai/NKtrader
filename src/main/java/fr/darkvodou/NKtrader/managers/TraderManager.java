package fr.darkvodou.NKtrader.managers;

import fr.darkvodou.NKtrader.entity.Trader;
import org.bukkit.command.ConsoleCommandSender;

import java.util.HashMap;

import static fr.darkvodou.NKtrader.enums.Msgmanager.*;

public class TraderManager
{
	private HashMap<String, Trader> traders = new HashMap<>();
	public ConsoleCommandSender console;

	public TraderManager(ConsoleCommandSender console)
	{
		this.console = console;
	}

	public boolean hasTrader(String id)
	{
		return traders.containsKey(id);
	}

	public void addTrader(String id, Trader trader)
	{
		if(hasTrader(id))
		{
			console.sendMessage(ERROR_TRADER_IS_CONTAINED + " : " + id);

			return;
		}

		traders.put(id, trader);
	}

	public void removeTrader(String id)
	{
		if(!hasTrader(id))
		{
			console.sendMessage(ERROR_TRADER_NOT_FOUND + id);

			return;
		}

		traders.remove(id);
	}

	public Trader getTrader(String id)
	{
		return traders.get(id);
	}

	public HashMap<String, Trader> getTraders()
	{
		return this.traders;
	}

	//TODO Load Traders in bdd
}
