package dev.chrs.essentialapis.api.inventorygui;

public class PlayerIsNullException extends Exception
{
	private static final long serialVersionUID = 599388956315223190L;

	public PlayerIsNullException()
	{
		super("Player is null");
	}
}
