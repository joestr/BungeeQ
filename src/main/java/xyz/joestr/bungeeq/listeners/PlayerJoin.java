/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.listeners;

import java.util.concurrent.CompletableFuture;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.joestr.bungeeq.unlockmanager.UnlockManager;

/**
 *
 * @author Joel
 */
public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PostLoginEvent event) {

        ProxiedPlayer player = event.getPlayer();

        CompletableFuture.runAsync(() -> {

            UnlockManager.getInstance().addPossibleUnlocker(player.getUniqueId());
        });
    }
}
