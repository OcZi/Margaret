package me.oczi.bukkit.objects.player;

import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.utils.DefaultGender;
import me.oczi.bukkit.utils.EmptyObjects;
import me.oczi.bukkit.utils.Genders;

import java.util.UUID;

public class PlayerData {
  private final UUID uuid;
  private final Partner partner;
  private final String name;
  private final Gender gender;

  public PlayerData(UUID uuid,
                    String name,
                    Partner partner,
                    String gender) {
    this.uuid = uuid;
    this.name = name;
    this.partner = partner == null ?
        EmptyObjects.getEmptyPartner() : partner;
    this.gender = Genders.genderExist(gender)
        ? Genders.getGender(gender)
        : Genders.getGender(DefaultGender.UNKNOWN.getName());
  }

  public String getName() {
    return name;
  }

  public UUID getUniqueId() {
    return uuid;
  }

  public Partner getPartner() {
    return partner;
  }

  public Gender getGender() {
    return gender;
  }

  @Override
  public String toString() {
    String[] values =
        {"uuid = " + uuid.toString(),
         "name = " + name,
         "partnerid = " + partner.getId(),
         "gender = " + gender.getFormalName()};
    return String.join(", ", values);
  }
}
