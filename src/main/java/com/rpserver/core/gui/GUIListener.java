package com.rpserver.core.gui;

import com.rpserver.core.RPCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

public class GUIListener implements Listener {

    public GUIListener(RPCore plugin) {
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof RPHolder rpHolder)) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;

        // Alt envanterden (oyuncu envanteri) menuye esya tasimayi engelle
        event.setCancelled(true);

        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getHolder() != holder) return;

        rpHolder.onClick(player, event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof RPHolder rpHolder)) return;
        if (!(event.getPlayer() instanceof Player player)) return;
        rpHolder.onClose(player);
    }
}
