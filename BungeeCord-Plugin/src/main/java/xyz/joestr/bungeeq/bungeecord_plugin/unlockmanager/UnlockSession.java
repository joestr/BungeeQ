/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.joestr.bungeeq.bungeecord_plugin.configuration.Configuration;

/**
 *
 * @author Joel
 */
public class UnlockSession {

    // the target of this unlock
    UUID target;

    String targetName;

    // the unlocker who finally unlock the target
    UUID unlocker;

    String unlockerName;

    // Stores the id of the current question
    Integer questionId;

    // Stores the messages
    Map<String, String> messages;

    // Stores the watcher of this session
    Set<UUID> watchers;

    LocalDateTime start;

    LocalDateTime end;

    UnlockStatus status;

    String notice = "";

    List<String> messageLog = new ArrayList<>();

    Boolean deletable = false;

    public UnlockSession(UUID target, UUID unlocker, Integer questionId) {

        this.target = target;
        this.targetName = ProxyServer.getInstance().getPlayer(target).getName();
        this.unlocker = unlocker;
        this.unlockerName = ProxyServer.getInstance().getPlayer(unlocker).getName();
        this.questionId = questionId;
        this.messages = new HashMap<>();
        this.watchers = new HashSet<>();
        this.status = UnlockStatus.RUNNING;
        this.start = LocalDateTime.now();
    }

    public void sendMessageToTargetAndUnlocker(UUID sender, String message) {

        if (target.equals(sender)) {

            if (("/" + Configuration.Exit.command()).equalsIgnoreCase(message)) {

                this.exit(true, "Abbrechen durch Gast");
                return;
            }
        }

        BaseComponent[] transformedMessages
            = Configuration.transformForUnlockSession(
                (sender != null
                    ? (sender.equals(target) ? targetName : unlockerName)
                    : "BungeeQ"),
                message
            );
        
        messageLog.add(TextComponent.toPlainText(transformedMessages));

        if (ProxyServer.getInstance().getPlayer(unlocker) != null) {
            ProxyServer
                .getInstance()
                .getPlayer(unlocker)
                .sendMessage(
                    transformedMessages
                );
        }

        if (ProxyServer.getInstance().getPlayer(target) != null) {
            ProxyServer
                .getInstance()
                .getPlayer(target)
                .sendMessage(
                    transformedMessages
                );
        }

        this.watchers
            .stream()
            .filter((watcher) -> {
                return ProxyServer
                    .getInstance()
                    .getPlayer(watcher) != null;
            })
            .forEach((watcher) -> {
                ProxyServer
                    .getInstance()
                    .getPlayer(watcher)
                    .sendMessage(
                        transformedMessages
                    );
            });

    }

    public void nextQuestion() {

        if (this.questionId >= UnlockManager.getInstance().getQuestions().size()) {

            ProxyServer.getInstance().getPlayer(unlocker)
                .sendMessage(
                    Configuration.transformForUnlocker(
                        "Es keine weiteren Fragen verfügbar!"
                    )
                );

            return;
        }

        this.sendMessageToTargetAndUnlocker(
            ProxyServer.getInstance().getPlayer(unlocker).getUniqueId(),
            UnlockManager.getInstance().getQuestions().get(questionId)
        );

        this.questionId++;
    }

    public void repeatQuestion() {

        this.sendMessageToTargetAndUnlocker(
            ProxyServer.getInstance().getPlayer(unlocker).getUniqueId(),
            UnlockManager.getInstance().getQuestions().get(questionId - 1)
        );
    }

    public void exit(Boolean asTarget) {

        this.sendMessageToTargetAndUnlocker(
            null,
            "Die Freischaltung wurde abgebrochen!"
        );

        UnlockManager
            .getInstance()
            .informUnlockers(
                Configuration.transformForUnlockers(
                    "Die Freischaltung von "
                    + targetName
                    + " wurde von "
                    + (asTarget ? targetName : unlockerName)
                    + " abgebrochen!"
                )
            );

        this.end = LocalDateTime.now();

        this.status = UnlockStatus.CANCELLED;

        this.deletable = true;

        UnlockManager.getInstance().deleteDeletableUnlocks();
    }

    public void exit(Boolean asTarget, String notice) {

        this.notice = notice;

        this.exit(asTarget);
    }

    public void decline() {

        this.sendMessageToTargetAndUnlocker(
            null,
            "Die Freischaltung wurde abgelehnt!"
        );

        UnlockManager
            .getInstance()
            .informUnlockers(
                Configuration.transformForUnlockers(
                    "Die Freischaltung von "
                    + targetName
                    + " wurde von "
                    + unlockerName
                    + " abgelehnt!"
                )
            );

        this.end = LocalDateTime.now();

        this.status = UnlockStatus.DECLINED;

        this.deletable = true;

        UnlockManager.getInstance().deleteDeletableUnlocks();
    }

    public void decline(String notice) {

        this.notice = notice;

        this.decline();
    }

    public boolean unlock() {

        if(this.questionId < UnlockManager.getInstance().getQuestions().size()) {
            ProxyServer.getInstance().getPlayer(unlocker)
                .sendMessage(
                    Configuration.transformForUnlocker(
                        "Du musst zuerst alle Fragen stellen!"
                    )
                );

            return false;
        }
        
        this.sendMessageToTargetAndUnlocker(
            null,
            "Die Freischaltung wurde angenommen!"
        );

        UnlockManager
            .getInstance()
            .informUnlockers(
                Configuration.transformForUnlockers(
                    "Die Freischaltung von "
                    + targetName
                    + " wurde von "
                    + unlockerName
                    + " angenommen!"
                )
            );

        User user
            = LuckPermsProvider
                .get()
                .getUserManager()
                .getUser(target);

        user.data().clear();

        Node n
            = Node.builder("group." + Configuration.unlockGroup()).build();

        user.data().add(n);

        user.setPrimaryGroup(Configuration.unlockGroup());

        LuckPermsProvider
            .get()
            .getUserManager()
            .saveUser(user);
        
        LuckPermsProvider.get().getMessagingService().get().pushUserUpdate(user);

        this.end = LocalDateTime.now();

        this.status = UnlockStatus.SUCCESSFUL;

        this.deletable = true;

        UnlockManager.getInstance().deleteDeletableUnlocks();
        
        return true;
    }

    public boolean unlock(String notice) {

        this.notice = notice;

        return this.unlock();
    }

    public UUID getTarget() {
        return target;
    }

    public UUID getUnlocker() {
        return unlocker;
    }

    public List<String> getMessageLog() {
        return messageLog;
    }

    public boolean addWatcher(UUID watcher) {

        return this.watchers.add(watcher);
    }

    public boolean removeWatcher(UUID watcher) {

        return this.watchers.remove(watcher);
    }

    @Deprecated
    public String unlockListString() {

        return targetName
            + " -> "
            + unlockerName;
    }
}
