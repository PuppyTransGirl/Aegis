package toutouchien.aegis;

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
import java.util.concurrent.TimeUnit;

public class AegisPaper extends JavaPlugin {
    private static final String IP_LINK = "https://raw.githubusercontent.com/pebblehost/hunter/master/ips.txt";
    private Set<String> blockedIPs = ConcurrentHashMap.newKeySet();

    @Override
    public void onEnable() {
        Bukkit.getAsyncScheduler().runAtFixedRate(this, task -> {
            Set<String> newBlockedIPs = this.getBotIPs();
            if (newBlockedIPs != null)
                this.blockedIPs = newBlockedIPs;
        }, 1L, 3L * 60L * 60L, TimeUnit.SECONDS);

        getServer().getPluginManager().registerEvents(
                new AegisPingListener(this), this
        );

        getServer().getPluginManager().registerEvents(
                new AegisJoinListener(this), this
        );
    }

    @Override
    public void onDisable() {
        Bukkit.getAsyncScheduler().cancelTasks(this);
    }

    @Nullable
    private Set<String> getBotIPs() {
        Set<String> newBlockedIPs = ConcurrentHashMap.newKeySet();
        URL url;
        try {
            url = URI.create(IP_LINK).toURL();
        } catch (MalformedURLException e) {
            this.getSLF4JLogger().error("Failed to create URL from ipLink='{}'. Malformed URL.", IP_LINK, e);
            return null;
        } catch (IllegalArgumentException e) {
            this.getSLF4JLogger().error("Failed to create URI from ipLink='{}'. Invalid syntax.", IP_LINK, e);
            return null;
        }

        try (InputStreamReader isr = new InputStreamReader(url.openStream());
             BufferedReader br = new BufferedReader(isr)) {
            String line;

            while ((line = br.readLine()) != null)
                newBlockedIPs.add(line.trim());
        } catch (IOException e) {
            this.getSLF4JLogger().error("I/O error while fetching bot IPs from {}.", url, e);
            return null;
        }

        return newBlockedIPs;
    }

    public Set<String> getBlockedIPs() {
        return blockedIPs;
    }
}
