/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.commands;

import xyz.joestr.bungeeq.configuration.Configuration;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.joestr.bungeeq.unlockmanager.UnlockManager;

/**
 *
 * @author Joel
 */
public class CommandActivate extends Command {

    public CommandActivate(String name, String permission, String... aliases) {
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

            UnlockManager.getInstance().queuePlayer(player.getUniqueId());

            return;
        }

        player.sendMessage(
            Configuration.usage(
                Configuration.Activate.command()
            )
        );

        return;
    }
}
