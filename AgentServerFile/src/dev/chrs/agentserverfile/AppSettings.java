package dev.chrs.agentserverfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public abstract class AppSettings
{
	public static void initSettings() throws IOException
	{
		final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(classloader.getResourceAsStream("app.properties")));
		
		for(String line; (line = bufferedReader.readLine()) != null;)
		{
			if(line.startsWith("#"))
			{
				continue;
			}
			
			String[] setting = line.split(":", 2);
			String key = setting[0];
			String value = setting[1].split("\"", 2)[1].replace("\"", "");
			switch (key)
			{
				case "_jarFile":
				{
					_jarFile = value;
					break;
				}
				case "_javaParameters":
				{
					_javaParameters = Arrays.asList(value.split(" "));
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + setting);
			}
		}
	}
	
	public static String getJarFile()
	{
		return _jarFile;
	}
	
	public static List<String> getJavaParameters()
	{
		return _javaParameters;
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	private static String _jarFile;
	private static List<String> _javaParameters;
}

// asszociatív tömb -> enum