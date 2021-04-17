package cc.minetale.survival.utils;

import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Inventories {

    public static Inventory getInventoryFromNBT(NBTTagCompound nbt) {
        NBTTagList inventory = nbt.getList("Inventory", NBTTagType.COMPOUND.getId());

        Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER);

        for (NBTBase nbtBase : inventory) {

            NBTTagCompound compound = (NBTTagCompound) nbtBase;

            if (!compound.isEmpty()) {

                ItemStack stack = CraftItemStack.asBukkitCopy(net.minecraft.server.v1_16_R3.ItemStack.fromCompound(compound));

                int slot = compound.getByte("Slot");

                //Transform slots from NBT to "Bukkit" ones
                switch (slot) {
                    case 103: slot = 36; break; //Helmet
                    case 102: slot = 37; break; //Chestplate
                    case 101: slot = 38; break; //Leggings
                    case 100: slot = 39; break; //Boots

                    case -106: slot = 40; break; //Offhand
                }

                inv.setItem(slot, stack);

            }

        }

        return inv;

    }

}