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

package space.x4yr;
import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import space.x4yr.alert.AlertManager;
import space.x4yr.checks.AICheck;
import space.x4yr.commands.CommandHandler;
import space.x4yr.compat.VersionAdapter;
import space.x4yr.config.Config;
import space.x4yr.datacollector.DataCollectorFactory;
import space.x4yr.listeners.HitListener;
import space.x4yr.listeners.PlayerListener;
import space.x4yr.listeners.RotationListener;
import space.x4yr.listeners.TeleportListener;
import space.x4yr.listeners.TickListener;
import space.x4yr.scheduler.SchedulerManager;
import space.x4yr.server.AIClientProvider;
import space.x4yr.session.ISessionManager;
import space.x4yr.session.SessionManager;
import space.x4yr.violation.ViolationManager;
import space.x4yr.util.FeatureCalculator;
import space.x4yr.util.UpdateChecker;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
public final class Main extends JavaPlugin {
    private Config config;
    private ISessionManager sessionManager;
    private FeatureCalculator featureCalculator;
    private TickListener tickListener;
    private HitListener hitListener;
    private RotationListener rotationListener;
    private PlayerListener playerListener;
    private TeleportListener teleportListener;
    private CommandHandler commandHandler;
    private AIClientProvider aiClientProvider;
    private AlertManager alertManager;
    private ViolationManager violationManager;
    private AICheck aiCheck;
    private UpdateChecker updateChecker;
    @Override
    public void onLoad() {
        VersionAdapter.init(getLogger());
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings()
            .reEncodeByDefault(false)
            .checkForUpdates(false)
            .bStats(false)
            .debug(false);
        PacketEvents.getAPI().load();
    }
    @Override
    public void onEnable() {
        try {
            SchedulerManager.initialize(this);
            getLogger().info("SchedulerManager initialized for " + SchedulerManager.getServerType());
        } catch (Exception e) {
            getLogger().severe("Failed to initialize SchedulerManager: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        PacketEvents.getAPI().init();
        VersionAdapter.get().logCompatibilityInfo();
        saveDefaultConfig();
        this.config = new Config(this, getLogger());
        String outDir = config.getOutputDirectory();
        if (outDir == null || outDir.isEmpty()) outDir = "data";
        File outputDir = (new File(outDir).isAbsolute())
            ? new File(outDir)
            : new File(getDataFolder(), outDir);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        this.featureCalculator = new FeatureCalculator();
        this.sessionManager = DataCollectorFactory.createSessionManager(this);
        this.aiClientProvider = new AIClientProvider(this, config);
        this.alertManager = new AlertManager(this, config);
        this.violationManager = new ViolationManager(this, config, alertManager);
        this.aiCheck = new AICheck(this, config, aiClientProvider, alertManager, violationManager);
        this.violationManager.setAICheck(aiCheck);
        if (config.isAiEnabled()) {
            aiClientProvider.initialize().thenAccept(success -> {
                if (success) {
                    getLogger().info("[AI] Connected to " + config.getServerAddress());
                } else {
                    getLogger().warning("[AI] Failed to connect to inference server");
                }
            });
        }
        this.tickListener = new TickListener(this, sessionManager, aiCheck);
        this.hitListener = new HitListener(sessionManager, aiCheck);
        this.rotationListener = new RotationListener(sessionManager, aiCheck);
        this.playerListener = new PlayerListener(this, aiCheck, alertManager, violationManager, 
            sessionManager instanceof SessionManager ? (SessionManager) sessionManager : null);
        this.teleportListener = new TeleportListener(aiCheck);
        this.tickListener.setHitListener(hitListener);
        this.playerListener.setHitListener(hitListener);
        this.hitListener.cacheOnlinePlayers();
        this.tickListener.start();
        getServer().getPluginManager().registerEvents(playerListener, this);
        getServer().getPluginManager().registerEvents(teleportListener, this);
        PacketEvents.getAPI().getEventManager().registerListener(hitListener);
        PacketEvents.getAPI().getEventManager().registerListener(rotationListener);
        this.commandHandler = new CommandHandler(sessionManager, alertManager, aiCheck, this);
        PluginCommand command = getCommand("xac");
        if (command != null) {
            command.setExecutor(commandHandler);
            command.setTabCompleter(commandHandler);
        }
        getLogger().info("X4yrAC enabled successfully!");
        getLogger().info("Data collector: ENABLED (output: " + config.getOutputDirectory() + ")");
        if (config.isAiEnabled()) {
            getLogger().info("AI detection: ENABLED (threshold: " + config.getAiAlertThreshold() + ")");
        } else {
            getLogger().info("AI detection: DISABLED");
        }

        this.updateChecker = new UpdateChecker(this);
        updateChecker.checkForUpdates().thenAccept(available -> {
            if (available) {
                getLogger().warning("=================================================");
                getLogger().warning("A NEW UPDATE IS AVAILABLE: " + updateChecker.getLatestVersion());
                getLogger().warning("Get it from GitHub: " + updateChecker.getReleasesUrl());
                getLogger().warning("=================================================");
            }
        });
    }
    @Override
    public void onDisable() {
        if (tickListener != null) {
            tickListener.stop();
        }
        if (sessionManager != null) {
            getLogger().info("Stopping all active sessions...");
            sessionManager.stopAllSessions();
        }
        if (aiCheck != null) {
            aiCheck.clearAll();
        }
        if (violationManager != null) {
            violationManager.shutdown();
        }
        if (commandHandler != null) {
            commandHandler.cleanup();
        }
        if (aiClientProvider != null && aiClientProvider.isAvailable()) {
            getLogger().info("Shutting down SignalR client...");
            try {
                aiClientProvider.shutdown().get(5, java.util.concurrent.TimeUnit.SECONDS);
            } catch (Exception e) {
                getLogger().warning("Error shutting down SignalR client: " + e.getMessage());
            }
        }
        PacketEvents.getAPI().terminate();
        getLogger().info("X4yrAC disabled successfully!");
    }
    public void reloadPluginConfig() {
        SchedulerManager.getAdapter().runSync(() -> {
            try {
                reloadConfig();
                this.config = new Config(this, getLogger());
                alertManager.setConfig(config);
                violationManager.setConfig(config);
                aiCheck.setConfig(config);
                if (aiClientProvider != null) {
                    aiClientProvider.setConfig(config);
                    if (config.isAiEnabled()) {
                        aiClientProvider.reload().thenAccept(success -> {
                            if (success) {
                                getLogger().info("[AI] Reconnected to " + config.getServerAddress());
                            }
                        });
                    } else {
                        aiClientProvider.shutdown();
                    }
                }
                getLogger().info("Configuration reloaded!");
            } catch (Exception e) {
                getLogger().severe("Failed to reload configuration: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    public Config getPluginConfig() {
        return config;
    }
    public ISessionManager getSessionManager() {
        return sessionManager;
    }
    public FeatureCalculator getFeatureCalculator() {
        return featureCalculator;
    }
    public AICheck getAiCheck() {
        return aiCheck;
    }
    public AlertManager getAlertManager() {
        return alertManager;
    }
    public ViolationManager getViolationManager() {
        return violationManager;
    }
    public AIClientProvider getAiClientProvider() {
        return aiClientProvider;
    }
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
    public void debug(String message) {
        if (config != null && config.isDebug()) {
            getLogger().info("[Debug] " + message);
        }
    }
}