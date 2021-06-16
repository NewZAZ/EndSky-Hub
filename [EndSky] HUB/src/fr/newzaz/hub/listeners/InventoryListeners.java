package fr.newzaz.hub.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.newzaz.hub.Hub;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListeners implements Listener {
    private final Hub plugin;

    public InventoryListeners(Hub plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        final Player p = (Player) e.getWhoClicked();

        if(e.getCurrentItem() == null)return;
        if(!e.getCurrentItem().hasItemMeta())return;
        e.setCancelled(true);

        if(e.getView().getTitle().equalsIgnoreCase("EndSky - Serveurs")){
            if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6❂ §e§lSkyBlock")){
                final ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF("skyblock");
                p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                p.closeInventory();
                p.getInventory().remove(Material.COMPASS);
                e.setCancelled(true);
            }
            e.setCancelled(true);
        }
    }
}
