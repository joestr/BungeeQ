/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author Joel
 */
@DatabaseTable(tableName = "logentry")
public class LogEntry {
    
    @DatabaseField(generatedId = true)
    Integer id;
    @DatabaseField(canBeNull = false)
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
