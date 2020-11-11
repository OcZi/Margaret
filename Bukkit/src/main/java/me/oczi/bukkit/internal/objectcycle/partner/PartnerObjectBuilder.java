package me.oczi.bukkit.internal.objectcycle.partner;

import com.google.common.collect.Lists;
import me.oczi.bukkit.internal.database.DbTasks;
import me.oczi.bukkit.objects.Home;
import me.oczi.bukkit.objects.PartnershipHome;
import me.oczi.bukkit.objects.Proposal;
import me.oczi.bukkit.objects.collections.HomeList;
import me.oczi.bukkit.objects.collections.PartnershipPermissionSet;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.partnership.PartnershipData;
import me.oczi.bukkit.objects.partnership.PartnershipImpl;
import me.oczi.bukkit.objects.partnership.PartnershipProperties;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.utils.MargaretPlayers;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.bukkit.utils.PartnershipPermission;
import me.oczi.bukkit.utils.Partnerships;
import me.oczi.common.api.Emptyble;
import me.oczi.common.storage.sql.dsl.result.SqlObject;
import me.oczi.common.utils.BitMasks;
import me.oczi.common.utils.CommonsUtils;
import me.oczi.common.utils.Statements;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static me.oczi.bukkit.utils.Partnerships.callPartnerStartEvent;

public class PartnerObjectBuilder {
  private final DbTasks dbTasks;

  private final int partnerMaxHomes = MargaretYamlStorage.getPartnerMaxHomes();

  public PartnerObjectBuilder(DbTasks dbTasks) {
    this.dbTasks = dbTasks;
  }

  public Partnership newPartner(MargaretPlayer margaretPlayer1,
                                MargaretPlayer margaretPlayer2) {
    if (callPartnerStartEvent(
        margaretPlayer1, margaretPlayer2)) {
      return null;
    }

    String id = dbTasks.foundUnusedPartnerId();

    Proposal proposal = margaretPlayer2.getCurrentProposal();
    PartnershipData partnershipData = new PartnershipData(id,
        margaretPlayer1.getUniqueId(),
        margaretPlayer2.getUniqueId(),
        proposal.getRelation());

    List<Player> pair = MargaretPlayers
        .getAsPlayer(
            margaretPlayer1, margaretPlayer2);
    PartnershipProperties partnershipProperties =
        createPartnerProperties(id,
            partnerMaxHomes,
            -1,
            pair);
    Partnership partnership = new PartnershipImpl(partnershipData, partnershipProperties);

    dbTasks.setupPartnershipData(partnership);
    dbTasks.setupPartnershipProperties(id, partnershipProperties);

    Partnerships.clearAndSetPartner(partnership,
        margaretPlayer1,
        margaretPlayer2);
    return partnership;
  }

  public PartnershipData initPartnerData(String id) {
    Map<String, SqlObject> metadata = dbTasks.getPartnershipData(id);
    if (CommonsUtils.isNullOrEmpty(metadata)) {
      MessageUtils.warning(
          "Partner ID "
              + id + " doesn't exist in database.");
      return null;
    }

    return createPartnerData(metadata);
  }

  public PartnershipProperties initPartnerProperties(String id,
                                                     Collection<Player> players) {
    Map<String, SqlObject> partnerProperties =
        dbTasks.getPartnershipProperties(id);
    if (CommonsUtils.isNullOrEmpty(partnerProperties)) {
      MessageUtils.warning(
          "Partner " + id + " properties doesn't exist. " +
              "Creating default properties...");
      PartnershipProperties properties =
          createPartnerProperties(id,
              partnerMaxHomes,
              -1,
              players);
      dbTasks.setupPartnershipProperties(id, properties);
      return properties;
    }

    int partnerMaxHomes = partnerProperties
        .get("maxhomes").getInteger();
    int bits = partnerProperties
        .get("bitpermissions").getInteger();
    return createPartnerProperties(id,
        partnerMaxHomes,
        bits,
        players);
  }

  public PartnershipData createPartnerData(Map<String, SqlObject> metadata) {
    SqlObject id = metadata.get("id");
    SqlObject player1 = metadata.get("player1");
    SqlObject player2 = metadata.get("player2");
    SqlObject relation = metadata.get("relation");
    Statements.checkObjects(
        "Partner data is null.",
        id, player1, player2, relation);

    UUID uuid1 = UUID.fromString(player1.getString());
    UUID uuid2 = UUID.fromString(player2.getString());
    return new PartnershipData(
        id.getString(),
        uuid1,
        uuid2,
        relation.getString());
  }

  /**
   * Create partner's properties.
   * <p>If bits is -1, will be refilled with the default permissions.</p>
   *
   * @param id       ID of partner.
   * @param maxHomes Maximum homes of partner.
   * @param bits     Partner's permission in bits.
   * @return Partner properties.
   */
  public PartnershipProperties createPartnerProperties(String id,
                                                       int maxHomes,
                                                       int bits,
                                                       Collection<Player> players) {
    if (bits == -1) {
      bits = refillPartnerPermission();
    }
    bits = Partnerships.transformPermissions(bits, players);
    maxHomes = Partnerships.getMaxHomesOf(maxHomes, players);

    PartnershipPermissionSet partnershipPermissionSet =
        new PartnershipPermissionSet(bits);
    HomeList homeList =
        createHomeList(id, maxHomes);
    return new PartnershipProperties(
        partnershipPermissionSet, homeList);
  }

  public int refillPartnerPermission() {
    int newBit = 0;
    List<String> defaultPermissions = MargaretYamlStorage
        .getDefaultPartnerPermissions();
    if (defaultPermissions.contains("*")) {
      return BitMasks.sumEnumClass(newBit, PartnershipPermission.class);
    }

    List<Integer> sumBits = new ArrayList<>();
    for (String defaultPerm : defaultPermissions) {
      if (CommonsUtils.enumExist(defaultPerm, PartnershipPermission.class)) {
        PartnershipPermission perm = PartnershipPermission
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
        dbTasks.getPartnershipHomeList(partnerId).values());

    // Remove all empty entries of query
    query.removeIf(Emptyble::isEmpty);
    for (SqlObject homeId : query) {
      PartnershipHome home = createHome(homeId.getString());
      if (home == null) {
        continue;
      }
      homeList.add(home);
    }
    return new HomeList(partnerId, homeList, partnerMaxHomes);
  }

  @Nullable
  public PartnershipHome createHome(String id) {
    Map<String, SqlObject> query = dbTasks.getHome(id);
    if (CommonsUtils.isNullOrEmpty(query)) {
      return null;
    }
    String alias = query.get("alias").getString();
    Date date = query.get("creation_date").getUtilDate();
    return new PartnershipHome(id, date, alias, newLocation(query));
  }

  public Location newLocation(Map<String, SqlObject> map) {
    String worldName = map.get("world").getString();
    Double x = map.get("x").getDouble();
    Double y = map.get("y").getDouble();
    Double z = map.get("z").getDouble();
    World world = Bukkit.getWorld(worldName);
    return new Location(world, x, y, z);
  }
}
