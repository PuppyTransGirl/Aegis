package toutouchien.aegis.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import toutouchien.aegis.AegisPaper;

public class AegisJoinListener implements Listener {
    private final AegisPaper plugin;

    public AegisJoinListener(AegisPaper plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        String hostAddress = event.getAddress().getHostAddress();
        if (!this.plugin.getBlockedIPs().contains(hostAddress))
            return;

        TranslatableComponent kickMessage = Component.translatable("multiplayer.disconnect.generic"); // "Disconnected"
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, kickMessage);
    }
}
