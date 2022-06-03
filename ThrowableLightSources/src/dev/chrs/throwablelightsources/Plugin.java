package dev.chrs.throwablelightsources;

import java.util.HashMap;
import java.util.Map.Entry;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import dev.chrs.essentialapis.api.color.ColorApi;

public class Plugin extends JavaPlugin implements Listener
{
	@Override
	public void onEnable()
	{
		_instance = this;
		
		DependencyUtil.requestDependencies();
		
		_console = Bukkit.getConsoleSender();
		
		loadEvents();

		_console.sendMessage(ColorApi.process("&a[ThrowableLightSources] &7Loaded."));
	}
	
	public static Plugin getInstance()
	{
		return _instance;
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

	private static Plugin _instance;
	private ConsoleCommandSender _console;
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
}
