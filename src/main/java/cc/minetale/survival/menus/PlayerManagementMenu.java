package cc.minetale.survival.menus;

import cc.minetale.survival.ManagablePlayer;
import cc.minetale.survival.Survival;
import cc.minetale.survival.utils.MetadataUtils;
import cc.minetale.survival.utils.StringUtils;
import com.customwrld.customlib.CustomLib;
import com.customwrld.customlib.smartinvs.ClickableItem;
import com.customwrld.customlib.smartinvs.SmartInventory;
import com.customwrld.customlib.smartinvs.content.InventoryContents;
import com.customwrld.customlib.smartinvs.content.InventoryProvider;
import com.customwrld.customlib.util.ItemBuilder;
import com.customwrld.customlib.util.TimeUtil;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerManagementMenu implements InventoryProvider {

    @Getter private final Player managing;
    @Getter private final ManagablePlayer managed;
    @Getter private final SmartInventory inventory;

    public PlayerManagementMenu(Player managing, ManagablePlayer managed) {
        this.managing = managing;
        this.managed = managed;
        inventory = SmartInventory.builder()
                .id("playerManagementMenu")
                .provider(this)
                .size(6, 9)
                .title("My Profile").build();
        inventory.open(managing, 0);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillRect(0, 0, 1, 8, ClickableItem.empty(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));

        Player managedPlayer = managed.getPlayer();
        if(managedPlayer != null && managedPlayer.isOnline()) {
            MetadataUtils.getListOrDefaultAndSet(Survival.get(), managedPlayer, "managedBy", Arrays.asList(inventory), SmartInventory.class);
        }

        contents.set(0, 6, getLocationItem());
        contents.set(0, 7, getHealthItem());
        contents.set(0, 8, getEffectsItem());

        resetInventory(contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        updateLocation(contents);
        updateEffects(contents);
    }

    @Override
    public void close(Player player, InventoryContents contents) {
        Player managedPlayer = managed.getPlayer();
        if(managedPlayer != null && managedPlayer.isOnline()) {
            MetadataUtils.removeElementAndDeleteIfEmpty(Survival.get(), managedPlayer, "managedBy", inventory, SmartInventory.class);
        }
    }

    public void resetInventory(InventoryContents contents) {
        Inventory inv = managed.getInventory();

        //Hotbar
        for(int i = 0; i < 9; i++) {
            int index = (inventory.getColumns() * 5) + i;
            contents.set(index / 9, index % 9, ClickableItem.empty(inv.getItem(i)));
        }

        //Inventory
        for(int i = 9; i < 36; i++) {
            final int _i = i;

            int index = (inventory.getColumns() * 2) + (i - 9);

            ItemStack item = inv.getItem(i);
            if(item == null || item.getAmount() == 0 || item.getType() == Material.AIR) { item = new ItemStack(Material.AIR); }

            contents.set(index / 9, index % 9, ClickableItem.of(item.clone(), (event) -> {
                ItemStack cursor = event.getCursor();
                Inventory managedInventory = managed.getInventory();
                if(cursor != null && cursor.getAmount() != 0 && cursor.getType() != Material.AIR) {
                    ItemStack previousItem = managedInventory.getItem(_i);
                    managedInventory.setItem(_i, cursor);
                    if(previousItem != null && previousItem.getAmount() != 0 && previousItem.getType() != Material.AIR) {
                        event.setCurrentItem(cursor.clone());
                        event.setCursor(previousItem.clone());
                    } else {
                        event.setCurrentItem(cursor.clone());
                        event.setCursor(null);
                    }
                } else {
                    ItemStack previousItem = managedInventory.getItem(_i);
                    managedInventory.setItem(_i, null);
                    if(previousItem != null && previousItem.getAmount() != 0 && previousItem.getType() != Material.AIR) {
                        event.setCurrentItem(null);
                        event.setCursor(previousItem.clone());
                    }/* else {
                        event.setCurrentItem(null);
                        event.setCursor(null);
                    }*/
                }
                //event.setCancelled(false);
                //event.setResult(Event.Result.ALLOW);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        managed.getPlayer().updateInventory();
                        resetInventory(contents);
                    }
                }.runTaskLater(Survival.get(), 1);
            }));
        }

        //Special Items
        contents.set(0, 0, ClickableItem.empty(
                new ItemBuilder(Material.BARRIER)
                        .title(ChatColor.WHITE.toString() + ChatColor.BOLD + "Helmet")
                        .build()));
        contents.set(0, 1, ClickableItem.empty(
                new ItemBuilder(Material.BARRIER)
                        .title(ChatColor.WHITE.toString() + ChatColor.BOLD + "Chestplate")
                        .build()));
        contents.set(0, 2, ClickableItem.empty(
                new ItemBuilder(Material.BARRIER)
                        .title(ChatColor.WHITE.toString() + ChatColor.BOLD + "Leggings")
                        .build()));
        contents.set(0, 3, ClickableItem.empty(
                new ItemBuilder(Material.BARRIER)
                        .title(ChatColor.WHITE.toString() + ChatColor.BOLD + "Boots")
                        .build()));
        contents.set(0, 4, ClickableItem.empty(
                new ItemBuilder(Material.BARRIER)
                        .title(ChatColor.WHITE.toString() + ChatColor.BOLD + "Offhand")
                        .build()));

        ItemStack top = inv.getItem(36);
        ItemStack middle = inv.getItem(37);
        ItemStack lower = inv.getItem(38);
        ItemStack bottom = inv.getItem(39);

        //Which order is correct? You tell me.
        if(inv instanceof PlayerInventory) {
            if(top != null      && top.getType() != Material.AIR)       { contents.set(0, 3, ClickableItem.empty(top)); }
            if(middle != null   && middle.getType() != Material.AIR)    { contents.set(0, 2, ClickableItem.empty(middle)); }
            if(lower != null    && lower.getType() != Material.AIR)     { contents.set(0, 1, ClickableItem.empty(lower)); }
            if(bottom != null   && bottom.getType() != Material.AIR)    { contents.set(0, 0, ClickableItem.empty(bottom)); }
        } else {
            if(top != null      && top.getType() != Material.AIR)       { contents.set(0, 0, ClickableItem.empty(top)); }
            if(middle != null   && middle.getType() != Material.AIR)    { contents.set(0, 1, ClickableItem.empty(middle)); }
            if(lower != null    && lower.getType() != Material.AIR)     { contents.set(0, 2, ClickableItem.empty(lower)); }
            if(bottom != null   && bottom.getType() != Material.AIR)    { contents.set(0, 3, ClickableItem.empty(bottom)); }
        }

        ItemStack offhand = inv.getItem(40);
        if(offhand != null && offhand.getType() != Material.AIR) { contents.set(0, 4, ClickableItem.empty(offhand)); }
    }

    public void updateLocation(InventoryContents contents) {
        contents.set(0, 6, getLocationItem());
    }

    public ClickableItem getLocationItem() {
        Location location = managed.getLocation();

        ItemStack locationInfo = new ItemBuilder(Material.COMPASS)
                .title(ChatColor.WHITE.toString() + ChatColor.BOLD + "Location")
                .lores(Arrays.asList(
                        ChatColor.GOLD + "World: " + ChatColor.RED + location.getWorld().getName(),
                        "",
                        ChatColor.GOLD + "X: " + ChatColor.RED + location.getBlockX(),
                        ChatColor.GOLD + "Y: " + ChatColor.RED + location.getBlockY(),
                        ChatColor.GOLD + "Z: " + ChatColor.RED + location.getBlockZ(),
                        "",
                        ChatColor.WHITE.toString() + ChatColor.BOLD + "Click to teleport!"
                )).build();

        return ClickableItem.empty(locationInfo);
    }

    public void updateHealth() {
        InventoryContents contents = CustomLib.getInstance().getInvManager().getContents(managing).get();
        contents.set(0, 7, getHealthItem());
    }

    public ClickableItem getHealthItem() {
        managed.reset();
        double health = managed.getHealth();
        double foodLevel = managed.getFoodLevel();
        double saturation = managed.getFoodSaturation();

        ItemStack healthInfo = new ItemBuilder(Material.COOKED_BEEF)
                .title(ChatColor.WHITE.toString() + ChatColor.BOLD + "Health Status")
                .lores(Arrays.asList(
                        ChatColor.GOLD + "Health: " + ChatColor.RED + health,
                        "",
                        ChatColor.GOLD + "Hunger: " + ChatColor.RED + foodLevel,
                        ChatColor.GOLD + "Saturation: " + ChatColor.RED + saturation
                )).build();

        return ClickableItem.empty(healthInfo);
    }

    public void updateEffects(InventoryContents contents) {
        contents.set(0, 8, getEffectsItem());
    }

    public ClickableItem getEffectsItem() {
        List<String> lore = new ArrayList<>();

        for(PotionEffect effect : managed.getEffects()) {
            PotionEffectType type = effect.getType();
            lore.add(ChatColor.WHITE + "- " + ChatColor.of(new Color(type.getColor().asRGB())) + StringUtils.snakeToCamelCase(type.getName().toLowerCase()));
            lore.add("    " + ChatColor.GOLD + "Strength: " + ChatColor.RESET + ChatColor.RED + (effect.getAmplifier() + 1));
            lore.add("    " + ChatColor.GOLD + "Remaining: " + ChatColor.RESET + ChatColor.RED + TimeUtil.millisToTimer(effect.getDuration() * 50L));
        }

        ItemStack effectsInfo = new ItemBuilder(Material.POTION)
                .basePotionData(new PotionData(PotionType.WATER))
                .addFlag(ItemFlag.HIDE_POTION_EFFECTS)
                .title(ChatColor.WHITE.toString() + ChatColor.BOLD + "Active Effects")
                .lores(lore).build();

        return ClickableItem.empty(effectsInfo);
    }
}
