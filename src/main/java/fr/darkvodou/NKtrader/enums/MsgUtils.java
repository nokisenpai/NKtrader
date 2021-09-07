package fr.darkvodou.NKtrader.enums;

import org.bukkit.ChatColor;

public enum MsgUtils
{

	PREFIX(ChatColor.DARK_GRAY + "(" + ChatColor.DARK_RED + ChatColor.BOLD + "NKT" + ChatColor.RESET + ChatColor.DARK_GRAY + ") "),

	PREFIX_ERROR(PREFIX.toString() + ChatColor.DARK_RED),
	PREFIX_SUCCESS(PREFIX.toString() + ChatColor.GREEN),
	PREFIX_USAGE(PREFIX.toString() + ChatColor.RED + "Usage: " + ChatColor.GOLD),
	DEBUG(PREFIX.toString() + ChatColor.GOLD + "DEBUG: " + ChatColor.YELLOW),

	SUCCES_ADDTRADER(PREFIX_SUCCESS + "Trader added successfully"),

	// #################
	// Errors
	// #################

	ERROR_CONSOL_IS_SENDER(PREFIX_ERROR + "Vous ne pouvez pas executer cette commande (console ou autre)."),
	ERROR_HAS_NOT_PERM(PREFIX_ERROR + "Vous n'avez pas la permission d'executer cette commande."),
	ERROR_NOT_ENOUGH_ARGS(PREFIX_ERROR + "Vous n'avez pas mis assez d'arguments."),
	ERROR_BAD_ARGS(PREFIX_ERROR + "Un ou plusieurs argument(s) ne sont pas correcte(s), verifiez la syntaxe."),
	ERROR_TOO_MANY_ARGS(PREFIX_ERROR + "Erreur syntax, trop d'arguments"),
	ERROR_TRY_KILL_TRADER(PREFIX_ERROR + "Hey ! Pas gentil ça !"),
	ERROR_NAME_LENGTH(PREFIX_ERROR + "Nom trop long (100 max)"),
	ERROR_ENTITY_TYPE(PREFIX_ERROR + "L'entityType précisé n'existe pas"),
	ERROR_MATERIAL_TYPE(PREFIX_ERROR + "Le materialType précisé n'existe pas"),
	ERROR_WORLD_NOT_EXIST(PREFIX_ERROR + "Le monde que vous voulez spécifier semble ne pas exister");

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
