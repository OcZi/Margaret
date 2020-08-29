package me.oczi.bukkit.internal.objectcycle;

public interface ObjectLoader<T> {

  void load(T loadObject);

  void close(T loadObject);

  void swapObject(T loadObject);
}
