package cc.minetale.survival;

import cc.minetale.survival.commands.inventories.OpenCommand;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public final class Survival extends JavaPlugin {

    private static Survival instance;

    public static Survival get() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        OpenCommand.command().register();
        Bukkit.getPluginManager().registerEvents(new PluginListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
