package dev.chrs.essentialapis;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import dev.chrs.essentialapis.api.color.ColorApi;
import dev.chrs.essentialapis.api.inventorygui.InventoryGuiApiEvents;
import dev.chrs.essentialapis.util.LoggerUtil;

public class Plugin extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		_instance = this;
		loadEvents();

		LoggerUtil.info(ColorApi.process("&a[EssentialApis] &7Loaded."));
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
	private Listener[] _listenerClasses = { new InventoryGuiApiEvents() };
}
