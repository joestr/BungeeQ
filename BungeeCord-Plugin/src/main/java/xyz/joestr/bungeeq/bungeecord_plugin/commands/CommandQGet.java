/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.bungeecord_plugin.commands;

import net.luckperms.api.LuckPermsProvider;
import xyz.joestr.bungeeq.bungeecord_plugin.configuration.Configuration;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager.UnlockManager;
import xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager.UnlockSession;

/**
 *
 * @author Joel
 */
public class CommandQGet extends Command {

    public CommandQGet(String name, String permission, String... aliases) {
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

        if (strings.length == 0) {

            UnlockSession unlock
                = UnlockManager
                    .getInstance()
                    .getUnlockByUnlocker(player.getUniqueId());

            if (unlock != null) {

                player.sendMessage(
                    Configuration.transformForUnlocker(
                        "Du schaltest zurzeit "
                        + LuckPermsProvider.get().getUserManager().getUser(unlock.getTarget()).getUsername()
                        + " frei!"
                    )
                );

                return;
            }

            if (UnlockManager.getInstance().unqueuePlayer(player.getUniqueId())) {

                player.sendMessage(
                    Configuration.transformForUnlocker(
                        "Du schaltest nun jemanden frei!"
                    )
                );
                
                UnlockSession unlockSession = UnlockManager
                    .getInstance()
                    .getUnlockByUnlocker(player.getUniqueId());
                
                ProxiedPlayer targetPlayer = 
                    ProxyServer.getInstance().getPlayer(
                        unlockSession.getTarget()
                    );
                
                player.sendMessage(
                    Configuration.transformModList(targetPlayer.getModList())
                );
                
                unlockSession.nextQuestion();

                return;
            }

            player.sendMessage(
                Configuration.transformForUnlocker(
                    "Zurzeit ist niemand in der Warteschlange!"
                )
            );

            return;
        }

        player.sendMessage(
            Configuration.usage(
                Configuration.Q.Get.command()
            )
        );

        return;
    }
}
