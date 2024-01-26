/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.time.LocalDateTime;

/**
 *
 * @author Joel
 */
@DatabaseTable(tableName = "unlockentry")
public class UnlockEntry {

  @DatabaseField(generatedId = true)
  Integer id;
  @DatabaseField(canBeNull = false)
  String target;
  @DatabaseField(canBeNull = false)
  String unlocker;
  @DatabaseField(canBeNull = false)
  LocalDateTime start;
  @DatabaseField(canBeNull = false)
  LocalDateTime end;
  @DatabaseField(canBeNull = false)
  Integer status;
  @DatabaseField(canBeNull = false)
  String notice;

  public UnlockEntry() {

  }

  public UnlockEntry(Integer id, String target, String unlocker, LocalDateTime start, LocalDateTime end, Integer status, String notice) {
    this.id = id;
    this.target = target;
    this.unlocker = unlocker;
    this.start = start;
    this.end = end;
    this.status = status;
    this.notice = notice;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTarget() {
    return target;
  }

  public String getUnlocker() {
    return unlocker;
  }

  public LocalDateTime getStart() {
    return start;
  }

  public LocalDateTime getEnd() {
    return end;
  }

  public Integer getStatus() {
    return status;
  }

  public String getNotice() {
    return notice;
  }
}
