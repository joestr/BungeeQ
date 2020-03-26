/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.bungeecord_plugin.listeners;

import java.util.concurrent.CompletableFuture;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager.UnlockManager;

/**
 *
 * @author Joel
 */
public class PlayerLeave implements Listener {

  @EventHandler
  public void onLeave(PlayerDisconnectEvent event) {
    ProxiedPlayer player = event.getPlayer();
    CompletableFuture.runAsync(() -> {
      UnlockManager.getInstance().removePossibleUnlocker(player.getUniqueId());
      UnlockManager.getInstance().removePlayerFromQueue(player.getUniqueId());
    });
  }
}
