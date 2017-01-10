package com.github.show0611.plugins.moreDifficult.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.show0611.plugins.moreDifficult.Main;
import com.github.show0611.plugins.moreDifficult.constructors.SettingsData;

public class EventListener implements Listener {
    private static List<Player> killers = new ArrayList<>();
    private SettingsData sd = new SettingsData();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        Location loc1 = event.getFrom(), loc2 = event.getTo();
        Location ploc = p.getLocation();
        ploc.subtract(0, 1, 0);

        if (!sd.canMove) penalty(p);
        if (!sd.canWalk && loc1.getY() == loc2.getY()) penalty(p);
        if (!sd.canSwim && ploc.getBlock().getType().equals(Material.WATER)) penalty(p);
        ploc.subtract(0, 2, 0);
        if (!sd.canFall && loc1.getY() != loc2.getY() && ploc.getBlock().getType().equals(Material.AIR)) penalty(p);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!sd.canInteract) penalty(event.getPlayer());
    }

    @EventHandler
    public void onPlayerStatisticIncrement(PlayerStatisticIncrementEvent event) {
        Player p = event.getPlayer();
        if (!sd.canStatisticIncrement) penalty(p);
        if (!sd.canJump && event.getStatistic() == Statistic.JUMP) penalty(p);
    }

    @EventHandler
    public void onPlayerAchievementAwarded(PlayerAchievementAwardedEvent event) {
        if (!sd.canAwardAchevement) penalty(event.getPlayer());
    }

    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent event) {
        if (!sd.canDropItem) penalty(event.getPlayer());
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (!sd.canItemConsume) penalty(event.getPlayer());
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        if (!sd.canSwitchHotbar) penalty(event.getPlayer());
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        System.out.println(!sd.canExpChange);
        if (!sd.canExpChange) penalty(event.getPlayer());
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (!sd.canPickupDropItem) penalty(event.getPlayer());
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!sd.canEntityKill) killers.forEach(p -> penalty(p));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player p = (Player) event.getDamager();
        killers.add(p);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player pl : killers) {
                    if (pl.equals(p)) killers.remove(pl);
                }
            }
        }.runTaskLater(Main.instance, 10);
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
    public void onCraftItem(CraftItemEvent event) {
        if (!sd.canCraft) penalty((Player) event.getWhoClicked());
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
        if (!sd.canOpenChest && event.getInventory().getType().equals(InventoryType.CHEST))
            penalty((Player) event.getPlayer());
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

    @EventHandler
    public void onPlayerItemBreak(PlayerItemBreakEvent event) {
        if (!sd.canItemBreak) penalty(event.getPlayer());
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
