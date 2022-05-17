package com.fusionflux.starminer.blockentites;

import com.fusionflux.starminer.StarMinerRefabricated;
import com.fusionflux.starminer.util.EntityAttachments;
import me.andrew.gravitychanger.api.GravityChangerAPI;
import me.andrew.gravitychanger.util.RotationUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StarCoreEntity extends BlockEntity {


    public StarCoreEntity(BlockPos pos, BlockState state) {
        super(StarMinerRefabricated.STAR_CORE_ENTITY, pos, state);
    }

    int radius = 0;

    public static void tick(World world, BlockPos pos, BlockState state, StarCoreEntity blockEntity) {
        Box innerBox = (new Box(new BlockPos(pos.getX(), pos.getY(), pos.getZ()))).expand(blockEntity.radius);

        List<Entity> list = world.getEntitiesByClass(Entity.class, innerBox, e -> true);

        for (Entity entity : list) {
            if (entity != null) {
                boolean dontrotate = entity.hasVehicle();

                if (entity instanceof PlayerEntity)
                    if (((PlayerEntity) entity).getAbilities().flying)
                        dontrotate = true;
                if (!dontrotate) {
                    //listb.remove(entity);
                    ((EntityAttachments) entity).setRemainingGravity(2);
                    if (((EntityAttachments) entity).getGravityTimer() == 0) {
                        double offsetX = Math.abs(innerBox.getCenter().getX() - entity.getPos().getX());
                        double offsetY = Math.abs(innerBox.getCenter().getY() - entity.getPos().getY());
                        double offsetZ = Math.abs(innerBox.getCenter().getZ() - entity.getPos().getZ());
                        if (offsetY > offsetX + 2 && offsetY > offsetZ + 2) {
                            if (entity.getPos().getY() > pos.getY()) {
                                if (GravityChangerAPI.getGravityDirection(entity) != Direction.DOWN)
                                    ((EntityAttachments) entity).setGravityTimer(5);
                                GravityChangerAPI.setGravityDirection(entity, Direction.DOWN);
                            }
                            if (entity.getPos().getY() < pos.getY()) {
                                if (GravityChangerAPI.getGravityDirection(entity) != Direction.UP)
                                    ((EntityAttachments) entity).setGravityTimer(5);
                                GravityChangerAPI.setGravityDirection(entity, Direction.UP);
                            }
                        }
                        if (offsetZ > offsetY + 2 && offsetZ > offsetX + 2) {
                            if (entity.getPos().getZ() > pos.getZ()) {
                                if (GravityChangerAPI.getGravityDirection(entity) != Direction.NORTH)
                                    ((EntityAttachments) entity).setGravityTimer(5);
                                GravityChangerAPI.setGravityDirection(entity, Direction.NORTH);
                            }
                            if (entity.getPos().getZ() < pos.getZ()) {
                                if (GravityChangerAPI.getGravityDirection(entity) != Direction.SOUTH)
                                    ((EntityAttachments) entity).setGravityTimer(5);
                                GravityChangerAPI.setGravityDirection(entity, Direction.SOUTH);
                            }
                        }

                        if (offsetX > offsetY + 2 && offsetX > offsetZ + 2) {
                            if (entity.getPos().getX() > pos.getX()) {
                                if (GravityChangerAPI.getGravityDirection(entity) != Direction.WEST)
                                    ((EntityAttachments) entity).setGravityTimer(5);
                                GravityChangerAPI.setGravityDirection(entity, Direction.WEST);
                            }
                            if (entity.getPos().getX() < pos.getX()) {
                                if (GravityChangerAPI.getGravityDirection(entity) != Direction.EAST)
                                    ((EntityAttachments) entity).setGravityTimer(5);
                                GravityChangerAPI.setGravityDirection(entity, Direction.EAST);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putInt("radius", radius);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        if (tag.contains("radius", NbtElement.INT_TYPE)) {
            radius = tag.getInt("radius");
        }
    }
}
