package com.rpserver.core.phone;

import com.rpserver.core.RPCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SosyalCommand implements CommandExecutor {

    private final RPCore plugin;

    public SosyalCommand(RPCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Bu komut sadece oyun icinde kullanilabilir.");
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Kullanim: /sosyal <mesaj>");
            return true;
        }
        String mesaj = String.join(" ", args);
        plugin.getSocialManager().paylas(player.getUniqueId(), player.getName(), mesaj);
        player.sendMessage(ChatColor.GREEN + "Paylasimin sosyal medyaya eklendi!");
        return true;
    }
}
