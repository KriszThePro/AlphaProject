package dev.chrs.throwablelightsources.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import dev.chrs.throwablelightsources.Plugin;
import dev.chrs.throwablelightsources.util.ForbiddenMaterialsUtil;

public class LeftClickListener implements Listener
{	
	@EventHandler
	public void onLeftClick(PlayerInteractEvent event)
	{
		final ItemStack eventItem = event.getItem();
		
		if (
			eventItem != null &&
			event.getAction() == Action.LEFT_CLICK_AIR &&
			eventItem.hasItemMeta() &&
			eventItem.getType() == Plugin.getActionItem().getType() &&
			eventItem.getItemMeta().equals(Plugin.getActionItem().getItemMeta())
		)
		{
			event.setCancelled(true);

			//1 Decrease item count in player's hand
			final Player player = event.getPlayer();
			final ItemStack droppedItemStack = new ItemStack(event.getItem());

			eventItem.setAmount(eventItem.getAmount() - 1);

			//2 Throw away the item by a vector
			final Location playerLocation = player.getEyeLocation();

			droppedItemStack.setAmount(1);

			final Item droppedItem = playerLocation.getWorld().dropItem(playerLocation, droppedItemStack);
			droppedItem.setVelocity(player.getLocation().getDirection().multiply(1.1));

			//3 If this item landed, put this item on the block where it's landed with the correct facing
			final BukkitRunnable runnable = new BukkitRunnable()
			{
				private Location lastLocation = null;

				@Override
				public void run()
				{
					final Location currentLocation = droppedItem.getLocation();

					if (!droppedItem.isValid() || currentLocation.equals(lastLocation))
					{
						cancel();
					}

					final Material actionItem = Material.TORCH;
					final Block currentLocationBlock = currentLocation.getBlock();
					for (Entry<Block, BlockFace> blockWithRelativeFacing : getScannedSurroundingBlocksWithRelativeFacing(currentLocation, actionItem).entrySet())
					{
						final Block block = blockWithRelativeFacing.getKey();
						final BlockFace blockFace = blockWithRelativeFacing.getValue();

						if (currentLocationBlock.getType() == Material.AIR && !ForbiddenMaterialsUtil.isForbiddenMaterial(block.getType()))
						{
							droppedItem.remove();
							setBlockDirection(currentLocationBlock, blockFace, actionItem);

							cancel();
						}
					}

					lastLocation = currentLocation;
				}
			};
			runnable.runTaskTimer(Plugin.getInstance(), 0L, 3L);
		}
	}

	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////

	private static HashMap<Block, BlockFace> getScannedSurroundingBlocksWithRelativeFacing(Location location, Material material)
	{
		final Block targetBlock = location.getBlock();
		final HashMap<Block, BlockFace> scannedSurroundingBlocksWithRelativeFacing = new HashMap<Block, BlockFace>();
		ArrayList<BlockFace> scannedFaces = new ArrayList<>();

		switch (material)
		{
			case TORCH:
			case REDSTONE_TORCH:
			case SOUL_TORCH:
			{
				scannedFaces = _torchFaces;
				break;
			}

			case LANTERN:
			case SOUL_LANTERN:
			{
				scannedFaces = _lanternFaces;
				break;
			}

			default:
				break;
		}

		for (BlockFace face : scannedFaces)
		{
			scannedSurroundingBlocksWithRelativeFacing.put(targetBlock.getRelative(face), face);
		}

		return scannedSurroundingBlocksWithRelativeFacing;
	}

	@SuppressWarnings("deprecation")
	private static void setBlockDirection(Block currentLocationBlock, BlockFace blockFace, Material material)
	{
		switch (material)
		{
			case TORCH:
			case REDSTONE_TORCH:
			{
				currentLocationBlock.setBlockData(Bukkit.getUnsafe().fromLegacy(material, _facesInBytes.get(blockFace)));

				break;
			}

			case SOUL_TORCH:
			{
				if (blockFace == BlockFace.DOWN)
				{
					currentLocationBlock.setType(material);

					break;
				}

				final Directional soulTorch = (Directional) Material.SOUL_WALL_TORCH.createBlockData();
				soulTorch.setFacing(currentLocationBlock.getRelative(blockFace).getFace(currentLocationBlock));
				currentLocationBlock.setBlockData(soulTorch);

				break;
			}

			case LANTERN:
			case SOUL_LANTERN:
			{
				currentLocationBlock.setType(material);

				if (!ForbiddenMaterialsUtil.isForbiddenMaterial(currentLocationBlock.getRelative(BlockFace.UP).getType()))
				{
					final BlockData lanternBlockData = ((Lantern) Material.LANTERN.createBlockData());
					((Lantern) lanternBlockData).setHanging(true);
					currentLocationBlock.setBlockData(lanternBlockData);
				}

				break;
			}

			default:
				break;
		}
	}

	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////

	private static final HashMap<BlockFace, Byte> _facesInBytes = new HashMap<BlockFace, Byte>()
	{
		private static final long serialVersionUID = -3719699417046935938L;

		{
			put(BlockFace.DOWN,  (byte) 5);
			put(BlockFace.NORTH, (byte) 3);
			put(BlockFace.SOUTH, (byte) 4);
			put(BlockFace.WEST,  (byte) 1);
			put(BlockFace.EAST,  (byte) 2);
		}
	};
	
	private static final ArrayList<BlockFace> _torchFaces = new ArrayList<>()
	{
		private static final long serialVersionUID = 2760943478592566475L;

		{
			add(BlockFace.DOWN);
			add(BlockFace.NORTH);
			add(BlockFace.SOUTH);
			add(BlockFace.WEST);
			add(BlockFace.EAST);
		}
	};
	
	private static final ArrayList<BlockFace> _lanternFaces = new ArrayList<>()
	{
		private static final long serialVersionUID = 2760943478592566475L;

		{
			add(BlockFace.UP);
			add(BlockFace.DOWN);
		}
	};
}
