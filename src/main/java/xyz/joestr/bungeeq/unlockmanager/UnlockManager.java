/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.unlockmanager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.lucko.luckperms.LuckPerms;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.DefinedPacket;
import xyz.joestr.bungeeq.configuration.Configuration;
import xyz.joestr.dbwrapper.DatabaseConnectionHandler;
import xyz.joestr.dbwrapper.DatabaseWrapper;

/**
 *
 * @author Joel
 */
public class UnlockManager {

    // Holds the current instance
    static UnlockManager instance = null;

    // Holds the available unlockers
    private List<UUID> availableUnlockers = new ArrayList<>();

    // Holds the players who want to get unlocked
    private Queue<UUID> unlockQueue = new LinkedList<>();

    // Holds the current runningUnlocks
    private List<UnlockSession> runningUnlocks = new ArrayList<>();

    // Holds a list of questions
    private List<String> questions = new ArrayList<>();

    DatabaseConnectionHandler databaseConnectionHandler = null;

    DatabaseWrapper<UnlockEntry> databaseWrapperUnlockEntry = null;

    private UnlockManager() {

        this.databaseConnectionHandler = new DatabaseConnectionHandler(
            Configuration.ConfigurationFileValues.connectionString()
        );

        try {
            this.databaseWrapperUnlockEntry
                = new DatabaseWrapper<>(
                    UnlockEntry.class,
                    this.databaseConnectionHandler
                );
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(UnlockManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.questions = Configuration.ConfigurationFileValues.quetions();
    }

    /**
     * Get the instace of the unlock manger.
     *
     * @return The unlock manager instance.
     */
    public static UnlockManager getInstance() {

        if (instance == null) {

            instance = new UnlockManager();
        }

        return instance;
    }

    public List<UnlockSession> getRunningUnlocks() {
        return runningUnlocks;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void queuePlayer(UUID target) {

        if (this.getUnlockByTarget(target) != null) {
            ProxyServer
                .getInstance()
                .getPlayer(target)
                .sendMessage(
                    Configuration.transformForTarget(
                        "Du bist zurzeit in einer Freischaltung!"
                    )
                );

            return;
        }

        if (this.unlockQueue.contains(target)) {

            ProxyServer
                .getInstance()
                .getPlayer(target)
                .sendMessage(
                    Configuration.transformForTarget(
                        "Du bist der Warteschlange bereits beigetreten!"
                    )
                );

            return;
        }

        if (this.unlockQueue.add(target)) {

            ProxyServer
                .getInstance()
                .getPlayer(target)
                .sendMessage(
                    Configuration.transformForTarget(
                        "Du bist der Warteschlange beigetreten!"
                    )
                );
        }

        this.informUnlockers(
            Configuration.transformForUnlockers(
                ProxyServer
                    .getInstance()
                    .getPlayer(target)
                    .getName()
                + " ist der Warteschlange beigetreten!"
            )
        );
    }

    public boolean unqueuePlayer(UUID unlocker) {

        if (this.unlockQueue.isEmpty()) {
            return false;
        }

        UUID target = this.unlockQueue.poll();

        this.runningUnlocks.add(new UnlockSession(target, unlocker, 0));

        this.informUnlockers(
            Configuration.transformForUnlockers(
                ProxyServer.getInstance().getPlayer(unlocker).getName()
                + " schaltet nun "
                + ProxyServer.getInstance().getPlayer(target).getName()
                + " frei."
            )
        );

        return true;
    }

    public UnlockSession getUnlockByUnlocker(UUID unlocker) {

        return this.runningUnlocks.stream().filter((unlock) -> {
            return unlock.unlocker.equals(unlocker);
        }).findFirst().orElse(null);
    }

    public UnlockSession getUnlockByTarget(UUID target) {
        return this.runningUnlocks.stream().filter((unlock) -> {
            return unlock.target.equals(target);
        }).findFirst().orElse(null);
    }

    public void informUnlockers(BaseComponent[] messages) {

        this.availableUnlockers.forEach((availableUnlocker) -> {

            if (ProxyServer.getInstance().getPlayer(availableUnlocker) != null) {

                ProxyServer
                    .getInstance()
                    .getPlayer(availableUnlocker)
                    .sendMessage(messages);
            }
        });
    }

    public void addPossibleUnlocker(UUID player) {

        if (!this.getPlayerFromUUID(player).hasPermission(Configuration.Informable.permission())) {
            return;
        }

        this.availableUnlockers.add(player);
    }

    public void removePossibleUnlocker(UUID player) {

        if (this.availableUnlockers.contains(player)) {

            this.availableUnlockers.remove(player);
        }
    }

    public void deleteDeletableUnlocks() {

        this.runningUnlocks
            .stream()
            .filter((unlock) -> unlock.deletable)
            .forEach((unlock) -> {

                try {
                    this.databaseWrapperUnlockEntry.insert(
                        new UnlockEntry(
                            unlock.target.toString(),
                            unlock.unlocker.toString(),
                            unlock.start,
                            unlock.end,
                            unlock.status.getValue(),
                            unlock.notice
                        )
                    );
                } catch (SQLException | NoSuchFieldException | IllegalAccessException ex) {
                    Logger.getLogger(UnlockManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        this.runningUnlocks.removeIf((unlock) -> unlock.deletable);
    }

    public List<UUID> getAvailableUnlockers() {
        return availableUnlockers;
    }

    public Collection<UnlockEntry> getHistoryOf(String playerName) {

        return this.getHistoryOf(this.getUUIDFromPlayerName(playerName));
    }

    public Collection<UnlockEntry> getHistoryOf(UUID targetUUID) {

        Collection<UnlockEntry> result = null;

        try {
            result = this.databaseWrapperUnlockEntry.select("target_uuid = '" + targetUUID + "'");
        } catch (NullPointerException | SQLException | InstantiationException | IllegalAccessException | NoSuchFieldException ex) {
            Logger.getLogger(UnlockManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public UnlockSession getUnlockByWatcher(UUID watcherUUID) {

        return this.runningUnlocks
            .stream()
            .filter((unlock) -> unlock.watchers.contains(watcherUUID))
            .findFirst()
            .orElse(null);
    }

    public Queue<UUID> getUnlockQueue() {

        return this.unlockQueue;
    }

    public List<UnlockSession> getUnlocks() {

        return this.runningUnlocks;
    }

    public void removePlayerFromQueue(UUID player) {

        this.unlockQueue.remove(player);
    }

    private ProxiedPlayer getPlayerFromUUID(UUID uuid) {

        return ProxyServer.getInstance().getPlayer(uuid);
    }

    public String getPlayerNameFromUUID(UUID uuid) {

        String result = null;

        try {
            result = LuckPerms.getApi().getUserManager().lookupUsername(uuid).get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public UUID getUUIDFromPlayerName(String playerName) {

        UUID result = null;

        try {
            result = LuckPerms.getApi().getUserManager().lookupUuid(playerName).get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public void reloadQuestions() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
