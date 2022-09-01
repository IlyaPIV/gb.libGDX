package gb.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Anim {
    private Texture img;
    private Animation<TextureRegion> anm;
    private float deltaTime;

    public Anim(String name, int col, int row, Animation.PlayMode playMode){
        img = new Texture(name);
        TextureRegion region0 = new TextureRegion(img);

        int xCnt = region0.getRegionWidth() / col;
        int yCnt = region0.getRegionHeight() / row;

        TextureRegion[][] regions = region0.split(xCnt, yCnt);

        TextureRegion[] region1 = new TextureRegion[regions.length * regions[0].length];
        int cnt = 0;
        for (TextureRegion[] region : regions) {
            for (int j = 0; j < regions[0].length; j++) {
                region1[cnt++] = region[j];
            }
        }
        anm = new Animation<TextureRegion>(1/15f, region1);
        anm.setPlayMode(playMode);

        deltaTime += Gdx.graphics.getDeltaTime();
    }

    /*
     * возвращает кадр
     */
    public TextureRegion getFrame(){
        return anm.getKeyFrame(deltaTime);
    }

    /*
     *
     */
    public void setDeltaTime(float deltaTime) { this.deltaTime += deltaTime; }

    public void zeroDeltaTime() { this.deltaTime = 0; }

    public boolean isAnimationOver(){
        return anm.isAnimationFinished(deltaTime);
    }

    public void setPlayMode(Animation.PlayMode playMode) {
        this.anm.setPlayMode(playMode);
    }

    public void dispose(){
        img.dispose();
    }

}
