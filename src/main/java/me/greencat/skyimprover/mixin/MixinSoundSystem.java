package me.greencat.skyimprover.mixin;

import me.greencat.skyimprover.event.SoundEvent;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundSystem.class)
public class MixinSoundSystem {
    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/sound/SoundInstanceListener;onSoundPlayed(Lnet/minecraft/client/sound/SoundInstance;Lnet/minecraft/client/sound/WeightedSoundSet;)V"))
    public void onSoundPlayed(SoundInstance sound, CallbackInfo ci){
        SoundEvent.EVENT.invoker().onSound(sound);
    }
}
