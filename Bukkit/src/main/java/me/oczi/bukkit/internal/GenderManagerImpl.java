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
    Set<String> root = gendersConfig.getKeys(false);
    // Init default gender and remove them from the root
    // to avoid a double register.
    initDefaultGender(root);
    root.removeIf(s -> s.equalsIgnoreCase("unknown"));
    for (String genderEntry : root) {
      Gender gender = createGenderByNode(genderEntry);
      registerGender(gender);
    }
  }

  private void initDefaultGender(Set<String> root) {
    String nodeRoot = "unknown";
    Gender gender;
    if (root.contains(nodeRoot)) {
      gender = createGenderByNode(nodeRoot);
    } else {
      gender = new GenderImpl(UNKNOWN.getName(),
          UNKNOWN.getName(),
          UNKNOWN.getColor(),
          UNKNOWN.getPrefix());
    }
    registerGender(gender);
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
    return genders.get(genderName);
  }
}
