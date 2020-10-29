package me.oczi.bukkit.utils;

import java.util.EnumSet;
import java.util.Set;

/**
 * All the messages of the plugin.
 */
public enum Messages {

  //Other messages
  PREFIX("$0[$1MG$0] $2» $0", "prefix"),

  //Prefix accepts ChatColor translate
  PREFIX_PARTNER("&4&l°", "prefix"),
  PREFIX_NO_PARTNER("&c&l!", "prefix"),

  //General error messages
  ONLY_PLAYER("$4This command is only for players.", "error"),
  NEEDS_ARGUMENT("$4Needs more arguments to execute this command.", "error"),
  WAIT_COOLDOWN("$4Wait $3{0} $4seconds before execute this action.", "error"),

  PLAYER_NO_PERMISSION("$4You don't have permissions to perform this command.", "error"),
  YOUR_PARTNER_NO_PERMISSION("$4Your partnership doesn't have permissions to perform this command.'", "error"),
  PARTNER_HAVE_PERMISSION("$4Partner {0} already have this permission.", "error"),
  PARTNER_NOT_HAVE_PERMISSION("$4Partner {0} doesn't have this permission.", "error"),

  INVALID_COMMAND("$4Command $3{0} $4invalid", "error"),
  INVALID_PARTNER("$3Partner $3{0} $4invalid", "error"),
  INVALID_PARTNER_PERMISSION("$3Partner permission $3'{0}' $4invalid", "error"),
  INVALID_ARGUMENT("$4Argument $3{0} $4is invalid.'", "error"),
  INVALID_GENDER("$4Gender $3{0} $4is invalid.", "error"),
  INVALID_RELATION("$4Relation $3{0} $4is invalid.", "error"),
  INVALID_SETTING("Invalid setting.", "error"),
  INVALID_GAMEMODE("$4Cannot use this command in {0} mode.", "error"),
  INVALID_HEALTH("$4Invalid $3{0} $4health. (Is greater than max health or minor than 0?)", "error"),
  INVALID_HOME_ALIAS("Invalid home alias.", "error"),

  PLAYER_ARG_OFFLINE("$4Player $3{0} $4is not offline.", "error"),
  PLAYER_OFFLINE("$4Player is not online.", "error"),
  PARTNER_OFFLINE("$4Your partner is offline.", "error"),

  PLAYER_HAVE_PARTNER("$4{0} already has a partner!", "error"),
  PLAYER_NOT_HAVE_PARTNER("$4{0} doesn't have a partner!", "error"),
  YOU_HAVE_A_PARTNER("$4You have a partner!", "error"),
  YOU_NOT_HAVE_A_PARTNER("$4You don't have a partner!", "error"),

  CONFIGURATION_NOT_FOUND("$4Configuration $3{0}$4 not found.", "error"),

  NOT_IN_PROPOSAL_LIST("$4You don't a proposal from $3{0}$4.", "error"),
  PROPOSAL_ALREADY_SENT("$4You already sent a proposal to $3{0}", "error"),
  CANNOT_PROPOSE_YOURSELF("$4You cannot propose yourself...", "error"),
  NO_HAVE_CURRENT_PROPOSAL("$4You don't have a current proposal.", "error"),
  PLAYER_NOT_ACCEPT_PROPOSALS("$3{0} $4doesn't accept proposals.", "error"),

  INSUFFICIENT_HEALTH("$4Health too low!", "error"),
  PARTNER_DEAD("$4Your partner is dead.", "error"),

  NO_MORE_HOMES("$4Partner cannot claim more homes.", "error"),
  HOME_LIMIT_REACHED("$4Partner cannot claim more homes (Max possible amount reached)", "error"),
  NOT_HAVE_HOMES("$4Partner's HomeList is empty..", "error"),
  HOME_NOT_EXIST("$4Home $3{0} $4doesn't exist.", "error"),

  INVENTORY_FULL("$4Partner's inventory is full!", "error"),
  PLAYER_PROPOSAL_LIST_FULL("$4Player's proposal list is full!", "error"),

