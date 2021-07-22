package fr.darkvodou.NKtrader.cmds;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static fr.darkvodou.NKtrader.enums.MsgUtils.*;
import static fr.darkvodou.NKtrader.enums.Permissions.*;
import static fr.darkvodou.NKtrader.enums.Usages.*;

public class addTraderCmd implements CommandExecutor
{
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

				return false;
			}

			if(args.length == 0)
			{
				sender.sendMessage("" + NKT_USAGE_ADDTRADER);
			}

			if(args.length >= 1)
			{
				if(args[0].equals("entity"))
				{
					//entity
				}
				else
				{
					//Block
				}
			}
		}

		return true;
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
