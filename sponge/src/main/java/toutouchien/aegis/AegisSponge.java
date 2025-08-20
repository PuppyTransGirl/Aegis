package toutouchien.aegis;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;
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

@Plugin("aegis")
public class AegisSponge {
    private static final String IP_LINK = "https://raw.githubusercontent.com/pebblehost/hunter/master/ips.txt";
    private Set<String> blockedIPs = ConcurrentHashMap.newKeySet();

    private Logger logger;
    private final PluginContainer container;

    @Inject
    public AegisSponge(PluginContainer container, Logger logger) {
        this.container = container;
        this.logger = logger;
    }

    @Listener
    public void onServerStart(StartedEngineEvent<Server> event) {
        Sponge.asyncScheduler().submit(
                Task.builder()
                        .execute(() -> {
                            Set<String> newBlockedIPs = this.getBotIPs();
                            if (newBlockedIPs != null)
                                this.blockedIPs = newBlockedIPs;
                        })
                        .delay(1L, TimeUnit.SECONDS)
                        .interval(3L, TimeUnit.HOURS)
                        .plugin(this.container)
                        .build()
        );

        Sponge.eventManager().registerListeners(
                this.container, new AegisPingListener(this)
        );

        Sponge.eventManager().registerListeners(
                this.container, new AegisJoinListener(this)
        );
    }

    @Listener
    public void onServerStop(StoppingEngineEvent<Server> event) {
        Sponge.asyncScheduler().tasks(this.container).forEach(ScheduledTask::cancel);
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
