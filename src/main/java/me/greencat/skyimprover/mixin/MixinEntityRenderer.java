package me.greencat.skyimprover.mixin;


import me.greencat.skyimprover.event.RenderLivingEntityPreEvent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> {
    @Inject(method = "render*",at = @At("HEAD"),cancellable = true)
    public void onRender(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci){
        boolean result = RenderLivingEntityPreEvent.EVENT.invoker().render(livingEntity);
        if(!result){
            ci.cancel();
        }
    }
}
