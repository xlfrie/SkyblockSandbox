package me.xlfrie.sbsandbox.CustomItems;

import java.util.HashMap;

public class ItemUtils {
    private static HashMap<String, CustomItemBase> registeredItems = new HashMap<>();

    public static void registerItem(CustomItemBase clazz) {
        registeredItems.put(clazz.getId(), clazz);
    }

    public static CustomItemBase getItemClass(String clazz) {
        return registeredItems.get(clazz);
    }

    public static HashMap<String, CustomItemBase> getRegisteredItems() {
        return registeredItems;
    }
}
