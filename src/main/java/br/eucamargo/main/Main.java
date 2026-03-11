package br.eucamargo.main;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ShootTntListener(this), this);
        getServer().getPluginManager().registerEvents(new BigExplosionListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
