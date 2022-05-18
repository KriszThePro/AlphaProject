package dev.chrs.gui_handler;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import dev.chrs.color_handler.api.ColorAPI;

public class Plugin extends JavaPlugin
{
	public final ColorAPI colorAPI = new ColorAPI();

	@Override
	public void onEnable()
	{
		console.sendMessage("sddsadsasad");
	}


	private ConsoleCommandSender console = Bukkit.getConsoleSender();
}
