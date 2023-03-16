package me.xlfrie.sbsandbox.Dungeons.F6.mobs;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;

import javax.annotation.Nullable;

public class TerracottaV2 extends Player {
    public TerracottaV2(ServerLevel world, BlockPos blockposition, float f, GameProfile gameprofile, @Nullable ProfilePublicKey profilepublickey) {
        super(world, blockposition, f, gameprofile);
        ServerPlayer serverPlayer = new ServerPlayer(world.getServer(), world, gameprofile);
        serverPlayer.setId(this.getId());
        for (org.bukkit.entity.Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) onlinePlayer).getHandle().connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, serverPlayer));
            ((CraftPlayer) onlinePlayer).getHandle().connection.send(new ClientboundAddPlayerPacket(this));
        }
        world.addFreshEntity(this);
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }
}
