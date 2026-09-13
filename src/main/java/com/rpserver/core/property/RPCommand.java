package com.rpserver.core.property;

import com.rpserver.core.RPCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RPCommand implements CommandExecutor {

    private final RPCore plugin;

    public RPCommand(RPCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("rpcore.admin")) {
            sender.sendMessage(ChatColor.RED + "Bu komutu kullanma yetkin yok.");
            return true;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Bu komut sadece oyun icinde kullanilabilir.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "/rp set <ev|isyeri|restoran|market> <ad> [fiyat] [kira]");
            sender.sendMessage(ChatColor.YELLOW + "/rp remove <ad>");
            sender.sendMessage(ChatColor.YELLOW + "/rp list [tip]");
            sender.sendMessage(ChatColor.YELLOW + "/rp tp <ad>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "set" -> {
                if (args.length < 3) {
                    player.sendMessage(ChatColor.RED + "Kullanim: /rp set <ev|isyeri|restoran|market> <ad> [fiyat] [kira]");
                    return true;
                }
                RPProperty.Tip tip;
                try {
                    tip = RPProperty.Tip.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException ex) {
                    player.sendMessage(ChatColor.RED + "Gecersiz tip. Kullan: ev, isyeri, restoran, market");
                    return true;
                }
                String ad = args[2];
                double fiyat = args.length > 3 ? parseD(args[3], 1000) : 1000;
                double kira = args.length > 4 ? parseD(args[4], fiyat * 0.05) : fiyat * 0.05;

                if (plugin.getPropertyManager().get(ad) != null) {
                    player.sendMessage(ChatColor.RED + "Bu isimde bir mulk zaten var.");
                    return true;
                }
                plugin.getPropertyManager().create(ad, tip, player.getLocation(), fiyat, kira);
                player.sendMessage(ChatColor.GREEN + tip.name() + " turunde '" + ad + "' konumu kaydedildi. Fiyat: " + fiyat + " | Kira: " + kira);
            }
            case "remove" -> {
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Kullanim: /rp remove <ad>");
                    return true;
                }
                boolean removed = plugin.getPropertyManager().remove(args[1]);
                player.sendMessage(removed ? ChatColor.GREEN + "Mulk silindi." : ChatColor.RED + "Mulk bulunamadi.");
            }
            case "list" -> {
                player.sendMessage(ChatColor.GOLD + "== Kayitli Mulkler ==");
                plugin.getPropertyManager().all().values().forEach(p ->
                        player.sendMessage(ChatColor.YELLOW + p.getAd() + ChatColor.GRAY + " (" + p.getTip() + ") "
                                + (p.isSahipli() ? ChatColor.RED + "[Sahipli]" : ChatColor.GREEN + "[Bos]")));
            }
            case "tp" -> {
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Kullanim: /rp tp <ad>");
                    return true;
                }
                RPProperty prop = plugin.getPropertyManager().get(args[1]);
                if (prop == null) {
                    player.sendMessage(ChatColor.RED + "Mulk bulunamadi.");
                    return true;
                }
                player.teleport(prop.getKonum());
            }
            default -> player.sendMessage(ChatColor.RED + "Bilinmeyen alt komut.");
        }
        return true;
    }

    private double parseD(String s, double def) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
