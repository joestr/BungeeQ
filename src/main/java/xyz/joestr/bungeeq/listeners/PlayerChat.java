/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import xyz.joestr.bungeeq.unlockmanager.UnlockSession;
import xyz.joestr.bungeeq.unlockmanager.UnlockManager;

/**
 *
 * @author Joel
 */
public class PlayerChat implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(ChatEvent event) {

        if (!(event.getSender() instanceof ProxiedPlayer)) {

            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        UnlockSession unlockSession
            = UnlockManager
                .getInstance()
                .getUnlockByTarget(player.getUniqueId());

        if (unlockSession == null) {

            return;
        }

        event.setCancelled(true);

        unlockSession.sendMessageToTargetAndUnlocker(
            player.getUniqueId(),
            event.getMessage()
        );
    }
}
