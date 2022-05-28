package dev.chrs.metin2styleshopsystem;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import dev.chrs.essentialsapis.api.color.ColorApi;
import dev.chrs.essentialsapis.api.inventorygui.InventoryGuiApi;
import dev.chrs.essentialsapis.exception.IllegalInventorySizeException;
import dev.chrs.essentialsapis.exception.InventoryIsNullException;
import dev.chrs.essentialsapis.exception.PlayerAlreadyHasGUIException;
import dev.chrs.essentialsapis.exception.PlayerIsNullException;

public class Plugin extends JavaPlugin implements Listener
{
	@Override
	public void onEnable()
	{
		loadEvents();

		_console.sendMessage(ColorApi.process("&a[Metin2StyleShopSystem] &7Loaded."));
	}

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) throws PlayerAlreadyHasGUIException, IllegalInventorySizeException
	{
		final Player player = event.getPlayer();

		InventoryGuiApi.createInventory(player, 36);

		new BukkitRunnable()
		{
			int counter = 0;

			@Override
			public void run()
			{
				try
				{
					InventoryGuiApi.updateTitle(player, counter + "");
				}
				catch (PlayerIsNullException | InventoryIsNullException ex)
				{
					cancel();
				}
				counter++;
			}
		}.runTaskTimer(this, 40L, 5L);
	}

	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////

	private void loadEvents()
	{
		for (Listener listener : _listenerClasses)
		{
			getServer().getPluginManager().registerEvents(listener, this);
		}
	}

	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////

	private ConsoleCommandSender _console = Bukkit.getConsoleSender();
	private Listener[] _listenerClasses = { this };
}
