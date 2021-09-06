package fr.darkvodou.NKtrader.listeners;

import fr.darkvodou.NKtrader.enums.MsgUtils;
import fr.darkvodou.NKtrader.managers.TraderManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class LeftClick implements Listener
{
	TraderManager traderManager;

	public LeftClick(TraderManager traderManager)
	{
		this.traderManager = traderManager;
	}

	@EventHandler
	public void onLeftClick(final PlayerInteractEvent event)
	{
		if(event.getClickedBlock() != null)
		{
			if(event.getAction().equals(Action.LEFT_CLICK_BLOCK) && traderManager.hasTrader(event.getClickedBlock().getLocation()))
			{
				event.getPlayer().sendMessage(MsgUtils.ERROR_TRY_KILL_TRADER + "");
				event.setCancelled(true);
			}
		}
	}
}
