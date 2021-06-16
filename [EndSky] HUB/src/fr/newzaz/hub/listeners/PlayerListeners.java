package fr.newzaz.hub.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.lielamar.auth.bukkit.TwoFactorAuthentication;
import com.lielamar.auth.shared.handlers.AuthHandler;
import fr.newzaz.hub.Hub;
import fr.newzaz.hub.utils.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.imageio.spi.RegisterableService;
import java.util.Arrays;
import java.util.Objects;

public class PlayerListeners implements Listener {
    private final Hub plugin;

    public PlayerListeners(Hub plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if(event.getTo().getY() >=0)return;

        event.getPlayer().teleport(new Location(Bukkit.getWorld("Lobby"),61,191, -37));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        final Player p = e.getPlayer();
        p.setPlayerListName("§7[§eHUB§7] §e"+p.getName());
        for(Player players : Bukkit.getOnlinePlayers()){
            p.hidePlayer(players);
            players.hidePlayer(p);
        }
        p.setInvulnerable(true);
        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,2));
        ItemBuilder item = new ItemBuilder(Material.COMPASS).setDisplayName("§2Navigateur").setLoreWithList(Arrays.asList("§7Cliquez pour rejoindre le SkyBlock."));
        p.getInventory().clear();
        p.getInventory().setItem(4,item.build());
        p.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
        e.setJoinMessage(null);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        p.sendMessage(ChatColor.RED + "Le chat est désactivé ici.");
        e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event){
        final Player p = event.getPlayer();
        if(event.getHand() != EquipmentSlot.HAND)return;
        if(!p.getInventory().getItemInMainHand().hasItemMeta())return;
        if(Objects.requireNonNull(p.getInventory().getItemInMainHand().getItemMeta()).getLore() == null)return;

        if(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§2Navigateur")){
        if(TwoFactorAuthentication.getAuthHandler().is2FAEnabled(p.getUniqueId())){
            if(TwoFactorAuthentication.getAuthHandler().getAuthState(p.getUniqueId()) == AuthHandler.AuthState.AUTHENTICATED){
                openGUI(p);
            }else {
                p.sendMessage("§6End§eSky §8- §cErreur : Veuillez vous identifier !");
            }
        }else {
                openGUI(p);
            }
        }
    }

    private void openGUI(Player p){
        Inventory inv = Bukkit.createInventory(null,27,"EndSky - Serveurs");
        final ByteArrayDataOutput outSkyblock = ByteStreams.newDataOutput();
        outSkyblock.writeUTF("PlayerCount");
        outSkyblock.writeUTF("skyblock");
        p.sendPluginMessage(plugin, "BungeeCord", outSkyblock.toByteArray());

        ItemBuilder item = new ItemBuilder(Material.GRASS).setDisplayName("§6❂ §e§lSkyBlock").setLoreWithList(Arrays.asList("§c","§2➜ §aCliquez pour y accéder","§2➜ §aEn Ligne","§2➜ §a"+plugin.getPlayerCountSkyBlock()+"§8/§a250","§c"));

        inv.setItem(13,item.build());
        p.openInventory(inv);
    }
}
