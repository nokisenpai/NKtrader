package fr.darkvodou.NKtrader.cmds;

import fr.darkvodou.NKtrader.entity.Trader;
import fr.darkvodou.NKtrader.managers.Manager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.*;

import java.util.Objects;

import static fr.darkvodou.NKtrader.enums.MsgUtils.*;
import static fr.darkvodou.NKtrader.enums.Msgmanager.ERROR_TRADER_IS_CONTAINED;
import static fr.darkvodou.NKtrader.enums.Permissions.*;
import static fr.darkvodou.NKtrader.enums.Usages.*;
import static fr.darkvodou.NKtrader.utils.CheckType.isNumber;
import static java.lang.Double.parseDouble;

public class addTraderCmd implements CommandExecutor
{
	private Manager manager = null;
	private Location location = null;
	private String name = "";

	public addTraderCmd(Manager manager)
	{
		this.manager = manager;
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
	{
		if(sender instanceof ConsoleCommandSender)
		{
			return true;
		}

		if(sender instanceof Player)
		{
			if(!hasAddTraderPermissions(sender))
			{
				sender.sendMessage(ERROR_HAS_NOT_PERM + " node : " + NKT_ADD_TRADER);

				return true;
			}

			if(args.length == 0)
			{
				sender.sendMessage("" + NKT_USAGE_ADDTRADER);

				return true;
			}

			if(args.length > 8)
			{
				sender.sendMessage(ERROR_TOO_MANY_ARGS + " :\n" + NKT_USAGE_ADDTRADER);

				return true;
			}

			if(!args[0].equals("entity") && !args[0].equals("block"))
			{
				sender.sendMessage(ERROR_BAD_ARGS + " :\n" + NKT_USAGE_ADDTRADER);

				return true;
			}

			if(args.length >= 2)
			{
				if(!args[1].equals("-n") && !args[1].equals("-w"))
				{
					sender.sendMessage(ERROR_BAD_ARGS + " :\n" + NKT_USAGE_ADDTRADER);

					return true;
				}
			}

			int wPLace = getPlaceArg(args, "-w");
			int nPLace = getPlaceArg(args, "-n");

			if(wPLace != -1)
			{
				if(args.length < wPLace + 4)
				{
					sender.sendMessage(ERROR_BAD_ARGS + " :\n" + NKT_USAGE_ADDTRADER);

					return true;
				}

				if(Bukkit.getWorld(args[wPLace + 1]) == null)
				{
					sender.sendMessage(ERROR_WORLD_NOT_EXIST + "");

					return true;
				}

				for(int i = 2; i < 5; i++)
				{
					if(!isNumber(args[wPLace + i]))
					{
						sender.sendMessage(ERROR_BAD_ARGS + " :\n" + NKT_USAGE_ADDTRADER);

						return true;
					}
				}

				location = new Location(Bukkit.getWorld(args[wPLace]), parseDouble(args[wPLace + 1]), parseDouble(args[wPLace + 2]), parseDouble(args[
						wPLace + 3]));
			}
			else
			{
				location = ((Player) sender).getLocation();
			}

			if(nPLace != -1)
			{
				if(args.length < nPLace + 1)
				{
					sender.sendMessage(ERROR_BAD_ARGS + " :\n" + NKT_USAGE_ADDTRADER);

					return true;
				}

				name = args[nPLace + 1];
			}

			if(args[0].equals("entity"))
			{
				Entity entity = Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(location, EntityType.VILLAGER);

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

				Trader trader = new Trader(location, args[0], "villager");

				if(!name.equals(""))
				{
					trader.setName(name);
					entity.setCustomName(name);
					entity.setCustomNameVisible(true);
				}

				if(!manager.getTraderManager().addTrader(trader))
				{
					sender.sendMessage(ERROR_TRADER_IS_CONTAINED + ", look at console");
				}

				return true;
			}
			else
			{
				if(location.getBlock().getType().isAir())
				{
					location.getBlock().setType(Material.CHEST);
					Trader trader = new Trader(location, args[0], "");
				}
				else
				{
					//todo
				}
			}
		}

		return true;
	}

	private int getPlaceArg(String[] args, String argToFind)
	{
		int i = 0;

		for(String arg : args)
		{
			if(arg.equals(argToFind))
			{

				return i;
			}

			i++;
		}

		return -1;
	}

	private boolean hasAdminPermissions(CommandSender sender)
	{
		return sender.hasPermission("" + NKT_ADMIN) || sender.hasPermission("" + NKT_ALL);
	}

	private boolean hasAddTraderPermissions(CommandSender sender)
	{
		return sender.hasPermission("" + NKT_ADD_TRADER) || hasAdminPermissions(sender);
	}
}
