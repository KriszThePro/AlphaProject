package dev.chrs.essentialapis.api.inventorygui;

import java.util.ArrayList;

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
	public static InventoryGuiApi createGui(final Player player, final int size) throws PlayerAlreadyHasGUIException, IllegalInventorySizeException
	{
		return new InventoryGuiApi(player, size, null);
	}

	public static InventoryGuiApi createGui(final Player player, final int size, final String title) throws PlayerAlreadyHasGUIException, IllegalInventorySizeException
	{
		return createGui(player, size, title);
	}
	
	public static InventoryGuiApi getGuiByPlayer(final Player player)
	{
		Object[] array = _guiInstances.stream().filter(g -> g._player == player).toArray();

		return array.length == 1 ? (InventoryGuiApi) array[0] : null;
	}
	
	public InventoryGuiApi addItem(final int slot, final Material material, final String name, final ArrayList<String> lore)
	{
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();

		if(name != null)
		{
			meta.setDisplayName(ColorApi.process(name));
		}
		meta.setLore(ColorApi.process(lore));

		item.setItemMeta(meta);

		getGui().setItem(slot, item);

		return this;
	}

	public InventoryGuiApi removeItem(final int slot)
	{
		getGui().setItem(slot, null);

		return this;
	}

	public Inventory getGui()
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
		if (getGuiByPlayer(player) == null)
		{
			throw new InventoryIsNullException();
		}

		InventoryGuiApi instance = getGuiByPlayer(player);
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
	
	protected static void removeInstance(final Player player)
	{
		_guiInstances.remove(getGuiByPlayer(player));
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////

	private InventoryGuiApi(final Player player, final int size, final String title) throws PlayerAlreadyHasGUIException, IllegalInventorySizeException
	{
		final int countOfGuiOfPlayer = (int) _guiInstances.stream().filter(g -> g.getPlayer() == player).count();
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
		_guiInstances.add(this);

		openInventory();
	}
	
	private void openInventory()
	{
		_player.openInventory(_inventory);
	}

	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	private final static ArrayList<InventoryGuiApi> _guiInstances = new ArrayList<InventoryGuiApi>();
	private Inventory _inventory;
	private Player _player;
	private int _size;
}
