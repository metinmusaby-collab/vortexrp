package com.rpserver.core.job;

import com.rpserver.core.RPCore;
import com.rpserver.core.gui.RPHolder;
import com.rpserver.core.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class JobGUI implements RPHolder {

    private final RPCore plugin;
    private final Inventory inventory;

    public JobGUI(RPCore plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 27, ChatColor.GOLD + "Meslek Sec");
        fill();
    }

    private void fill() {
        inventory.setItem(10, new ItemBuilder(Material.WHEAT)
                .name("&aCiftci")
                .lore("&7Olgun ekin hasat ederek para kazan.").build());
        inventory.setItem(12, new ItemBuilder(Material.IRON_PICKAXE)
                .name("&7Madenci")
                .lore("&7Cevher kirarak para kazan.").build());
        inventory.setItem(14, new ItemBuilder(Material.FISHING_ROD)
                .name("&bBalikci")
                .lore("&7Balik tutarak para kazan.").build());
        inventory.setItem(16, new ItemBuilder(Material.IRON_SWORD)
                .name("&9Polis")
                .lore("&7Nobet tutarak duzenli maas al.", "&7/polis komutuyla nobete basla.").build());
        inventory.setItem(22, new ItemBuilder(Material.BARRIER)
                .name("&cMeslegi Birak")
                .build());
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        JobType secilen = switch (event.getSlot()) {
            case 10 -> JobType.CIFTCI;
            case 12 -> JobType.MADENCI;
            case 14 -> JobType.BALIKCI;
            case 16 -> JobType.POLIS;
            default -> null;
        };
        if (event.getSlot() == 22) {
            plugin.getJobManager().setJob(player, null);
            player.sendMessage(ChatColor.YELLOW + "Meslegini biraktin.");
            player.closeInventory();
            return;
        }
        if (secilen == null) return;
        plugin.getJobManager().setJob(player, secilen);
        player.sendMessage(ChatColor.GREEN + "Yeni meslegin: " + secilen.getGosterimAdi());
        player.closeInventory();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
