package me.oczi.bukkit.internal.commandflow.components;

import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.translator.TranslationProvider;
import me.fixeddev.commandflow.translator.Translator;
import me.oczi.bukkit.utils.MessageUtils;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;

import java.util.function.Function;

public class MargaretTranslator implements Translator {
  private TranslationProvider provider;

  public MargaretTranslator() {
    this.provider = new MargaretTranslationProvider();
  }

  @Override
  public Component translate(Component component, Namespace namespace) {
    if (!(component instanceof TranslatableComponent)) {
      return component;
    }
    TranslatableComponent translatable = (TranslatableComponent) component;
    String translation = provider.getTranslation(namespace, translatable.key());
    return MessageUtils.composeComponent(translation, translatable.args(), false);
  }

  @Override
  public void setProvider(TranslationProvider provider) {
    this.provider = provider;
  }

  // Never used.
  @Override
  public void setConverterFunction(Function<String, TextComponent> stringToComponent) {}
}
