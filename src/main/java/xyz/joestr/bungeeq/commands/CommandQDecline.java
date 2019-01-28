/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.commands;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import xyz.joestr.bungeeq.configuration.Configuration;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.joestr.bungeeq.unlockmanager.UnlockManager;
import xyz.joestr.bungeeq.unlockmanager.UnlockSession;

/**
 *
 * @author Joel
 */
public class CommandQDecline extends Command {

    public CommandQDecline(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {

        if (!(commandSender instanceof ProxiedPlayer)) {

            commandSender.sendMessage(
                Configuration.commandSenderIsNotAProxiedPlayer()
            );
        }

        ProxiedPlayer player = (ProxiedPlayer) commandSender;

        if (!player.hasPermission(Configuration.Q.Decline.permission())) {

            player.sendMessage(
                Configuration.proxiedPlayerLacksPermission(
                    Configuration.Q.Decline.permission()
                )
            );

            return;
        }

        String notice = "";

        if (strings.length > 0) {
            notice
                = Arrays
                    .asList(strings)
                    .stream()
                    .collect(Collectors.joining(" "));

            UnlockSession unlock
                = UnlockManager
                    .getInstance()
                    .getUnlockByUnlocker(player.getUniqueId());

            if (unlock != null) {

                unlock.decline(notice);

                CompletableFuture.runAsync(() -> {
                    UnlockManager.getInstance().deleteDeletableUnlocks();
                });

                return;
            }

            player.sendMessage(
                Configuration.transformForUnlocker(
                    "Du schaltest zurzeit niemanden frei!"
                )
            );

            return;
        }

        player.sendMessage(
            Configuration.usage(
                Configuration.Q.Decline.command() + " <Notiz ...>"
            )
        );

        return;
    }
}
