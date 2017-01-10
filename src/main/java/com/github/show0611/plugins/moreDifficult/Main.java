package com.github.show0611.plugins.moreDifficult;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.show0611.plugins.moreDifficult.listeners.EventListener;

public class Main extends JavaPlugin {
    public static Plugin instance;
    public static Server server;
    public static PluginManager pm;

    @Override
    public void onEnable() {
        instance = this;
        server = getServer();
        pm = server.getPluginManager();

        pm.registerEvents(new EventListener(), this);
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
    }

    /* Getter */
    public static Plugin getPlugin() {
        return instance;
    }

    public static Server getSvr() {
        return server;
    }

    public static PluginManager getPluginManager() {
        return pm;
    }
}
