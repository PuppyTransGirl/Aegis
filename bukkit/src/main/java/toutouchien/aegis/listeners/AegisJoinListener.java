package toutouchien.aegis.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import toutouchien.aegis.AegisBukkit;

public class AegisJoinListener implements Listener {
    private final AegisBukkit plugin;

    public AegisJoinListener(AegisBukkit plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        String hostAddress = event.getAddress().getHostAddress();
        if (!this.plugin.getBlockedIPs().contains(hostAddress))
            return;

        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Disconnected");
    }
}
