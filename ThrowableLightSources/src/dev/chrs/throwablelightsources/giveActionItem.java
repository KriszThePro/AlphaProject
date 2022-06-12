package dev.chrs.throwablelightsources;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class giveActionItem implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(sender instanceof Player player)
		{
			player.getInventory().addItem(Plugin.getActionItem());
		}
			
		return true;
	}
}
