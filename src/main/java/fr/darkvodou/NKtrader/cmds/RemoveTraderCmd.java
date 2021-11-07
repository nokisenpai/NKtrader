package fr.darkvodou.NKtrader.cmds;

import fr.darkvodou.NKtrader.managers.ConfigManager;
import fr.darkvodou.NKtrader.managers.TraderManager;
import fr.darkvodou.NKtrader.utils.Targeter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import static fr.darkvodou.NKtrader.enums.MsgUtils.*;
import static fr.darkvodou.NKtrader.enums.Permissions.*;
import static fr.darkvodou.NKtrader.enums.Usages.NKT_USAGE_REMOVETRADER;

public class RemoveTraderCmd implements CommandExecutor
{
	private final TraderManager traderManager;
	private final ConfigManager configManager;

	public RemoveTraderCmd(TraderManager traderManager, ConfigManager configManager)
	{
		this.traderManager = traderManager;
		this.configManager = configManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
	{
		if(sender instanceof ConsoleCommandSender)
		{
			return true;
		}

		if(!hasRemoveTraderPermissions(sender))
		{
			sender.sendMessage(ERROR_HAS_NOT_PERM + " node : " + NKT_REMOVE_TRADER);
			return true;
		}

		if(args.length > 0)
		{
			sender.sendMessage("" + NKT_USAGE_REMOVETRADER);

			return true;
		}

		Block block = null;
		Entity entity = Targeter.getTargetEntity(Bukkit.getPlayer(sender.getName()));
		Player player;
		Location location = null;

		if((player = (Bukkit.getPlayer(sender.getName()))) != null)
		{
			block = player.getTargetBlockExact(configManager.getDistanceTarget());
		}

		if(block == null && entity == null)
		{
			sender.sendMessage("" + ERROR_CANT_REACH_TARGET);
			return true;
		}

		if(block != null)
		{
			location = block.getLocation();
			block.setType(Material.AIR);
		}

		if(entity != null)
		{
			location = entity.getLocation();
		}

		if(traderManager.existTrader(location))
		{
			String id = TraderManager.locationToId(location);

			if(id == null)
			{
				sender.sendMessage("" + ERROR_ID_NULL);

				return true;
			}

				traderManager.removeTrader(id);
		}

		if(entity != null)
		{
			entity.remove();
		}
		if(block != null)
		{
			block.setType(Material.AIR);
		}

		sender.sendMessage(PREFIX_SUCCESS + "The trader has been successfully removed");

		return true;
	}

	private boolean hasAdminPermissions(CommandSender sender)
	{
		return sender.hasPermission("" + ADMIN) || sender.hasPermission("" + ALL) || sender.hasPermission("" + NKT_ALL);
	}

	private boolean hasRemoveTraderPermissions(CommandSender sender)
	{
		return sender.hasPermission("" + NKT_REMOVE_TRADER) || hasAdminPermissions(sender);
	}
}
