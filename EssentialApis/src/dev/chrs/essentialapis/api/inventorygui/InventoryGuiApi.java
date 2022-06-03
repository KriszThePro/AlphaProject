package dev.chrs.essentialapis.api.inventorygui;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.chrs.essentialapis.api.color.ColorApi;

public class InventoryGuiApi implements Listener
{
	public static InventoryGuiApi createInventory(final Player player, final int size) throws PlayerAlreadyHasGUIException, IllegalInventorySizeException
	{
		return new InventoryGuiApi(player, size, null);
	}

	public static InventoryGuiApi createInventory(final Player player, final int size, final String title) throws PlayerAlreadyHasGUIException, IllegalInventorySizeException
	{
		return createInventory(player, size, title);
	}
	
	public static InventoryGuiApi getInventoryByPlayer(final Player player)
	{
		Object[] array = _guiList.stream().filter(g -> g._player == player).toArray();

		return array.length == 1 ? (InventoryGuiApi) array[0] : null;
	}
	
	public InventoryGuiApi addItem(final int slot, final Material material, final String name, final String... lore)
	{
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(ColorApi.process(name));
		meta.setLore(ColorApi.process(Arrays.asList(lore)));

		item.setItemMeta(meta);

		getInventory().setItem(slot, item);

		return this;
	}

	public InventoryGuiApi removeItem(final int slot)
	{
		getInventory().setItem(slot, null);

		return this;
	}

	public Inventory getInventory()
	{
		return _inventory;
	}

	public Player getPlayer()
	{
		return _player;
	}

	public int getSize()
	{
		return _size;
	}
	
	public static void updateTitle(Player player, String title) throws PlayerIsNullException, InventoryIsNullException
	{
		if (getInventoryByPlayer(player) == null)
		{
			throw new InventoryIsNullException();
		}

		InventoryGuiApi instance = getInventoryByPlayer(player);
		final ItemStack[] contents = instance._inventory.getContents();

		instance._inventory = Bukkit.createInventory(null, instance._size, ColorApi.process(title));
		instance._inventory.setContents(contents);

		if (!player.isOnline())
		{
			throw new PlayerIsNullException();
		}

		instance.openInventory();
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	protected static void removeFromList(final Player player)
	{
		_guiList.remove(getInventoryByPlayer(player));
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////

	private InventoryGuiApi(final Player player, final int size, final String title) throws PlayerAlreadyHasGUIException, IllegalInventorySizeException
	{
		final int countOfGuiOfPlayer = (int) _guiList.stream().filter(g -> g.getPlayer() == player).count();
		if (countOfGuiOfPlayer > 1)
		{
			throw new PlayerAlreadyHasGUIException();
		}

		if (size < 9 || size > 54 || size % 9 != 0)
		{
			throw new IllegalInventorySizeException();
		}

		_size = size;
		_inventory = (title == null ? Bukkit.createInventory(null, _size) : Bukkit.createInventory(null, _size, ColorApi.process(title)));
		_player = player;
		_guiList.add(this);

		openInventory();
	}
	
	private void openInventory()
	{
		_player.openInventory(_inventory);
	}

	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	private final static ArrayList<InventoryGuiApi> _guiList = new ArrayList<InventoryGuiApi>();
	private Inventory _inventory;
	private Player _player;
	private int _size;
}
