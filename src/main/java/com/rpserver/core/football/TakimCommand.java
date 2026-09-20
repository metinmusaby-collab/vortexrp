package com.rpserver.core.football;

import com.rpserver.core.RPCore;
import com.rpserver.core.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TakimCommand implements CommandExecutor {

    private final RPCore plugin;

    public TakimCommand(RPCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Bu komut sadece oyun icinde kullanilabilir.");
            return true;
        }
        if (args.length < 1) {
            player.sendMessage(ChatColor.YELLOW + "/takim kur <isim>");
            player.sendMessage(ChatColor.YELLOW + "/takim katil <isim>");
            player.sendMessage(ChatColor.YELLOW + "/takim ayril");
            return true;
        }

        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
        LeagueManager lm = plugin.getLeagueManager();

        switch (args[0].toLowerCase()) {
            case "kur" -> {
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Kullanim: /takim kur <isim>");
                    return true;
                }
                if (data.getTakimAdi() != null) {
                    player.sendMessage(ChatColor.RED + "Zaten bir takimdasin.");
                    return true;
                }
                if (lm.get(args[1]) != null) {
                    player.sendMessage(ChatColor.RED + "Bu isimde bir takim zaten var.");
                    return true;
                }
                double ucret = plugin.getConfig().getDouble("takim.kurma-ucreti", 1000);
                if (!data.harca(ucret)) {
                    player.sendMessage(ChatColor.RED + "Takim kurmak icin " + ucret + " $ gerekiyor.");
                    return true;
                }
                lm.create(args[1], player.getUniqueId());
                data.setTakimAdi(args[1]);
                player.sendMessage(ChatColor.GREEN + "'" + args[1] + "' takimini kurdun ve kaptani oldun!");
            }
            case "katil" -> {
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Kullanim: /takim katil <isim>");
                    return true;
                }
                if (data.getTakimAdi() != null) {
                    player.sendMessage(ChatColor.RED + "Zaten bir takimdasin.");
                    return true;
                }
                Team team = lm.get(args[1]);
                if (team == null) {
                    player.sendMessage(ChatColor.RED + "Takim bulunamadi.");
                    return true;
                }
                double ucret = plugin.getConfig().getDouble("takim.katilma-ucreti", 100);
                if (!data.harca(ucret)) {
                    player.sendMessage(ChatColor.RED + "Takima katilmak icin " + ucret + " $ gerekiyor.");
                    return true;
                }
                team.getUyeler().add(player.getUniqueId());
                team.ekleKasa(ucret);
                data.setTakimAdi(team.getAd());
                player.sendMessage(ChatColor.GREEN + "'" + team.getAd() + "' takimina katildin!");
            }
            case "ayril" -> {
                if (data.getTakimAdi() == null) {
                    player.sendMessage(ChatColor.RED + "Bir takimda degilsin.");
                    return true;
                }
                Team team = lm.get(data.getTakimAdi());
                if (team != null) team.getUyeler().remove(player.getUniqueId());
                data.setTakimAdi(null);
                player.sendMessage(ChatColor.YELLOW + "Takimdan ayrildin.");
            }
            default -> player.sendMessage(ChatColor.RED + "Bilinmeyen alt komut.");
        }
        return true;
    }
}
