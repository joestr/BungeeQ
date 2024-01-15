/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.bungeecord_plugin.commands;

import com.google.common.collect.ImmutableList;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.luckperms.api.LuckPermsProvider;
import xyz.joestr.bungeeq.bungeecord_plugin.configuration.Configuration;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager.UnlockEntry;
import xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager.UnlockManager;
import xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager.UnlockSession;

/**
 *
 * @author Joel
 */
public class CommandQHistory extends Command implements TabExecutor {

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

        for (UnlockEntry unlock : unlockHistory) {

          LuckPermsProvider.get()
            .getUserManager()
            .lookupUsername(UUID.fromString(unlock.getUnlocker()))
            .whenComplete((name, throwable) -> {

              player.sendMessage(
                Configuration.transformHistoryBody(
                  throwable == null ? name : unlock.getUnlocker(),
                  unlock.getStart(),
                  unlock.getEnd(),
                  unlock.getStatus(),
                  unlock.getNotice()
                )
              );
            });
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

  @Override
  public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
    if (args.length <= 1) {
      return ProxyServer.getInstance()
        .getPlayers().stream()
        .map(ProxiedPlayer::getName)
        .filter((s) ->{
          return args.length == 1 ? s.startsWith(args[0]) : true;
        })
        .collect(Collectors.toList());
    }

    return ImmutableList.of();
  }
}
