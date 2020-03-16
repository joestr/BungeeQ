/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.bungeecord_plugin.commands;

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
public class CommandQAsk extends Command {

    public CommandQAsk(String name, String permission, String... aliases) {
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

        if (!player.hasPermission(Configuration.Q.Ask.permission())) {

            player.sendMessage(
                Configuration.proxiedPlayerLacksPermission(
                    Configuration.Q.Ask.permission()
                )
            );

            return;
        }

        if (strings.length == 0) {

            UnlockSession unlock
                = UnlockManager
                    .getInstance()
                    .getUnlockByUnlocker(player.getUniqueId());

            if (unlock != null) {

                unlock.nextQuestion();

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
                Configuration.Q.Ask.command()
            )
        );

        return;
    }
}
