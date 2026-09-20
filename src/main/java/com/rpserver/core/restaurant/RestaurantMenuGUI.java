package com.rpserver.core.restaurant;

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

public class RestaurantMenuGUI implements RPHolder {

    private final RPCore plugin;
    private final Inventory inventory;

    private record Menu(int slot, Material material, String ad, double fiyat, int doyum, double susamaArtisi) {}

    private final Menu[] menuler = new Menu[]{
            new Menu(10, Material.POTION, "Su", 5.0, 0, 40.0),
            new Menu(12, Material.APPLE, "Elma", 3.0, 2, 0),
            new Menu(14, Material.BREAD, "Ekmek", 8.0, 4, 0),
            new Menu(16, Material.COOKED_BEEF, "Biftek", 15.0, 6, 0),
    };

    public RestaurantMenuGUI(RPCore plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 27, ChatColor.DARK_RED + "Restoran Menusu");
        fill();
    }

    private void fill() {
        for (Menu m : menuler) {
            inventory.setItem(m.slot(), new ItemBuilder(m.material())
                    .name("&e" + m.ad())
                    .lore("&7Fiyat: &f" + m.fiyat() + " $",
                            m.doyum() > 0 ? "&7Doyum: &f+" + m.doyum() : null,
                            m.susamaArtisi() > 0 ? "&7Susama: &f+" + (int) m.susamaArtisi() : null,
                            "",
                            "&aSatin almak icin tikla")
                    .build());
        }
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        for (Menu m : menuler) {
            if (event.getSlot() != m.slot()) continue;
            PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
            if (!data.harca(m.fiyat())) {
                player.sendMessage(ChatColor.RED + "Yeterli paran yok!");
                return;
            }
            if (m.doyum() > 0) {
                player.setFoodLevel(Math.min(20, player.getFoodLevel() + m.doyum()));
                player.setSaturation(Math.min(20f, player.getSaturation() + m.doyum()));
            }
            if (m.susamaArtisi() > 0) {
                data.setSusama(data.getSusama() + m.susamaArtisi());
            }
            player.sendMessage(ChatColor.GREEN + m.ad() + " satin alindi. Afiyet olsun!");
            return;
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
