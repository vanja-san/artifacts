package artifacts.common.item;

import artifacts.Artifacts;
import artifacts.common.init.Items;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

public class UmbrellaItem extends ArtifactItem {

    private static final AttributeModifier UMBRELLA_SLOW_FALLING = new AttributeModifier(UUID.fromString("a7a25453-2065-4a96-bc83-df600e13f390"), "artifacts:umbrella_slow_falling", -0.875, AttributeModifier.Operation.MULTIPLY_TOTAL).setSaved(false);

    public UmbrellaItem() {
        super(new Properties(), "umbrella");
        this.addPropertyOverride(new ResourceLocation("blocking"), (stack, world, entity) -> entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1 : 0);
        DispenserBlock.registerDispenseBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }

    @Override
    public boolean isShield(ItemStack stack, LivingEntity entity) {
        return true;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        player.setActiveHand(hand);
        return ActionResult.resultConsume(itemstack);
    }

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(modid = Artifacts.MODID)
    public static class Events {
        @SubscribeEvent
        public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
            LivingEntity entity = event.getEntityLiving();
            IAttributeInstance gravity = entity.getAttribute(LivingEntity.ENTITY_GRAVITY);
            if (!entity.onGround && !entity.isInWater() && event.getEntity().getMotion().y < 0 && !entity.isPotionActive(Effects.SLOW_FALLING)
                    && (entity.getHeldItemOffhand().getItem() == Items.UMBRELLA
                    || entity.getHeldItemMainhand().getItem() == Items.UMBRELLA) && !(entity.isHandActive() && !entity.getActiveItemStack().isEmpty() && entity.getActiveItemStack().getItem().getUseAction(entity.getActiveItemStack()) == UseAction.BLOCK)) {
                if (!gravity.hasModifier(UMBRELLA_SLOW_FALLING)) {
                    gravity.applyModifier(UMBRELLA_SLOW_FALLING);
                }
                entity.fallDistance = 0;
            } else if (gravity.hasModifier(UMBRELLA_SLOW_FALLING)) {
                gravity.removeModifier(UMBRELLA_SLOW_FALLING);
            }
        }
    }

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Artifacts.MODID)
    public static class ClientEvents {

        @SubscribeEvent
        public static void onLivingRender(RenderLivingEvent.Pre<?, BipedModel<?>> event) {
            LivingEntity entity = event.getEntity();
            if (!(entity.isHandActive() && !entity.getActiveItemStack().isEmpty() && entity.getActiveItemStack().getItem().getUseAction(entity.getActiveItemStack()) == UseAction.BLOCK)) {
                boolean isHoldingOffHand = entity.getHeldItemOffhand().getItem() == Items.UMBRELLA;
                boolean isHoldingMainHand = entity.getHeldItemMainhand().getItem() == Items.UMBRELLA;
                if ((isHoldingMainHand && Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT) || (isHoldingOffHand && Minecraft.getInstance().gameSettings.mainHand == HandSide.LEFT)) {
                    event.getRenderer().getEntityModel().rightArmPose = BipedModel.ArmPose.THROW_SPEAR;
                } else if ((isHoldingMainHand && Minecraft.getInstance().gameSettings.mainHand == HandSide.LEFT) || (isHoldingOffHand && Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT)) {
                    event.getRenderer().getEntityModel().leftArmPose = BipedModel.ArmPose.THROW_SPEAR;
                }
            }
        }
    }
}
