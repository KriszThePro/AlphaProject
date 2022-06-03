package dev.chrs.throwablelightsources;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;

public class DependencyUtil
{
	public static void requestDependencies()
	{
		if(_instance == null)
		{
			_instance = new DependencyUtil();
			
			downloadDependencies();
		}
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	private DependencyUtil()
	{
		
	}
	
	private static void downloadDependencies()
	{	
		for (String dependency : Plugin.getInstance().getDescription().getSoftDepend())
		{
			PluginManager pluginManager = Plugin.getInstance().getServer().getPluginManager();
			
			if(pluginManager.getPlugin(dependency) != null)
			{
				continue;
			}
			
			String url = String.format("https://github.com/KriszThePro/AlphaProject/raw/main/%s/%s.jar", dependency, dependency);
			String fileName = url.substring(url.lastIndexOf('/') + 1);

			File file = new File(_dependenciesPath + fileName);
			file.getParentFile().mkdirs();
			
			try (
			BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
			FileOutputStream fileOutputStream = new FileOutputStream(file, false))
			{
				byte dataBuffer[] = new byte[1024];
				int bytesRead;
				while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1)
				{
					fileOutputStream.write(dataBuffer, 0, bytesRead);
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
				continue;
			}
			
			org.bukkit.plugin.Plugin dependencyPlugin = null;
			try
			{
				dependencyPlugin = pluginManager.loadPlugin(new File(_dependenciesPath + dependency + ".jar"));
				pluginManager.enablePlugin(dependencyPlugin);
			}
			catch (UnknownDependencyException | InvalidPluginException | InvalidDescriptionException ex)
			{
				ex.printStackTrace();
				continue;
			}
		}
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	private static DependencyUtil _instance;
	private final static String _dependenciesPath = ".\\plugins\\_dependencies\\";
}
