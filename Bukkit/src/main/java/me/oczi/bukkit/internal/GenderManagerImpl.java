package me.oczi.bukkit.internal;

import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.objects.GenderImpl;
import me.oczi.bukkit.utils.BukkitUtils;
import me.oczi.bukkit.utils.GenderManager;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.common.utils.CommonsUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

import static me.oczi.bukkit.utils.DefaultGender.UNKNOWN;

/**
 * Implementation of Gender Manager.
 */
public class GenderManagerImpl implements GenderManager {
  private final Gender defaultGender = new GenderImpl(UNKNOWN.getName(),
      "Unknown",
      UNKNOWN.getColor(),
      UNKNOWN.getPrefix());

  private final Map<String, Gender> genders = new HashMap<>();
  private final FileConfiguration gendersConfig;

  public GenderManagerImpl(FileConfiguration gendersConfig) {
    this.gendersConfig = gendersConfig;
    initGenders();
  }

  /**
   * Initialization of gender.
   */
  public void initGenders() {
    Set<String> genderRoot = gendersConfig.getKeys(false);
    // Always register Unknown gender.
    genders.put(defaultGender.getRealName(), defaultGender);

    for (String genderEntry : genderRoot) {
      Gender gender = createGenderByNode(genderEntry);
      registerGender(gender);
    }
  }

  private Gender createGenderByNode(String gender) {
    String formalName = gendersConfig.getString
        (gender + ".formal-name", "");
    String colorName = gendersConfig.getString
        (gender + ".color", "");
    String prefix = gendersConfig.getString
        (gender + ".prefix", "");

    CommonsUtils.checkStrings(
        "Properties of gender " + gender + " is invalid.",
        formalName, colorName, prefix);

    ChatColor color;
    if (BukkitUtils.isChatColor(colorName)) {
      color = ChatColor.valueOf(colorName.toUpperCase());
    } else {
      MessageUtils.warning(
          "Color name "+ "(" + colorName + ")" +
              " of " + gender + " is invalid.",
          "Set color to WHITE.");
      color = ChatColor.WHITE;
    }
    return new GenderImpl(gender, formalName, color, prefix);
  }

  @Override
  public void registerGender(Gender gender) {
    String genderName = gender.getRealName();
    if (genders.containsKey(genderName)) {
      MessageUtils.warning(
          "Gender " + genderName
              + " already registered. Ignored it.");
      return;
    }

    genders.put(genderName, gender);
  }

  @Override
  public boolean genderExist(String gender) {
    return genders.containsKey(gender);
  }

  @Override
  public Collection<Gender> getGenders() {
    return genders.values();
  }

  @Override
  public Gender getGender(String genderName) {
    return genders.getOrDefault(genderName, defaultGender);
  }
}
