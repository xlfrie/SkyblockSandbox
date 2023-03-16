package me.xlfrie.sbsandbox.CustomItems.Weapons;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.xlfrie.sbsandbox.CustomItems.CustomItemBase;
import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R2.profile.CraftPlayerProfile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class GloriousArmor {
    public static class GloriousHelmet extends CustomItemBase {

        @Override
        public String getId() {
            return "GLORIOUS_HELMET";
        }

        @Override
        public String localizedName() {
            return "§dGlorious Helmet";
        }

        @Override
        public double cooldownDelay() {
            return 0;
        }

        @Override
        public Material getMaterial() {
            return Material.PLAYER_HEAD;
        }

        @Override
        public ItemMeta getMetadata(ItemStack itemStack) {
            ItemMeta m = itemStack.getItemMeta();
            m.setDisplayName(this.localizedName());
            NamespacedKey key = new NamespacedKey(SBSandbox.getInstance(), "sandbox-id");
            m.getPersistentDataContainer().set(key, PersistentDataType.STRING, this.getId());
            m.setUnbreakable(true);
            GameProfile gp = new GameProfile(UUID.fromString("ca49c1c6-1151-55a8-88e1-d857e9d9cd7a"), null);
            gp.getProperties().put("textures", new Property("textures", "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQ3NDllMmY1MGIzNmU4OWU5NmVkZDcxOTdjMmJmYWRlMjUzYzNhODNhOGNlYzhhMjE5MmZjMDEzN2QyMDY0NyJ9fX0="));
            ((SkullMeta) m).setOwnerProfile(new CraftPlayerProfile(gp));
            return m;
        }

        private final HashMap<String, Object> _itemData = new HashMap<>() {{
            put("health", 100);
            put("intelligence", 2450);
        }};

        @Override
        public HashMap<String, Object> itemData() {
            return _itemData;
        }
    }

    public static class GloriousChestplate extends CustomItemBase {

        @Override
        public String getId() {
            return "GLORIOUS_CHESTPLATE";
        }

        @Override
        public String localizedName() {
            return "§dGlorious Chestplate";
        }

        @Override
        public double cooldownDelay() {
            return 0;
        }

        @Override
        public Material getMaterial() {
            return Material.LEATHER_CHESTPLATE;
        }

        private final HashMap<String, Object> _itemData = new HashMap<>() {{
            put("health", 100);
            put("intelligence", 3120);
        }};

        @Override
        public HashMap<String, Object> itemData() {
            return _itemData;
        }

        @Override
        public ItemMeta getMetadata(ItemStack itemStack) {
            ItemMeta m = itemStack.getItemMeta();
            m.setDisplayName(this.localizedName());
            NamespacedKey key = new NamespacedKey(SBSandbox.getInstance(), "sandbox-id");
            m.getPersistentDataContainer().set(key, PersistentDataType.STRING, this.getId());
            m.setUnbreakable(true);
            ((LeatherArmorMeta) m).setColor(Color.fromRGB(66, 135, 245));
            return m;
        }
    }

    public static class GloriousLeggings extends CustomItemBase {

        @Override
        public String getId() {
            return "GLORIOUS_LEGGINGS";
        }

        @Override
        public String localizedName() {
            return "§dGlorious Leggings";
        }

        @Override
        public double cooldownDelay() {
            return 0;
        }

        @Override
        public Material getMaterial() {
            return Material.LEATHER_LEGGINGS;
        }

        private final HashMap<String, Object> _itemData = new HashMap<>() {{
            put("health", 100);
            put("intelligence", 2015);
        }};

        @Override
        public HashMap<String, Object> itemData() {
            return _itemData;
        }

        @Override
        public ItemMeta getMetadata(ItemStack itemStack) {
            ItemMeta m = itemStack.getItemMeta();
            m.setDisplayName(this.localizedName());
            NamespacedKey key = new NamespacedKey(SBSandbox.getInstance(), "sandbox-id");
            m.getPersistentDataContainer().set(key, PersistentDataType.STRING, this.getId());
            m.setUnbreakable(true);
            ((LeatherArmorMeta) m).setColor(Color.fromRGB(66, 179, 245));
            return m;
        }
    }

    public static class GloriousBoots extends CustomItemBase {

        @Override
        public String getId() {
            return "GLORIOUS_BOOTS";
        }

        @Override
        public String localizedName() {
            return "§dGlorious Boots";
        }

        @Override
        public double cooldownDelay() {
            return 0;
        }

        @Override
        public Material getMaterial() {
            return Material.LEATHER_BOOTS;
        }

        private final HashMap<String, Object> _itemData = new HashMap<>() {{
            put("health", 100);
            put("intelligence", 2550);
        }};

        @Override
        public HashMap<String, Object> itemData() {
            return _itemData;
        }

        @Override
        public ItemMeta getMetadata(ItemStack itemStack) {
            ItemMeta m = itemStack.getItemMeta();
            m.setDisplayName(this.localizedName());
            NamespacedKey key = new NamespacedKey(SBSandbox.getInstance(), "sandbox-id");
            m.getPersistentDataContainer().set(key, PersistentDataType.STRING, this.getId());
            m.setUnbreakable(true);
            ((LeatherArmorMeta) m).setColor(Color.fromRGB(66, 203, 245));
            return m;
        }
    }

}
