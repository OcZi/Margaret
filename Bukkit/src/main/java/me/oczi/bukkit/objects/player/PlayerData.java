package me.oczi.bukkit.objects.player;

import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.utils.DefaultGender;
import me.oczi.bukkit.utils.EmptyObjects;
import me.oczi.bukkit.utils.Genders;
import me.oczi.common.storage.sql.dsl.result.SqlObject;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
  private final UUID uuid;
  private String name;
  private Partnership partnership;
  private Gender gender;

  /**
   * Convert a row of Player's data to object.
   *
   * @param row Row of Player's data table.
   * @return Player data object.
   */
  public static PlayerData serialize(Map<String, SqlObject> row) {
    String id = row.get("id").getString();
    String name = row.get("name").getString();
    String gender = row.get("gender").getString();

    return new PlayerData(UUID.fromString(id),
        name,
        null,
        gender);
  }

  public PlayerData(UUID uuid) {
    this(uuid, null, null, null);
  }

  public PlayerData(UUID uuid,
                    @Nullable String name,
                    @Nullable Partnership partnership,
                    @Nullable String gender) {
    this.uuid = uuid;
    this.name = name;
    this.partnership = partnership == null
        ? EmptyObjects.getEmptyPartner()
        : partnership;
    this.gender = Genders.genderExist(gender)
        ? Genders.getGender(gender)
        : Genders.getGender(DefaultGender.UNKNOWN.getName());
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public void setPartnership(Partnership partnership) {
    this.partnership = partnership;
  }

  public String getName() {
    return name;
  }

  public UUID getUniqueId() {
    return uuid;
  }

  public Partnership getPartnership() {
    return partnership;
  }

  public Gender getGender() {
    return gender;
  }

  public Map<String, String> toMap() {
    Map<String, String> map = new HashMap<>();
    map.put("uuid", uuid.toString());
    map.put("name", name);
    map.put("partnerid", partnership.getId());
    map.put("gender", gender.getFormalName());
    return map;
  }

  @Override
  public String toString() {
    String[] values =
        {"uuid = " + uuid.toString(),
         "name = " + name,
         "partnerid = " + partnership.getId(),
         "gender = " + gender.getFormalName()};
    return String.join(", ", values);
  }
}
