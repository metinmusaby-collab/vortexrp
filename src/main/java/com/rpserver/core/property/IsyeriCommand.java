package com.rpserver.core.property;

import com.rpserver.core.RPCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IsyeriCommand implements CommandExecutor {

    private final RPCore plugin;

    public IsyeriCommand(RPCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Bu komut sadece oyun icinde kullanilabilir.");
            return true;
        }
        player.openInventory(new PropertyGUI(plugin, RPProperty.Tip.ISYERI).getInventory());
        return true;
    }
}
