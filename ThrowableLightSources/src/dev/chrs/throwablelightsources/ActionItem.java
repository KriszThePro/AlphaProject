package dev.chrs.throwablelightsources;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.chrs.throwablelightsources.config.ConfigYml;

public class ActionItem
{
	public ActionItem()
	{
		_actionItem = new ItemStack(ConfigYml.material);
		
		ItemMeta itemMeta = _actionItem.getItemMeta();
		
		itemMeta.setDisplayName(ConfigYml.displayname);
		itemMeta.setLore(ConfigYml.lore.size() > 0 ? ConfigYml.lore : null);
		
		_actionItem.setItemMeta(itemMeta);
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	public ItemStack getActionItem()
	{
		return _actionItem;
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	private ItemStack _actionItem;
}
