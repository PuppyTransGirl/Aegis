package toutouchien.aegis.listeners;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import toutouchien.aegis.AegisVelocity;

public class AegisPingListener {
    private final AegisVelocity plugin;

    public AegisPingListener(AegisVelocity plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onProxyPing(ProxyPingEvent event) {
        String hostAddress = event.getConnection().getRemoteAddress().getAddress().getHostAddress();
        if (!this.plugin.getBlockedIPs().contains(hostAddress))
            return;

        event.setResult(ResultedEvent.GenericResult.denied());
    }
}
