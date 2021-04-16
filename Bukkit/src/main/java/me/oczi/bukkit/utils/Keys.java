package me.oczi.bukkit.utils;

import me.fixeddev.commandflow.annotated.part.Key;
import me.fixeddev.commandflow.bukkit.annotation.Sender;

import java.lang.reflect.Type;

public interface Keys {

  static Key senderOf(Type type) {
    return new Key(type, Sender.class);
  }
}
