package me.oczi.bukkit.internal.database;

import me.oczi.bukkit.internal.database.tasks.*;

/**
 * A collection of all DbTask interfaces.
 */
public interface DbTasks
    extends DbTaskSet, DbTaskGet,
    DbTaskSetup, DbTaskUpdate,
    DbTaskDelete, DbTaskOthers {}