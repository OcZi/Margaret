package me.oczi.bukkit.internal.objectcycle.partner;

import com.google.common.collect.Lists;
import me.oczi.bukkit.internal.database.DbTasks;
import me.oczi.bukkit.objects.Home;
import me.oczi.bukkit.objects.PartnerHome;
import me.oczi.bukkit.objects.Proposal;
import me.oczi.bukkit.objects.collections.HomeList;
import me.oczi.bukkit.objects.collections.PartnerPermissionSet;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.objects.partner.PartnerData;
import me.oczi.bukkit.objects.partner.PartnerImpl;
import me.oczi.bukkit.objects.partner.PartnerProperties;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.utils.*;
import me.oczi.common.api.Emptyble;
import me.oczi.common.storage.sql.dsl.result.SqlObject;
import me.oczi.common.utils.BitMasks;
import me.oczi.common.utils.CommonsUtils;
import me.oczi.common.utils.Statements;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static me.oczi.bukkit.utils.Partners.callPartnerStartEvent;

public class PartnerObjectBuilder {
  private final DbTasks dbTasks;

  private final int partnerMaxHomes = MargaretYamlStorage.getPartnerMaxHomes();

  public PartnerObjectBuilder(DbTasks dbTasks) {
    this.dbTasks = dbTasks;
  }

  public Partner newPartner(MargaretPlayer margaretPlayer1,
                            MargaretPlayer margaretPlayer2) {
    if (callPartnerStartEvent(
        margaretPlayer1, margaretPlayer2)) {
      return null;
    }

    String id = dbTasks.foundUnusedPartnerId();

    Proposal proposal = margaretPlayer2.getCurrentProposal();
    PartnerData partnerData = new PartnerData(id,
        margaretPlayer1.getUniqueId(),
        margaretPlayer2.getUniqueId(),
        proposal.getRelation());

    PartnerProperties partnerProperties =
        createPartnerProperties(id,
            partnerMaxHomes,
            -1,
            MargaretPlayers
                .getAsPlayer(
                    margaretPlayer1, margaretPlayer2));
    Partner partner = new PartnerImpl(partnerData, partnerProperties);

    dbTasks.setupPartnerData(partner);
    dbTasks.setupPartnerProperties(id, partnerProperties);

    Partners.clearAndSetPartner(partner,
        margaretPlayer1,
        margaretPlayer2);
    return partner;
  }

  public PartnerData initPartnerData(String id) {
    Map<String, SqlObject> metadata = dbTasks.getPartnerData(id);
    if (CommonsUtils.isNullOrEmpty(metadata)) {
      MessageUtils.warning(
          "Partner ID "
              + id + " doesn't exist in database.");
      return null;
    }

    return createPartnerData(metadata);
  }

  public PartnerProperties initPartnerProperties(String id) {
    Map<String, SqlObject> partnerProperties =
        dbTasks.getPartnerProperties(id);
    if (CommonsUtils.isNullOrEmpty(partnerProperties)) {
      MessageUtils.warning(
          "Partner " + id + " properties doesn't exist. " +
              "Creating default properties...");
      PartnerProperties properties =
          createPartnerProperties(id, partnerMaxHomes, -1);
      dbTasks.setupPartnerProperties(id, properties);
      return properties;
    }

    int partnerMaxHomes = partnerProperties
        .get("maxhomes").getInteger();
    int bits = partnerProperties
        .get("bitpermissions").getInteger();
    return createPartnerProperties(id, partnerMaxHomes, bits);
  }

  public PartnerData createPartnerData(Map<String, SqlObject> metadata) {
    SqlObject id = metadata.get("id");
    SqlObject player1 = metadata.get("player1");
    SqlObject player2 = metadata.get("player2");
    SqlObject relation = metadata.get("relation");
    Statements.checkObjects(
        "Partner data is null.",
        id, player1, player2, relation);

    UUID uuid1 = UUID.fromString(player1.getString());
    UUID uuid2 = UUID.fromString(player2.getString());
    return new PartnerData(
        id.getString(),
        uuid1,
        uuid2,
        relation.getString());
  }


  public PartnerProperties createPartnerProperties(String id,
                                                   int maxHomes,
                                                   int bits) {
    return createPartnerProperties(id, maxHomes, bits, null);
  }
  /**
   * Create partner's properties.
   * <p>If bits is -1, will be refilled.</p>
   * @param id ID of partner.
   * @param maxHomes Maximum homes of partner.
   * @param bits Partner's permission in bits.
   * @return Partner properties.
   */
  public PartnerProperties createPartnerProperties(String id,
                                                   int maxHomes,
                                                   int bits,
                                                   @Nullable List<Player> players) {
    if (bits == -1) {
      bits = refillPartnerPermission();
    }
    if (!CommonsUtils.isNullOrEmpty(players)) {
      bits = Partners.transformPermissions(bits, players);
    }

    PartnerPermissionSet partnerPermissionSet =
        new PartnerPermissionSet(bits);
    HomeList homeList =
        createHomeList(id, maxHomes);
    return new PartnerProperties(
        partnerPermissionSet, homeList);
  }

  public int refillPartnerPermission() {
    int newBit = 0;
    List<String> defaultPermissions = MargaretYamlStorage
        .getDefaultPartnerPermissions();
    if (defaultPermissions.contains("*")) {
      return BitMasks.sumEnumClass(newBit, PartnerPermission.class);
    }

    List<Integer> sumBits = new ArrayList<>();
    for (String defaultPerm : defaultPermissions) {
      if (CommonsUtils.enumExist(defaultPerm, PartnerPermission.class)) {
        PartnerPermission perm = PartnerPermission
            .valueOf(defaultPerm.toUpperCase());
        sumBits.add(BitMasks.mask(perm));
      }
    }

    return BitMasks.sumBits(newBit, sumBits);
  }

  public HomeList createHomeList(String partnerId,
                                 int partnerMaxHomes) {
    List<Home> homeList = Lists.newArrayList();
    List<SqlObject> query = Lists.newArrayList(
        dbTasks.getPartnerHomeList(partnerId).values());

    // Remove all null entries of query
    query.removeIf(Emptyble::isEmpty);
    for (SqlObject homeId : query) {
      PartnerHome home = createHome(homeId.getString());
      if (home == null)  { continue; }
      homeList.add(home);
    }
    return new HomeList(partnerId, homeList, partnerMaxHomes);
  }

  @Nullable
  public PartnerHome createHome(String id) {
    Map<String, SqlObject> query = dbTasks.getHome(id);
    if (CommonsUtils.isNullOrEmpty(query)) {
      return null;
    }
    String alias = query.get("alias").getString();
    return new PartnerHome(id, alias, newLocation(query));
  }

  public Location newLocation(Map<String, SqlObject> map) {
    return BukkitUtils.newLocation(
        map.get("world").getString(),
        map.get("x").getDouble(),
        map.get("y").getDouble(),
        map.get("z").getDouble(),
        0,
        0);
  }
}
