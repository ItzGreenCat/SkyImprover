package me.greencat.skyimprover.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.LivingEntity;

public interface SoundEvent {
    Event<SoundEvent> EVENT = EventFactory.createArrayBacked(SoundEvent.class,(listeners) -> (sound) -> {
        for(SoundEvent event : listeners){
            event.onSound(sound);
        }
    });
    void onSound(SoundInstance sound);
}
