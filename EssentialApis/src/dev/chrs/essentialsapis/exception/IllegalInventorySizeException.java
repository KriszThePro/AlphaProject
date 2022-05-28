package dev.chrs.essentialsapis.exception;

public class IllegalInventorySizeException extends Exception
{
	private static final long serialVersionUID = 4359695339080857874L;

	public IllegalInventorySizeException()
	{
		super("Inventory size is out of range (9 - 54) OR not plural of 9");
	}
}
