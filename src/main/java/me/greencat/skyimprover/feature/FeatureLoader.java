package me.greencat.skyimprover.feature;

public class FeatureLoader {
    public static void load(Class<? extends Module> clazz){
        try {
            clazz.newInstance().registerEvent();
        } catch(Exception e){
            throw  new RuntimeException(e);
        }
    }
}
