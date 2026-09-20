package com.rpserver.core.property;

import com.rpserver.core.RPCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PropertyManager {

    private final RPCore plugin;
    private final File file;
    private final Map<String, RPProperty> properties = new LinkedHashMap<>();

    public PropertyManager(RPCore plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "mulkler.yml");
    }

    public void load() {
        if (!file.exists()) return;
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (cfg.getConfigurationSection("mulkler") == null) return;
        for (String ad : cfg.getConfigurationSection("mulkler").getKeys(false)) {
            String path = "mulkler." + ad + ".";
            RPProperty.Tip tip = RPProperty.Tip.valueOf(cfg.getString(path + "tip"));
            World world = Bukkit.getWorld(cfg.getString(path + "dunya"));
            Location loc = new Location(world,
                    cfg.getDouble(path + "x"), cfg.getDouble(path + "y"), cfg.getDouble(path + "z"),
                    (float) cfg.getDouble(path + "yaw"), (float) cfg.getDouble(path + "pitch"));
            double fiyat = cfg.getDouble(path + "fiyat");
            double kira = cfg.getDouble(path + "kira");
            RPProperty prop = new RPProperty(ad, tip, loc, fiyat, kira);
            String sahipStr = cfg.getString(path + "sahip", null);
            if (sahipStr != null) prop.setSahip(UUID.fromString(sahipStr));
            prop.setKirada(cfg.getBoolean(path + "kirada", false));
            properties.put(ad.toLowerCase(), prop);
        }
    }

    public void saveAll() {
        YamlConfiguration cfg = new YamlConfiguration();
        for (RPProperty prop : properties.values()) {
            String path = "mulkler." + prop.getAd() + ".";
            cfg.set(path + "tip", prop.getTip().name());
            cfg.set(path + "dunya", prop.getKonum().getWorld().getName());
            cfg.set(path + "x", prop.getKonum().getX());
            cfg.set(path + "y", prop.getKonum().getY());
            cfg.set(path + "z", prop.getKonum().getZ());
            cfg.set(path + "yaw", (double) prop.getKonum().getYaw());
            cfg.set(path + "pitch", (double) prop.getKonum().getPitch());
            cfg.set(path + "fiyat", prop.getFiyat());
            cfg.set(path + "kira", prop.getKiraFiyati());
            cfg.set(path + "sahip", prop.getSahip() == null ? null : prop.getSahip().toString());
            cfg.set(path + "kirada", prop.isKirada());
        }
        try {
            cfg.save(file);
        } catch (Exception e) {
            plugin.getLogger().warning("Mulkler kaydedilemedi: " + e.getMessage());
        }
    }

    public RPProperty create(String ad, RPProperty.Tip tip, Location loc, double fiyat, double kira) {
        RPProperty prop = new RPProperty(ad, tip, loc, fiyat, kira);
        properties.put(ad.toLowerCase(), prop);
        return prop;
    }

    public boolean remove(String ad) {
        return properties.remove(ad.toLowerCase()) != null;
    }

    public RPProperty get(String ad) {
        return properties.get(ad.toLowerCase());
    }

    public List<RPProperty> listByType(RPProperty.Tip tip) {
        List<RPProperty> list = new ArrayList<>();
        for (RPProperty prop : properties.values()) {
            if (prop.getTip() == tip) list.add(prop);
        }
        return list;
    }

    public List<RPProperty> listOwnedBy(UUID uuid, RPProperty.Tip tip) {
        List<RPProperty> list = new ArrayList<>();
        for (RPProperty prop : properties.values()) {
            if (prop.getTip() == tip && uuid.equals(prop.getSahip())) list.add(prop);
        }
        return list;
    }

    public Map<String, RPProperty> all() {
        return properties;
    }
}
