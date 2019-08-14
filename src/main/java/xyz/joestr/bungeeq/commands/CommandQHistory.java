/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.commands;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.lucko.luckperms.LuckPerms;
import xyz.joestr.bungeeq.configuration.Configuration;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.joestr.bungeeq.unlockmanager.UnlockEntry;
import xyz.joestr.bungeeq.unlockmanager.UnlockManager;

/**
 *
 * @author Joel
 */
public class CommandQHistory extends Command {

    public CommandQHistory(String name, String permission, String... aliases) {
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

        if (strings.length == 1) {

            CompletableFuture.runAsync(() -> {
                Collection<UnlockEntry> unlockHistory
                    = UnlockManager.getInstance().getHistoryOf(strings[0]);

                if (unlockHistory == null) {

                    player.sendMessage(
                        Configuration.transformForUnlocker(
                            "Dieser Spieler hat keine Freischaltgeschichte!"
                        )
                    );

                    return;
                }

                if (unlockHistory.isEmpty()) {

                    player.sendMessage(
                        Configuration.transformForUnlocker(
                            "Dieser Spieler hat keine Freischaltgeschichte!"
                        )
                    );

                    return;
                }

                player.sendMessage(
                    Configuration.transformHistoryHead(strings[0])
                );

                for(UnlockEntry unlock : unlockHistory) {
                    
                    player.sendMessage(
                        Configuration.transformHistoryBody(
                            LuckPerms.getApi()
                                .getUser(UUID.fromString(unlock.getUnlocker()))
                                .getFriendlyName(),
                            unlock.getStart(),
                            unlock.getEnd(),
                            unlock.getStatus(),
                            unlock.getNotice()
                        )
                    );
                }
            });

            return;
        }

        player.sendMessage(
            Configuration.usage(
                Configuration.Q.History.command() + " <Spieler>"
            )
        );
    }
}
