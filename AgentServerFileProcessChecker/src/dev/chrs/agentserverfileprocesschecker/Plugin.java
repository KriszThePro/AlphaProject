package dev.chrs.agentserverfileprocesschecker;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		Thread checkerThread = new Thread()
		{
			@Override
			public void run()
			{
				Optional<ProcessHandle> optionalParentProcessHandle = ProcessHandle.current().parent();
				ProcessHandle parentProcessHandle = optionalParentProcessHandle.isPresent() ? optionalParentProcessHandle.get() : null;

				while (parentProcessHandle != null && parentProcessHandle.isAlive())
				{
					try
					{
						TimeUnit.SECONDS.sleep(5L);
					}
					catch (InterruptedException ex)
					{
						ex.printStackTrace();
					}
				}

				getServer().shutdown();
			}
		};
		checkerThread.setDaemon(true);
		checkerThread.start();
	}
}
