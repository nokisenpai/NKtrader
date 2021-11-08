package fr.darkvodou.NKtrader.managers;

import fr.darkvodou.NKtrader.entity.Trader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static fr.darkvodou.NKtrader.enums.MsgUtils.ERROR_WORLD_NOT_EXIST;
import static fr.darkvodou.NKtrader.enums.Msgmanager.ERROR_TRADER_IS_CONTAINED;
import static fr.darkvodou.NKtrader.enums.Msgmanager.ERROR_TRADER_NOT_FOUND;

public class TraderManager
{
	private HashMap<String, Trader> traders = new HashMap<>();
	private ConsoleCommandSender console;
	private QueueManager queueManager;

	public TraderManager(ConsoleCommandSender console, Manager manager)
	{
		this.console = console;
		this.queueManager = manager.getQueueManager();
	}

	public boolean existTrader(String id)
	{
		return traders.containsKey(id);
	}

	public static String locationToId(Location location)
	{
		if(location.getWorld() == null)
		{
			return null;
		}

		return location.getWorld().getName() + location.getBlockX() + location.getBlockY() + location.getBlockZ();
	}

	public boolean existTrader(Location location)
	{
		if(location.getWorld() == null)
		{
			console.sendMessage(ERROR_WORLD_NOT_EXIST + "");

			return false;
		}

		return traders.containsKey(locationToId(location));
	}

	public boolean addTrader(Trader trader)
	{
		String id = trader.getId();

		if(trader.getLocation().getWorld() == null)
		{
			console.sendMessage(ERROR_WORLD_NOT_EXIST + "");

			return false;
		}

		if(existTrader(id))
		{
			console.sendMessage(ERROR_TRADER_IS_CONTAINED + " : " + id);

			return false;
		}

		queueManager.addToQueue(o -> {
			Connection bdd;
			PreparedStatement ps;
			String req;

			try
			{
				bdd = DatabaseManager.getConnection();

				req = "INSERT INTO " + DatabaseManager.table.TRADER
						+ " (`id`, `x`, `y`, `z`, `world_name`, `name`, `type`, `data_type`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				ps = bdd.prepareStatement(req);
				ps.setString(1, id);
				ps.setInt(2, trader.getLocation().getBlockX());
				ps.setInt(3, trader.getLocation().getBlockY());
				ps.setInt(4, trader.getLocation().getBlockZ());
				ps.setString(5, trader.getLocation().getWorld().getName());
				ps.setString(6, trader.getName());
				ps.setString(7, trader.getType());
				ps.setString(8, trader.getDataType());

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
		return true;
	}

	public void removeTrader(String id)
	{
		if(!existTrader(id))
		{
			console.sendMessage(ERROR_TRADER_NOT_FOUND + id);

			return;
		}

		queueManager.addToQueue(o -> {
			Connection bdd;
			PreparedStatement ps;
			String req;

			try
			{
				bdd = DatabaseManager.getConnection();

				req = "DELETE " + DatabaseManager.table.TRADER + " WHERE `id` = ?";
				ps = bdd.prepareStatement(req);
				ps.setString(1, id);

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

	@SuppressWarnings("unused")
	public Trader getTrader(String id)
	{
		return traders.get(id);
	}

	@SuppressWarnings("unused")
	public ConsoleCommandSender getConsole()
	{
		return console;
	}

	@SuppressWarnings("unused")
	public QueueManager getQueueManager()
	{
		return queueManager;
	}

	@SuppressWarnings("unused")
	public void setTraders(HashMap<String, Trader> traders)
	{
		this.traders = traders;
	}

	@SuppressWarnings("unused")
	public void setConsole(ConsoleCommandSender console)
	{
		this.console = console;
	}

	@SuppressWarnings("unused")
	public void setQueueManager(QueueManager queueManager)
	{
		this.queueManager = queueManager;
	}

	@SuppressWarnings("unused")
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
		Connection bdd;
		ResultSet result;
		PreparedStatement ps;
		String req;

		bdd = DatabaseManager.getConnection();

		try
		{
			req = "SELECT * FROM " + DatabaseManager.table.TRADER;

			ps = bdd.prepareStatement(req);
			result = ps.executeQuery();

			while(result.next())
			{
				String world_name = result.getString("world_name");
				Location location = new Location(Bukkit.getWorld(world_name), result.getDouble("x"), result.getDouble("y"), result.getDouble("z"));
				Trader trader = new Trader(location, result.getString("type"), result.getString("data_type"), result.getString("name"));
				traders.put(trader.getId(), trader);
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

	public boolean createTraderEntity(Trader trader)
	{
		Location location = trader.getLocation();
		String dataType = trader.getDataType();
		EntityType entityType;

		try
		{
			entityType = EntityType.valueOf(dataType.toUpperCase());
		}
		catch(Exception e)
		{
			return false;
		}

		if(location.getWorld() == null)
		{
			return false;
		}

		Entity entity = location.getWorld().spawnEntity(location, entityType);
		String name = trader.getName();

		if(entity instanceof LivingEntity)
		{
			LivingEntity livingEntity = (LivingEntity) entity;
			livingEntity.setInvulnerable(true);
			livingEntity.setGravity(false);
			livingEntity.setPersistent(true);
			livingEntity.setAI(false);
			livingEntity.setCollidable(false);
			livingEntity.setCanPickupItems(false);
		}

		if(!name.equals(""))
		{
			entity.setCustomName(name);
			entity.setCustomNameVisible(true);
		}

		return true;
	}

	public boolean createTraderBlock(Trader trader)
	{
		String dataType = trader.getType();
		Block block = trader.getLocation().getBlock();
		World world = block.getWorld();
		Location location = block.getLocation();
		String name = trader.getName();
		Material material = Material.getMaterial(dataType.toUpperCase());

		if(material == null)
		{
			return false;
		}

		if(!dataType.equals(""))
		{
			block.setType(material);
			trader = new Trader(location, trader.getType(), material.toString());
		}
		else
		{
			if(block.getType().isAir())
			{
				block.setType(Material.CHEST);
			}

			trader = new Trader(location, trader.getType(), block.getType().toString());
		}

		if(!name.equals(""))
		{
			Location locationArmorStand = location.getBlock().getLocation();
			locationArmorStand.add(0.5, 1.0, 0.5);
			Entity armorStand = world.spawnEntity(locationArmorStand, EntityType.ARMOR_STAND);

			if(armorStand instanceof LivingEntity)
			{
				LivingEntity livingEntity = (LivingEntity) armorStand;
				livingEntity.setInvisible(true);
				livingEntity.setGravity(false);
				livingEntity.setPersistent(true);
				livingEntity.setInvulnerable(true);
				livingEntity.setCollidable(false);
				((ArmorStand) livingEntity).setMarker(true);
			}

			trader.setName(name);
			armorStand.setCustomName(name);
			armorStand.setCustomNameVisible(true);
		}

		return true;
	}
}
