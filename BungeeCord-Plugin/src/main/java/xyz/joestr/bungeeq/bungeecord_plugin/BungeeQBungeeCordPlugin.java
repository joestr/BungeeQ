/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.bungeecord_plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.api.ProxyServer;
import xyz.joestr.bungeeq.bungeecord_plugin.commands.CommandActivate;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import xyz.joestr.bungeeq.bungeecord_plugin.commands.CommandBungeeQ;
import xyz.joestr.bungeeq.bungeecord_plugin.commands.CommandExit;
import xyz.joestr.bungeeq.bungeecord_plugin.commands.CommandQAsk;
import xyz.joestr.bungeeq.bungeecord_plugin.commands.CommandQChat;
import xyz.joestr.bungeeq.bungeecord_plugin.commands.CommandQDecline;
import xyz.joestr.bungeeq.bungeecord_plugin.commands.CommandQExit;
import xyz.joestr.bungeeq.bungeecord_plugin.commands.CommandQUnlock;
import xyz.joestr.bungeeq.bungeecord_plugin.commands.CommandQGet;
import xyz.joestr.bungeeq.bungeecord_plugin.commands.CommandQHistory;
import xyz.joestr.bungeeq.bungeecord_plugin.commands.CommandQList;
import xyz.joestr.bungeeq.bungeecord_plugin.commands.CommandQRepeat;
import xyz.joestr.bungeeq.bungeecord_plugin.commands.CommandQWatch;
import xyz.joestr.bungeeq.bungeecord_plugin.configuration.Configuration;
import xyz.joestr.bungeeq.bungeecord_plugin.listeners.PlayerChat;
import xyz.joestr.bungeeq.bungeecord_plugin.listeners.PlayerJoin;
import xyz.joestr.bungeeq.bungeecord_plugin.listeners.PlayerLeave;
import xyz.joestr.bungeeq.bungeecord_plugin.listeners.TabComplete;

/**
 *
 * @author Joel
 */
public class BungeeQBungeeCordPlugin extends Plugin {

    public static BungeeQBungeeCordPlugin instance;
    public static net.md_5.bungee.config.Configuration configuration;
    public static File configurationFile;
    
    @Override
    public void onEnable() {
        
        instance = this;
        
        boolean successfullyLoaded = false;

        configurationFile = new File(getDataFolder(), "config.yml");

        // Check if the configuration file exists
        if (configurationFile.exists()) {

            try {
                configuration
                    = ConfigurationProvider
                        .getProvider(YamlConfiguration.class)
                        .load(configurationFile);
                successfullyLoaded = true;
            } catch (IOException ex) {
                Logger.getLogger(BungeeQBungeeCordPlugin.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
        } else {

            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }

            File file = new File(getDataFolder(), "config.yml");

            if (!file.exists()) {
                try (InputStream in = getResourceAsStream("config.yml")) {
                    Files.copy(in, file.toPath());
                } catch (IOException ex) {
                    Logger.getLogger(BungeeQBungeeCordPlugin.class.getName())
                        .log(Level.SEVERE, null, ex);
                }
            }
        }

        if (!successfullyLoaded) {
            return;
        }
        
        this.getProxy().registerChannel("bungeeq:sound.enqueue");

        this.getProxy().getPluginManager().registerCommand(this,
            new CommandActivate(
                Configuration.Activate.command(),
                Configuration.Activate.permission(),
                Configuration.Activate.alias()
            )
        );
        this.getProxy().getPluginManager().registerCommand(this,
            new CommandExit(
                Configuration.Exit.command(),
                Configuration.Exit.permission(),
                Configuration.Exit.alias()
            )
        );
        this.getProxy().getPluginManager().registerCommand(this,
            new CommandQGet(
                Configuration.Q.Get.command(),
                Configuration.Q.Get.permission(),
                Configuration.Q.Get.alias()
            )
        );
        this.getProxy().getPluginManager().registerCommand(this,
            new CommandQAsk(
                Configuration.Q.Ask.command(),
                Configuration.Q.Ask.permission(),
                Configuration.Q.Ask.alias()
            )
        );
        this.getProxy().getPluginManager().registerCommand(this,
            new CommandQRepeat(
                Configuration.Q.Repeat.command(),
                Configuration.Q.Repeat.permission(),
                Configuration.Q.Repeat.alias()
            )
        );
        this.getProxy().getPluginManager().registerCommand(this,
            new CommandQChat(
                Configuration.Q.Chat.command(),
                Configuration.Q.Chat.permission(),
                Configuration.Q.Chat.alias()
            )
        );
        this.getProxy().getPluginManager().registerCommand(this,
            new CommandQExit(
                Configuration.Q.Exit.command(),
                Configuration.Q.Exit.permission(),
                Configuration.Q.Exit.alias()
            )
        );
        this.getProxy().getPluginManager().registerCommand(this,
            new CommandQDecline(
                Configuration.Q.Decline.command(),
                Configuration.Q.Decline.permission(),
                Configuration.Q.Decline.alias()
            )
        );
        this.getProxy().getPluginManager().registerCommand(this,
            new CommandQUnlock(
                Configuration.Q.Unlock.command(),
                Configuration.Q.Unlock.permission(),
                Configuration.Q.Unlock.alias()
            )
        );
        this.getProxy().getPluginManager().registerCommand(this,
            new CommandQHistory(
                Configuration.Q.History.command(),
                Configuration.Q.History.permission(),
                Configuration.Q.History.alias()
            )
        );
        this.getProxy().getPluginManager().registerCommand(this,
            new CommandQWatch(
                Configuration.Q.Watch.command(),
                Configuration.Q.Watch.permission(),
                Configuration.Q.Watch.alias()
            )
        );
        this.getProxy().getPluginManager().registerCommand(this,
            new CommandQList(
                Configuration.Q.List.command(),
                Configuration.Q.List.permission(),
                Configuration.Q.List.alias()
            )
        );
        this.getProxy().getPluginManager().registerCommand(this,
            new CommandBungeeQ(
                Configuration.BungeeQ.command(),
                Configuration.BungeeQ.permission(),
                Configuration.BungeeQ.alias()
            )
        );
        this.getProxy().getPluginManager().registerListener(this,
            new PlayerChat()
        );
        this.getProxy().getPluginManager().registerListener(this,
            new PlayerJoin()
        );
        this.getProxy().getPluginManager().registerListener(this,
            new PlayerLeave()
        );
        this.getProxy().getPluginManager().registerListener(this,
            new TabComplete(this)
        );
    }
}
