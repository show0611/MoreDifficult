package com.github.show0611.plugins.moreDifficult.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.github.show0611.plugins.moreDifficult.constructors.SettingsData;

public class EventListener implements Listener {
    private SettingsData sd = new SettingsData();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        Location loc1 = event.getFrom(), loc2 = event.getTo();
        Location ploc = p.getLocation();
        ploc.subtract(0, 1, 0);

        if (!sd.canMove) penalty(p);
        if (!sd.canWalk && (loc1.getBlockX() == loc2.getBlockX() || loc1.getBlockY() == loc2.getBlockY()
                || loc1.getBlockZ() == loc2.getBlockZ()))
            penalty(p);
        if (!sd.canSwim && ploc.getBlock().getType().equals(Material.STATIONARY_WATER)) penalty(p);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!sd.canInteract) penalty(event.getPlayer());
    }

    @EventHandler
    public void onPlayerStatisticIncrement(PlayerStatisticIncrementEvent event) {
        Player p = event.getPlayer();
        String s = event.getStatistic().toString();

        if (!sd.canStatisticIncrement) penalty(p);
        if (!sd.canJump && s.equals("JUMP")) penalty(p);
        if (!sd.canCraft && s.equals("CRAFT_ITEM")) penalty(p);
        if (!sd.canEntityKill && s.equals("ENTITY_KILL")) penalty(p);
        if (!sd.canDropItem && s.equals("DROP")) penalty(event.getPlayer());
        if (!sd.canItemBreak && s.equals("BREAK_ITEM")) penalty(event.getPlayer());
        if (!sd.canItemConsume && s.equals("USE_ITEM")) penalty(event.getPlayer());
        if (!sd.canPickupDropItem && s.equals("PICKUP")) penalty(event.getPlayer());
        if (!sd.canOpenChest && (s.equals("CHEST_OPENED") || s.equals("ENDERCHEST_OPENED")))
            penalty((Player) event.getPlayer());
    }

    @EventHandler
    public void onPlayerAchievementAwarded(PlayerAchievementAwardedEvent event) {
        if (!sd.canAwardAchevement) penalty(event.getPlayer());
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        if (!sd.canSwitchHotbar) penalty(event.getPlayer());
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        if (!sd.canExpChange) penalty(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!sd.canEntityInteract) penalty(event.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!sd.canInventoryClick) penalty((Player) event.getWhoClicked());
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (!sd.canSneak) penalty(event.getPlayer());
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (!sd.canBedEnter) penalty(event.getPlayer());
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!sd.canOpenInventory) penalty((Player) event.getPlayer());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!sd.canPlaceBlock) penalty(event.getPlayer());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!sd.canBreakBlock) penalty(event.getPlayer());
    }

    public void penalty(Player p) {
        sd.penalties.forEach(penalty -> {
            switch (penalty) {
            case KILL:
                penalty.run(p);
                break;

            case BAN:
                penalty.run(p, sd.reason);
                break;

            case TIME_BAN:
                penalty.run(p, sd.reason, sd.time);
                break;

            case KICK:
                penalty.run(p);
                return;

            case DAMAGE:
                penalty.run(p, sd.damage);
                break;

            case CLEAR_INVENTORY:
                penalty.run(p);
                break;

            case TELEPORT:
                Double[] d = sd.loc.toArray(new Double[0]);
                penalty.run(p, d[0], d[1], d[2]);

            default:
                break;
            }
        });
    }
}
