package com.rpserver.core.economy;

import com.rpserver.core.RPCore;
import com.rpserver.core.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankaCommand implements CommandExecutor {

    private final RPCore plugin;

    public BankaCommand(RPCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Bu komut sadece oyun icinde kullanilabilir.");
            return true;
        }

        if (args.length >= 3 && args[0].equalsIgnoreCase("gonder")) {
            OfflinePlayer hedef = Bukkit.getOfflinePlayer(args[1]);
            if (hedef == null || (!hedef.hasPlayedBefore() && !hedef.isOnline())) {
                player.sendMessage(ChatColor.RED + "Oyuncu bulunamadi.");
                return true;
            }
            double miktar;
            try {
                miktar = Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Gecersiz miktar.");
                return true;
            }
            if (miktar <= 0) {
                player.sendMessage(ChatColor.RED + "Miktar sifirdan buyuk olmali.");
                return true;
            }
            PlayerData gonderen = plugin.getPlayerDataManager().get(player.getUniqueId());
            if (!gonderen.harca(miktar)) {
                player.sendMessage(ChatColor.RED + "Yeterli bakiyen yok.");
                return true;
            }
            PlayerData alan = plugin.getPlayerDataManager().get(hedef.getUniqueId());
            alan.ekleBakiye(miktar);
            player.sendMessage(ChatColor.GREEN + miktar + " $ gonderildi -> " + hedef.getName());
            if (hedef.isOnline() && hedef.getPlayer() != null) {
                hedef.getPlayer().sendMessage(ChatColor.GREEN + player.getName() + " sana " + miktar + " $ gonderdi.");
            }
            return true;
        }

        player.openInventory(new BankGUI(plugin, player).getInventory());
        return true;
    }
}
