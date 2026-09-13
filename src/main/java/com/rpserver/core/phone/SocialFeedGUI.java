package com.rpserver.core.phone;

import com.rpserver.core.RPCore;
import com.rpserver.core.gui.RPHolder;
import com.rpserver.core.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SocialFeedGUI implements RPHolder {

    private final Inventory inventory;

    public SocialFeedGUI(RPCore plugin) {
        this.inventory = plugin.getServer().createInventory(this, 54, ChatColor.LIGHT_PURPLE + "Sosyal Medya");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");
        int slot = 0;
        for (SocialPost post : plugin.getSocialManager().sonGonderiler(45)) {
            if (slot >= 54) break;
            inventory.setItem(slot, new ItemBuilder(Material.PAPER)
                    .name("&d" + post.getYazarAdi())
                    .lore("&f" + wrap(post.getMesaj()), "", "&7" + sdf.format(new Date(post.getZaman())))
                    .build());
            slot++;
        }
        if (slot == 0) {
            inventory.setItem(22, new ItemBuilder(Material.BARRIER)
                    .name("&cHenuz paylasim yok")
                    .lore("&7/sosyal <mesaj> ile ilk paylasimi yap!")
                    .build());
        }
    }

    private String wrap(String s) {
        return s.length() > 40 ? s.substring(0, 40) + "..." : s;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        // Sadece goruntuleme amacli.
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
