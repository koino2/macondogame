package game.scripts.weapons.chainsaw;

import lib.Object2D;
import lib.StaticTextures;

public class ChainsawSlashVFX extends Object2D {
    public ChainsawSlashVFX() {
        super(0, 0, 100, 100, 90);
        texture = StaticTextures.read("src/assets/textures/weapons/chainsaw/slashvfx.png");
        tags.add("noCollision");
        addScript(new ChainsawSlashVFXAnimation());
    }
}
