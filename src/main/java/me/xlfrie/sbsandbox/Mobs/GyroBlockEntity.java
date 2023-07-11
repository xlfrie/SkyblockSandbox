package me.xlfrie.sbsandbox.Mobs;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.v1_20_R1.event.CraftEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class GyroBlockEntity extends FallingBlockEntity {

    private BlockState blockState;

    public GyroBlockEntity(EntityType<? extends FallingBlockEntity> entitytypes, Level world) {
        super(entitytypes, world);
        this.blockState = Blocks.SAND.defaultBlockState();
        this.dropItem = false;
    }

    private GyroBlockEntity(Level world, double d0, double d1, double d2, BlockState iblockdata) {
        this(EntityType.FALLING_BLOCK, world);
        this.blockState = iblockdata;
        this.blocksBuilding = true;
        this.setPos(d0, d1, d2);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = d0;
        this.yo = d1;
        this.zo = d2;
        this.setStartPos(this.blockPosition());
    }

    public static FallingBlockEntity fall(Level world, BlockPos blockposition, BlockState iblockdata) {
        return fall(world, blockposition, iblockdata, CreatureSpawnEvent.SpawnReason.DEFAULT);
    }

    public static FallingBlockEntity fall(Level world, double x, double y, double z, BlockState iblockdata, CreatureSpawnEvent.SpawnReason spawnReason) {
        FallingBlockEntity entityfallingblock = new GyroBlockEntity(world, x, y, z, iblockdata.hasProperty(BlockStateProperties.WATERLOGGED) ? iblockdata.setValue(BlockStateProperties.WATERLOGGED, false) : iblockdata);
        if (CraftEventFactory.callEntityChangeBlockEvent(entityfallingblock, new BlockPos((int) x, (int) y, (int) z), iblockdata.getFluidState().createLegacyBlock()).isCancelled()) {
            return entityfallingblock;
        } else {
            world.addFreshEntity(entityfallingblock, spawnReason);
            return entityfallingblock;
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbttagcompound) {
        this.blockState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), nbttagcompound.getCompound("BlockState"));
        this.time = nbttagcompound.getInt("Time");
        if (nbttagcompound.contains("HurtEntities", 99)) {
            this.hurtEntities = nbttagcompound.getBoolean("HurtEntities");
        } else if (this.blockState.is(BlockTags.ANVIL)) {
            this.hurtEntities = true;
        }

        if (nbttagcompound.contains("DropItem", 99)) {
            this.dropItem = nbttagcompound.getBoolean("DropItem");
        }

        if (nbttagcompound.contains("TileEntityData", 10)) {
            this.blockData = nbttagcompound.getCompound("TileEntityData");
        }

        if (this.blockState.isAir()) {
            this.blockState = Blocks.SAND.defaultBlockState();
        }

    }

    protected void addAdditionalSaveData(CompoundTag nbttagcompound) {
        nbttagcompound.put("BlockState", NbtUtils.writeBlockState(this.blockState));
        nbttagcompound.putInt("Time", this.time);
        nbttagcompound.putBoolean("DropItem", this.dropItem);
        nbttagcompound.putBoolean("HurtEntities", this.hurtEntities);
        if (this.blockData != null) {
            nbttagcompound.put("TileEntityData", this.blockData);
        }

    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    @Override
    public void tick() {

    }
}
