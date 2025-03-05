package ru.xopek.universalkits;

import lombok.Getter;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.xopek.universalkits.database.HibernateUtils;
import ru.xopek.universalkits.kits.Kit;
import ru.xopek.universalkits.kits.KitsSerializer;

import java.io.File;
import java.util.LinkedHashMap;

import static org.bukkit.Bukkit.getPluginManager;

@Getter
public final class UniversalKits extends JavaPlugin {

    @Getter
    private static UniversalKits inst;
    private LinkedHashMap<String, Kit> kits;
    private PlayerPoints playerPoints;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        inst = this;
        kits = new LinkedHashMap<>();

        hookPlayerPoints();

        File file = new File(this.getDataFolder(), "/kits.yml");

        if(!file.exists())
            saveResource("kits.yml", false);

        KitsSerializer.serialize(YamlConfiguration.loadConfiguration(file));

        HibernateUtils.init(getConfig().getConfigurationSection("database"));

        getCommand("ekits").setExecutor(new UniversalKits());

        getPluginManager().registerEvents(new EventProcessor(), this);
    }

    private void hookPlayerPoints() {
        final Plugin plugin = this.getServer().getPluginManager().getPlugin("PlayerPoints");
        playerPoints = (PlayerPoints) plugin;
    }

    @Override
    public void onDisable() {
    }
}
