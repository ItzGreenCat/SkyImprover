package me.greencat.skyimprover.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

public interface RenderLivingEntityPreEvent {
    Event<RenderLivingEntityPreEvent> EVENT = EventFactory.createArrayBacked(RenderLivingEntityPreEvent.class,(listeners) -> (entity) -> {
        for(RenderLivingEntityPreEvent event : listeners){
            boolean result = event.render(entity);
            if(!result){
                return false;
            }
        }
        return true;
    });
    boolean render(LivingEntity entity);
}
