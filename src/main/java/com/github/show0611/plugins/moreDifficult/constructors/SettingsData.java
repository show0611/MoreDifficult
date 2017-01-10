package com.github.show0611.plugins.moreDifficult.constructors;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import com.github.show0611.plugins.moreDifficult.Main;
import com.github.show0611.plugins.moreDifficult.enums.Penalty;

public class SettingsData {
    // Settings
    public final Date time;
    public final String reason;
    public final Double damage;
    public final List<Double> loc;
    public final List<Penalty> penalties = new ArrayList<>();

    // Highest
    public final boolean canMove;
    public final boolean canInteract;
    public final boolean canStatisticIncrement;

    // High
    public final boolean canDropItem;
    public final boolean canExpChange;
    public final boolean canEntityKill;
    public final boolean canItemConsume;
    public final boolean canPickupDropItem;
    public final boolean canEntityInteract;
    public final boolean canInventoryClick;
    public final boolean canAwardAchevement;

    // Normal
    public final boolean canWalk;
    public final boolean canSwim;
    public final boolean canFall;
    public final boolean canJump;
    public final boolean canSneak;
    public final boolean canCraft;
    public final boolean canBedEnter;
    public final boolean canOpenChest;
    public final boolean canItemBreak;
    public final boolean canBreakBlock;
    public final boolean canPlaceBlock;
    public final boolean canSwitchHotbar;
    public final boolean canOpenInventory;

    public SettingsData() {
        FileConfiguration conf = Main.getPlugin().getConfig();
        Date t = null;

        // Settings
        try {
            for (String str : conf.getStringList("Settings.Penalty")) {
                penalties.add(Penalty.valueOf(str));
            }
            t = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss").parse(conf.getString("Settings.Time"));
        } catch (Exception e) {
        }
        time = t;
        reason = conf.getString("Settings.Reason");
        damage = conf.getDouble("Settings.Damage");
        loc = conf.getDoubleList("Settings.Location");

        // Highest
        canMove = conf.getBoolean("Difficulty.Highest.canMove");
        canInteract = conf.getBoolean("Difficulty.Highest.canInteract");
        canStatisticIncrement = conf.getBoolean("Difficulty.Highest.canStatisticIncrement");

        // High
        canDropItem = conf.getBoolean("Difficulty.High.canDropItem");
        canExpChange = conf.getBoolean("Difficulty.High.canExpChange");
        canEntityKill = conf.getBoolean("Difficulty.High.canEntityKill");
        canItemConsume = conf.getBoolean("Difficulty.High.canItemConsume");
        canPickupDropItem = conf.getBoolean("Difficulty.High.canPickupDropItem");
        canEntityInteract = conf.getBoolean("Difficulty.High.canEntityInteract");
        canInventoryClick = conf.getBoolean("Difficulty.High.canInventoryClick");
        canAwardAchevement = conf.getBoolean("Difficulty.High.canAwardAchevement");

        // Normal
        canJump = conf.getBoolean("Difficulty.Normal.canJump");
        canFall = conf.getBoolean("Difficulty.Normal.canFall");
        canSwim = conf.getBoolean("Difficulty.Normal.canSwim");
        canWalk = conf.getBoolean("Difficulty.Normal.canWalk");
        canSneak = conf.getBoolean("Difficulty.Normal.canSneak");
        canCraft = conf.getBoolean("Difficulty.Normal.canCraft");
        canBedEnter = conf.getBoolean("Difficulty.Normal.canBedEnter");
        canOpenChest = conf.getBoolean("Difficulty.Normal.canOpenChest");
        canItemBreak = conf.getBoolean("Difficulty.Normal.canItemBreak");
        canBreakBlock = conf.getBoolean("Difficulty.Normal.canBreakBlock");
        canPlaceBlock = conf.getBoolean("Difficulty.Normal.canPlaceBlock");
        canSwitchHotbar = conf.getBoolean("Difficulty.Normal.canSwitchHotbar");
        canOpenInventory = conf.getBoolean("Difficulty.Normal.canOpenInventory");
    }
}
