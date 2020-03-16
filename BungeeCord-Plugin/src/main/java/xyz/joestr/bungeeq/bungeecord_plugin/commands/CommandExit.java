/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.bungeecord_plugin.commands;

import java.util.concurrent.CompletableFuture;
import xyz.joestr.bungeeq.bungeecord_plugin.configuration.Configuration;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager.UnlockManager;
import xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager.UnlockSession;

/**
 *
 * @author Joel
 */
public class CommandExit extends Command {

    public CommandExit(String name, String permission, String... aliases) {
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

        if (strings.length == 0) {

            UnlockSession unlockSession
                = UnlockManager
                    .getInstance()
                    .getUnlockByTarget(player.getUniqueId());

            if (unlockSession == null) {

                player.sendMessage(
                    Configuration.transformForTarget(
                        "Du bist zurzeit in keiner Freischaltung!"
                    )
                );

                return;
            }

            unlockSession.exit(true);

            CompletableFuture.runAsync(() -> {
                UnlockManager.getInstance().deleteDeletableUnlocks();
            });

            return;
        }

        player.sendMessage(
            Configuration.usage(
                Configuration.Exit.command()
            )
        );

        return;
    }
}
