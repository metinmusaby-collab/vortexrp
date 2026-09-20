package com.rpserver.core.car;

import com.rpserver.core.RPCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CarManager {

    private final RPCore plugin;
    private final File file;
    private final Map<String, Car> cars = new HashMap<>();

    public CarManager(RPCore plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "arabalar.yml");
    }

    public void load() {
        if (!file.exists()) return;
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (cfg.getConfigurationSection("arabalar") == null) return;
        for (String id : cfg.getConfigurationSection("arabalar").getKeys(false)) {
            String path = "arabalar." + id + ".";
            UUID sahip = UUID.fromString(cfg.getString(path + "sahip"));
            Car.Tip tip = Car.Tip.valueOf(cfg.getString(path + "tip"));
            cars.put(id, new Car(id, sahip, tip));
        }
    }

    public void saveAll() {
        YamlConfiguration cfg = new YamlConfiguration();
        for (Car car : cars.values()) {
            String path = "arabalar." + car.getId() + ".";
            cfg.set(path + "sahip", car.getSahip().toString());
            cfg.set(path + "tip", car.getTip().name());
        }
        try {
            cfg.save(file);
        } catch (Exception e) {
            plugin.getLogger().warning("Arabalar kaydedilemedi: " + e.getMessage());
        }
    }

    public double priceOf(Car.Tip tip) {
        return switch (tip) {
            case EKONOMIK -> plugin.getConfig().getDouble("araba.ekonomik-fiyat", 800);
            case SPOR -> plugin.getConfig().getDouble("araba.spor-fiyat", 2500);
            case LUKS -> plugin.getConfig().getDouble("araba.luks-fiyat", 5000);
        };
    }

    public Car satinAl(UUID sahip, Car.Tip tip) {
        String id = sahip.toString().substring(0, 8) + "-" + System.currentTimeMillis();
        Car car = new Car(id, sahip, tip);
        cars.put(id, car);
        return car;
    }

    public List<Car> arabalariniGetir(UUID sahip) {
        List<Car> list = new ArrayList<>();
        for (Car car : cars.values()) {
            if (car.getSahip().equals(sahip)) list.add(car);
        }
        return list;
    }

    /**
     * Araci belirtilen konuma at (Horse) olarak cagirir.
     */
    public void cagir(Car car, Location location) {
        despawn(car);
        Horse horse = location.getWorld().spawn(location, Horse.class);
        horse.setTamed(true);
        horse.setAdult();
        horse.setCustomNameVisible(true);
        String etiket = switch (car.getTip()) {
            case EKONOMIK -> "§7Ekonomik Araba";
            case SPOR -> "§cSpor Araba";
            case LUKS -> "§6Luks Araba";
        };
        horse.customName(net.kyori.adventure.text.Component.text(etiket));
        double hiz = switch (car.getTip()) {
            case EKONOMIK -> 0.20;
            case SPOR -> 0.34;
            case LUKS -> 0.28;
        };
        var attr = horse.getAttribute(Attribute.MOVEMENT_SPEED);
        if (attr != null) attr.setBaseValue(hiz);
        horse.setJumpStrength(0.7);
        car.setSpawnliVarlik(horse.getUniqueId());
    }

    public void despawn(Car car) {
        if (car.getSpawnliVarlik() == null) return;
        Entity e = Bukkit.getEntity(car.getSpawnliVarlik());
        if (e != null) e.remove();
        car.setSpawnliVarlik(null);
    }

    public Car get(String id) {
        return cars.get(id);
    }
}
