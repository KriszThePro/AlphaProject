package dev.chrs.throwablelightsources.listener;

import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import dev.chrs.essentialapis.api.inventorygui.IllegalInventorySizeException;
import dev.chrs.essentialapis.api.inventorygui.InventoryGuiApi;
import dev.chrs.essentialapis.api.inventorygui.PlayerAlreadyHasGUIException;
import dev.chrs.throwablelightsources.Plugin;
import dev.chrs.throwablelightsources.config.ConfigYml;

public class RightClickListener implements Listener
{
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) throws PlayerAlreadyHasGUIException, IllegalInventorySizeException
	{
		final ItemStack eventItem = event.getItem();

		if (
			eventItem != null &&
			Stream.of(new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}).filter(a -> a == event.getAction()).count() != 0L &&
			eventItem.hasItemMeta() &&
			eventItem.getType() == Plugin.getActionItem().getType() &&
			eventItem.getItemMeta().equals(Plugin.getActionItem().getItemMeta())
		)
		{
			Player player = event.getPlayer();
			
			InventoryGuiApi inventory = InventoryGuiApi.createGui(player, 9);
			inventory.addItem(0, Material.TORCH, ConfigYml.displayname, ConfigYml.lore);
		}
	}
}
