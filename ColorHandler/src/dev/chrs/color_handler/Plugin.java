package dev.chrs.color_handler;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import dev.chrs.color_handler.api.ColorAPI;

public class Plugin extends JavaPlugin
{
	private ConsoleCommandSender console = Bukkit.getConsoleSender();
	
	@Override
	public void onEnable()
	{	
		console.sendMessage(ColorAPI.process("<SOLID:bfff00>[Color-API] <SOLID:808080>Loaded."));
	}
}
