/*
 * This file is part of X4yrAC - AI powered Anti-Cheat
 * Copyright (C) 2026 X4yrAC Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * This file contains code derived from:
 *   - SlothAC © 2025 KaelusMC, https://github.com/KaelusMC/SlothAC
 *   - Grim © 2025 GrimAnticheat, https://github.com/GrimAnticheat/Grim
 *   - client-side © 2025 MLSAC, https://github.com/MLSAC/client-side/
 * All derived code is licensed under GPL-3.0.
 */

package space.x4yr.listeners;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import space.x4yr.Main;
import space.x4yr.Permissions;
import space.x4yr.alert.AlertManager;
import space.x4yr.checks.AICheck;
import space.x4yr.scheduler.SchedulerManager;
import space.x4yr.session.SessionManager;
import space.x4yr.violation.ViolationManager;
public class PlayerListener implements Listener {
    private final JavaPlugin plugin;
    private final AICheck aiCheck;
    private final AlertManager alertManager;
    private final ViolationManager violationManager;
    private final SessionManager sessionManager;
    private HitListener hitListener;
    public PlayerListener(JavaPlugin plugin, AICheck aiCheck, AlertManager alertManager, 
                          ViolationManager violationManager, SessionManager sessionManager) {
        this.plugin = plugin;
        this.aiCheck = aiCheck;
        this.alertManager = alertManager;
        this.violationManager = violationManager;
        this.sessionManager = sessionManager;
    }
    public void setHitListener(HitListener hitListener) {
        this.hitListener = hitListener;
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (hitListener != null) {
            hitListener.cacheEntity(player);
        }
        // Check for updates notification
        try {
            SchedulerManager.getAdapter().runSyncDelayed(() -> {
                if (player.isOnline()) {
                    if (player.hasPermission(Permissions.ALERTS) || player.hasPermission(Permissions.ADMIN)) {
                        if (plugin instanceof Main) {
                            Main main = (Main) plugin;
                            if (main.getUpdateChecker() != null && main.getUpdateChecker().isUpdateAvailable()) {
                                player.sendMessage(ChatColor.GOLD + "=================================================");
                                player.sendMessage(ChatColor.YELLOW + "A NEW X4yrAC UPDATE IS AVAILABLE: " + ChatColor.WHITE + main.getUpdateChecker().getLatestVersion());
                                player.sendMessage(ChatColor.YELLOW + "Get it from GitHub: " + ChatColor.AQUA + main.getUpdateChecker().getReleasesUrl());
                                player.sendMessage(ChatColor.GOLD + "=================================================");
                            }
                        }
                    }
                }
            }, 20L);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to schedule player join task: " + e.getMessage());
        }
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        handlePlayerLeave(event.getPlayer());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {
        handlePlayerLeave(event.getPlayer());
    }
    private void handlePlayerLeave(Player player) {
        if (hitListener != null) {
            hitListener.uncachePlayer(player);
        }
        if (aiCheck != null) {
            aiCheck.handlePlayerQuit(player);
        }
        if (alertManager != null) {
            alertManager.handlePlayerQuit(player);
        }
        if (violationManager != null) {
            violationManager.handlePlayerQuit(player);
        }
        if (sessionManager != null) {
            sessionManager.removeAimProcessor(player.getUniqueId());
        }
    }
}