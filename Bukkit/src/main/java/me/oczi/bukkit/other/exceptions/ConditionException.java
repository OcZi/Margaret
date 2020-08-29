package me.oczi.bukkit.other.exceptions;

import app.ashcon.intake.CommandException;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.bukkit.utils.Messages;

public class ConditionException extends CommandException {

  public ConditionException(String errMessage) {
    super(errMessage);
  }

  public static ConditionException newException(Messages errMessage,
                                                Object... objects) {
    return new ConditionException(
        MessageUtils.composeMessage(
            errMessage, false, objects));
  }

  public static InvocationConditionException newRuntimeException(Messages errMessage,
                                                                 Object... objects) {
    return new InvocationConditionException(
        newException(errMessage, objects));
  }

  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}
