package cn.ussshenzhou.extinguish.blockentities;

import cn.ussshenzhou.extinguish.Extinguish;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

/**
 * @author USS_Shenzhou
 */
public class AutoWaterCannonEntityModel extends Model {
    public static final ResourceLocation LOCATION = new ResourceLocation("extinguish:item/auto_water_cannon");
    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(LOCATION, "main");
    private final ModelPart base;
    private final ModelPart tube;

    public AutoWaterCannonEntityModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.base = root;
        this.tube = root.getChild("tube");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, 0.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(10, 21)
                        .addBox(-4.0F, 1.0F, -2.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 19)
                        .addBox(3.0F, 1.0F, -2.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 0)
                        .addBox(-1.0F, 1.0F, 0.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0, 16, 0, (float) Math.PI, 0, 0));

        PartDefinition tube = partdefinition.addOrReplaceChild("tube", CubeListBuilder.create()
                        .texOffs(0, 29)
                        .addBox(-2.0F, -0.5F, -7.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 9)
                        .addBox(-1.5F, -1.5F, -3.0F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
                        .texOffs(13, 9)
                        .addBox(-5.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(15, 14)
                        .addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0, 9, 0, (float) Math.PI, 0, 0));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        base.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        //tube.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public ModelPart getBase() {
        return base;
    }

    public ModelPart getTube() {
        return tube;
    }
}
