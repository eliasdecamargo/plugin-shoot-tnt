package br.eucamargo.main;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ShootTntListener implements Listener {

    private final Main main;
    public ShootTntListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        boolean leftButtonWasClicked = (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK);
        boolean handleItemIsGunpowder = player.getInventory().getItemInMainHand().getType() == Material.GUNPOWDER;

        if (leftButtonWasClicked && handleItemIsGunpowder) {
            Location spawnTnt = player.getEyeLocation();
            TNTPrimed tnt = spawnTnt.getWorld().spawn(spawnTnt, TNTPrimed.class);
            tnt.setMetadata("bigExplosion", new FixedMetadataValue(main, true));
            Vector throwingForce = player.getLocation().getDirection().multiply(1.8);
            tnt.setVelocity(throwingForce);
            tnt.setFuseTicks(50);


            new BukkitRunnable() {
                @Override
                public void run() {
                    if (tnt == null || !tnt.isValid()){
                        this.cancel();
                        return;
                    }
                    World world = player.getWorld();
                    world.spawnParticle(Particle.FLAME, tnt.getLocation(), 5, 0, 0, 0, 0);

                }
            }.runTaskTimer((Plugin) main, 0L, 1L);

            player.playSound(player.getLocation(), Sound.ENTITY_WITCH_THROW, 1.0f, 0.5f);
        }
    }
}