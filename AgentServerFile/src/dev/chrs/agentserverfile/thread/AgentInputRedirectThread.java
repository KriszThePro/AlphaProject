package dev.chrs.agentserverfile.thread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

public final class AgentInputRedirectThread extends Thread
{
	public AgentInputRedirectThread(final OutputStream serverOutputStream)
	{
		this._serverOutputStream = serverOutputStream;
		setDaemon(true);
		start();
	}
	
	@Override
	public void run()
	{
		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		try
		{
			String data;

			while ((data = bufferedReader.readLine()) != null)
			{
				data += "\n";

				_serverOutputStream.write(data.getBytes("UTF-8"));
				_serverOutputStream.flush();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	private final OutputStream _serverOutputStream;
}
