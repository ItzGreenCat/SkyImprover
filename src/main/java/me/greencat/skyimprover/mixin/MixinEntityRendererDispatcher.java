package me.greencat.skyimprover.mixin;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(EntityRenderDispatcher.class)
public abstract class MixinEntityRendererDispatcher {
    @Shadow public abstract <T extends Entity> EntityRenderer<? super T> getRenderer(T entity);

    /**
     * @author GreenCat
     * @reason fix the crash problem
     */
    @Overwrite
    public <E extends Entity> boolean shouldRender(E entity, Frustum frustum, double x, double y, double z) {
        try {
            EntityRenderer<? super E> entityRenderer = this.getRenderer(entity);
            return entityRenderer.shouldRender(entity, frustum, x, y, z);
        } catch(Exception e){
            return false;
        }
    }

}
