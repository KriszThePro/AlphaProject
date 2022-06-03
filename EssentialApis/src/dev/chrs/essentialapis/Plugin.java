package dev.chrs.essentialapis;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import dev.chrs.essentialapis.api.color.ColorApi;
import dev.chrs.essentialapis.api.inventorygui.InventoryGuiApiEvents;

public class Plugin extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		_instance = this;
		_console = Bukkit.getConsoleSender();
		
		loadEvents();

		_console.sendMessage(ColorApi.process("&a[EssentialApis] &7Loaded."));
	}

	public static Plugin getInstance()
	{
		return _instance;
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	private void loadEvents()
	{
		for (Listener listener : _listenerClasses)
		{
			getServer().getPluginManager().registerEvents(listener, _instance);
		}
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////

	private static Plugin _instance;
	private ConsoleCommandSender _console;
	private Listener[] _listenerClasses = { new InventoryGuiApiEvents() };
}
