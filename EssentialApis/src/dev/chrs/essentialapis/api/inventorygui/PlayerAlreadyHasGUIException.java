package dev.chrs.essentialapis.api.inventorygui;

public class PlayerAlreadyHasGUIException extends Exception
{
	private static final long serialVersionUID = -7973822262803242798L;

	public PlayerAlreadyHasGUIException()
	{
		super("The player has already got a InventoryGuiApi");
	}
}
