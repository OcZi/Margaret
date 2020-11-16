package me.oczi.bukkit.commands;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.oczi.bukkit.internal.CooldownManager;
import me.oczi.bukkit.internal.commandflow.CommandFlow;
import me.oczi.bukkit.objects.CooldownPlayer;
import me.oczi.bukkit.objects.Proposal;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.utils.*;
import me.oczi.bukkit.utils.settings.BasicSetting;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

import static me.oczi.bukkit.utils.CommandPreconditions.*;

@Command(
    names = {"proposal", "prop"},
    desc = "%translatable:proposal.desc%",
    permission = "margaret.proposal")
public class CommandProposal implements CommandClass {

  @Command(
      names = {"help", "?"},
      desc = "%translatable:proposal.help.desc%")
  public void mainCommand(CommandSender sender,
                          CommandFlow commandFlow) {
    Commands.composeFullChildrenHelp(sender,
        commandFlow.getSubCommandsOf("proposal"),
        "margaret",
        "proposal");
  }

  @Command(
      names = {"send"},
      desc = "%translatable:proposal.send.desc%",
      permission = "margaret.proposal")
  public void send(CooldownManager cooldownManager,
                   @Sender MargaretPlayer margaretSender,
                   @OptArg @Sender Partnership optionalPartnership,
                   MargaretPlayer playerTarget,
                   @OptArg("") String relation)
      throws ConditionException {
    CooldownPlayer cooldown = cooldownManager.getProposalCooldown();
    UUID uuid = margaretSender.getUniqueId();
    checkCollectionNotContains(cooldown.getCacheSet(),
        uuid,
        Messages.WAIT_COOLDOWN,
        cooldown.getCountdownInSeconds(uuid));
    throwIf(optionalPartnership, p -> !p.isEmpty(), Messages.YOU_HAVE_A_PARTNER);

    checkStringEquals(playerTarget.getName(), margaretSender.getName(),
        Messages.CANNOT_PROPOSE_YOURSELF);
    // If not have permission and relation is set, throw ConditionException.
    Player player = MargaretPlayers.getAsPlayer(margaretSender);
    throwIf(relation,
        r -> !r.isEmpty() &&
            !player.hasPermission(
                PartnershipPermission.CUSTOM_RELATION.getNode()),
        Messages.PLAYER_NO_PERMISSION);

    checkMargaretPlayerHavePartner(playerTarget);
    checkSetting(playerTarget,
        BasicSetting.ALLOW_PROPOSALS,
        Messages.PLAYER_NOT_ACCEPT_PROPOSALS);
    checkCurrentProposal(margaretSender);

    if (!playerTarget.getCurrentProposal().isEmpty()) {
      if (margaretSender.containsProposalOf(playerTarget)) {
        accept(margaretSender, cooldownManager, playerTarget);
        return;
      }
    }

    Set<Proposal> proposals = playerTarget.getProposals();
    int maximumProposals = MargaretYamlStorage.getMaxProposals();
    throwIf(maximumProposals,
        maxProp -> maxProp <= proposals.size(),
        Messages.PLAYER_PROPOSAL_LIST_FULL);

    MargaretPlayers.sendProposal(margaretSender,
        playerTarget,
        relation,
        !relation.isEmpty());
  }

  @Command(
      names = "accept",
      desc = "%translatable:proposal.help.desc%",
      permission = "margaret.proposal")
  public void accept(@Sender MargaretPlayer margaretSender,
                     CooldownManager cooldownManager,
                     MargaretPlayer playerTarget)
      throws ConditionException {
    checkNotHavePartner(margaretSender);
    CooldownPlayer cooldown = cooldownManager.getProposalCooldown();
    UUID uuid = margaretSender.getUniqueId();
    checkCollectionNotContains(cooldown.getCacheSet(),
        uuid,
        Messages.WAIT_COOLDOWN,
        cooldown.getCountdownInSeconds(uuid));
    checkNotHavePartner(margaretSender);

    checkStringEquals(playerTarget.getName(),
        margaretSender.getName(),
        Messages.CANNOT_PROPOSE_YOURSELF);
    checkProposalList(margaretSender, playerTarget);

    Partnerships.newPartner(margaretSender, playerTarget);
  }

  @Command(
      names = "decline",
      desc = "%translatable:proposal.decline.desc%",
      permission = "margaret.proposal")
  public void decline(@Sender MargaretPlayer margaretSender,
                      CooldownManager cooldownManager,
                      MargaretPlayer playerTarget)
      throws ConditionException {
    checkProposalList(margaretSender, playerTarget);

    margaretSender.removeProposal(
        playerTarget.getCurrentProposal());
    MessageUtils.compose(margaretSender,
        Messages.PROPOSAL_DECLINED,
        true,
        playerTarget.getName());
    MessageUtils.compose(playerTarget,
        Messages.PROPOSAL_DENIED,
        true,
        margaretSender.getName());
    cooldownManager.setProposalCooldown
        (playerTarget.getUniqueId());
  }

  @Command(
      names = "cancel",
      desc = "%translatable:proposal.cancel.desc%",
      permission = "margaret.proposal")
  public void cancel(CooldownManager cooldownPlayer,
                     @Sender MargaretPlayer margaretSender)
      throws ConditionException {
    Proposal actualProposal = margaretSender.getCurrentProposal();
    checkProposal(actualProposal, Messages.NO_HAVE_CURRENT_PROPOSAL);

    MargaretPlayer margaretPlayer2 = actualProposal.getReceiver();
    margaretPlayer2.removeProposal(actualProposal);
    cooldownPlayer.setProposalCooldown(margaretSender.getUniqueId());
  }
}
