/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager;

import xyz.joestr.dbwrapper.annotations.WrappedField;
import xyz.joestr.dbwrapper.annotations.WrappedTable;

/**
 *
 * @author Joel
 */
@WrappedTable(name = "bq_logs")
public class LogEntry {
    
    @WrappedField(name = "unlock_id")
    Integer id;
    @WrappedField(name = "log")
    String log;

    public LogEntry() {
    }

    public LogEntry(Integer id, String log) {
        this.id = id;
        this.log = log;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
