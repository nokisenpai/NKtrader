package fr.darkvodou.NKtrader.enums;

import org.bukkit.ChatColor;

public enum MsgUtils
{

	PREFIX(ChatColor.DARK_GRAY + "(" + ChatColor.DARK_RED + ChatColor.BOLD + "NKT" + ChatColor.RESET + ChatColor.DARK_GRAY + ") " + ChatColor.RESET),

	PREFIX_ERROR(PREFIX.toString() + ChatColor.DARK_RED),
	PREFIX_SUCCESS(PREFIX.toString() + ChatColor.GREEN),

	// #################
	// Errors
	// #################

	ERROR_CONSOL_IS_SENDER(PREFIX_ERROR + "Vous ne pouvez pas executer cette commande (console ou autre)."),
	ERROR_HAS_NOT_PERM(PREFIX_ERROR + "Vous n'avez pas la permission d'executer cette commande."),
	ERROR_NOT_ENOUGH_ARGS(PREFIX_ERROR + "Vous n'avez pas mis assez d'arguments."),
	ERROR_BAD_ARGS(PREFIX_ERROR + "Un ou plusieurs argument(s) ne sont pas correcte(s), verifiez la syntaxe.");

	private String name;

	MsgUtils(String name)
	{
		this.name = name;
	}

	public String toString()
	{
		return name;
	}
}
