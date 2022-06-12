package dev.chrs.throwablelightsources.util;

import org.bukkit.command.ConsoleCommandSender;

import dev.chrs.essentialapis.api.color.ColorApi;
import dev.chrs.throwablelightsources.Plugin;

public class LoggerUtil
{
	public static void info(String message)
	{
		_sender.sendMessage(ColorApi.process("&a" + message));
	}
	
	public static void warn(String message)
	{
		_sender.sendMessage(ColorApi.process("&e" + message));
	}
	
	public static void error(String message)
	{
		_sender.sendMessage(ColorApi.process("&4" + message));
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	private LoggerUtil()
	{
		
	}

	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	private static final ConsoleCommandSender _sender = Plugin.getInstance().getServer().getConsoleSender();
}
