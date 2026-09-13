package com.rpserver.core.thirst;

import com.rpserver.core.RPCore;
import com.rpserver.core.player.PlayerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ThirstManager {

    private final RPCore plugin;

    public ThirstManager(RPCore plugin) {
        this.plugin = plugin;
    }

    public void start() {
        long aralikTick = plugin.getConfig().getLong("susama.azalma-araligi-saniye", 60) * 20L;
        double azalma = plugin.getConfig().getDouble("susama.azalma-miktari", 2.0);
        double kritikEsik = plugin.getConfig().getDouble("susama.kritik-esik", 20.0);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
                data.setSusama(data.getSusama() - azalma);

                if (data.getSusama() <= kritikEsik) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, (int) aralikTick + 40, 0, true, false));
                    player.sendActionBar(Component.text("Susadin! Su icmek icin /su ", NamedTextColor.RED));
                } else if (data.getSusama() < 50) {
                    player.sendActionBar(Component.text("Susama: " + (int) data.getSusama() + "/100", NamedTextColor.YELLOW));
                }
            }
        }, aralikTick, aralikTick);
    }
}
