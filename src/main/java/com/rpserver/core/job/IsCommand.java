package com.rpserver.core.job;

import com.rpserver.core.RPCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IsCommand implements CommandExecutor {

    private final RPCore plugin;

    public IsCommand(RPCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Bu komut sadece oyun icinde kullanilabilir.");
            return true;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("birak")) {
            plugin.getJobManager().setJob(player, null);
            player.sendMessage(ChatColor.YELLOW + "Meslegini biraktin.");
            return true;
        }
        JobType mevcut = plugin.getJobManager().getJob(player);
        if (mevcut != null) {
            player.sendMessage(ChatColor.GRAY + "Mevcut meslegin: " + ChatColor.YELLOW + mevcut.getGosterimAdi());
        }
        player.openInventory(new JobGUI(plugin).getInventory());
        return true;
    }
}
