package com.rpserver.core.thirst;

import com.rpserver.core.RPCore;
import com.rpserver.core.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SuCommand implements CommandExecutor {

    private final RPCore plugin;

    public SuCommand(RPCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Bu komut sadece oyun icinde kullanilabilir.");
            return true;
        }
        PlayerInventory inv = player.getInventory();
        int idx = inv.first(Material.WATER_BUCKET);
        if (idx == -1) {
            player.sendMessage(ChatColor.RED + "Uzerinde su matarasi (su kovasi) yok. Market veya restorandan alabilirsin.");
            return true;
        }
        ItemStack stack = inv.getItem(idx);
        if (stack.getAmount() > 1) {
            stack.setAmount(stack.getAmount() - 1);
        } else {
            inv.setItem(idx, new ItemStack(Material.BUCKET));
        }
        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
        data.setSusama(data.getSusama() + 50);
        player.sendMessage(ChatColor.AQUA + "Su ictin. Susama: " + (int) data.getSusama() + "/100");
        return true;
    }
}
