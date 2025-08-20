package toutouchien.aegis;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import toutouchien.aegis.listeners.AegisJoinListener;
import toutouchien.aegis.listeners.AegisPingListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class AegisBukkit extends JavaPlugin {
    private static final String IP_LINK = "https://raw.githubusercontent.com/pebblehost/hunter/master/ips.txt";
    private Set<String> blockedIPs = ConcurrentHashMap.newKeySet();

    @Override
    public void onLoad() {
        PacketEvents.setAPI((SpigotPacketEventsBuilder.build(this)));
        PacketEvents.getAPI().getSettings()
                .checkForUpdates(false)
                .reEncodeByDefault(false);
    }

    @Override
    public void onEnable() {
        PacketEvents.getAPI().init();
        PacketEvents.getAPI().load();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, task -> {
            Set<String> newBlockedIPs = this.getBotIPs();
            if (newBlockedIPs != null)
                this.blockedIPs = newBlockedIPs;
        }, 20L, 60L * 60L * 60L);

        PacketEvents.getAPI().getEventManager().registerListener(
                new AegisPingListener(this), PacketListenerPriority.LOWEST
        );

        getServer().getPluginManager().registerEvents(
                new AegisJoinListener(this), this
        );
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        PacketEvents.getAPI().terminate();
    }

    @Nullable
    private Set<String> getBotIPs() {
        Set<String> newBlockedIPs = ConcurrentHashMap.newKeySet();
        URL url;
        try {
            url = URI.create(IP_LINK).toURL();
        } catch (MalformedURLException e) {
            this.getLogger().log(Level.WARNING, "Failed to create URL from ipLink='{0}'. Malformed URL.", IP_LINK);
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            this.getLogger().log(Level.WARNING, "Failed to create URI from ipLink='{0}'. Invalid syntax.", IP_LINK);
            e.printStackTrace();
            return null;
        }

        try (InputStreamReader isr = new InputStreamReader(url.openStream());
             BufferedReader br = new BufferedReader(isr)) {
            String line;

            while ((line = br.readLine()) != null)
                newBlockedIPs.add(line.trim());
        } catch (IOException e) {
            this.getLogger().log(Level.WARNING, "I/O error while fetching bot IPs from {0}.", url);
            e.printStackTrace();
            return null;
        }

        return newBlockedIPs;
    }

    public Set<String> getBlockedIPs() {
        return this.blockedIPs;
    }
}
