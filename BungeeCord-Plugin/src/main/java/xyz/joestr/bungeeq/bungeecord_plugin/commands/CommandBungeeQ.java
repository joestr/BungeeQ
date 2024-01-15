/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.bungeecord_plugin.commands;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import xyz.joestr.bungeeq.bungeecord_plugin.configuration.Configuration;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import xyz.joestr.bungeeq.bungeecord_plugin.BungeeQBungeeCordPlugin;
import static xyz.joestr.bungeeq.bungeecord_plugin.BungeeQBungeeCordPlugin.configuration;
import xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager.UnlockManager;

/**
 *
 * @author Joel
 */
public class CommandBungeeQ extends Command {

    public CommandBungeeQ(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {

        if (!(commandSender instanceof ProxiedPlayer)) {

            commandSender.sendMessage(
                Configuration.commandSenderIsNotAProxiedPlayer()
            );

            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) commandSender;

        if (strings.length == 1) {

            if (strings[0].equalsIgnoreCase("reload")) {

                try {
                    configuration
                        = ConfigurationProvider
                            .getProvider(YamlConfiguration.class)
                            .load(BungeeQBungeeCordPlugin.configurationFile);
                    player.sendMessage();

                    player.sendMessage(
                        Configuration.transformForUnlocker(
                            "Das Laden der Konfigurationsdatei war erfolgreich!"
                        )
                    );

                    UnlockManager.getInstance().reloadQuestions();
                } catch (IOException ex) {
                    Logger.getLogger(BungeeQBungeeCordPlugin.class.getName())
                        .log(Level.SEVERE, null, ex);

                    player.sendMessage(
                        Configuration.transformForUnlocker(
                            "Das Laden der Konfigurationsdatei war nicht erfolgreich!"
                        )
                    );
                }

                return;
            }

            if (strings[0].equalsIgnoreCase("update")) {

                player.sendMessage(
                    Configuration.transformForUnlocker(
                        "Noch nicht implementiert!"
                    )
                );

                return;
            }
        }

        player.sendMessage(
            Configuration.usage(
                Configuration.BungeeQ.command() + " <reload|update>"
            )
        );

        return;
    }
}
