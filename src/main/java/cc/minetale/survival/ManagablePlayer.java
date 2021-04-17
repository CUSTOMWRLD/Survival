package cc.minetale.survival;

import cc.minetale.survival.utils.Inventories;
import cc.minetale.survival.utils.NBTUtils;
import lombok.Getter;
import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_16_R3.potion.CraftPotionEffectType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ManagablePlayer {
    //@Getter private Player player;
    @Getter private Inventory inventory;

    @Getter @Nullable private NBTTagCompound nbt;
    @Getter @Nullable private Player player;

    @Nullable private Double health;
    @Nullable private Integer foodLevel;
    @Nullable private Float foodSaturationLevel;

    @Nullable private Location location;
    @Nullable private List<PotionEffect> activeEffects;

    public ManagablePlayer(UUID uuid) {

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

        if(offlinePlayer.isOnline()) {

            this.player = offlinePlayer.getPlayer();
            this.inventory = player.getInventory();

        } else {

            if (offlinePlayer.hasPlayedBefore()) {

                this.nbt = NBTUtils.getPlayerData(offlinePlayer.getUniqueId());

                if (this.nbt != null) {
                    this.inventory = Inventories.getInventoryFromNBT(this.nbt);
                }

            }

        }

    }

    public Location getLocation() {
        Location location;
        if(player != null && player.isOnline()) {
            location = player.getLocation();
        } else {
            if(this.location != null) { return this.location; }

            String dimension = nbt.getString("Dimension").split(":")[1];

            double x = 0;
            double y = 0;
            double z = 0;

            //if(dimension.equals("overworld")) {
                NBTTagList nbtLocation = nbt.getList("Pos", 6);
                x = nbtLocation.getDoubleAt(0);
                y = nbtLocation.getDoubleAt(1);
                z = nbtLocation.getDoubleAt(2);
            /*} else if(dimension.equals("the_nether")) {
                NBTTagCompound nbtLocation = nbt.getCompound("enteredNetherPosition");
                x = nbtLocation.getDouble("x");
                y = nbtLocation.getDouble("y");
                z = nbtLocation.getDouble("z");
            }*/

            location = new Location(Bukkit.getWorld(NamespacedKey.minecraft(dimension)), x, y, z);

            this.location = location;
        }

        return location;
    }

    public double getHealth() {
        double health;
        if(player != null && player.isOnline()) {
            health = player.getHealth();
        } else {
            if(this.health != null) { return this.health; }

            health = nbt.getFloat("Health");

            this.health = health;
        }
        return health;
    }

    public int getFoodLevel() {
        int foodLevel;
        if(player != null && player.isOnline()) {
            foodLevel = player.getFoodLevel();
        } else {
            if(this.foodLevel != null) { return this.foodLevel; }

            foodLevel = nbt.getInt("foodLevel");

            this.foodLevel = foodLevel;
        }
        return foodLevel;
    }

    public float getFoodSaturation() {
        float foodSaturationLevel;
        if(player != null && player.isOnline()) {
            foodSaturationLevel = player.getSaturation();
        } else {
            if(this.foodSaturationLevel != null) { return this.foodSaturationLevel; }

            foodSaturationLevel = nbt.getFloat("foodSaturationLevel");

            this.foodSaturationLevel = foodSaturationLevel;
        }
        return foodSaturationLevel;
    }

    public Collection<PotionEffect> getEffects() {
        Collection<PotionEffect> activeEffects;
        if(player != null && player.isOnline()) {
            activeEffects = player.getActivePotionEffects();
        } else {
            if(this.activeEffects != null ) { return this.activeEffects; }

            activeEffects = new ArrayList<>();
            NBTTagList nbtEffects = nbt.getList("ActiveEffects", 10);
            for(NBTBase base : nbtEffects) {
                NBTTagCompound compound = (NBTTagCompound) base;
                activeEffects.add(new PotionEffect(
                        PotionEffectType.getById(compound.getByte("Id")),
                        compound.getInt("Duration"),
                        compound.getByte("Amplifier"),
                        compound.getBoolean("Ambient"),
                        compound.getBoolean("ShowParticles"),
                        compound.getBoolean("ShowIcon")));
            }
        }
        return activeEffects;
    }

    public void reset() {
        health = null;
        foodLevel = null;
        foodSaturationLevel = null;

        location = null;
        activeEffects = null;
    }
}
