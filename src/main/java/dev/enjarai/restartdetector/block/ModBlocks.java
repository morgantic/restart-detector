package dev.enjarai.restartdetector.block;

import dev.enjarai.restartdetector.RestartDetector;
import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
/*? <1.21.2 {*//*import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;*//*?}*/
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class ModBlocks {
    public static final BlockBehaviour.Properties DETECTOR_SETTINGS = /*? >=1.21.2 {*/
            BlockBehaviour.Properties.of()
            /*?} else if >=1.20 {*/
            /*FabricBlockSettings.create()*/
            /*?} else {*/
            /*FabricBlockSettings.of(Material.WOOD)*/
            /*?}*/
            .mapColor(MapColor.WOOD)
            /*? >=1.20 <1.21 {*//*.instrument(Instrument.BASS)*//*?}*/
            .strength(0.2F)
            .sound(SoundType.WOOD)
            /*? >=1.20 {*/.ignitedByLava()/*?}*/;

    public static final RestartDetectorBlock RESTART_DETECTOR =
            Registry.register(BuiltInRegistries.BLOCK, RestartDetector.id("restart_detector"),
                    new RestartDetectorBlock(DETECTOR_SETTINGS));
    public static final TpsDetectorBlock TPS_DETECTOR =
            Registry.register(BuiltInRegistries.BLOCK, RestartDetector.id("tps_detector"),
                    new TpsDetectorBlock(BlockBehaviour.Properties.ofFullCopy(RESTART_DETECTOR)));

    public static final BlockItem RESTART_DETECTOR_ITEM =
            Registry.register(BuiltInRegistries.ITEM, RestartDetector.id("restart_detector"),
                    new PolymerBlockItem(RESTART_DETECTOR, new Item.Properties()
                            /*? if >=1.21.2 {*/.setId(ResourceKey.create(Registries.ITEM, RestartDetector.id("restart_detector")))/*?}*/,
                            Items.DAYLIGHT_DETECTOR));
    public static final BlockItem TPS_DETECTOR_ITEM =
            Registry.register(BuiltInRegistries.ITEM, RestartDetector.id("tps_detector"),
                    new PolymerBlockItem(TPS_DETECTOR, new Item.Properties()
                            /*? if >=1.21.2 {*/.setId(ResourceKey.create(Registries.ITEM, RestartDetector.id("tps_detector")))/*?}*/,
                            Items.DAYLIGHT_DETECTOR));

    public static void register() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(group -> {
            group.accept(RESTART_DETECTOR_ITEM);
            group.accept(TPS_DETECTOR_ITEM);
        });
    }
}
