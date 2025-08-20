package toutouchien.aegis.listeners;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import toutouchien.aegis.AegisBungee;

public class AegisPingListener implements Listener {
    private final AegisBungee plugin;

    public AegisPingListener(AegisBungee plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerPreLogin(ProxyPingEvent event) {
        String hostAddress = event.getConnection().getAddress().getAddress().getHostAddress();
        if (!this.plugin.getBlockedIPs().contains(hostAddress))
            return;

        event.setResponse(new ServerPing());
    }
}
