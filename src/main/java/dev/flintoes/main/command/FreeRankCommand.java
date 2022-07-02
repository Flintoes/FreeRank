package dev.flintoes.main.command;

import dev.flintoes.main.FreeRank;
import dev.flintoes.main.utils.Strings;
import dev.flintoes.main.utils.titles.TitleManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class FreeRankCommand implements CommandExecutor {

    @Getter
    private final FreeRank plugin;

    public FreeRankCommand(FreeRank instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            Strings.sendMessage(sender, plugin.getConfig().getString("messages.player-only")
                    .replace("{action}", "/freerank"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            if (!plugin.getPlayerData().hasAlreadyClaimed(player.getUniqueId())) {

                plugin.getPlayerData().setClaimed(player.getUniqueId());

                if (plugin.getConfig().getBoolean("free-rank.broadcast.enabled")) {
                    for (String msg : plugin.getConfig().getStringList("free-rank.broadcast.message"))
                        for (Player players : Bukkit.getOnlinePlayers())
                            Strings.sendMessage(players, msg.replace("{player}", player.getName()));
                }

                if (plugin.getConfig().getBoolean("free-rank.sounds.enabled")) {
                    player.playSound(player.getLocation(), plugin.getConfig().getString("free-rank.sounds.sound"), 1.0F, 0.5F);
                }

                if (plugin.getConfig().getBoolean("free-rank.titles.enabled")) {
                    TitleManager.sendTitle(player, 0, 80, 25,
                            plugin.getConfig().getString("free-rank.titles.title"),
                            plugin.getConfig().getString("free-rank.titles.subtitle"));
                }

                for (String msg : plugin.getConfig().getStringList("free-rank.claimed"))
                    Strings.sendMessage(player, msg.replace("{player}", player.getName()));

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("free-rank.command")
                        .replace("{player}", player.getName()));
                return true;
            }

            Strings.sendMessage(player, plugin.getConfig().getString("messages.already-claimed"));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("freerank.admin")) {
                Strings.sendMessage(player, plugin.getConfig().getString("messages.no-permission")
                        .replace("{action}", args[0]));
                return true;
            }

            long start = System.currentTimeMillis();

            plugin.getPlayerData().saveData(true);
            plugin.reloadConfig();

            long end = System.currentTimeMillis() - start;

            Strings.sendMessage(player, plugin.getConfig().getString("messages.reloaded").replace("{time}",
                    (new DecimalFormat("###.#")).format(end)));
            return true;
        }
        return true;
    }
}
