package toutouchien.aegis.listeners;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import toutouchien.aegis.AegisPaper;

public class AegisPingListener implements Listener {
    private final AegisPaper plugin;

    public AegisPingListener(AegisPaper plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPaperServerListPing(PaperServerListPingEvent event) {
        String hostAddress = event.getAddress().getHostAddress();
        if (!this.plugin.getBlockedIPs().contains(hostAddress))
            return;

        event.setCancelled(true);
    }
}
