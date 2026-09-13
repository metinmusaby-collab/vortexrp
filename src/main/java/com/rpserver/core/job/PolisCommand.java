package com.rpserver.core.job;

import com.rpserver.core.RPCore;
import com.rpserver.core.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PolisCommand implements CommandExecutor {

    private final RPCore plugin;

    public PolisCommand(RPCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Bu komut sadece oyun icinde kullanilabilir.");
            return true;
        }
        JobType job = plugin.getJobManager().getJob(player);
        if (job != JobType.POLIS) {
            player.sendMessage(ChatColor.RED + "Polis meslegine sahip degilsin. /is komutuyla sec.");
            return true;
        }
        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
        boolean yeniDurum = !data.isPolisNobette();
        data.setPolisNobette(yeniDurum);

        if (yeniDurum) {
            player.getInventory().addItem(
                    new ItemStack(Material.IRON_SWORD),
                    new ItemStack(Material.IRON_CHESTPLATE),
                    new ItemStack(Material.IRON_HELMET)
            );
            player.sendMessage(ChatColor.GREEN + "Nobete basladin. Duzenli maas almaya basladin.");
        } else {
            player.sendMessage(ChatColor.YELLOW + "Nobeti bitirdin.");
        }
        return true;
    }
}
