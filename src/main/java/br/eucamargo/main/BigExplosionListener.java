package br.eucamargo.main;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import java.util.List;

public class BigExplosionListener implements Listener {

    private final Main main;
    private final NamespacedKey explosionKey;

    public BigExplosionListener(Main main) {
        this.main = main;
        this.explosionKey = new NamespacedKey(main, "fallingBlockExplosion");
    }

    @EventHandler
    public void flayingBlocksOnTntExplosion(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof TNTPrimed)) return;

        TNTPrimed tnt = (TNTPrimed) event.getEntity();
        if (!tnt.hasMetadata("bigExplosion")) return;

        event.setCancelled(true);

        Location tntLocation = tnt.getLocation();
        tntLocation.getWorld().createExplosion(tntLocation, 7, false, false);

        List<Block> blocks = event.blockList();

        for (Block block : blocks) {
            if (block.getType() == Material.AIR) continue;

            FallingBlock fallingBlock = tntLocation.getWorld().spawnFallingBlock(block.getLocation(), block.getBlockData());
            fallingBlock.setDropItem(false);

            fallingBlock.getPersistentDataContainer().set(explosionKey, PersistentDataType.BYTE, (byte) 1);

            double x = (Math.random() - 0.5) * 1.5;
            double z = (Math.random() - 0.5) * 1.5;
            double y = (Math.random() * 1.5);
            fallingBlock.setVelocity(new Vector(x, y, z));

            block.setType(Material.AIR);
        }
    }

    @EventHandler
    public void onFallingBlockLand(EntityChangeBlockEvent event) {
        if (!(event.getEntity() instanceof FallingBlock)) return;

        if (event.getEntity().getPersistentDataContainer().has(explosionKey, PersistentDataType.BYTE)) {
            event.setCancelled(true);
            event.getEntity().remove();
        }
    }
}