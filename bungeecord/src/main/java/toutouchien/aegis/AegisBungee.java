package toutouchien.aegis;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import toutouchien.aegis.listeners.AegisJoinListener;
import toutouchien.aegis.listeners.AegisPingListener;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class AegisBungee extends Plugin {
    private static final String IP_LINK = "https://raw.githubusercontent.com/pebblehost/hunter/master/ips.txt";
    private Set<String> blockedIPs = ConcurrentHashMap.newKeySet();

    @Override
    public void onLoad() {
        ProxyServer.getInstance().getScheduler().schedule(this, () -> {
            Set<String> newBlockedIPs = this.getBotIPs();
            if (newBlockedIPs != null)
                this.blockedIPs = newBlockedIPs;
        }, 1L, 3L * 60L * 60L, TimeUnit.SECONDS);
    }

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerListener(
                this, new AegisPingListener(this)
        );

        ProxyServer.getInstance().getPluginManager().registerListener(
                this, new AegisJoinListener(this)
        );
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getScheduler().cancel(this);
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