  NEED_ITEM_IN_HAND("$4Grab an item to send!.", "error"),

  SETTING_ERROR("$4Setting {0} is not {1}.", "error"),
  SETTING_PARTNER_NOT_APPLY("$4This setting will not be applied due to Partner's permission.", "error"),
  SETTING_PARTNER_ONLY("$4This setting will not have effects without a partnership.", "error"),
  RELATION_TOO_LONG("$4Relation $3{0} $4too long!", "error"),

  //Partner messages
  CURRENT_PARTNER("Partner: {0}", "partnership"),
  PARTNER_STARTED("$3{0} $5and $3{1} $5are now partners!", "partnership"),
  PARTNER_ENDED("$3{0} $5and $3{1} $5are no longer partners.", "partnership"),
  PARTNER_TELEPORT_TO_YOU("$1Your partnership has teleported to you.", "partnership"),
  PARTNER_TELEPORT_SUCCESS("$1Teleported successfully.", "partnership"),

  //Health messages
  HEALTH_SENT("$1You sent $3{0} $1of your health to you partner.", "partnership"),
  HEALTH_RECEIVED("$1You received $3{0} $1of health from your partnership.", "partnership"),

  //Gift messages
  GIFT_SENT("$1You gift $3{0} $1to your partner!", "partnership"),
  GIFT_RECEIVED("$1You receive d$3{0} $1from your partner!", "partnership"),

  //Relation messages
  RELATION_SET("$2Relation set to: $3{0}", "partnership"),

  //Home messages
  HOME_CREATED("$2Home created in {0}.", "home"),
  HOME_CREATED_AS("$2Home created in {0} as {1}.", "home"),
  HOME_DELETED("$4Home {0} deleted.", "home"),
  HOME_TELEPORT("$2Teleported to home {0}.", "home"),

  //Partner's information messages
  PARTNER_ID("Partner's ID: $3{0}", "partner-info"),
  PARTNER_PLAYER_1("Player 1: $3{0}", "partner-info"),
  PARTNER_PLAYER_2("Player 2: $3{0}", "partner-info"),
  PARTNER_RELATION("Relation: $3{0}", "partner-info"),
  PARTNER_PERMISSIONS("Permissions: $3{0}", "partner-info"),
  PARTNER_HOMES("Homes: $3{0}", "partner-info"),
  PARTNER_MAX_HOMES("Maximum homes: $3{0}", "partner-info"),

  //Partner Chat messages
  CHAT_MESSAGE_SENT("$2[$3{0} $2>> $3{1}$2] $2» $5{2}", "chat"),
  CHAT_MESSAGE_RECEIVED("$2[$3{0} $2<< $3{1}$2] $2» $5{2}", "chat"),
  CHAT_NO_MESSAGE("Write the message you want to send.", "chat"),

  CHAT_SPY_RECEIVED("$2[ID:$3{0}$2] [$3{1} $2<< $3{2}$2] » $5{3}", "chat"),

  //Proposal messages
  YOU_RECEIVE_A_PROPOSAL("$1New proposal from: $3{0}", "proposal"),
  YOU_RECEIVE_A_PROPOSAL_TO_BE("$1New proposal from: $3{0} $1to be $3{1}", "proposal"),
  PROPOSAL_SENT("$1Proposal sent to: $3{0}", "proposal"),

  ACCEPT_PROPOSAL("$3{0}'s $1proposal accepted!", "proposal"),
  PROPOSAL_ACCEPTED("$3{0} $1accepts your proposal!", "proposal"),

  PROPOSAL_DECLINED("$3{0}'s $1proposal denied.", "proposal"),
  PROPOSAL_DENIED("$1Your proposal has been denied.", "proposal"),

  PROPOSAL_CANCELLED("$4Proposal cancelled...", "proposal"),

  // Interactive messages will have hardcoded colors.
  PROPOSAL_ACCEPT("Accept", "proposal"),
  PROPOSAL_DENY("Deny", "proposal"),

