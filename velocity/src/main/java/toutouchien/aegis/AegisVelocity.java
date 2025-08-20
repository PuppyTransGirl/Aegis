package toutouchien.aegis;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
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

@Plugin(
        id = "aegis",
        name = "Aegis",
        version = "1.0.0",
        description = "Anti Server Scanner plugin",
        authors = {"Toutouchien"}
)
public class AegisVelocity {
    private static final String IP_LINK = "https://raw.githubusercontent.com/pebblehost/hunter/master/ips.txt";
    private Set<String> blockedIPs = ConcurrentHashMap.newKeySet();

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public AegisVelocity(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        this.server.getScheduler()
                .buildTask(this, () -> {
                    Set<String> newBlockedIPs = this.getBotIPs();
                    if (newBlockedIPs != null)
                        this.blockedIPs = newBlockedIPs;
                })
                .delay(1L, TimeUnit.SECONDS)
                .repeat(3L, TimeUnit.HOURS)
                .schedule();

        this.server.getEventManager().register(
                this, new AegisPingListener(this)
        );

        this.server.getEventManager().register(
                this, new AegisJoinListener(this)
        );
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        this.server.getScheduler().tasksByPlugin(this).forEach(ScheduledTask::cancel);
    }

    @Nullable
    private Set<String> getBotIPs() {
        Set<String> newBlockedIPs = ConcurrentHashMap.newKeySet();
        URL url;
        try {
            url = URI.create(IP_LINK).toURL();
        } catch (MalformedURLException e) {
            this.logger.error("Failed to create URL from ipLink='{}'. Malformed URL.", IP_LINK, e);
            return null;
        } catch (IllegalArgumentException e) {
            this.logger.error("Failed to create URI from ipLink='{}'. Invalid syntax.", IP_LINK, e);
            return null;
        }

        try (InputStreamReader isr = new InputStreamReader(url.openStream());
             BufferedReader br = new BufferedReader(isr)) {
            String line;

            while ((line = br.readLine()) != null)
                newBlockedIPs.add(line.trim());
        } catch (IOException e) {
            this.logger.error("I/O error while fetching bot IPs from {}.", url, e);
            return null;
        }

        return newBlockedIPs;
    }

    public Set<String> getBlockedIPs() {
        return blockedIPs;
    }
}
