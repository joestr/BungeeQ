/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.joestr.bungeeq.bungeecord_plugin.unlockmanager;

/**
 *
 * @author Joel
 */
public enum UnlockStatus {

    RUNNING(0),
    SUCCESSFUL(1),
    CANCELLED(2),
    DECLINED(3);

    private final int value;

    private UnlockStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
