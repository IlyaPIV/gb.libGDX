package gb.mygdx.game.character;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class CharTextures {
    private final TextureAtlas atlas;
    private final TextureAtlas.AtlasRegion texturesStand;
    private final TextureAtlas.AtlasRegion texturesWalk;
    private final TextureAtlas.AtlasRegion texturesJump;
    private final TextureAtlas.AtlasRegion texturesHurt;
    private final TextureAtlas.AtlasRegion texturesDuck;

    public CharTextures() {
        this.atlas = new TextureAtlas("maps/atlas/p3_anim.atlas");

        this.texturesHurt = atlas.findRegion("p3_hurt");
        this.texturesStand = atlas.findRegion("p3_front");
        this.texturesDuck = atlas.findRegion("p3_duck");
        this.texturesWalk = atlas.findRegion("p3_walk");
        this.texturesJump = atlas.findRegion("p3_jump");
    }

     public TextureAtlas.AtlasRegion getTexturesStand() {
        return texturesStand;
    }

    public TextureAtlas.AtlasRegion getTexturesWalk() {
        return texturesWalk;
    }

    public TextureAtlas.AtlasRegion getTexturesJump() {
        return texturesJump;
    }

    public TextureAtlas.AtlasRegion getTexturesHurt() {
        return texturesHurt;
    }

    public TextureAtlas.AtlasRegion getTexturesDuck() {
        return texturesDuck;
    }

    public void dispose(){
        atlas.dispose();
    }
}
