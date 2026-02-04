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

package space.x4yr.penalty.handlers;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import space.x4yr.penalty.ActionHandler;
import space.x4yr.penalty.ActionType;
import space.x4yr.penalty.PenaltyContext;
import space.x4yr.scheduler.SchedulerManager;
public class KickHandler implements ActionHandler {
    private final JavaPlugin plugin;
    public KickHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public void handle(String command, PenaltyContext context) {
        if (command == null || command.isEmpty()) {
            return;
        }
        SchedulerManager.getAdapter().runSync(() -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
    }
    @Override
    public ActionType getActionType() {
        return ActionType.KICK;
    }
}