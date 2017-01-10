package com.github.show0611.plugins.moreDifficult.enums;

import java.util.Date;

import org.bukkit.BanList;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.show0611.plugins.moreDifficult.Main;

public enum Penalty {
    KILL("Kill"), BAN("Ban"), TIME_BAN("TimeBan"), KICK("Kick"), DAMAGE("Damage"), CLEAR_INVENTORY(
            "ClearInventory"), TELEPORT("Teleport");

    private final String str;
    private BanList bl = Main.getSvr().getBanList(BanList.Type.NAME);

    private Penalty(String str) {
        this.str = str;
    }

    public void run(Player p, Object... objs) {
        switch (str) {
        case ("Kill"):
            p.performCommand("kill");
            break;

        case ("Ban"):
            bl.addBan(p.getName(), objs[0].toString(), null, null);
            break;

        case ("TimeBan"):
            bl.addBan(p.getName(), objs[0].toString(), (Date) objs[1], null);
            break;

        case ("Kick"):
            p.kickPlayer("You got a penalty.\nPlease relogin.");
            break;

        case ("Damage"):
            p.damage((Double) objs[0]);
            break;

        case ("ClearInventory"):
            p.getInventory().clear();
            break;

        case ("Teleport"):
            Location loc = p.getLocation();
            loc.setX((Double) objs[0]);
            loc.setY((Double) objs[1]);
            loc.setZ((Double) objs[2]);
            p.teleport(loc);
            break;

        default:
            break;
        }
    }
}
