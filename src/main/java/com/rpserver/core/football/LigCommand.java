package com.rpserver.core.football;

import com.rpserver.core.RPCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LigCommand implements CommandExecutor {

    private final RPCore plugin;

    public LigCommand(RPCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.GOLD + "== Futbol Ligi Puan Durumu ==");
        int sira = 1;
        for (Team team : plugin.getLeagueManager().puanDurumu()) {
            sender.sendMessage(ChatColor.YELLOW + "" + sira + ". " + ChatColor.WHITE + team.getAd()
                    + ChatColor.GRAY + "  O:" + team.getOynanan() + " G:" + team.getGalibiyet()
                    + " B:" + team.getBeraberlik() + " M:" + team.getMaglubiyet()
                    + " AG:" + team.getAtilanGol() + " YG:" + team.getYenenGol()
                    + ChatColor.GOLD + "  Puan:" + team.getPuan());
            sira++;
        }
        if (plugin.getLeagueManager().puanDurumu().isEmpty()) {
            sender.sendMessage(ChatColor.GRAY + "Henuz kayitli takim yok. /takim kur <isim> ile kur!");
        }
        return true;
    }
}
