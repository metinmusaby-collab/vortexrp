package com.rpserver.core.job;

import com.rpserver.core.RPCore;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.EnumSet;
import java.util.Set;

public class JobListener implements Listener {

    private final RPCore plugin;

    private static final Set<Material> MAHSULLER = EnumSet.of(
            Material.WHEAT, Material.CARROTS, Material.POTATOES, Material.BEETROOTS
    );

    private static final Set<Material> CEVHERLER = EnumSet.of(
            Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE,
            Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE,
            Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE,
            Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE,
            Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE,
            Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE,
            Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE,
            Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE
    );

    public JobListener(RPCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        JobType job = plugin.getJobManager().getJob(player);
        if (job == null) return;

        Block block = event.getBlock();

        if (job == JobType.CIFTCI && MAHSULLER.contains(block.getType())) {
            if (block.getBlockData() instanceof Ageable ageable && ageable.getAge() >= ageable.getMaximumAge()) {
                double odeme = plugin.getConfig().getDouble("is.ciftci-odeme", 4.0);
                plugin.getJobManager().pay(player, odeme, "Hasat");
            }
            return;
        }

        if (job == JobType.MADENCI && CEVHERLER.contains(block.getType())) {
            double odeme = plugin.getConfig().getDouble("is.madenci-odeme", 5.0);
            plugin.getJobManager().pay(player, odeme, "Madencilik");
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        Player player = event.getPlayer();
        JobType job = plugin.getJobManager().getJob(player);
        if (job != JobType.BALIKCI) return;
        double odeme = plugin.getConfig().getDouble("is.balikci-odeme", 6.0);
        plugin.getJobManager().pay(player, odeme, "Balikcilik");
    }
}
