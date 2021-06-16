package fr.newzaz.hub;

import fr.newzaz.hub.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Hub extends JavaPlugin implements PluginMessageListener {
    private int playerCountSkyBlock;

    @Override
    public void onEnable() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(),this);
        Bukkit.getPluginManager().registerEvents(new EntityListeners(),this);
        Bukkit.getPluginManager().registerEvents(new InventoryListeners(this),this);
        Bukkit.getPluginManager().registerEvents(new PlayerListeners(this),this);
    }


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        String subchannel = null;
        try {
            subchannel = in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (subchannel.equals("PlayerCount")) {

            try {
                String servername = in.readUTF();
                if(servername.equalsIgnoreCase("skyblock")) {

                    playerCountSkyBlock = in.readInt();
                    player.updateInventory();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getPlayerCountSkyBlock() {
        return playerCountSkyBlock;
    }
}
