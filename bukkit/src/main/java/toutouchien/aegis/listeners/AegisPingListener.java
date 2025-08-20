package toutouchien.aegis.listeners;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import toutouchien.aegis.AegisBukkit;

public class AegisPingListener implements PacketListener {
    private final AegisBukkit plugin;

    public AegisPingListener(AegisBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Status.Client.PING)
            return;

        User user = event.getUser();
        String hostAddress = user.getAddress().getAddress().getHostAddress();
        if (!this.plugin.getBlockedIPs().contains(hostAddress))
            return;

        event.setCancelled(true);
    }
}
