package dev.chrs.agentserverfile.thread;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerInputRedirectThread extends Thread
{
	public ServerInputRedirectThread(final InputStream serverInputStream)
	{
		this._serverInputStream = serverInputStream;
		setDaemon(false);
		start();
	}

	@Override
	public void run()
	{
		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(_serverInputStream));
		
		try
		{
			String data;

			while ((data = bufferedReader.readLine()) != null)
			{
				System.out.println(data);
			}
		}
		catch (Exception ex)
		{
			System.err.println("[CRITICAL] TERMINATED FROM MINECRAFT SERVER -> TERMINATING MAIN");
			ex.printStackTrace();
		}
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	private final InputStream _serverInputStream;
}
