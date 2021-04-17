package cc.minetale.survival.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_16_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class NBTUtils {

    public static NBTTagCompound getPlayerData(UUID uuid) {

        File inventoryFile = new File(getServer().getWorldContainer() + File.separator + "world" + File.separator + "playerdata", uuid.toString() + ".dat");

        try {
            return NBTCompressedStreamTools.readNBT(new FileInputStream(inventoryFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

}
