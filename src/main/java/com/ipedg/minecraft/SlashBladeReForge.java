package com.ipedg.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SlashBladeReForge extends JavaPlugin implements Listener {
    private Boolean IsCat;
    @Override
    public void onEnable() {
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            getConfig().options().copyDefaults(true);
        }
        saveDefaultConfig();
        String version = Bukkit.getServer().getVersion();
        if (version.contains("CatServer")){
            System.out.println("【淘宝定制】1.12.2CatServerHook");
            IsCat = true;
        }else {
            System.out.println("【淘宝定制】1.7.10Hook");
            IsCat = false;
        }
        System.out.println("【调试数据】"+version);
        Bukkit.getServer().getPluginManager().registerEvents(this,this);
        super.onEnable();
    }

    @EventHandler
    public void onInventClick(InventoryClickEvent e)
    {
        Player player = (Player)e.getWhoClicked();
        if (e.getInventory().getType().equals(InventoryType.ANVIL)){
            ItemStack currentItem = e.getCurrentItem();
            int ItemLevel = 0;
            int level = compareLimitLevel(player);
            if (IsCat){
                ItemLevel = ItemGetNbtTagKey_112(currentItem);
                System.out.println("1.12.2CatServerHook");
            }else {
                ItemLevel = ItemGetNbtTagKey_17(currentItem);
                System.out.println("1.7.10Hook");
            }
            System.out.println(ItemLevel+"调试数据");
            if (ItemLevel>=level){
                player.sendMessage(getConfig().getString("Msg"));
                e.setCancelled(true);
            }
        }
    }
    public static int ItemGetNbtTagKey_112(ItemStack item)
    {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asNMSCopy(item);
        if (nmsItem.hasTag())
        {
            net.minecraft.server.v1_12_R1.NBTTagCompound compound = nmsItem.getTag();
            assert compound != null;
            if (compound.hasKey("RepairCounter")) {
                return compound.getInt("RepairCounter");
            }
        }
        return 0;
    }

    public static int ItemGetNbtTagKey_17(ItemStack item)
    {
        net.minecraft.server.v1_7_R4.ItemStack nmsItem = org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack.asNMSCopy(item);
        if (nmsItem.hasTag())
        {
            net.minecraft.server.v1_7_R4.NBTTagCompound compound = nmsItem.getTag();
            if (compound.hasKey("RepairCounter")) {
                return compound.getInt("RepairCounter");
            }
        }
        return 0;
    }

    private int compareLimitLevel(Player player)
    {
        int bMaxLevel = getConfig().getInt("MaxRepairCounter");
        int defaultRepair = getConfig().getInt("defaultMaxRepairCounter");
        for (int i = 1; i < bMaxLevel; i++) {
            if (player.hasPermission("slashblade." + i))
            {
                return i;
            }
        }
        return defaultRepair;
    }

}
