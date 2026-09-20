package com.rpserver.core.player;

import com.rpserver.core.RPCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager implements Listener {

    private final RPCore plugin;
    private final File file;
    private final Map<UUID, PlayerData> dataMap = new HashMap<>();

    public PlayerDataManager(RPCore plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "oyuncular.yml");
    }

    public void load() {
        if (!file.exists()) return;
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (cfg.getConfigurationSection("oyuncular") == null) return;
        for (String key : cfg.getConfigurationSection("oyuncular").getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            PlayerData data = new PlayerData(uuid);
            String path = "oyuncular." + key + ".";
            data.setBakiye(cfg.getDouble(path + "bakiye", plugin.getConfig().getDouble("baslangic-bakiye", 500)));
            data.setMeslek(cfg.getString(path + "meslek", null));
            data.setPolisNobette(cfg.getBoolean(path + "polisNobette", false));
            data.setSusama(cfg.getDouble(path + "susama", 100.0));
            data.setEvAdi(cfg.getString(path + "ev", null));
            data.setIsyeriAdi(cfg.getString(path + "isyeri", null));
            data.setRestoranAdi(cfg.getString(path + "restoran", null));
            data.setArabaId(cfg.getString(path + "araba", null));
            data.setTakimAdi(cfg.getString(path + "takim", null));
            dataMap.put(uuid, data);
        }
    }

    public void saveAll() {
        YamlConfiguration cfg = new YamlConfiguration();
        for (PlayerData data : dataMap.values()) {
            String path = "oyuncular." + data.getUuid() + ".";
            cfg.set(path + "bakiye", data.getBakiye());
            cfg.set(path + "meslek", data.getMeslek());
            cfg.set(path + "polisNobette", data.isPolisNobette());
            cfg.set(path + "susama", data.getSusama());
            cfg.set(path + "ev", data.getEvAdi());
            cfg.set(path + "isyeri", data.getIsyeriAdi());
            cfg.set(path + "restoran", data.getRestoranAdi());
            cfg.set(path + "araba", data.getArabaId());
            cfg.set(path + "takim", data.getTakimAdi());
        }
        try {
            cfg.save(file);
        } catch (Exception e) {
            plugin.getLogger().warning("Oyuncu verileri kaydedilemedi: " + e.getMessage());
        }
    }

    public PlayerData get(UUID uuid) {
        return dataMap.computeIfAbsent(uuid, id -> {
            PlayerData data = new PlayerData(id);
            data.setBakiye(plugin.getConfig().getDouble("baslangic-bakiye", 500));
            return data;
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        get(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        saveAll();
    }
}
