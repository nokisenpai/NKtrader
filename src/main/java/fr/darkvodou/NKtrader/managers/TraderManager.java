package fr.darkvodou.NKtrader.managers;

import fr.darkvodou.NKtrader.entity.Trader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
		String dataType = trader.getDataType();

		queueManager.addToQueue(o -> {
			Connection bdd = null;
			PreparedStatement ps = null;
			String req = null;

			try
			{
				bdd = DatabaseManager.getConnection();

				req = "INSERT INTO " + DatabaseManager.table.TRADER
						+ " (`id`,`x`,`y`,`z`,`name`,`type`,`dataType`,`world_name`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				ps = bdd.prepareStatement(req);
				ps.setString(1, id);
				ps.setInt(2, x);
				ps.setInt(3, y);
				ps.setInt(4, z);
				ps.setString(5, name);
				ps.setString(6, type);
				ps.setString(7, dataType);
				ps.setString(8, world_name);

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

		queueManager.addToQueue(o -> {
			Connection bdd = null;
			PreparedStatement ps = null;
			String req = null;

			try
			{
				bdd = DatabaseManager.getConnection();

				req = "DELETE " + DatabaseManager.table.TRADER + "WHERE `id` = `" + id + "`";
				ps = bdd.prepareStatement(req);

				ps.execute();
				ps.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}

			return null;
		});
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
		Connection bdd = null;
		ResultSet result = null;
		PreparedStatement ps = null;
		String req = null;

		bdd = DatabaseManager.getConnection();

		try
		{
			req = "SELECT * FROM" + DatabaseManager.table.TRADER;

			ps = bdd.prepareStatement(req);
			result = ps.executeQuery();

			while(result.next())
			{
				String world_name = result.getString("world_name");
				Location location = new Location(Bukkit.getWorld(world_name), result.getDouble("x"), result.getDouble("y"), result.getDouble("z"));
				Trader trader = new Trader(location, result.getString("type"), result.getString("data_type"));

				traders.put(trader.getId(), trader.setName(result.getString("name")));
			}
			ps.close();

			return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();

			return false;
		}
	}
}
