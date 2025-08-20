package toutouchien.aegis.listeners;

import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import toutouchien.aegis.AegisBungee;

public class AegisJoinListener implements Listener {
    private final AegisBungee plugin;

    public AegisJoinListener(AegisBungee plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerPreLogin(PreLoginEvent event) {
        String hostAddress = event.getConnection().getAddress().getAddress().getHostAddress();
        if (!this.plugin.getBlockedIPs().contains(hostAddress))
            return;

        event.setCancelled(true);
    }
}
