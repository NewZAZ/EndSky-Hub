package fr.newzaz.hub.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityListeners implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {event.setCancelled(true); }

}
