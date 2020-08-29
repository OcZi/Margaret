package me.oczi.bukkit.utils;

import me.oczi.bukkit.MargaretMain;
import me.oczi.bukkit.PluginCore;
import me.oczi.bukkit.objects.Gender;
import me.oczi.common.exceptions.NotInstantiatedClassException;
import org.bukkit.ChatColor;

import java.util.Collection;

public final class Genders {
  private static final PluginCore core = MargaretMain.getCore();
  private static final GenderManager genderManager = core.getGenderManager();

  private Genders() { throw new NotInstantiatedClassException(); }

  /**
   * Get gender from {@link GenderManager}.
   * @param genderName Gender to get.
   * @return Gender, or EmptyGender if not exist.
   */
  public static Gender getGender(String genderName) {
    return genderManager.getGender(genderName);
  }

  /**
   * Check is gender exist.
   * @param genderName Gender to check.
   * @return gender exist or not.
   */
  public static boolean genderExist(String genderName) {
    return genderManager.genderExist(genderName);
  }

  /**
   * Get all the genders colorized as String.
   * @return all genders as string.
   */
  public static String getGendersNameColorized() {
    StringBuilder builder = new StringBuilder();
    for (Gender gender : genderManager.getGenders()) {
      if (builder.length() != 0) {
        builder
            .append(ChatColor.RESET)
            .append(", ");
      }
      builder.append(gender.getFormalNameColorized());
    }
    return builder.toString();
  }

  /**
   * Get all the genders.
   * @return All genders.
   */
  public static Collection<Gender> getGenders() {
    return genderManager.getGenders();
  }

  /**
   * Get the {@link GenderManager}
   * @return Gender manager
   */
  public static GenderManager getGenderManager() {
    return genderManager;
  }

  public static void checkGender(Gender gender) {
    if (gender == null)
      throw new NullPointerException(
          "Gender is null (Bad initialization?)");
  }
}
