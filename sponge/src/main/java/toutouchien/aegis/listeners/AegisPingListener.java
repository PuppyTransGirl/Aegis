package toutouchien.aegis.listeners;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.server.ClientPingServerEvent;
import toutouchien.aegis.AegisSponge;

public class AegisPingListener {
    private final AegisSponge plugin;

    public AegisPingListener(AegisSponge plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void onClientPingServer(ClientPingServerEvent event) {
        String hostAddress = event.client().address().getAddress().getHostAddress();
        if (!this.plugin.getBlockedIPs().contains(hostAddress))
            return;

        event.setCancelled(true);
    }
}
