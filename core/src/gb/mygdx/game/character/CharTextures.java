package gb.mygdx.game.character;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class CharTextures {
    private final TextureAtlas atlas;
    private final Array<TextureAtlas.AtlasRegion> texturesStand;
    private final Array<TextureAtlas.AtlasRegion> texturesWalk;
    private final Array<TextureAtlas.AtlasRegion> texturesJump;
    private final Array<TextureAtlas.AtlasRegion> texturesHurt;
    private final Array<TextureAtlas.AtlasRegion> texturesDuck;

    public CharTextures() {
        this.atlas = new TextureAtlas("maps/atlas/p3_anim.atlas");

        this.texturesHurt = atlas.findRegions("p3_hurt");
        this.texturesStand = atlas.findRegions("p3_front");
        this.texturesDuck = atlas.findRegions("p3_duck");
        this.texturesWalk = atlas.findRegions("p3_walk");
        this.texturesJump = atlas.findRegions("p3_jump");
    }

     public Array<TextureAtlas.AtlasRegion> getTexturesStand() {
        return texturesStand;
    }

    public Array<TextureAtlas.AtlasRegion> getTexturesWalk() {
        return texturesWalk;
    }

    public Array<TextureAtlas.AtlasRegion> getTexturesJump() {
        return texturesJump;
    }

    public Array<TextureAtlas.AtlasRegion> getTexturesHurt() {
        return texturesHurt;
    }

    public Array<TextureAtlas.AtlasRegion> getTexturesDuck() {
        return texturesDuck;
    }

    public void dispose(){
        atlas.dispose();
    }
}
