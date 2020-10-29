package me.oczi.bukkit.commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import app.ashcon.intake.parametric.annotation.Default;
import me.oczi.bukkit.internal.CooldownManager;
import me.oczi.bukkit.objects.CooldownPlayer;
import me.oczi.bukkit.objects.Proposal;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.utils.*;
import me.oczi.bukkit.utils.settings.BasicSettings;
import org.bukkit.command.CommandSender;

import java.util.Set;
import java.util.UUID;

import static me.oczi.bukkit.utils.CommandPreconditions.*;

public class CommandProposal {

  @Command(
      aliases = {"send"},
      desc = "Propose a player to be your partner.",
      perms = "margaret.proposal")
  public void send(@Sender CommandSender sender,
                   CooldownManager cooldownManager,
                   MargaretPlayer margaretPlayer2,
                   @Default("") String relation)
      throws ConditionException {
    checkInstanceOfPlayer(sender);

    MargaretPlayer margaretPlayer1 = MargaretPlayers
        .getAsMargaretPlayer(sender);
    CooldownPlayer cooldown = cooldownManager.getProposalCooldown();
    UUID uuid = margaretPlayer1.getUniqueId();
    checkCollectionNotContains(cooldown.getCacheSet(),
        uuid,
        Messages.WAIT_COOLDOWN,
        cooldown.getCountdownInSeconds(uuid));
    checkNotHavePartner(margaretPlayer1);

    checkStringEquals(margaretPlayer2.getName(), sender.getName(),
        Messages.CANNOT_PROPOSE_YOURSELF);
    // If not have permission and relation is set, throw ConditionException.
    throwIf(relation,
        r -> !r.isEmpty() ||
             !sender.hasPermission(
                 PartnershipPermission.CUSTOM_RELATION.getNode()),
        Messages.PLAYER_NO_PERMISSION);

    checkMargaretPlayerHavePartner(margaretPlayer2);
    checkSetting(margaretPlayer2,
        BasicSettings.ALLOW_PROPOSALS,
        Messages.PLAYER_NOT_ACCEPT_PROPOSALS);
    checkCurrentProposal(margaretPlayer1);

    if (!margaretPlayer2.getCurrentProposal().isEmpty()) {
      if (margaretPlayer1.containsProposalOf(margaretPlayer2)) {
        accept(sender, cooldownManager, margaretPlayer2);
        return;
      }
    }

    Set<Proposal> proposals = margaretPlayer2.getProposals();
    int maximumProposals = MargaretYamlStorage.getMaxProposals();
    throwIf(maximumProposals,
        maxProp -> maxProp <= proposals.size(),
        Messages.PLAYER_PROPOSAL_LIST_FULL);

    MargaretPlayers.sendProposal(margaretPlayer1,
        margaretPlayer2,
        relation,
        !relation.isEmpty());
  }

  @Command(
      aliases = "accept",
      desc = "Accept a proposal.",
      perms = "margaret.proposal")
  public void accept(@Sender CommandSender sender,
                     CooldownManager cooldownManager,
                     MargaretPlayer margaretPlayer2)
      throws ConditionException {
    checkInstanceOfPlayer(sender);

    MargaretPlayer margaretPlayer1 = MargaretPlayers
        .getAsMargaretPlayer(sender);
    checkNotHavePartner(margaretPlayer1);
    CooldownPlayer cooldown = cooldownManager.getProposalCooldown();
    UUID uuid = margaretPlayer1.getUniqueId();
    checkCollectionNotContains(cooldown.getCacheSet(),
        uuid,
        Messages.WAIT_COOLDOWN,
        cooldown.getCountdownInSeconds(uuid));
    checkNotHavePartner(margaretPlayer1);

    checkStringEquals(margaretPlayer2.getName(),
        sender.getName(),
        Messages.CANNOT_PROPOSE_YOURSELF);
    checkProposalList(margaretPlayer1, margaretPlayer2);

    Partnerships.newPartner(margaretPlayer1, margaretPlayer2);
  }

  @Command(
      aliases = "decline",
      desc = "Decline a proposal.",
      perms = "margaret.proposal")
  public void decline(@Sender CommandSender sender,
                      CooldownManager cooldownPlayer,
                      MargaretPlayer margaretPlayer2)
      throws ConditionException {
    checkInstanceOfPlayer(sender);

    MargaretPlayer margaretPlayer1 = MargaretPlayers
        .getAsMargaretPlayer(sender);
    checkProposalList(margaretPlayer1, margaretPlayer2);

    margaretPlayer1.removeProposal(margaretPlayer2.getCurrentProposal());
    MessageUtils.compose(sender, Messages.PROPOSAL_DECLINED,
        true,
        margaretPlayer1.getName());
    MessageUtils.compose(margaretPlayer2,
        Messages.PROPOSAL_DENIED,
        true,
        sender.getName());
    cooldownPlayer.setProposalCooldown(margaretPlayer2.getUniqueId());
  }

  @Command(
      aliases = "cancel",
      desc = "Cancel your actual proposal.",
      perms = "margaret.proposal")
  public void cancel(@Sender CommandSender sender,
                     CooldownManager cooldownPlayer)
      throws ConditionException {
    checkInstanceOfPlayer(sender);
    MargaretPlayer margaretPlayer1 = MargaretPlayers
        .getAsMargaretPlayer(sender);
    Proposal actualProposal = margaretPlayer1.getCurrentProposal();
    checkProposal(actualProposal, Messages.NO_HAVE_CURRENT_PROPOSAL);

    MargaretPlayer margaretPlayer2 = actualProposal.getReceiver();
    margaretPlayer2.removeProposal(actualProposal);
    cooldownPlayer.setProposalCooldown(margaretPlayer1.getUniqueId());
  }
}
