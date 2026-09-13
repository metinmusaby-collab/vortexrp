package com.rpserver.core.property;

import com.rpserver.core.RPCore;
import com.rpserver.core.gui.RPHolder;
import com.rpserver.core.player.PlayerData;
import com.rpserver.core.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PropertyGUI implements RPHolder {

    private final RPCore plugin;
    private final RPProperty.Tip tip;
    private final Inventory inventory;
    private final List<RPProperty> shown = new ArrayList<>();

    public PropertyGUI(RPCore plugin, RPProperty.Tip tip) {
        this.plugin = plugin;
        this.tip = tip;
        String baslik = switch (tip) {
            case EV -> "Emlak - Evler";
            case ISYERI -> "Emlak - Is Yerleri";
            case RESTORAN -> "Emlak - Restoranlar";
            default -> "Emlak";
        };
        this.inventory = plugin.getServer().createInventory(this, 54, ChatColor.DARK_GREEN + baslik);
        fill();
    }

    private void fill() {
        inventory.clear();
        shown.clear();
        int slot = 0;
        for (RPProperty prop : plugin.getPropertyManager().listByType(tip)) {
            if (slot >= 53) break;
            shown.add(prop);
            inventory.setItem(slot, buildItem(prop));
            slot++;
        }
        if (shown.isEmpty()) {
            inventory.setItem(22, new ItemBuilder(Material.BARRIER)
                    .name("&cHenuz kayitli mulk yok")
                    .lore("&7Bir yetkili /rp set ile ekleyebilir.")
                    .build());
        }
    }

    private ItemStack buildItem(RPProperty prop) {
        Material mat = switch (prop.getTip()) {
            case EV -> Material.OAK_DOOR;
            case ISYERI -> Material.CHEST;
            case RESTORAN -> Material.COOKED_BEEF;
            case MARKET -> Material.EMERALD;
        };
        ItemBuilder b = new ItemBuilder(mat).name("&e" + prop.getAd());
        if (prop.isSahipli()) {
            String sahipAdi = plugin.getServer().getOfflinePlayer(prop.getSahip()).getName();
            b.lore("&7Durum: " + (prop.isKirada() ? "&6Kirada" : "&cSatilmis"),
                    "&7Sahip: &f" + sahipAdi,
                    "",
                    "&8Tikla: Sahipsen isin yerine isinlan");
        } else {
            b.lore("&7Durum: &aSatilik / Kiralik",
                    "&7Satin alma: &f" + prop.getFiyat() + " $",
                    "&7Kira: &f" + prop.getKiraFiyati() + " $",
                    "",
                    "&aSol tik: Satin al",
                    "&6Sag tik: Kirala",
                    "&8Orta tik: Konuma isinlan");
        }
        return b.build();
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        int slot = event.getSlot();
        if (slot < 0 || slot >= shown.size()) return;
        RPProperty prop = shown.get(slot);
        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());

        if (prop.isSahipli()) {
            if (prop.getSahip().equals(player.getUniqueId())) {
                player.closeInventory();
                player.teleport(prop.getKonum());
                player.sendMessage(ChatColor.GREEN + prop.getAd() + " konumuna isinlandin.");
            } else {
                player.sendMessage(ChatColor.RED + "Bu mulk baskasina ait.");
            }
            return;
        }

        if (event.getClick() == ClickType.LEFT) {
            if (!data.harca(prop.getFiyat())) {
                player.sendMessage(ChatColor.RED + "Yeterli paran yok! Gereken: " + prop.getFiyat() + " $");
                return;
            }
            prop.setSahip(player.getUniqueId());
            prop.setKirada(false);
            assignToPlayer(data, prop);
            player.sendMessage(ChatColor.GREEN + prop.getAd() + " satin alindi!");
            fill();
            player.openInventory(inventory);
        } else if (event.getClick() == ClickType.RIGHT) {
            if (!data.harca(prop.getKiraFiyati())) {
                player.sendMessage(ChatColor.RED + "Yeterli paran yok! Gereken: " + prop.getKiraFiyati() + " $");
                return;
            }
            prop.setSahip(player.getUniqueId());
            prop.setKirada(true);
            assignToPlayer(data, prop);
            player.sendMessage(ChatColor.GREEN + prop.getAd() + " kiralandi!");
            fill();
            player.openInventory(inventory);
        } else if (event.getClick() == ClickType.MIDDLE) {
            player.closeInventory();
            player.teleport(prop.getKonum());
        }
    }

    private void assignToPlayer(PlayerData data, RPProperty prop) {
        switch (prop.getTip()) {
            case EV -> data.setEvAdi(prop.getAd());
            case ISYERI -> data.setIsyeriAdi(prop.getAd());
            case RESTORAN -> data.setRestoranAdi(prop.getAd());
            default -> {}
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
