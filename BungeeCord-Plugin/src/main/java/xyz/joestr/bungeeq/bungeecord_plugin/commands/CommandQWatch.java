/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package xyz.joestr.bungeeq.bungeecord_plugin.commands;

import com.google.common.collect.ImmutableList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import xyz.joestr.bungeeq.bungeecord_plugin.configuration.Configuration;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager.UnlockManager;
import xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager.UnlockSession;

/**
 * @author Joel
 */
public class CommandQWatch extends Command implements TabExecutor {

  public CommandQWatch(String name, String permission, String... aliases) {
    super(name, permission, aliases);
  }

  @Override
  public void execute(CommandSender commandSender, String[] strings) {

    if (!(commandSender instanceof ProxiedPlayer)) {
      commandSender.sendMessage(Configuration.commandSenderIsNotAProxiedPlayer());
    }

    ProxiedPlayer player = (ProxiedPlayer) commandSender;

    if (strings.length == 0) {

      UnlockSession unlockSession
        = UnlockManager.getInstance().getUnlockByWatcher(player.getUniqueId());

      if (unlockSession == null) {

        player.sendMessage(
          Configuration.transformForUnlocker("Du beobachtest zurzeit keine Freischaltung!"));

        return;
      }

      unlockSession.removeWatcher(player.getUniqueId());

      player.sendMessage(
        Configuration.transformForUnlocker("Du beobachtest die Freischaltung nicht mehr!"));

      return;
    }

    if (strings.length == 1) {

      User user = LuckPermsProvider.get().getUserManager().getUser(strings[0]);

      if (user == null) {

        player.sendMessage(
          Configuration.transformForUnlocker(
            "Diese Freischaltung kann nicht beobachtet werden!"));

        return;
      }

      UnlockSession unlockSession
        = UnlockManager.getInstance().getUnlockByTarget(user.getUniqueId());

      if (unlockSession == null) {

        player.sendMessage(
          Configuration.transformForUnlocker(
            "Diese Freischaltung kann nicht beobachtet werden!"));

        return;
      }

      unlockSession.addWatcher(player.getUniqueId());

      player.sendMessage(
        Configuration.transformForUnlocker("Du beobachtest nun die Freischaltung!"));

      return;
    }

    player.sendMessage(Configuration.usage(Configuration.Q.Watch.command() + " [Spieler]"));
  }

  @Override
  public Iterable<String> onTabComplete(CommandSender sender, String[] args) {

    if (args.length <= 1) {
      return UnlockManager.getInstance().getRunningUnlocks().stream()
        .map(UnlockSession::getTarget)
        .map((uuid) -> {
            return LuckPermsProvider.get().getUserManager().getUser(uuid).getUsername();
          })
        .filter((s) -> {
          return args.length == 1 ? s.startsWith(args[0]) : true;
        })
        .collect(Collectors.toList());
    }
    
    return ImmutableList.of();
  }
}
