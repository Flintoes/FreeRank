package dev.flintoes.main;

import dev.flintoes.main.command.FreeRankCommand;
import dev.flintoes.main.data.PlayerData;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;


public final class FreeRank extends JavaPlugin {

    @Getter
    private static FreeRank instance;

    @Getter
    private PlayerData playerData;

    @Override
    public void onEnable() {
        (instance = this).saveDefaultConfig();

        playerData = new PlayerData(this);

        getCommand("freerank").setExecutor(new FreeRankCommand(this));
    }

    @Override
    public void onDisable() {

        saveDefaultConfig();

        if (playerData != null) playerData.saveData(false);

    }
}
