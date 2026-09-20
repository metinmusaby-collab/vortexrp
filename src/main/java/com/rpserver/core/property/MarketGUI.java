package com.rpserver.core.property;

import com.rpserver.core.RPCore;
import com.rpserver.core.gui.RPHolder;
import com.rpserver.core.player.PlayerData;
import com.rpserver.core.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MarketGUI implements RPHolder {

    private final RPCore plugin;
    private final Inventory inventory;

    private record Urun(int slot, Material material, double fiyat) {}

    private final Urun[] urunler = new Urun[]{
            new Urun(10, Material.BREAD, 6),
            new Urun(11, Material.APPLE, 3),
            new Urun(12, Material.COOKED_BEEF, 14),
            new Urun(13, Material.WATER_BUCKET, 10),
            new Urun(14, Material.MILK_BUCKET, 12),
            new Urun(15, Material.TORCH, 2),
            new Urun(16, Material.IRON_PICKAXE, 120),
            new Urun(19, Material.FISHING_ROD, 90),
            new Urun(20, Material.WOODEN_HOE, 40),
            new Urun(21, Material.LEATHER_CHESTPLATE, 60),
    };

    public MarketGUI(RPCore plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 27, ChatColor.DARK_AQUA + "Market");
        fill();
    }

    private void fill() {
        for (Urun u : urunler) {
            inventory.setItem(u.slot(), new ItemBuilder(u.material())
                    .name("&e" + prettyName(u.material()))
                    .lore("&7Fiyat: &f" + u.fiyat() + " $", "", "&aSatin almak icin tikla")
                    .build());
        }
    }

    private String prettyName(Material m) {
        String s = m.name().replace('_', ' ').toLowerCase();
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        for (Urun u : urunler) {
            if (event.getSlot() != u.slot()) continue;
            PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
            if (!data.harca(u.fiyat())) {
                player.sendMessage(ChatColor.RED + "Yeterli paran yok!");
                return;
            }
            ItemStack item = new ItemStack(u.material());
            player.getInventory().addItem(item);
            player.sendMessage(ChatColor.GREEN + prettyName(u.material()) + " satin alindi.");
            return;
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
