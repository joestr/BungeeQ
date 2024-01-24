package xyz.joestr.bungeeq.listeners;

import java.util.List;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.joestr.bungeeq.bungeecord_plugin.BungeeQBungeeCordPlugin;
import xyz.joestr.bungeeq.bungeecord_plugin.configuration.Configuration;
import xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager.UnlockManager;
import xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager.UnlockSession;

/**
 *
 * @author SamyPro
 */
public class TabComplete implements Listener {

    private BungeeQBungeeCordPlugin pl;

    public TabComplete(BungeeQBungeeCordPlugin plugin) {
        this.pl = plugin;
	}

	@EventHandler
    public void onTabComplete(TabCompleteEvent event) {

        String[] args = event.getCursor().split(" ");

        List<String> sug = event.getSuggestions();

        // args[1] = first arg 
        if (args.length == 1) {
            // -/bungeeq <reload|update>
            if(event.getCursor().startsWith("/" + Configuration.BungeeQ.command()) || event.getCursor().startsWith("/" + Configuration.BungeeQ.alias())) {
                sug.add("reload");
                sug.add("update");
                return;
            }

            // -/qhistory <Proxyplayer playername>
            if(event.getCursor().startsWith("/" + Configuration.Q.History.command()) || event.getCursor().startsWith("/" + Configuration.Q.History.alias())) {
                for (ProxiedPlayer player : pl.getProxy().getPlayers()) {
                    sug.add(player.getName());
                }
                return;
            }

            // -/qwatch <RunningUnlocks playername>
            if(event.getCursor().startsWith("/" + Configuration.Q.Watch.command()) || event.getCursor().startsWith("/" + Configuration.Q.Watch.alias())) {
                List<UnlockSession> sessions = UnlockManager.getInstance().getRunningUnlocks();
                for (UnlockSession session : sessions) {
                    ProxiedPlayer p = pl.getProxy().getPlayer(session.getTarget());
                    sug.add(p.getName());
                }
                return;
            }
        }



    }
}