package net.draycia.carbon.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import java.util.ArrayList;
import java.util.UUID;
import net.draycia.carbon.api.CarbonServer;
import net.draycia.carbon.api.users.CarbonPlayer;
import net.draycia.carbon.api.users.UserManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class CarbonServerVelocity implements CarbonServer, ForwardingAudience.Single {

    private final ProxyServer server;
    private final UserManager userManager;

    @Inject
    private CarbonServerVelocity(final ProxyServer server, final UserManager userManager) {
        this.server = server;
        this.userManager = userManager;
    }

    @Override
    public Audience audience() {
        return this.console();
    }

    @Override
    public Audience console() {
        return this.server.getConsoleCommandSource();
    }

    @Override
    public Iterable<? extends CarbonPlayer> players() {
        final var players = new ArrayList<CarbonPlayer>();

        for (final var player : this.server.getAllPlayers()) {
            final @Nullable CarbonPlayer carbonPlayer = this.player(player);

            if (carbonPlayer != null) {
                players.add(carbonPlayer);
            }
        }

        return players;
    }

    @Override
    public @Nullable CarbonPlayer player(final UUID uuid) {
        return this.userManager.carbonPlayer(uuid);
    }

    @Override
    public @Nullable CarbonPlayer player(final String username) {
        return this.userManager.carbonPlayer(username);
    }

    private @Nullable CarbonPlayer player(final Player player) {
        return this.player(player.getUniqueId());
    }

}