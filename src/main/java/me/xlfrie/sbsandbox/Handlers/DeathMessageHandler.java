package me.xlfrie.sbsandbox.Handlers;

import me.xlfrie.sbsandbox.SBSandbox;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathMessageHandler implements Listener {
    public DeathMessageHandler(SBSandbox plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void death(PlayerDeathEvent event) {
        String deathMsg = event.getEntity().getDisplayName() + ChatColor.RED + " ";
        EntityDamageEvent.DamageCause cause = event.getEntity().getLastDamageCause().getCause();
        Player killer = event.getEntity().getKiller();
        switch (cause) {
            case BLOCK_EXPLOSION:
                deathMsg += "was blasted to Uranus.";
                break;
            case CONTACT:
                deathMsg += "contact?";
                break;
            case CRAMMING:
                deathMsg += "was murdered by P.E.T.A's bane.";
                break;
            case CUSTOM:
                deathMsg += "custom.";
                break;
            case DRAGON_BREATH:
                deathMsg += "fainted from the dragon's bad breath.";
                break;
            case DROWNING:
                deathMsg += "found out they aren't a fish.";
                break;
            case DRYOUT:
                deathMsg += "forgot they were a fish.";
                break;
            case ENTITY_ATTACK:
                deathMsg += "got ez'd on.";
                break;
            case ENTITY_EXPLOSION:
                deathMsg += "was blasted to Uranus by " + killer.getDisplayName();
                break;
            case ENTITY_SWEEP_ATTACK:
                deathMsg += "was swept under the rug by " + killer.getDisplayName();
                break;
            case FALL:
                deathMsg += "forgot their Feather Falling.";
                break;
            case FALLING_BLOCK:
                deathMsg += "was crushed.";
                break;
            case FIRE:
                deathMsg += "forgot their firefighter gear.";
                break;
            case FIRE_TICK:
                deathMsg += "was toasted.";
                break;
            case FLY_INTO_WALL:
                deathMsg += "experienced kinetic energy.";
                break;
            case FREEZE:
                deathMsg += "experienced hypothermia.";
                break;
            case HOT_FLOOR:
                deathMsg += "had burnt toes.";
                break;
            case LAVA:
                deathMsg += "tried to swim in lava.";
                break;
            case LIGHTNING:
                deathMsg += "got exterminated by Zeus.";
                break;
            case MAGIC:
                deathMsg += "met a magician.";
                break;
            case MELTING:
                deathMsg += "melted.";
                break;
            case POISON:
                deathMsg += "was poisoned.";
                break;
            case PROJECTILE:
                deathMsg += "was killed from a distance by " + event.getEntity().getKiller().getDisplayName();
                break;
            case SONIC_BOOM:
                deathMsg += "went deaf.";
                break;
            case STARVATION:
                deathMsg += "forgot his chicken strips.";
                break;
            case SUFFOCATION:
                deathMsg += "suffocated.";
                break;
            case SUICIDE:
                deathMsg += "committed not alive.";
                break;
            case THORNS:
                deathMsg += "was pricked to death.";
                break;
            case VOID:
                deathMsg += "slipped.";
                break;
            case WITHER:
                deathMsg += "withered away.";
                break;
            default:
                deathMsg += "died.";
                break;
        }
        event.setDeathMessage(deathMsg);
    }
}
