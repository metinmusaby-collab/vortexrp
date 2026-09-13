package com.rpserver.core.property;

import com.rpserver.core.RPCore;
import com.rpserver.core.restaurant.RestaurantMenuGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RestoranCommand implements CommandExecutor {

    private final RPCore plugin;

    public RestoranCommand(RPCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Bu komut sadece oyun icinde kullanilabilir.");
            return true;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("menu")) {
            player.openInventory(new RestaurantMenuGUI(plugin).getInventory());
            return true;
        }
        player.openInventory(new PropertyGUI(plugin, RPProperty.Tip.RESTORAN).getInventory());
        player.sendMessage(ChatColor.GRAY + "Ipucu: Yemek yemek icin '/restoran menu' kullan.");
        return true;
    }
}
