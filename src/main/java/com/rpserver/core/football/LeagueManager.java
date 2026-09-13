package com.rpserver.core.football;

import com.rpserver.core.RPCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class LeagueManager {

    private final RPCore plugin;
    private final File file;
    private final Map<String, Team> teams = new LinkedHashMap<>();
    private final Random random = new Random();

    public LeagueManager(RPCore plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "ligler.yml");
    }

    public void load() {
        if (!file.exists()) return;
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        if (cfg.getConfigurationSection("takimlar") == null) return;
        for (String ad : cfg.getConfigurationSection("takimlar").getKeys(false)) {
            String path = "takimlar." + ad + ".";
            UUID kaptan = UUID.fromString(cfg.getString(path + "kaptan"));
            Team team = new Team(ad, kaptan);
            team.getUyeler().clear();
            for (String uyeStr : cfg.getStringList(path + "uyeler")) {
                team.getUyeler().add(UUID.fromString(uyeStr));
            }
            team.setKasa(cfg.getDouble(path + "kasa", 0));
            team.setStats(cfg.getInt(path + "oynanan", 0), cfg.getInt(path + "galibiyet", 0),
                    cfg.getInt(path + "beraberlik", 0), cfg.getInt(path + "maglubiyet", 0),
                    cfg.getInt(path + "atilanGol", 0), cfg.getInt(path + "yenenGol", 0));
            teams.put(ad.toLowerCase(), team);
        }
    }

    public void saveAll() {
        YamlConfiguration cfg = new YamlConfiguration();
        for (Team team : teams.values()) {
            String path = "takimlar." + team.getAd() + ".";
            cfg.set(path + "kaptan", team.getKaptan().toString());
            List<String> uyeler = new ArrayList<>();
            team.getUyeler().forEach(u -> uyeler.add(u.toString()));
            cfg.set(path + "uyeler", uyeler);
            cfg.set(path + "kasa", team.getKasa());
            cfg.set(path + "oynanan", team.getOynanan());
            cfg.set(path + "galibiyet", team.getGalibiyet());
            cfg.set(path + "beraberlik", team.getBeraberlik());
            cfg.set(path + "maglubiyet", team.getMaglubiyet());
            cfg.set(path + "atilanGol", team.getAtilanGol());
            cfg.set(path + "yenenGol", team.getYenenGol());
        }
        try {
            cfg.save(file);
        } catch (Exception e) {
            plugin.getLogger().warning("Ligler kaydedilemedi: " + e.getMessage());
        }
    }

    public Team get(String ad) {
        return teams.get(ad.toLowerCase());
    }

    public Team create(String ad, UUID kaptan) {
        Team team = new Team(ad, kaptan);
        teams.put(ad.toLowerCase(), team);
        return team;
    }

    public void remove(String ad) {
        teams.remove(ad.toLowerCase());
    }

    public List<Team> puanDurumu() {
        List<Team> list = new ArrayList<>(teams.values());
        list.sort(Comparator.comparingInt(Team::getPuan).reversed()
                .thenComparingInt(t -> t.getAtilanGol() - t.getYenenGol()));
        return list;
    }

    public Team takimiBul(UUID oyuncu) {
        for (Team team : teams.values()) {
            if (team.getUyeler().contains(oyuncu)) return team;
        }
        return null;
    }

    /**
     * Belirli araliklarla rastgele iki takim arasinda mac simule eder ve
     * puan durumunu gunceller. Basit bir "otomatik lig" hissi verir.
     */
    public void startMatchSimulationTask() {
        long araikTick = 20L * 60L * 30L; // 30 dakikada bir
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            List<Team> list = new ArrayList<>(teams.values());
            if (list.size() < 2) return;
            Team ev = list.get(random.nextInt(list.size()));
            Team deplasman;
            do {
                deplasman = list.get(random.nextInt(list.size()));
            } while (deplasman == ev);

            int evGucu = Math.max(1, ev.getUyeler().size());
            int depGucu = Math.max(1, deplasman.getUyeler().size());
            int evGol = random.nextInt(evGucu + 2);
            int depGol = random.nextInt(depGucu + 1);

            ev.maçSonucu(evGol, depGol);
            deplasman.maçSonucu(depGol, evGol);

            String sonuc = ChatColor.GOLD + "[Futbol Ligi] " + ChatColor.WHITE + ev.getAd()
                    + " " + evGol + " - " + depGol + " " + deplasman.getAd();
            Bukkit.broadcastMessage(sonuc);
        }, araikTick, araikTick);
    }
}
