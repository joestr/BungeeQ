/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.bungeecord_plugin.configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import xyz.joestr.bungeeq.bungeecord_plugin.BungeeQBungeeCordPlugin;
import xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager.UnlockManager;
import xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager.UnlockStatus;

/**
 *
 * @author Joel
 */
public class Configuration {

    public static class Q {

        public static class Get {

            public static String command() {

                return "qget";
            }

            public static String permission() {

                return "bungeeq.command.qget";
            }

            public static String alias() {

                return "qg";
            }
        }

        public static class Ask {

            public static String command() {

                return "qask";
            }

            public static String permission() {

                return "bungeeq.command.qask";
            }

            public static String alias() {

                return "qa";
            }
        }

        public static class Repeat {

            public static String command() {

                return "qrepeat";
            }

            public static String permission() {

                return "bungeeq.command.qrepeat";
            }

            public static String alias() {

                return "qr";
            }
        }

        public static class Chat {

            public static String command() {

                return "qchat";
            }

            public static String permission() {

                return "bungeeq.command.qchat";
            }

            public static String alias() {

                return "qc";
            }
        }

        public static class Exit {

            public static String command() {

                return "qexit";
            }

            public static String permission() {

                return "bungeeq.command.qexit";
            }

            public static String alias() {

                return "qe";
            }
        }

        public static class Decline {

            public static String command() {

                return "qdecline";
            }

            public static String permission() {

                return "bungeeq.command.qdecline";
            }

            public static String alias() {

                return "qd";
            }
        }

        public static class Unlock {

            public static String command() {

                return "qunlock";
            }

            public static String permission() {

                return "bungeeq.command.qunlock";
            }

            public static String alias() {

                return "qu";
            }
        }

        public static class History {

            public static String command() {

                return "qhistory";
            }

            public static String permission() {

                return "bungeeq.command.qhistory";
            }

            public static String alias() {

                return "qh";
            }
        }

        public static class Watch {

            public static String command() {

                return "qwatch";
            }

            public static String permission() {

                return "bungeeq.command.qwatch";
            }

            public static String alias() {

                return "qw";
            }
        }

        public static class List {

            public static String command() {

                return "qlist";
            }

            public static String permission() {

                return "bungeeq.command.qlist";
            }

            public static String alias() {

                return "ql";
            }
        }

        public static class Solution {

            public static String command() {

                return "qsolution";
            }

            public static String permission() {

                return "bungeeq.command.qsolution";
            }

            public static String alias() {

                return "qs";
            }
        }
    }

    public static class Activate {

        public static String command() {

            return "activate";
        }

        public static String permission() {

            return "bungeeq.command.activate";
        }

        public static String alias() {

            return "activate";
        }
    }

    public static class Exit {

        public static String command() {

            return "exit";
        }

        public static String permission() {

            return "bungeeq.command.exit";
        }

        public static String alias() {

            return "exit";
        }
    }

    public static class BungeeQ {

        public static String command() {

            return "bungeeq";
        }

        public static String permission() {

            return "bungeeq.command.bungeeq";
        }

        public static String alias() {

            return "bungeeq";
        }
    }

    public static String commandSenderIsNotAProxiedPlayer() {

        return "Only players can execute this command!";
    }

    @Deprecated
    public static String proxiedPlayerLacksPermission(String permission) {

        return "You are lacking permission " + permission + "!";
    }

    public static BaseComponent[] usage(String commandString) {

        return new ComponentBuilder("[Q] ")
            .color(ChatColor.DARK_AQUA)
            .append("Benutze: ")
            .color(ChatColor.RED)
            .append("/" + commandString)
            .color(ChatColor.GRAY)
            .create();
    }

    public static BaseComponent[] transformForUnlockSession(String sender, String message) {

        if (sender != null) {
            return new ComponentBuilder("[Q] ")
                .color(ChatColor.DARK_AQUA)
                .append(sender)
                .color(ChatColor.GRAY)
                .append(": ")
                .color(ChatColor.DARK_AQUA)
                .append(message)
                .color(ChatColor.AQUA)
                .create();
        }

        return new ComponentBuilder("[Q] ")
            .color(ChatColor.DARK_AQUA)
            .append(message)
            .color(ChatColor.AQUA)
            .create();
    }

    public static BaseComponent[] transformForUnlockers(String message) {

        return new ComponentBuilder("[Q] ")
            .color(ChatColor.DARK_AQUA)
            .append(message)
            .color(ChatColor.AQUA)
            .bold(true)
            .create();
    }

    public static BaseComponent[] transformForUnlocker(String message) {

        return new ComponentBuilder("[Q] ")
            .color(ChatColor.DARK_AQUA)
            .append(message)
            .color(ChatColor.AQUA)
            .create();
    }

    public static BaseComponent[] transformForTarget(String message) {

        return new ComponentBuilder("[Q] ")
            .color(ChatColor.DARK_AQUA)
            .append(message)
            .color(ChatColor.AQUA)
            .create();
    }

