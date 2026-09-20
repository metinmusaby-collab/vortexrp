package com.rpserver.core.car;

import com.rpserver.core.RPCore;
import com.rpserver.core.gui.RPHolder;
import com.rpserver.core.player.PlayerData;
import com.rpserver.core.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class CarGUI implements RPHolder {

    private final RPCore plugin;
    private final Inventory inventory;
    private final List<Car> ownedShown = new ArrayList<>();

    public CarGUI(RPCore plugin, Player player) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 36, ChatColor.DARK_GRAY + "Araba Galerisi");
        fill(player);
    }

    private void fill(Player player) {
        CarManager cm = plugin.getCarManager();
        inventory.setItem(10, satinAlEsyasi(Car.Tip.EKONOMIK, Material.IRON_HORSE_ARMOR));
        inventory.setItem(12, satinAlEsyasi(Car.Tip.SPOR, Material.GOLDEN_HORSE_ARMOR));
        inventory.setItem(14, satinAlEsyasi(Car.Tip.LUKS, Material.DIAMOND_HORSE_ARMOR));

        List<Car> owned = cm.arabalariniGetir(player.getUniqueId());
        ownedShown.clear();
        int slot = 19;
        for (Car car : owned) {
            if (slot >= 36) break;
            ownedShown.add(car);
            inventory.setItem(slot, new ItemBuilder(Material.SADDLE)
                    .name("&e" + car.getTip().name())
                    .lore("&7ID: " + car.getId(),
                            car.getSpawnliVarlik() != null ? "&aSu an disarida" : "&7Garajda",
                            "",
                            "&aTikla: cagir / geri gonder")
                    .build());
            slot++;
        }
    }

    private org.bukkit.inventory.ItemStack satinAlEsyasi(Car.Tip tip, Material material) {
        double fiyat = plugin.getCarManager().priceOf(tip);
        return new ItemBuilder(material)
                .name("&b" + tip.name() + " Araba Satin Al")
                .lore("&7Fiyat: &f" + fiyat + " $", "", "&aSatin almak icin tikla")
                .build();
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        int slot = event.getSlot();
        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
        CarManager cm = plugin.getCarManager();

        Car.Tip satinAlinacak = switch (slot) {
            case 10 -> Car.Tip.EKONOMIK;
            case 12 -> Car.Tip.SPOR;
            case 14 -> Car.Tip.LUKS;
            default -> null;
        };
        if (satinAlinacak != null) {
            double fiyat = cm.priceOf(satinAlinacak);
            if (!data.harca(fiyat)) {
                player.sendMessage(ChatColor.RED + "Yeterli paran yok!");
                return;
            }
            Car car = cm.satinAl(player.getUniqueId(), satinAlinacak);
            player.sendMessage(ChatColor.GREEN + satinAlinacak.name() + " araba satin alindi. Garajinda bekliyor.");
            player.closeInventory();
            return;
        }

        int index = slot - 19;
        if (index >= 0 && index < ownedShown.size()) {
            Car car = ownedShown.get(index);
            if (car.getSpawnliVarlik() != null) {
                cm.despawn(car);
                player.sendMessage(ChatColor.YELLOW + "Araci garaja gonderdin.");
            } else {
                cm.cagir(car, player.getLocation());
                player.sendMessage(ChatColor.GREEN + "Arac yanina cagirildi.");
            }
            player.closeInventory();
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
