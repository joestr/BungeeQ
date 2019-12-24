/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.commands;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import me.lucko.luckperms.LuckPerms;
import xyz.joestr.bungeeq.configuration.Configuration;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.joestr.bungeeq.unlockmanager.UnlockSession;
import xyz.joestr.bungeeq.unlockmanager.UnlockManager;

/**
 *
 * @author Joel
 */
public class CommandQUnlock extends Command {

    public CommandQUnlock(String name, String permission, String... aliases) {
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

        String notice = "";

        if (strings.length > 0) {
            notice
                = Arrays
                    .asList(strings)
                    .stream()
                    .collect(Collectors.joining(" "));
        }

        if (strings.length == 0) {
            notice = "";
        }

        UnlockSession unlock
            = UnlockManager
                .getInstance()
                .getUnlockByUnlocker(player.getUniqueId());

        if (unlock != null) {

            boolean unlockStatus = unlock.unlock(notice);

            if(!unlockStatus) {
                return;
            }
            
            CompletableFuture.runAsync(() -> {
                UnlockManager.getInstance().deleteDeletableUnlocks();
            });
            
            LuckPerms.getApi().runUpdateTask();

            return;
        }

        player.sendMessage(
            Configuration.transformForUnlocker(
                "Du schaltest zurzeit niemanden frei!"
            )
        );

        return;

        /*
        player.sendMessage(
            Configuration.usage(
                Configuration.Unlock.Finish.command() + " [<Nachricht ...>]"
            )
        );

        return;
         */
    }
}