    @Deprecated
    public static String unlockGroup() {

        return ConfigurationFileValues.unlockGroup();
    }

    public static BaseComponent[] transformHistoryHead(String target) {

        return new ComponentBuilder("Freischaltgeschichte von ")
            .color(ChatColor.DARK_AQUA)
            .append(target)
            .color(ChatColor.GRAY)
            .append(":")
            .color(ChatColor.DARK_AQUA)
            .create();
    }

    public static BaseComponent[] transformHistoryBody(String unlocker, LocalDateTime start, LocalDateTime end, Integer status, String notice) {

        BaseComponent[] statusComponents = null;

        switch (UnlockStatus.values()[status]) {
            case RUNNING:
                statusComponents = new ComponentBuilder("Laufend")
                    .color(ChatColor.GRAY)
                    .create();
                break;
            case SUCCESSFUL:
                statusComponents = new ComponentBuilder("Erfolgreich")
                    .color(ChatColor.DARK_GREEN)
                    .create();
                break;
            case DECLINED:
                statusComponents = new ComponentBuilder("Abgelehnt")
                    .color(ChatColor.DARK_RED)
                    .create();
                break;
            case CANCELLED:
                statusComponents = new ComponentBuilder("Abgebrochen")
                    .color(ChatColor.GOLD)
                    .create();
                break;
            default:
                statusComponents = new ComponentBuilder("Unbekannt")
                    .color(ChatColor.GRAY)
                    .create();
                break;
        }

        return new ComponentBuilder(" ╔ Freischaltung bei ")
            .color(ChatColor.AQUA)
            .append(unlocker)
            .color(ChatColor.GRAY)
            .append("\n")
            .append(" ╠ Startzeitpunkt: ")
            .color(ChatColor.AQUA)
            .append(start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .color(ChatColor.GRAY)
            .append("\n")
            .append(" ╠ Endzeitpunkt: ")
            .color(ChatColor.AQUA)
            .append(end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .color(ChatColor.GRAY)
            .append("\n")
            .append(" ╠ Status: ")
            .color(ChatColor.AQUA)
            .append(statusComponents)
            .append("\n")
            .append(" ╚ Notiz: ")
            .color(ChatColor.AQUA)
            .append(notice)
            .color(ChatColor.GRAY)
            .create();
    }

    public static class ConfigurationFileValues {

        public static String connectionString() {

            return BungeeQBungeeCordPlugin.configuration.getString("connection_string");
        }

        public static List<String> quetions() {

            return BungeeQBungeeCordPlugin.configuration.getStringList("questions");
        }

        public static String unlockGroup() {

            return BungeeQBungeeCordPlugin.configuration.getString("unlock_group");
        }
        
        public static boolean sendGlobalMessage() {
          return BungeeQBungeeCordPlugin.configuration.getBoolean("send_global_message");
        }
        
        public static String globalMessage() {
          return BungeeQBungeeCordPlugin.configuration.getString("global_message");
        }
    }

    public static class Informable {

        public static String permission() {

            return "bungeeq.informable";
        }
    }

    public static BaseComponent[] unlockList() {

        return new ComponentBuilder(
            "[Q] "
        )
            .color(ChatColor.DARK_AQUA)
            .append("Es sind zurzeit ")
            .color(ChatColor.AQUA)
            .append(String.valueOf(UnlockManager.getInstance().getUnlockQueue().size()))
            .color(ChatColor.GRAY)
            .append(" Gäste in der Warteschlange:\n")
            .color(ChatColor.AQUA)
            .append(
                UnlockManager.getInstance()
                    .getUnlockQueue()
                    .stream()
                    .map(
                        (target) -> UnlockManager.getInstance().getPlayerNameFromUUID(target)
                    )
                    .collect(Collectors.joining(", ", "", ""))
            )
            .color(ChatColor.GRAY)
            .append("\n")
            .append("[Q] ")
            .color(ChatColor.DARK_AQUA)
            .append(
                "Es laufen zurzeit "
            )
            .color(ChatColor.AQUA)
            .append(UnlockManager.getInstance().getUnlocks().size() + "")
            .color(ChatColor.GRAY)
            .append(" Freischaltungen:\n")
            .color(ChatColor.AQUA)
            .append(
                UnlockManager.getInstance()
                    .getUnlocks()
                    .stream()
                    .map((name) -> name.unlockListString())
                    .collect(Collectors.joining("\n"))
            )
            .color(ChatColor.GRAY)
            .create();
    }
    
    public static BaseComponent[] transformModList(Map<String, String> modList) {
        
        ComponentBuilder result = new ComponentBuilder(
            ""
        );
        
        modList.forEach((mod, version) -> {
            
            result.append(mod);
            result.color(ChatColor.AQUA);
            result.append("("+ version + ")");
            result.color(ChatColor.GRAY);
            result.append(", ");
            result.color(ChatColor.DARK_AQUA);
        });
        
        return result.create();
    }
}
