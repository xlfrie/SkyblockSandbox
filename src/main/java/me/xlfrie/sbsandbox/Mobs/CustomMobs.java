package me.xlfrie.sbsandbox.Mobs;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

@SuppressWarnings("SameParameterValue")
public enum CustomMobs {
    ENDERMAN("Enderman", 2000, EntityType.ENDERMAN);

    private final String name;
    private final double maxHealth;
    private final EntityType type;

    CustomMobs(String name, double maxHealth, EntityType type) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.type = type;
    }

    @SuppressWarnings("UnusedReturnValue")
    public LivingEntity spawn(Location loc) {
        LivingEntity entity = (LivingEntity) loc.getWorld().spawnEntity(loc, type);
        entity.setCustomNameVisible(true);
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        entity.setHealth(maxHealth);
        return entity;
    }

    public String getName() {
        return name;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public EntityType getEntityType() {
        return type;
    }
}
