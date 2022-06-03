package dev.chrs.essentialapis.api.inventorygui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class InventoryGuiApiEvents implements Listener
{
	@EventHandler
	public void onInventoryClose(final InventoryCloseEvent event)
	{
		final Player player = (Player) event.getPlayer();

		InventoryGuiApi.removeFromList(player);
	}

	@EventHandler
	public void onDisconnect(final PlayerQuitEvent event)
	{
		final Player player = (Player) event.getPlayer();

		InventoryGuiApi.removeFromList(player);
	}
}
