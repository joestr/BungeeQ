/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.bukkit_plugin;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * The entry point for this Bukkit plugin.
 * 
 * @author Joel
 */
public class BungeeQBukkitPlugin extends JavaPlugin implements PluginMessageListener {

    @Override
    public void onEnable() {
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "bungeeq:sound.enqueue", this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        
        if(!channel.equals("bungeeq:sound.enqueue")) {
            return;
        }
        
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, SoundCategory.MASTER, 1, 0);
    }
}
