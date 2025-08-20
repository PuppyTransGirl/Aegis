package toutouchien.aegis.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import toutouchien.aegis.AegisVelocity;

public class AegisJoinListener {
    private final AegisVelocity plugin;

    public AegisJoinListener(AegisVelocity plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPreLogin(PreLoginEvent event) {
        String hostAddress = event.getConnection().getRemoteAddress().getAddress().getHostAddress();
        if (!plugin.getBlockedIPs().contains(hostAddress))
            return;

        TranslatableComponent kickMessage = Component.translatable("multiplayer.disconnect.generic"); // "Disconnected"
        event.setResult(PreLoginEvent.PreLoginComponentResult.denied(kickMessage));
    }
}
