package artifacts;

import artifacts.client.render.MimicRenderer;
import artifacts.common.init.*;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Tuple;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

@Mod("artifacts")
@SuppressWarnings("unused")
public class Artifacts {

    public static final String MODID = "artifacts";

    public static final ItemGroup CREATIVE_TAB = new ItemGroup(MODID) {
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(Items.PLASTIC_DRINKING_HAT);
        }
    };

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void commonSetup(FMLCommonSetupEvent event) {
            Features.addFeatures();
        }

        @SubscribeEvent
        public static void setupClient(final FMLClientSetupEvent event) {
            RenderingRegistry.registerEntityRenderingHandler(Entities.MIMIC, MimicRenderer::new);
        }

        @SubscribeEvent
        public static void enqueueIMC(final InterModEnqueueEvent event) {
            String[] slots = {"head", "necklace", "hands", "belt", "feet"};
            for (String slot : slots) {
                InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage(slot));
            }
            InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_ICON, () -> new Tuple<>("feet", PlayerContainer.EMPTY_ARMOR_SLOT_BOOTS));
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            Items.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
            Entities.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
            SoundEvents.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerLootModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
            LootModifiers.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
            Features.registerFeatures(event.getRegistry());
        }
    }
}
