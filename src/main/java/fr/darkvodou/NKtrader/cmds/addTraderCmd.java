package fr.darkvodou.NKtrader.cmds;

import fr.darkvodou.NKtrader.entity.Trader;
import fr.darkvodou.NKtrader.enums.MsgUtils;
import fr.darkvodou.NKtrader.enums.Permissions;
import fr.darkvodou.NKtrader.managers.Manager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.*;

import java.util.Objects;

import static fr.darkvodou.NKtrader.enums.MsgUtils.*;
import static fr.darkvodou.NKtrader.enums.Msgmanager.ERROR_TRADER_IS_CONTAINED;
import static fr.darkvodou.NKtrader.enums.Msgmanager.ERROR_TRADER_IS_SET;
import static fr.darkvodou.NKtrader.enums.Permissions.*;
import static fr.darkvodou.NKtrader.enums.Usages.*;
import static fr.darkvodou.NKtrader.utils.CheckType.isNumber;
import static java.lang.Double.parseDouble;

public class addTraderCmd implements CommandExecutor
{
	private Manager manager;
	private Location location = null;
	private String name = "";
	private World world = Bukkit.getWorld("world");

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

			int wPlace = getPlaceArg(args, "-w");
			int nPlace = getPlaceArg(args, "-n");

			if(wPlace != -1)
			{
				if(args.length < wPlace + 4)
				{
					sender.sendMessage(ERROR_BAD_ARGS + " :\n" + NKT_USAGE_ADDTRADER);

					return true;
				}

				if(!args[wPlace + 5].equals("-n") && nPlace != -1)
				{
					sender.sendMessage(ERROR_BAD_ARGS + " :\n" + NKT_USAGE_ADDTRADER);

					return true;
				}

				if(Bukkit.getWorld(args[wPlace + 1]) == null)
				{
					sender.sendMessage(ERROR_WORLD_NOT_EXIST + "");

					return true;
				}

				world = Bukkit.getWorld(args[wPlace + 1]);

				for(int i = 2; i < 5; i++)
				{
					if(!isNumber(args[wPlace + i]))
					{
						sender.sendMessage(ERROR_BAD_ARGS + " :\n" + NKT_USAGE_ADDTRADER);

						return true;
					}
				}

				location = new Location(world, parseDouble(args[wPlace + 1]), parseDouble(args[wPlace + 2]), parseDouble(args[wPlace + 3]));
			}
			else
			{
				location = ((Player) sender).getLocation();
			}

			if(manager.getTraderManager().hasTrader(location))
			{
				sender.sendMessage(ERROR_TRADER_IS_SET + "");
				return true;
			}

			if(nPlace != -1)
			{
				if(args.length < nPlace + 1)
				{
					sender.sendMessage(ERROR_BAD_ARGS + " :\n" + NKT_USAGE_ADDTRADER);

					return true;
				}

				int nbArgsForN = 1;

				int j = 0;
				int i;

				//If name composed increase nbArgs by the length of the composed name
				if(args[nPlace + 1].charAt(0) == '"')
				{

					for(i = nPlace + 1; i < args.length && args[i].charAt(args[i].length() - 1) != '"'; i++)
					{
						j++;
					}

					if(args[i].charAt(args[i].length() - 1) != '"')
					{
						sender.sendMessage(ERROR_BAD_ARGS + "\n" + NKT_USAGE_ADDTRADER);

						return true;
					}

					nbArgsForN = j;
				}

				if(!args[nPlace + nbArgsForN + 1].equals("-w") && wPlace != -1)
				{
					sender.sendMessage(ERROR_BAD_ARGS + "\n" + NKT_USAGE_ADDTRADER);

					return true;
				}

				StringBuilder stringBuilder = new StringBuilder();

				for(i = nPlace + 1; i <= nPlace + j + 1; i++)
				{
					stringBuilder.append(args[i]);

					if(i != nPlace + j + 1)
					{
						stringBuilder.append(" ");
					}
				}

				name = stringBuilder.toString();
				name = name.replace("\"", "");

				if(name.length() > 100)
				{
					sender.sendMessage(ERROR_NAME_LENGTH + "");

					return true;
				}

				name = name.replace("&", "§");

			}

			Trader trader;

			if(args[0].equals("entity"))
			{
				if(world == null)
				{
					sender.sendMessage(ERROR_WORLD_NOT_EXIST + "");
					return true;
				}

				Entity entity = world.spawnEntity(location, EntityType.VILLAGER);

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

				trader = new Trader(location, args[0], "villager");

				if(!name.equals(""))
				{
					trader.setName(name);
					entity.setCustomName(name);
					entity.setCustomNameVisible(true);
				}
			}
			else
			{
				Block block = location.getBlock();

				if(block.getType().isAir())
				{
					block.setType(Material.CHEST);
					trader = new Trader(location, args[0], Material.CHEST.toString());
				}
				else
				{
					trader = new Trader(location, args[0], location.getBlock().getType().toString());
				}

				if(!name.equals(""))
				{
					if(world == null)
					{
						sender.sendMessage(ERROR_WORLD_NOT_EXIST + "");

						return true;
					}

					Location loct = location.getBlock().getLocation();
					loct.add(0.5, -1.0, 0.5);
					Entity armor_stand = world.spawnEntity(loct, EntityType.ARMOR_STAND);

					if(armor_stand instanceof LivingEntity)
					{
						LivingEntity livingEntity = (LivingEntity) armor_stand;
						livingEntity.setInvisible(true);
						livingEntity.setGravity(false);
						livingEntity.setPersistent(true);
						livingEntity.setCollidable(false);
					}

					trader.setName(name);
					armor_stand.setCustomName(name);
					armor_stand.setCustomNameVisible(true);
				}
			}

			if(!manager.getTraderManager().addTrader(trader))
			{
				sender.sendMessage(ERROR_TRADER_IS_CONTAINED + ", look at console");
			}

			sender.sendMessage(SUCCES_ADDTRADER + "");
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
