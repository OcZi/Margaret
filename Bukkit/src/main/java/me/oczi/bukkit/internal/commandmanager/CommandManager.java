package me.oczi.bukkit.internal.commandmanager;

import app.ashcon.intake.bukkit.graph.BasicBukkitCommandGraph;
import app.ashcon.intake.dispatcher.Dispatcher;
import app.ashcon.intake.fluent.DispatcherNode;

public interface CommandManager {

  BasicBukkitCommandGraph getGraph();

  Dispatcher getDispatcher();

  DispatcherNode getMargaretNode();

  DispatcherNode getListNode();

  DispatcherNode getPartnerNode();

  DispatcherNode getProposalNode();

  DispatcherNode getAdminNode();
}
