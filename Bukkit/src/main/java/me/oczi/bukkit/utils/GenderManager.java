package me.oczi.bukkit.utils;

import me.oczi.bukkit.objects.Gender;

import java.util.Collection;
import java.util.Set;

public interface GenderManager {

  /**
   * GenderManager contains gender.
   * @param genderName Gender to search
   * @return Gender exist.
   */
  boolean genderExist(String genderName);

  /**
   * Register gender into GenderManager.
   * @param gender Gender to register.
   */
  void registerGender(Gender gender);

  /**
   * Get all genders.
   * @return All genders.
   */
  Collection<Gender> getGenders();

  /**
   * Get gender registered in GenderManager.
   * @param genderName Gender to search.
   * @return Gender searched, or {@link DefaultGender}.
   */
  Gender getGender(String genderName);
}
