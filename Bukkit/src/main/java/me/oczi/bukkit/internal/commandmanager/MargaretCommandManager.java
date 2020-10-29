package me.oczi.bukkit.internal.commandmanager;

import app.ashcon.intake.CommandMapping;
import app.ashcon.intake.bukkit.BukkitIntake;
import app.ashcon.intake.bukkit.graph.BasicBukkitCommandGraph;
import app.ashcon.intake.dispatcher.Dispatcher;
import app.ashcon.intake.fluent.DispatcherNode;
import me.oczi.bukkit.PluginCore;
import me.oczi.bukkit.commands.*;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class MargaretCommandManager implements CommandManager {
  private final static Map<String, DispatcherNode> registeredNodes = new HashMap<>();

  private final BasicBukkitCommandGraph graph;
  private final BukkitIntake intake;

  private DispatcherNode margaretNode;
  private DispatcherNode proposalNode;
  private DispatcherNode adminNode;
  private DispatcherNode listNode;
  private DispatcherNode permNode;

  private DispatcherNode partnerNode;
  private DispatcherNode homeNode;

  public MargaretCommandManager(PluginCore core, Plugin plugin) {
    this.graph = new BasicBukkitCommandGraph(
        new MargaretCoreModule(this, core));
    registerNodes();
    registerBasicCommands();
    registerProposalCommands();
    registerPartnerCommands();
    registerListCommands();
    registerAdminCommands();
    registerPermissionCommand();

    this.intake = new MargaretIntake(plugin, graph);
    intake.register();
  }

  private void registerPermissionCommand() {
    permNode.registerCommands(new CommandPermission());
  }

  public void registerPartnerCommands() {
    registerCommands(partnerNode,
        new CommandPartnership(),
        new SubCommandPartnership());
    homeNode.registerCommands(new CommandHome());
  }

  public void registerBasicCommands() {
    registerCommands(margaretNode,
        new CommandMain(),
        new CommandReload(),
        new CommandVersion());
  }

  public void registerProposalCommands() {
    proposalNode.registerCommands(new CommandProposal());
  }

  public void registerListCommands() {
    listNode.registerCommands(new CommandList());
  }

  public void registerAdminCommands() {
    adminNode.registerCommands(new CommandAdmin());
  }

  public void registerCommands(DispatcherNode node, Object... objects) {
    for (Object object : objects) {
      node.registerCommands(object);
    }
  }

  public void registerNodes() {
    this.margaretNode = registerNode(
        graph.getRootDispatcherNode(),
        "margaret", "mr", "mg");
    this.proposalNode = registerNode(margaretNode,
        "proposal", "prop");
    this.listNode = registerNode(margaretNode,
        "list", "ls");
    this.adminNode = registerNode(margaretNode,
        "admin", "ad");
    this.permNode = registerNode(margaretNode,
        "permission", "perm");
    this.partnerNode = registerNode(margaretNode,
        "partnership", "partner", "pr");
    this.homeNode = registerNode(partnerNode,
        "home", "h");
  }

  public DispatcherNode registerNode(DispatcherNode parent,
                                     String... aliases) {
    DispatcherNode node = parent.registerNode(aliases);
    for (String alias : aliases) {
      registeredNodes.put(alias, node);
    }
    return node;
  }

  public static String getUsageOf(String node) {
    DispatcherNode dispatcherNode = registeredNodes.get(node);
    if (dispatcherNode == null) { return ""; }
    StringBuilder builder = new StringBuilder("<");
    for (CommandMapping command : dispatcherNode.getDispatcher().getCommands()) {
      if (builder.length() > 1) {
        builder.append("|");
      }
      builder.append(command.getPrimaryAlias());
    }
    return builder.append(">").toString();
  }

  public Dispatcher getDispatcher() {
    return margaretNode.getDispatcher();
  }

  @Override
  public BasicBukkitCommandGraph getGraph() {
    return graph;
  }

  @Override
  public DispatcherNode getMargaretNode() {
    return margaretNode;
  }

  @Override
  public DispatcherNode getListNode() {
    return listNode;
  }

  @Override
  public DispatcherNode getPartnerNode() {
    return partnerNode;
  }

  @Override
  public DispatcherNode getProposalNode() {
    return proposalNode;
  }

  @Override
  public DispatcherNode getAdminNode() {
    return adminNode;
  }
}
