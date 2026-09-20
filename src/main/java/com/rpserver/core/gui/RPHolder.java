package com.rpserver.core.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * Tum RP GUI menuleri bu arayuzu uygular. GUIListener, tiklamalari
 * buraya yonlendirir; boylece her menu icin ayri listener yazmaya gerek kalmaz.
 */
public interface RPHolder extends InventoryHolder {

    void onClick(Player player, InventoryClickEvent event);

    default void onClose(Player player) {
    }
}
