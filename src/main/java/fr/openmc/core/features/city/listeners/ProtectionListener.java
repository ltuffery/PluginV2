package fr.openmc.core.features.city.listeners;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import com.sk89q.worldedit.math.BlockVector2;
import fr.openmc.core.features.city.City;
import fr.openmc.core.features.city.CityManager;
import fr.openmc.core.utils.messages.MessageType;
import fr.openmc.core.utils.messages.MessagesManager;
import fr.openmc.core.utils.messages.Prefix;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.Nullable;

public class ProtectionListener implements Listener {

    private boolean isMemberOf(@Nullable City city, Player player) {
        if (city == null) {
            return true;
        }

        return city.getMembers().contains(player.getUniqueId());
    }

    @Nullable
    private City getCityByChunk(Chunk chunk) {
        for (City city: CityManager.getCities()) {
            if (city.getChunks().contains(BlockVector2.at(chunk.getX(), chunk.getZ()))) {
                return city;
            }
        }
        return null;
    }

    private void verify(Player player, Cancellable event) {
        City city = getCityByChunk(player.getChunk());
        City cityz = CityManager.getPlayerCity(player.getUniqueId());

        if (isMemberOf(city, player)) return;
        if (cityz!=null){
            String city_type = CityManager.getCityType(city.getUUID());
            String cityz_type = CityManager.getCityType(cityz.getUUID());
            if (city_type!=null && cityz_type!=null){
                if (city_type.equals("war") && cityz_type.equals("war")){
                    return;
                }
            }
        }
        event.setCancelled(true);

        MessagesManager.sendMessage(player, Component.text("Vous n'avez pas l'autorisation de faire ceci !"), Prefix.CITY, MessageType.ERROR, true);
    }

    @EventHandler
    void onBlockBreak(BlockBreakEvent event) { verify(event.getPlayer(), event); }

    @EventHandler
    void onInteract(PlayerInteractEvent event) { verify(event.getPlayer(), event); }

    @EventHandler
    void onInteractAtEntity(PlayerInteractAtEntityEvent event) { verify(event.getPlayer(), event); }

    @EventHandler
    void onInteractEntity(PlayerInteractEntityEvent event) { verify(event.getPlayer(), event); }

    @EventHandler
    void onFish(PlayerFishEvent event) { verify(event.getPlayer(), event); }

    @EventHandler
    void onShear(PlayerShearEntityEvent event) { verify(event.getPlayer(), event); }

    @EventHandler
    void onLeash(PlayerLeashEntityEvent event) { verify(event.getPlayer(), event); }

    @EventHandler
    void onUnleash(PlayerUnleashEntityEvent event) { verify(event.getPlayer(), event); }

    @EventHandler
    void onLaunchProjectile(PlayerLaunchProjectileEvent event) { verify(event.getPlayer(), event); }

    @EventHandler
    void onPlaceBlock(BlockPlaceEvent event) { verify(event.getPlayer(), event); }
}