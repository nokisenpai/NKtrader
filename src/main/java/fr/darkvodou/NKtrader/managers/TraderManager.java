package fr.darkvodou.NKtrader.managers;

import fr.darkvodou.NKtrader.entity.Trader;
import org.bukkit.command.ConsoleCommandSender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import static fr.darkvodou.NKtrader.enums.Msgmanager.*;

public class TraderManager
{
	private HashMap<String, Trader> traders = new HashMap<>();
	private ConsoleCommandSender console;
	private QueueManager queueManager = null;
	private DatabaseManager databaseManager = null;

	public TraderManager(ConsoleCommandSender console, Manager manager)
	{
		this.console = console;
		this.queueManager = manager.getQueueManager();
		this.databaseManager = manager.getDatabaseManager();
	}

	public boolean hasTrader(String id)
	{
		return traders.containsKey(id);
	}

	public void addTrader(Trader trader)
	{
		String id = trader.getId();

		if(hasTrader(id))
		{
			console.sendMessage(ERROR_TRADER_IS_CONTAINED + " : " + id);

			return;
		}

		int x = trader.getLocation().getBlockX();
		int y = trader.getLocation().getBlockY();
		int z = trader.getLocation().getBlockZ();
		String name = trader.getName();
		String type = trader.getType();
		String world_name = trader.getLocation().getWorld().getName();
		String block_type = "NULL";
		String entity_type = "NULL";

		if(type.equals("block"))
		{
			block_type = trader.getBlockType();
		}

		if(type.equals("entity"))
		{
			entity_type = trader.getEntityType();
		}

		String finalEntity_type = entity_type;
		String finalBlock_type = block_type;

		queueManager.addToQueue(o -> {
			Connection bdd = null;
			PreparedStatement ps = null;
			String req = null;

			try
			{
				bdd = DatabaseManager.getConnection();

				req = "INSERT INTO " + DatabaseManager.table.TRADER + " (`id`,`x`,`y`,`z`,`name`,`type`,`entity_type`,`block_type`,`world_name`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				ps = bdd.prepareStatement(req);
				ps.setString(1, id);
				ps.setInt(2, x);
				ps.setInt(3, y);
				ps.setInt(4, z);
				ps.setString(5, name);
				ps.setString(6, type);
				ps.setString(7, finalEntity_type);
				ps.setString(8, finalBlock_type);
				ps.setString(9, world_name);

				ps.executeUpdate();
				ps.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}

			return null;
		});

		traders.put(id, trader);
	}

	public void removeTrader(String id)
	{
		if(!hasTrader(id))
		{
			console.sendMessage(ERROR_TRADER_NOT_FOUND + id);

			return;
		}
		//TODO remove in the bdd
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

	public void unload()
	{
		traders.clear();
	}

	public boolean load()
	{
		return true;
	}

	//TODO Load Traders in bdd
}
