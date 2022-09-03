package gb.mygdx.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import gb.mygdx.game.Anim;
import gb.mygdx.game.character.CharStatus;

public class CharAnimation {

    private float currentX;
    private float currentY;

    private CharStatus currentStatus;
    private float deltaTime;
    private Animation<TextureAtlas.AtlasRegion> anm;
    private final Animation.PlayMode playMode;
    private final CharTextures charTextures;

    private boolean directionLeft;
    private final float FRAME_DURATION = 1/2f;

    public CharAnimation(Animation.PlayMode playMode, float cX, float cY) {

        this.currentX = cX;
        this.currentY = cY;
        this.playMode = playMode;
        this.charTextures = new CharTextures();
        this.currentStatus = CharStatus.STAND;
        this.deltaTime +=Gdx.graphics.getDeltaTime();
        this.anm = new Animation<TextureAtlas.AtlasRegion>(FRAME_DURATION, charTextures.getTexturesStand());
        anm.setPlayMode(this.playMode);

    }


    public void setCurrentPosition(float cX, float cY){
        float prvX = currentX;
        float prvY = currentY;

        this.currentY = cY;
        this.currentX = cX;

        changeCharStatus(prvX, prvY);
    }

    /*
     * возвращает кадр
     */
    public TextureRegion getFrame(){
        return anm.getKeyFrame(deltaTime);
    }

    public void setDeltaTime(float deltaTime) { this.deltaTime += deltaTime; }

    public void zeroDeltaTime() { this.deltaTime = 0; }

    public boolean isAnimationOver(){
        return anm.isAnimationFinished(deltaTime);
    }

    public void setPlayMode(Animation.PlayMode playMode) {
        this.anm.setPlayMode(playMode);
    }


    private void changeCharStatus(float prvX, float prvY){
        CharStatus prevStatus = currentStatus;
        if (prvX == currentX) {
            currentStatus = CharStatus.STAND;
            if (prevStatus != currentStatus) {
                zeroDeltaTime();
                changeCurrentAnimation();
            }
        }
        if (prvX != currentX && prvY == currentY) {
            currentStatus = CharStatus.WALK;
            if (prevStatus != currentStatus) {
                zeroDeltaTime();
                changeCurrentAnimation();
            }
        }
        if (prvX != currentX && currentY > prvY) {
            currentStatus = CharStatus.JUMP;
            if (prevStatus != currentStatus) {
                zeroDeltaTime();
                changeCurrentAnimation();
            }
        }
        if (currentX != prvX && currentY < prvY) {
            currentStatus = CharStatus.DUCK;
            if (prevStatus != currentStatus) {
                zeroDeltaTime();
                changeCurrentAnimation();
            }
        }

        directionLeft = currentX < prvX;
    }

    private void changeCurrentAnimation() {
        switch (currentStatus){
            case WALK: anm = new Animation<TextureAtlas.AtlasRegion>(FRAME_DURATION, charTextures.getTexturesWalk());
                break;
            case STAND: anm = new Animation<TextureAtlas.AtlasRegion>(FRAME_DURATION, charTextures.getTexturesStand());
                break;
            case JUMP: anm = new Animation<TextureAtlas.AtlasRegion>(FRAME_DURATION, charTextures.getTexturesJump());
                break;
            case DUCK: anm = new Animation<TextureAtlas.AtlasRegion>(FRAME_DURATION, charTextures.getTexturesDuck());
                break;
            case HURT: anm = new Animation<TextureAtlas.AtlasRegion>(FRAME_DURATION, charTextures.getTexturesHurt());
        }
        anm.setPlayMode(this.playMode);
    }

    public boolean isDirectionLeft() {
        return directionLeft;
    }

    public void setDirectionLeft(boolean directionLeft) {
        this.directionLeft = directionLeft;
    }


    /*
     * отрисовка фрейма персонажа
     */
    public void drawCharacter(SpriteBatch batch){

        TextureRegion currentTexture = this.getFrame();

        checkFlip(currentTexture);

        batch.draw(currentTexture, currentX - currentTexture.getRegionWidth()/2.0f,
                                        currentY - currentTexture.getRegionHeight()/2.0f + 15);
    }

    /*
     * проверка смены направления в зависимости от нажатой клавиши пользователем
     */
    private void checkFlip(TextureRegion texture){
        if (directionLeft) {
            texture.flip(!texture.isFlipX(), false);
        } else {
            texture.flip(texture.isFlipX(), false);
        }
    }

    public CharStatus getCurrentStatus() {
        return currentStatus;
    }

    public void dispose(){
        charTextures.dispose();
    }
}
