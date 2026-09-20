package com.rpserver.core.job;

import com.rpserver.core.RPCore;
import com.rpserver.core.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class JobManager {

    private final RPCore plugin;

    public JobManager(RPCore plugin) {
        this.plugin = plugin;
    }

    public void load() {
        // Meslek verisi PlayerData icinde saklaniyor, ayrica bir dosyaya gerek yok.
    }

    public void saveAll() {
        // bkz. load()
    }

    public void setJob(Player player, JobType job) {
        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
        data.setMeslek(job == null ? null : job.name());
        if (job != JobType.POLIS) {
            data.setPolisNobette(false);
        }
    }

    public JobType getJob(Player player) {
        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
        if (data.getMeslek() == null) return null;
        try {
            return JobType.valueOf(data.getMeslek());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void pay(Player player, double amount, String sebep) {
        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
        data.ekleBakiye(amount);
        player.sendMessage(ChatColor.GREEN + "+" + amount + " $ " + ChatColor.GRAY + "(" + sebep + ")");
    }

    public void startPoliceSalaryTask() {
        long aralikSaniye = plugin.getConfig().getLong("is.polis-nobet-araligi-saniye", 600);
        double maas = plugin.getConfig().getDouble("is.polis-maas", 50.0);
        long ticks = aralikSaniye * 20L;
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
                if (data.isPolisNobette()) {
                    pay(player, maas, "Polis maasi");
                }
            }
        }, ticks, ticks);
    }
}
