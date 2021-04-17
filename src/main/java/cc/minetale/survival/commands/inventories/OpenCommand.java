package cc.minetale.survival.commands.inventories;

import cc.minetale.survival.ManagablePlayer;
import cc.minetale.survival.menus.PlayerManagementMenu;
import cc.minetale.survival.utils.Arguments;
import cc.minetale.survival.utils.Inventories;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;

import static cc.minetale.survival.utils.Arguments.offlinePlayerArgument;

public class OpenCommand {

    public static CommandAPICommand command() {
        return new CommandAPICommand("invsee")
                .withPermission("blaze.invsee")
                .withOptionalArgument(offlinePlayerArgument("player"), () -> null)
                .executesPlayer((sender, args) -> {
                    if (args.length == 0 || args[0] == null) {
                        sender.sendMessage(ChatColor.RED + "You must provide a player.");
                        return;
                    }

                    OfflinePlayer offlinePlayer = (OfflinePlayer) args[0];

                    /*if(sender.getUniqueId().equals(profile.getUuid())) {
                        sender.sendMessage(ChatColor.RED + "You cannot view your own inventory.");
                        return;
                    }*/

                    //Inventory inventory = Inventories.getInventoryFromNBT(profile.getUuid());

                    ManagablePlayer managable = new ManagablePlayer(offlinePlayer.getUniqueId());

                    if(managable.getInventory() == null) {
                        sender.sendMessage(ChatColor.RED + "Cannot retrieve this player's inventory");
                        return;
                    }

                    new PlayerManagementMenu(sender, managable);
                });
    }

}
