package fr.darkvodou.NKtrader.cmds;

import fr.darkvodou.NKtrader.entity.Trader;
import fr.darkvodou.NKtrader.managers.TraderManager;
import org.bukkit.*;
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
import static fr.darkvodou.NKtrader.utils.GetArgs.getPlaceArg;

public class addTraderCmd implements CommandExecutor
{
	private final TraderManager traderManager;

	public addTraderCmd(TraderManager traderManager)
	{
		this.traderManager = traderManager;
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
			String dataType;
			String name = "";
			Location location;
			World world;

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
				if(!args[1].equals("-n") && !args[1].equals("-w") && !args[1].equals("-t"))
				{
					sender.sendMessage(ERROR_BAD_ARGS + " :\n" + NKT_USAGE_ADDTRADER);

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
					sender.sendMessage(ERROR_BAD_ARGS + " :\n" + NKT_USAGE_ADDTRADER);

					return true;
				}

				//If more args after -w args
				if(args.length > wPlace + 5)
				{
					if(args[wPlace + 5].equals("-n") && args[wPlace + 5].equals("-t"))
					{
						sender.sendMessage(ERROR_BAD_ARGS + " :\n" + NKT_USAGE_ADDTRADER);

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
						sender.sendMessage(ERROR_BAD_ARGS + " :\n" + NKT_USAGE_ADDTRADER);

						return true;
					}
				}

				location = new Location(world, parseDouble(args[wPlace + 2]), parseDouble(args[wPlace + 3]), parseDouble(args[wPlace + 4]));
			}
			else
			{
				location = ((Player) sender).getLocation();
			}

			if(traderManager.existTrader(location))
			{
				sender.sendMessage(ERROR_TRADER_IS_SET + "");

				return true;
			}

			if(nPlace != -1)
			{
				if(args.length <= nPlace + 1)
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
					StringBuilder stringBuilder = new StringBuilder();

					for(i = nPlace + 1; i < args.length && args[i].charAt(args[i].length() - 1) != '"'; i++)
					{
						j++;
						stringBuilder.append(args[i]);
						stringBuilder.append(" ");
					}

					stringBuilder.deleteCharAt(stringBuilder.length() - 1);

					if(args[i].charAt(args[i].length() - 1) != '"')
					{
						sender.sendMessage(ERROR_BAD_ARGS + " :\n" + NKT_USAGE_ADDTRADER);

						return true;
					}

					name = stringBuilder.toString();

					nbArgsForN = j;
				}
				else
				{
					name = args[nPlace + 1];
				}
				name = name.replace("\"", "");

				//If more args after -n args
				if(args.length > nPlace + nbArgsForN + 2)
				{
					if(!args[nPlace + nbArgsForN + 2].equals("-w") && !args[nPlace + nbArgsForN + 2].equals("-t"))
					{
						sender.sendMessage(args[nPlace + nbArgsForN + 1]);
						sender.sendMessage(ERROR_BAD_ARGS + " :\n" + NKT_USAGE_ADDTRADER);

						return true;
					}
				}

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
					sender.sendMessage(ERROR_BAD_ARGS + " :\n" + NKT_USAGE_ADDTRADER);

					return true;
				}

				//If more args after -t args
				if(args.length > tPlace + 2)
				{
					if(!args[tPlace + 2].equals("-n") && !args[tPlace + 2].equals("-w"))
					{
						sender.sendMessage(ERROR_BAD_ARGS + " :\n" + NKT_USAGE_ADDTRADER);

						return true;
					}
				}

				dataType = args[tPlace + 1];
			}
			else
			{
				dataType = "villager";
			}

			Trader trader;

			if(args[0].equals("entity"))
			{
				trader = new Trader(location, args[0], dataType).setName(name).setType("entity").setLocation(location);

				if(!traderManager.createTraderEntity(trader))
				{
					sender.sendMessage(ERROR_ENTITY_TYPE + "");

					return true;
				}
			}
			else
			{
				trader = new Trader(location, args[0], dataType).setName(name).setType("block").setLocation(location);

				if(traderManager.createTraderBlock(trader))
				{
					sender.sendMessage(ERROR_MATERIAL_TYPE + "");

					return true;
				}
			}

			if(!traderManager.addTrader(trader))
			{
				sender.sendMessage(ERROR_TRADER_IS_CONTAINED + ", look at console");

				return true;
			}

			sender.sendMessage(SUCCES_ADDTRADER + "");
		}

		return true;
	}

	private boolean hasAdminPermissions(CommandSender sender)
	{
		return sender.hasPermission("" + ADMIN) || sender.hasPermission("" + ALL) || sender.hasPermission("" + NKT_ALL);
	}

	private boolean hasAddTraderPermissions(CommandSender sender)
	{
		return sender.hasPermission("" + NKT_ADD_TRADER) || hasAdminPermissions(sender);
	}
}
