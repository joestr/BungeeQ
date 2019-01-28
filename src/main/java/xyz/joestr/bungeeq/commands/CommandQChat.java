/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.commands;

import java.util.Arrays;
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
public class CommandQChat extends Command {

    public CommandQChat(String name, String permission, String... aliases) {
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

        if (!player.hasPermission(Configuration.Q.Chat.permission())) {

            player.sendMessage(
                Configuration.proxiedPlayerLacksPermission(
                    Configuration.Q.Chat.permission()
                )
            );

            return;
        }

        if (strings.length != 0) {

            UnlockSession unlock
                = UnlockManager
                    .getInstance()
                    .getUnlockByUnlocker(player.getUniqueId());

            if (unlock != null) {

                String message
                    = Arrays
                        .asList(strings)
                        .stream()
                        .collect(Collectors.joining(" "));

                UnlockManager
                    .getInstance()
                    .getUnlockByUnlocker(player.getUniqueId())
                    .sendMessageToTargetAndUnlocker(
                        player.getUniqueId(),
                        message
                    );

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
                Configuration.Q.Chat.command() + " <Nachricht ...>"
            )
        );

        return;
    }
}
