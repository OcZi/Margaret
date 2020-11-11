package me.oczi.bukkit.other.exceptions;

import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.bukkit.utils.Messages;

public class ConditionException extends ArgumentParseException {

  protected ConditionException(String errMessage) {
    super(errMessage);
  }

  protected ConditionException(String errMessage, Throwable throwable) {
    super(errMessage, throwable);
  }

  public static ConditionException newException(String errMessage,
                                                Throwable throwable) {
    return new ConditionException(errMessage, throwable);
  }

  public static ConditionException newException(Messages errMessage, Object... objects) {
    return newException(MessageUtils.composeMessage(errMessage, false, objects), null);
  }

  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}
