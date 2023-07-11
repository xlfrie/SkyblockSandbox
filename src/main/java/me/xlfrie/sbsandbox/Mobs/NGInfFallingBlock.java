package me.xlfrie.sbsandbox.Mobs;

import com.mojang.logging.LogUtils;
import net.minecraft.CrashReportCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.v1_20_R1.event.CraftEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.slf4j.Logger;

import javax.annotation.Nullable;

public class NGInfFallingBlock extends FallingBlockEntity {
    protected static final EntityDataAccessor<BlockPos> DATA_START_POS;
    private static final Logger LOGGER = LogUtils.getLogger();

    static {
        DATA_START_POS = SynchedEntityData.defineId(FallingBlockEntity.class, EntityDataSerializers.BLOCK_POS);
    }

    public int time;
    public boolean dropItem;
    public boolean hurtEntities;
    @Nullable
    public CompoundTag blockData;
    private BlockState blockState;
    private boolean cancelDrop;
    private int fallDamageMax;
    private float fallDamagePerDistance;

    public NGInfFallingBlock(EntityType<? extends FallingBlockEntity> entitytypes, Level world) {
        super(entitytypes, world);
        this.blockState = Blocks.SAND.defaultBlockState();
        this.dropItem = true;
        this.fallDamageMax = 40;
    }

    private NGInfFallingBlock(Level world, double d0, double d1, double d2, BlockState iblockdata) {
        this(EntityType.FALLING_BLOCK, world);
        this.blockState = iblockdata;
        this.blocksBuilding = true;
        this.setPos(d0, d1, d2);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = d0;
        this.yo = d1;
        this.zo = d2;
        this.setStartPos(new BlockPos((int) xo, (int) yo, (int) zo));
    }

    public static FallingBlockEntity fall(Level world, BlockPos blockposition, BlockState iblockdata) {
        return fall(world, blockposition, iblockdata, CreatureSpawnEvent.SpawnReason.DEFAULT);
    }

    public static FallingBlockEntity fall(Level world, BlockPos blockposition, BlockState iblockdata, CreatureSpawnEvent.SpawnReason spawnReason) {
        FallingBlockEntity entityfallingblock = new NGInfFallingBlock(world, (double) blockposition.getX() + 0.5, blockposition.getY(), (double) blockposition.getZ() + 0.5, iblockdata.hasProperty(BlockStateProperties.WATERLOGGED) ? iblockdata.setValue(BlockStateProperties.WATERLOGGED, false) : iblockdata);
        if (CraftEventFactory.callEntityChangeBlockEvent(entityfallingblock, blockposition, iblockdata.getFluidState().createLegacyBlock()).isCancelled()) {
            return entityfallingblock;
        } else {
            return entityfallingblock;
        }
    }

    public boolean isAttackable() {
        return false;
    }

    public BlockPos getStartPos() {
        return this.entityData.get(DATA_START_POS);
    }

    public void setStartPos(BlockPos blockposition) {
        this.entityData.set(DATA_START_POS, blockposition);
    }

    protected Entity.MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    protected void defineSynchedData() {
        this.entityData.define(DATA_START_POS, BlockPos.ZERO);
    }

    public boolean isPickable() {
        return !this.isRemoved();
    }

    public void tick() {
        if (this.blockState.isAir()) {
            this.discard();
        } else {
            Block block = this.blockState.getBlock();
            ++this.time;
            this.setDeltaMovement(Vec3.ZERO);
            this.move(MoverType.SELF, Vec3.ZERO);
            if (!this.level().isClientSide) {
                boolean flag = this.blockState.getBlock() instanceof ConcretePowderBlock;
                double d0 = this.getDeltaMovement().lengthSqr();
            }
        }

    }

    public void callOnBrokenAfterFall(Block block, BlockPos blockposition) {
        if (block instanceof Fallable) {
            ((Fallable) block).onBrokenAfterFall(this.level(), blockposition, this);
        }

    }

    protected void addAdditionalSaveData(CompoundTag nbttagcompound) {
        nbttagcompound.put("BlockState", NbtUtils.writeBlockState(this.blockState));
        nbttagcompound.putInt("Time", this.time);
        nbttagcompound.putBoolean("DropItem", this.dropItem);
        nbttagcompound.putBoolean("HurtEntities", this.hurtEntities);
        nbttagcompound.putFloat("FallHurtAmount", this.fallDamagePerDistance);
        nbttagcompound.putInt("FallHurtMax", this.fallDamageMax);
        if (this.blockData != null) {
            nbttagcompound.put("TileEntityData", this.blockData);
        }

    }

    public void setHurtsEntities(float f, int i) {
        this.hurtEntities = true;
        this.fallDamagePerDistance = f;
        this.fallDamageMax = i;
    }

    public boolean displayFireAnimation() {
        return false;
    }

    public void fillCrashReportCategory(CrashReportCategory crashreportsystemdetails) {
        super.fillCrashReportCategory(crashreportsystemdetails);
        crashreportsystemdetails.setDetail("Immitating BlockState", this.blockState.toString());
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public void recreateFromPacket(ClientboundAddEntityPacket packetplayoutspawnentity) {
        super.recreateFromPacket(packetplayoutspawnentity);
        this.blockState = Block.stateById(packetplayoutspawnentity.getData());
        this.blocksBuilding = true;
        double d0 = packetplayoutspawnentity.getX();
        double d1 = packetplayoutspawnentity.getY();
        double d2 = packetplayoutspawnentity.getZ();
        this.setPos(d0, d1, d2);
        this.setStartPos(this.blockPosition());
    }
}
