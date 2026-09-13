package com.rpserver.core.phone;

import com.rpserver.core.RPCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class SocialManager {

    private static final int MAX_GONDERI = 50;

    private final RPCore plugin;
    private final File file;
    private final LinkedList<SocialPost> gonderiler = new LinkedList<>();

    public SocialManager(RPCore plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "sosyal.yml");
    }

    public void load() {
        if (!file.exists()) return;
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        List<?> list = cfg.getList("gonderiler");
        if (list == null) return;
        for (Object obj : list) {
            if (!(obj instanceof java.util.Map<?, ?> map)) continue;
            UUID yazar = UUID.fromString((String) map.get("yazar"));
            String yazarAdi = (String) map.get("yazarAdi");
            String mesaj = (String) map.get("mesaj");
            long zaman = ((Number) map.get("zaman")).longValue();
            gonderiler.add(new SocialPost(yazar, yazarAdi, mesaj, zaman));
        }
    }

    public void saveAll() {
        YamlConfiguration cfg = new YamlConfiguration();
        List<java.util.Map<String, Object>> list = new ArrayList<>();
        for (SocialPost post : gonderiler) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("yazar", post.getYazar().toString());
            map.put("yazarAdi", post.getYazarAdi());
            map.put("mesaj", post.getMesaj());
            map.put("zaman", post.getZaman());
            list.add(map);
        }
        cfg.set("gonderiler", list);
        try {
            cfg.save(file);
        } catch (Exception e) {
            plugin.getLogger().warning("Sosyal medya kaydedilemedi: " + e.getMessage());
        }
    }

    public void paylas(UUID yazar, String yazarAdi, String mesaj) {
        gonderiler.addFirst(new SocialPost(yazar, yazarAdi, mesaj, System.currentTimeMillis()));
        while (gonderiler.size() > MAX_GONDERI) {
            gonderiler.removeLast();
        }
    }

    public List<SocialPost> sonGonderiler(int adet) {
        List<SocialPost> list = new ArrayList<>(gonderiler.subList(0, Math.min(adet, gonderiler.size())));
        return Collections.unmodifiableList(list);
    }
}
