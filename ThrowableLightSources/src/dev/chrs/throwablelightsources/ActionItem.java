package dev.chrs.throwablelightsources;

import org.bukkit.inventory.ItemStack;

import dev.chrs.throwablelightsources.config.ConfigYml;

public abstract class ActionItem
{
	public ActionItem()
	{
		_actionItem = new ItemStack(ConfigYml.material);
		_actionItem.getItemMeta().setDisplayName(ConfigYml.displayname);
		_actionItem.getItemMeta().setLore(ConfigYml.lore.size() > 0 ? ConfigYml.lore : null);
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	public static ItemStack getActionItem()
	{
		return _actionItem;
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	private static ItemStack _actionItem;
}
