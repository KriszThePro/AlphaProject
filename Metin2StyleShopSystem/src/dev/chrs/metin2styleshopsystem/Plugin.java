package dev.chrs.metin2styleshopsystem;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import dev.chrs.essentialapis.api.color.ColorApi;
import dev.chrs.essentialapis.api.inventorygui.IllegalInventorySizeException;
import dev.chrs.essentialapis.api.inventorygui.InventoryGuiApi;
import dev.chrs.essentialapis.api.inventorygui.InventoryIsNullException;
import dev.chrs.essentialapis.api.inventorygui.PlayerAlreadyHasGUIException;
import dev.chrs.essentialapis.api.inventorygui.PlayerIsNullException;

public class Plugin extends JavaPlugin implements Listener
{
	@Override
	public void onEnable()
	{	
		PluginManager pluginManager = getServer().getPluginManager();
		
		org.bukkit.plugin.Plugin apiPlugin = null;
		try
		{
			apiPlugin = pluginManager.loadPlugin(new File("EssentialApis.jar"));
		}
		catch (UnknownDependencyException | InvalidPluginException | InvalidDescriptionException e)
		{
			e.printStackTrace();
			return;
		}
		
		pluginManager.enablePlugin(apiPlugin);
		
		//loadClassesFromJarURL("https://scontent.fbud5-1.fna.fbcdn.net/v/t39.30808-6/285832573_1082777942307622_8906668590808603864_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=8bfeb9&_nc_ohc=89GfI0xDgzoAX80z4kS&_nc_ht=scontent.fbud5-1.fna&oh=00_AT-Z9jQc2al95ajmNe9zwelsItIzp4IFmf50gngJZ2aXsA&oe=629EF937", new String[] {
		//		"dev.chrs.essentialapis.Plugin",
		//});

		//loadEvents();

		_console.sendMessage(ColorApi.process("&a[Metin2StyleShopSystem] &7Loaded."));
	}

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) throws PlayerAlreadyHasGUIException, IllegalInventorySizeException
	{
		final Player player = event.getPlayer();

		InventoryGuiApi.createInventory(player, 36);

		new BukkitRunnable()
		{
			int counter = 0;

			@Override
			public void run()
			{
				try
				{
					InventoryGuiApi.updateTitle(player, counter + "");
				}
				catch (PlayerIsNullException | InventoryIsNullException ex)
				{
					cancel();
				}
				counter++;
			}
		}.runTaskTimer(this, 40L, 5L);
	}

	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////

	private void loadEvents()
	{
		for (Listener listener : _listenerClasses)
		{
			getServer().getPluginManager().registerEvents(listener, this);
		}
	}

	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////

	private ConsoleCommandSender _console = Bukkit.getConsoleSender();
	private Listener[] _listenerClasses = { this };


	private HashMap<Block, BlockFace> getSurroundingBlocksWithRelativeFacing(Location location)
	{
		Block targetBlock = location.getBlock();
		HashMap<Block, BlockFace> surroundingBlocksWithRelativeFacing = new HashMap<Block, BlockFace>();
		BlockFace[] scannedFaces = {
				BlockFace.DOWN,
				BlockFace.NORTH,
				BlockFace.SOUTH,
				BlockFace.WEST,
				BlockFace.EAST
		};
		for (BlockFace face : scannedFaces)
		{
			surroundingBlocksWithRelativeFacing.put(targetBlock.getRelative(face), face);
		}

		return surroundingBlocksWithRelativeFacing;
	}

	private HashMap<BlockFace, Byte> facesInBytes = new HashMap<BlockFace, Byte>()
	{
		private static final long serialVersionUID = -3719699417046935938L;

		{
			put(BlockFace.DOWN, (byte) 5);
			put(BlockFace.NORTH, (byte) 3);
			put(BlockFace.SOUTH, (byte) 4);
			put(BlockFace.WEST, (byte) 1);
			put(BlockFace.EAST, (byte) 2);
		}
	};

	@EventHandler
	public void onClick_Left(PlayerInteractEvent event)
	{
		if (event.getItem() != null && event.getAction() == Action.LEFT_CLICK_AIR && event.getItem().hasItemMeta() && event.getItem().getItemMeta().getDisplayName().equals("Custom_Item") && event.getItem().getType() == Material.TORCH)
		{
			event.setCancelled(true);

			//1 Decrease item count in player's hand
			Player player = event.getPlayer();
			ItemStack dropped_item_stack = new ItemStack(event.getItem());

			event.getItem().setAmount(event.getItem().getAmount() - 1);

			//2 Throw away the item to somewhere (by a vector)
			Location player_location = player.getEyeLocation();

			dropped_item_stack.setAmount(1);

			Item dropped_item = player_location.getWorld().dropItem(player_location, dropped_item_stack);
			dropped_item.setVelocity(player.getLocation().getDirection().multiply(1.1));

			//3 If this item landed, put this item on the block where it's landed...
			BukkitRunnable runnable = new BukkitRunnable()
			{
				private Location last_location = null;

				@SuppressWarnings("deprecation")
				@Override
				public void run()
				{
					Location current_location = dropped_item.getLocation();

					if (!dropped_item.isValid() || current_location.equals(last_location))
					{
						cancel();
					}

					Block current_location_block = current_location.getBlock();
					for (Entry<Block, BlockFace> blockWithRelativeFacing : getSurroundingBlocksWithRelativeFacing(current_location).entrySet())
					{
						Block block = blockWithRelativeFacing.getKey();
						BlockFace face = blockWithRelativeFacing.getValue();

						if (current_location_block.getType() == Material.AIR && !ForbiddenMaterials.materials.contains(block.getType()))
						{
							dropped_item.remove();
							current_location_block.setType(Material.TORCH);
							current_location_block.setBlockData(Bukkit.getUnsafe().fromLegacy(Material.TORCH, facesInBytes.get(face)));

							cancel();
						}
					}

					last_location = current_location;
				}
			};
			runnable.runTaskTimer(this, 0L, 1L);
		}
	}


	public void loadClassesFromJarURL(String url, String[] classPaths)
	{
		/*
		byte[] buffer = new byte[1024];
		int bytesLeft = 5 * 1024 * 1024; // Or whatever
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream("EssentialApis.jar");
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			FileOutputStream fos = null;
			try
			{
				fos = new FileOutputStream("EssentialApis.txt");
			}
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try
			{
				while (bytesLeft > 0)
				{
					int read = 0;
					try
					{
						read = fis.read(buffer, 0, Math.min(bytesLeft, buffer.length));
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (read == -1)
					{
						throw new EOFException("Unexpected end of data");
					}
					try
					{
						fos.write(buffer, 0, read);
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					bytesLeft -= read;
				}
			}
			catch (EOFException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				try
				{
					fos.close();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // Or use Guava's Closeables.closeQuietly,
					// or try-with-resources in Java 7
			}
		}
		finally
		{
			try
			{
				fis.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/


		
		final byte[] buffer;
		
		final ArrayList<Byte> bufferList = new ArrayList<Byte>();
		try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream()))
		{
			byte dataBuffer[] = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1)
			{
				for(int i = 0; i < bytesRead; i++)
				{
					bufferList.add(dataBuffer[i]);
				}
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return;
		}
		
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final ObjectOutputStream oos;
		try
		{
			oos = new ObjectOutputStream(baos);
			oos.writeObject(bufferList);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return;
		}
		buffer = baos.toByteArray();
		
		Bukkit.getConsoleSender().sendMessage(bufferList.size() + "");
		
		final HashMap<String, byte[]> map = new HashMap<String, byte[]>();
		try (JarInputStream jis = new JarInputStream(new FileInputStream("EssentialApis.jar")))//new ByteArrayInputStream(buffer)))
		{
			int i = 0;
			for (;;)
			{
				final JarEntry nextEntry = jis.getNextJarEntry();
				i++;
				if (nextEntry == null)
				{
					Bukkit.getConsoleSender().sendMessage("break" + i);
					break;
				}
		
				final int est = (int) nextEntry.getSize();
				byte[] data = new byte[est > 0 ? est : 1024];
				int real = 0;
		
				for (int r = jis.read(data); r > 0; r = jis.read(data, real, data.length - real))
				{
					if (data.length == (real += r))
					{
						data = Arrays.copyOf(data, data.length * 2);
					}
				}
		
				if (real != data.length)
				{
					data = Arrays.copyOf(data, real);
				}
		
				map.put("/" + nextEntry.getName(), data);
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return;
		}
		
		if(true)
		{
			Bukkit.getConsoleSender().sendMessage(map.size() + "");
			map.forEach((x,y) -> Bukkit.getConsoleSender().sendMessage(x + " : " + y));
			
		}
		
		URL u = null;
		try
		{
			u = new URL("x-buffer", null, -1, "/", new URLStreamHandler()
			{
				protected URLConnection openConnection(URL u) throws IOException
				{
					final byte[] data = map.get(u.getFile());
					if (data == null)
					{
						throw new FileNotFoundException(u.getFile());
					}
		
					return new URLConnection(u)
					{
						public void connect() throws IOException
						{
						}
		
						@Override
						public InputStream getInputStream() throws IOException
						{
							return new ByteArrayInputStream(data);
						}
					};
				}
			});
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			return;
		}
		
		try (URLClassLoader cl = new URLClassLoader(new URL[] { u }))
		{
			for (String classPath : classPaths)
			{
				Bukkit.getConsoleSender().sendMessage(classPath);
				cl.loadClass(classPath);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return;
		}
		
	}
}
