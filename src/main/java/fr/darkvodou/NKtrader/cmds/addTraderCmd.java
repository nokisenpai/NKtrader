package fr.darkvodou.NKtrader.cmds;

import fr.darkvodou.NKtrader.entity.Trader;
import fr.darkvodou.NKtrader.managers.Manager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.*;

import static fr.darkvodou.NKtrader.enums.MsgUtils.*;
import static fr.darkvodou.NKtrader.enums.Msgmanager.ERROR_TRADER_IS_CONTAINED;
import static fr.darkvodou.NKtrader.enums.Msgmanager.ERROR_TRADER_IS_SET;
import static fr.darkvodou.NKtrader.enums.Permissions.*;
import static fr.darkvodou.NKtrader.enums.Usages.*;
import static fr.darkvodou.NKtrader.utils.CheckType.isNumber;
import static java.lang.Double.parseDouble;

public class addTraderCmd implements CommandExecutor
{
	private final Manager manager;
	private String name = "";
	private World world = Bukkit.getWorld("world");
	private String type;

	public addTraderCmd(Manager manager)
	{
		this.manager = manager;
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
	{
		Location location;

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
				sender.sendMessage("1 :" + NKT_USAGE_ADDTRADER);

				return true;
			}

			if(!args[0].equals("entity") && !args[0].equals("block"))
			{
				sender.sendMessage(ERROR_BAD_ARGS + " 2:\n" + NKT_USAGE_ADDTRADER);

				return true;
			}

			if(args.length >= 2)
			{
				if(!args[1].equals("-n") && !args[1].equals("-w") && !args[1].equals("-t"))
				{
					sender.sendMessage(ERROR_BAD_ARGS + " 3:\n" + NKT_USAGE_ADDTRADER);

					return true;
				}
			}

			int wPlace = getPlaceArg(args, "-w");
			int nPlace = getPlaceArg(args, "-n");
			int tPlace = getPlaceArg(args, "-t");

			if(wPlace != -1)
			{
				if(args.length < wPlace + 4)
				{
					sender.sendMessage(ERROR_BAD_ARGS + " 4:\n" + NKT_USAGE_ADDTRADER);

					return true;
				}

				//If more args after -w args
				if(args.length > wPlace + 5)
				{
					if(args[wPlace + 5].equals("-n") && args[wPlace + 5].equals("-t"))
					{
						sender.sendMessage(ERROR_BAD_ARGS + " 5:\n" + NKT_USAGE_ADDTRADER);

						return true;
					}
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
						sender.sendMessage(ERROR_BAD_ARGS + " 6:\n" + NKT_USAGE_ADDTRADER);

						return true;
					}
				}

				location = new Location(world, parseDouble(args[wPlace + 2]), parseDouble(args[wPlace + 3]), parseDouble(args[wPlace + 4]));
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
				if(args.length <= nPlace + 1)
				{
					sender.sendMessage(ERROR_BAD_ARGS + " 7:\n" + NKT_USAGE_ADDTRADER);

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
						sender.sendMessage(ERROR_BAD_ARGS + " 8:\n" + NKT_USAGE_ADDTRADER);

						return true;
					}

					nbArgsForN = j;
				}

				//If more args after -n args
				if(args.length > nPlace + nbArgsForN + 2)
				{
					if(!args[nPlace + nbArgsForN + 2].equals("-w") && !args[nPlace + nbArgsForN + 2].equals("-t"))
					{
						sender.sendMessage(args[nPlace + nbArgsForN + 1]);
						sender.sendMessage(ERROR_BAD_ARGS + " 9:\n" + NKT_USAGE_ADDTRADER);

						return true;
					}
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

			if(tPlace != -1)
			{
				if(args.length <= tPlace + 1)
				{
					sender.sendMessage(ERROR_BAD_ARGS + " 10:\n" + NKT_USAGE_ADDTRADER);

					return true;
				}

				//If more args after -t args
				if(args.length > tPlace + 2)
				{
					if(!args[tPlace + 2].equals("-n") && !args[tPlace + 2].equals("-w"))
					{
						sender.sendMessage(ERROR_BAD_ARGS + " 11:\n" + NKT_USAGE_ADDTRADER);

						return true;
					}
				}

				type = args[tPlace + 1];
			}

			Trader trader;

			if(args[0].equals("entity"))
			{
				if(world == null)
				{
					sender.sendMessage(ERROR_WORLD_NOT_EXIST + "");
					return true;
				}

				EntityType entityType = EntityType.valueOf("villager".toUpperCase());

				if(tPlace != -1)
				{
					try
					{
						entityType = EntityType.valueOf(type.toUpperCase());
					}
					catch(Exception e)
					{
						e.printStackTrace();
						sender.sendMessage(ERROR_ENTITY_TYPE + "");
					}
				}

				Entity entity = world.spawnEntity(location, entityType);

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

				trader = new Trader(location, args[0], type);

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
				Material material = Material.CHEST;

				if(tPlace != -1)
				{
					try
					{
						material = Material.valueOf(type.toUpperCase());
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}

				if(block.getType().isAir())
				{
					block.setType(material);
					trader = new Trader(location, args[0], material.toString());
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