  // Hover Proposal
  PROPOSAL_ACCEPT_HOVER("$1Click to accept the proposal of $3'{0}'", "proposal"),
  PROPOSAL_DENY_HOVER("$1Click to decline the proposal of $3'{0}'", "proposal"),
  PROPOSAL_ACTION_ENTRY("{0} - {1}", "proposal"),

  //Gender messages
  SET_GENDER("$1Gender set to: {0}", "gender"),
  SET_GENDER_OF("$1Gender of $2{0} $1set to $2{1}", "gender"),

  //List messages
  LIST_HEADER("{0}[{1}]", "list"),
  LIST_HOME_HEADER("Homes[{0}]", "list"),
  LIST_PROPOSALS_HEADER("Proposals[{0}]", "list"),
  LIST_RELATIONS_HEADER("Relations[{0}]", "list"),
  LIST_SETTINGS_HEADER("Settings[{0}]", "list"),
  LIST_GENDERS_HEADER("Genders[{0}]", "list"),
  LIST_PARTNER_HEADER("Top partnerships[{0} of {1}]", "list"),

  LIST_ENTRY("- {0}", "list"),
  LIST_ENUM_ENTRY("{0}. {1}", "list"),
  LIST_HOME_ENTRY("{0}. {1}: $1{2}", "list"),
  LIST_SETTING_ENTRY_HOVER("$1Click to set $3{0} $1to $3{1}!", "list"),
  LIST_PARTNER_ENTRY("$1{0}. {1} $1- {2}", "list"),
  LIST_PARTNER_ENTRY_EMPTY("$1{0}. Nothing $1- here!", "list"),

  // Help messages.
  COMMAND_LIST_PAGE_HEADER("$0-------$2[$3{0} {1}$2]$0-------", "help"),
  COMMAND_LIST_PAGE_FOOTER("$0--------------------------------", "help"),

  ALL_PERMISSION_ADDED("$1All Partner's Permission added to this partnership session.",  "admin"),
  MAX_HOMES_SET_TO("$1Max homes set to $3'{0}' $1for this partnership session.", "admin"),
  MAX_HOMES_CHANGED("$1Your Partner now has a maximum of $3'{0}' $1homes.", "admin"),

  COMMAND_LIST_ENTRY("/{0} {1} - {2}", "help"),
  HIDDEN_NODE("$1Command children of $3{0}$1.", "help"),
  UNKNOWN_DESC("{0} command.", "help"),
  SUGGESTIONS("Suggestions: {0}", "help"),

  USAGE_ENTRY("Usage: $1/{0} - {1}", "help"),

  PERMISSION_ADDED("$1Permission $3{0} $1added to partnership $1'{1}'", "partnership-permission"),
  PERMISSION_REMOVE("$1Permission $3{0} $1removed from partnership $1'{1}'", "partnership-permission"),

  //Reload messages
  CONFIGURATION_RELOADED("$1Configuration $3{0} $1has been reloaded.", "reload"),

  //Other messages
  MARGARET_INFO("Margaret: $1{0}", "other"),
  AUTHORS("Author(s): $1{0}", "other"),
  GITHUB_URL("Github: {0}", "other"),
  GITHUB_HOVER("$1Click to go Github's repository", "other"),
  SETTING_ENTRY("{0}: $1{1}", "other"),
  SETTING_ENTRY_OF("{0}'s {1}: $1{2}", "other"),
  CURRENT_GENDER("Current Gender: {0}", "list"),
  NONE("$2None.", "other"),

  VOID(" ", "test"),
  ERROR_FATAL("NANI!?", "test");

  private final String message;
  private final String category;

  private static final Set<Messages> messages =
      EnumSet.allOf(Messages.class);

  Messages(String message, String category) {
    this.message = message;
    this.category = category;
  }

  public String getNode() {
    return (category + "." + name()).toLowerCase();
  }

  public String getCategory() {
    return category;
  }

  public String getMessage() {
    return message;
  }

  public static Set<Messages> defaultMessagesSet() {
    return messages;
  }
}