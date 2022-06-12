package dev.chrs.metin2styleshopsystem.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import dev.chrs.metin2styleshopsystem.Plugin;

public class LoggerUtil
{
	public static void info(String message)
	{
		_logger.log(Level.INFO, message);
	}
	
	public static void warn(String message)
	{
		_logger.log(Level.WARNING, message);
	}
	
	public static void error(String message)
	{
		_logger.log(Level.SEVERE, message);
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	private LoggerUtil()
	{
		
	}

	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	private static final Logger _logger = Plugin.getInstance().getLogger();
}
