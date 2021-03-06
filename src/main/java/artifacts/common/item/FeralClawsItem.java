package artifacts.common.item;

import artifacts.Artifacts;
import artifacts.client.render.model.curio.ClawsModel;
import com.google.common.collect.Multimap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.UUID;

public class FeralClawsItem extends ArtifactItem {

    private static final ResourceLocation TEXTURE_DEFAULT = new ResourceLocation(Artifacts.MODID, "textures/entity/curio/feral_claws_default.png");
    private static final ResourceLocation TEXTURE_SLIM = new ResourceLocation(Artifacts.MODID, "textures/entity/curio/feral_claws_default.png");

    public static AttributeModifier FERAL_CLAWS_ATTACK_SPEED = new AttributeModifier(UUID.fromString("7a3367b2-0a38-491d-b5c7-338d5d0c1dd4"), "artifacts:feral_claws_attack_speed", 1, AttributeModifier.Operation.MULTIPLY_TOTAL);

    public FeralClawsItem() {
        super(new Properties(), "feral_claws");
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return Curio.createProvider(new GloveCurio(this) {

            @Override
            public Multimap<String, AttributeModifier> getAttributeModifiers(String identifier) {
                Multimap<String, AttributeModifier> result = super.getAttributeModifiers(identifier);
                result.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), FERAL_CLAWS_ATTACK_SPEED);
                return result;
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            protected ResourceLocation getTexture() {
                return TEXTURE_DEFAULT;
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            protected ResourceLocation getSlimTexture() {
                return TEXTURE_SLIM;
            }

            @OnlyIn(Dist.CLIENT)
            protected ClawsModel getSlimModel() {
                if (model_slim == null) {
                    model_slim = new ClawsModel(true);
                }
                return (ClawsModel) model_slim;
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            protected ClawsModel getModel() {
                if (model_default == null) {
                    model_default = new ClawsModel(false);
                }
                return (ClawsModel) model_default;
            }
        });
    }
}
