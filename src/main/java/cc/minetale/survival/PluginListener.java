package cc.minetale.survival;

import cc.minetale.survival.menus.PlayerManagementMenu;
import cc.minetale.survival.utils.MetadataUtils;
import com.customwrld.customlib.CustomLib;
import com.customwrld.customlib.smartinvs.SmartInventory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class PluginListener implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if(entity instanceof Player) {
            updateStatusForManagingPlayers((Player) entity);
        }
    }

    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent event) {
        Entity entity = event.getEntity();
        if(entity instanceof Player) {
            updateStatusForManagingPlayers((Player) entity);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        Entity entity = event.getEntity();
        if(entity instanceof Player) {
            updateStatusForManagingPlayers((Player) entity);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        updateStatusForManagingPlayers(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        updateInventoryForManagingPlayers((Player) event.getWhoClicked());
    }

    public static void updateInventoryForManagingPlayers(Player managed) {
        List<SmartInventory> managedBy = MetadataUtils.getListOrEmpty(managed, "managedBy", SmartInventory.class);
        if(!managedBy.isEmpty()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (SmartInventory inventory : managedBy) {
                        PlayerManagementMenu managementMenu = ((PlayerManagementMenu) inventory.getProvider());
                        managementMenu.resetInventory(CustomLib.getInstance().getInvManager().getContents(managementMenu.getManaging()).get());
                    }
                }
            }.runTaskLater(Survival.get(), 0);
        }
    }

    public static void updateStatusForManagingPlayers(Player managed) {
        if(managed.isOnline()) {
            List<SmartInventory> managedBy = MetadataUtils.getListOrEmpty(managed, "managedBy", SmartInventory.class);
            for(SmartInventory inventory : managedBy) {
                ((PlayerManagementMenu) inventory.getProvider()).updateHealth();
            }
        }
    }
}
