package dev.chrs.throwablelightsources;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import dev.chrs.throwablelightsources.config.ConfigYml;
import dev.chrs.throwablelightsources.listener.LeftClickListener;
import dev.chrs.throwablelightsources.listener.RightClickListener;
import dev.chrs.throwablelightsources.util.DependencyUtil;
import dev.chrs.throwablelightsources.util.LoggerUtil;

public class Plugin extends JavaPlugin implements Listener
{
	@Override
	public void onEnable()
	{
		_instance = this;
		DependencyUtil.requestDependencies();
		loadConfigs();
		loadEvents();
		
		_actionItem = new ActionItem().getActionItem();
		
		getCommand("gai").setExecutor(new giveActionItem());

		LoggerUtil.info("[ThrowableLightSources] &7Loaded.");
		LoggerUtil.warn(ConfigYml.displayname);
	}

	public static Plugin getInstance()
	{
		return _instance;
	}
	
	public static ItemStack getActionItem()
	{
		return _actionItem;
	}

	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////

	private void loadConfigs()
	{
		saveDefaultConfig();
		
		ConfigYml.material = Material.valueOf(getConfig().getString("throwable_light_source_item.material"));
		ConfigYml.displayname = getConfig().getString("throwable_light_source_item.displayname");
		ConfigYml.lore = (ArrayList<String>) getConfig().getStringList("throwable_light_source_item.lore");
	}
	
	private void loadEvents()
	{
		for (Listener listener : _listenerClasses)
		{
			getServer().getPluginManager().registerEvents(listener, this);
		}
	}

	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////

	private static Plugin _instance;
	private Listener[] _listenerClasses = { new LeftClickListener(), new RightClickListener() };
	private static ItemStack _actionItem;
}
