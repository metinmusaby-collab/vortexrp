package com.rpserver.core.economy;

import com.rpserver.core.RPCore;
import com.rpserver.core.gui.RPHolder;
import com.rpserver.core.player.PlayerData;
import com.rpserver.core.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class BankGUI implements RPHolder {

    private final RPCore plugin;
    private final Inventory inventory;

    public BankGUI(RPCore plugin, Player player) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 27, ChatColor.DARK_GREEN + "Banka");
        fill(player);
    }

    private void fill(Player player) {
        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
        inventory.setItem(13, new ItemBuilder(Material.GOLD_INGOT)
                .name("&6Bakiyen: " + data.getBakiye() + " $")
                .lore("&7Para gondermek icin:",
                        "&f/banka gonder <oyuncu> <miktar>")
                .build());
        inventory.setItem(11, new ItemBuilder(Material.PAPER)
                .name("&eMeslegin")
                .lore("&7" + (data.getMeslek() == null ? "Mesleksiz" : data.getMeslek()))
                .build());
        inventory.setItem(15, new ItemBuilder(Material.MAP)
                .name("&eMulklerin")
                .lore("&7Ev: " + (data.getEvAdi() == null ? "-" : data.getEvAdi()),
                        "&7Is yeri: " + (data.getIsyeriAdi() == null ? "-" : data.getIsyeriAdi()),
                        "&7Restoran: " + (data.getRestoranAdi() == null ? "-" : data.getRestoranAdi()))
                .build());
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        // Bilgi ekrani, tiklamada islem yok.
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
