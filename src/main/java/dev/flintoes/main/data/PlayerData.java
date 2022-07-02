package dev.flintoes.main.data;

import dev.flintoes.main.FreeRank;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerData {

    @Getter
    private final FreeRank plugin;

    @Getter
    private FileConfiguration data;

    @Getter
    private boolean saving;

    public PlayerData(FreeRank instance) {
        this.saving = false;
        this.plugin = instance;
        loadData();
    }

    public void loadData() {
        File folder = new File(plugin.getDataFolder(), File.separator + "data");
        File file = new File(folder, File.separator + "playerData.yml");
        data = YamlConfiguration.loadConfiguration(file);
    }

    public void saveData(boolean async) {
        if (saving)
            return;
        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                File folder = new File(plugin.getDataFolder(), File.separator + "data");
                File file = new File(folder, File.separator + "playerData.yml");
                try {
                    data.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                saving = false;
            });
        } else {
            File folder = new File(plugin.getDataFolder(), File.separator + "data");
            File file = new File(folder, File.separator + "playerData.yml");
            try {
                data.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            saving = false;
        }
    }

    public void setClaimed(UUID uuid) {
        data.set(String.valueOf(uuid), Boolean.TRUE);
        saveData(true);
    }

    public boolean hasAlreadyClaimed(UUID uuid) {
        if (data.get(String.valueOf(uuid)) == null)
            return false;
        return data.getBoolean(String.valueOf(uuid));
    }
}