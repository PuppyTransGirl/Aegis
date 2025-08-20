package toutouchien.aegis.listeners;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import toutouchien.aegis.AegisSponge;

public class AegisJoinListener {
    private final AegisSponge plugin;

    public AegisJoinListener(AegisSponge plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void onClientPingServer(ServerSideConnectionEvent.Auth event) {
        String hostAddress = event.connection().address().getAddress().getHostAddress();
        if (!this.plugin.getBlockedIPs().contains(hostAddress))
            return;

        event.setCancelled(true);
    }
}
