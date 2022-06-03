package dev.chrs.essentialapis.api.inventorygui;

public class InventoryIsNullException extends Exception
{
	private static final long serialVersionUID = 6364941405136722499L;

	public InventoryIsNullException()
	{
		super("Inventory is null");
	}
}
