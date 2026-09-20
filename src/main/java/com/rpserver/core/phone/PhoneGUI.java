package com.rpserver.core.phone;

import com.rpserver.core.RPCore;
import com.rpserver.core.car.CarGUI;
import com.rpserver.core.economy.BankGUI;
import com.rpserver.core.gui.RPHolder;
import com.rpserver.core.job.JobGUI;
import com.rpserver.core.property.MarketGUI;
import com.rpserver.core.property.PropertyGUI;
import com.rpserver.core.property.RPProperty;
import com.rpserver.core.restaurant.RestaurantMenuGUI;
import com.rpserver.core.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class PhoneGUI implements RPHolder {

    private final RPCore plugin;
    private final Inventory inventory;

    public PhoneGUI(RPCore plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 27, ChatColor.AQUA + "Telefon");
        fill();
    }

    private void fill() {
        inventory.setItem(10, uygulama(Material.GOLD_INGOT, "&6Banka", "&7Bakiye ve para transferi"));
        inventory.setItem(11, uygulama(Material.IRON_PICKAXE, "&7Is", "&7Meslek sec / degistir"));
        inventory.setItem(12, uygulama(Material.OAK_DOOR, "&aEmlak - Ev", "&7Ev satin al / kirala"));
        inventory.setItem(13, uygulama(Material.CHEST, "&eEmlak - Is Yeri", "&7Is yeri satin al / kirala"));
        inventory.setItem(14, uygulama(Material.EMERALD, "&bMarket", "&7Esya satin al"));
        inventory.setItem(15, uygulama(Material.COOKED_BEEF, "&cRestoran", "&7Yemek ye / restoran satin al"));
        inventory.setItem(16, uygulama(Material.SADDLE, "&8Araba", "&7Araba galerisi / garaj"));
        inventory.setItem(20, uygulama(Material.NETHER_STAR, "&2Futbol Ligi", "&7Takim ve puan durumu"));
        inventory.setItem(22, uygulama(Material.WRITABLE_BOOK, "&dSosyal Medya", "&7Son paylasimlari gor"));
        inventory.setItem(24, uygulama(Material.BOOK, "&eKomutlar", "&7/sosyal, /su, /polis, /takim..."));
    }

    private org.bukkit.inventory.ItemStack uygulama(Material mat, String ad, String aciklama) {
        return new ItemBuilder(mat).name(ad).lore(aciklama).build();
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        switch (event.getSlot()) {
            case 10 -> player.openInventory(new BankGUI(plugin, player).getInventory());
            case 11 -> player.openInventory(new JobGUI(plugin).getInventory());
            case 12 -> player.openInventory(new PropertyGUI(plugin, RPProperty.Tip.EV).getInventory());
            case 13 -> player.openInventory(new PropertyGUI(plugin, RPProperty.Tip.ISYERI).getInventory());
            case 14 -> player.openInventory(new MarketGUI(plugin).getInventory());
            case 15 -> player.openInventory(new RestaurantMenuGUI(plugin).getInventory());
            case 16 -> player.openInventory(new CarGUI(plugin, player).getInventory());
            case 20 -> {
                player.closeInventory();
                player.performCommand("lig");
            }
            case 22 -> player.openInventory(new SocialFeedGUI(plugin).getInventory());
            default -> {}
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
