package dev.chrs.agentserverfile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev.chrs.agentserverfile.thread.AgentInputRedirectThread;
import dev.chrs.agentserverfile.thread.ServerErrorRedirectThread;
import dev.chrs.agentserverfile.thread.ServerInputRedirectThread;

public final class Agent extends Thread
{
	public Agent()
	{
		 setDaemon(false);
		 start();
	}
	
	@Override
	public void run()
	{
		try
		{
			AppSettings.initSettings();
		}
		catch (IOException ex)
		{
			System.err.println("[FATAL] COULDN'T LOAD APP SETTINGS -> TERMINATING MAIN THREAD");
			return;
		}

		final File jarFile = new File(AppSettings.getJarFile());

		if (jarFile == null || !jarFile.exists())
		{
			System.err.println("[FATAL] JAR FILE NOT SPECIFIED / DOESN'T EXIST -> TERMINATING MAIN THREAD");
			return;
		}

		final List<String> commands = new ArrayList<String>();
		commands.add("java");
		commands.addAll(AppSettings.getJavaParameters());
		commands.add("-jar");
		commands.add(jarFile.getAbsolutePath());
		commands.add("nogui");

		final ProcessBuilder serverProcessBuilder = new ProcessBuilder(commands).directory(new File(jarFile.getParent() == null ? "." : jarFile.getParent()));
		_serverProcess = null;
		try
		{
			_serverProcess = serverProcessBuilder.start();
		}
		catch (IOException ex)
		{
			System.err.println("[FATAL] FAILED TO START SERVER PROCESS -> TERMINATING MAIN THREAD");
			return;
		}
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		Agent agent = new Agent();
		agent.join();
		agent.startAgentThreads();
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	private void startAgentThreads()
	{
		new ServerInputRedirectThread(_serverProcess.getInputStream());
		new ServerErrorRedirectThread(_serverProcess.getErrorStream());
		new AgentInputRedirectThread(_serverProcess.getOutputStream());
	}

	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	private Process _serverProcess;
}
