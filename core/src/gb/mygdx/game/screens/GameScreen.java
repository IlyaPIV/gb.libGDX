package gb.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import gb.mygdx.game.GameAction;
import gb.mygdx.game.character.CharAnimation;
import gb.mygdx.game.MainClass;
import gb.mygdx.game.PhysX;
import gb.mygdx.game.character.CharStatus;

import java.util.ArrayList;

public class GameScreen implements Screen {

    private final MainClass game;
    private boolean isPlaying;
    private final SpriteBatch batch;
    private final PhysX physX;
    private final Music backgroundMusic;
    private Music soundEffect;
    private CharAnimation character;
    private final OrthographicCamera camera;
    private TiledMap map;
    private Texture img;
    private OrthogonalTiledMapRenderer mapRenderer;
    private final float STEP = 15f;
    private Rectangle mapSize;
    private ShapeRenderer mapBorder;
    private int[] bg;
    private int[] l1;
    private Body heroBody;
    public static ArrayList<Body> destroyList;
    public static GameAction action;


    public GameScreen(MainClass game) {
        this.game = game;
        this.isPlaying = true;
        this.physX = new PhysX();
        this.batch = new SpriteBatch();

        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1.60f;

        loadGameMap();
        loadHero();
        loadSetting();

        destroyList = new ArrayList<Body>();

        this.backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/gameplay_music.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    private void loadGameMap(){
        this.map = new TmxMapLoader().load("maps/gamemap.tmx");
        this.mapRenderer = new OrthogonalTiledMapRenderer(map);

        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("obj").getObjects().get("border");
        this.mapSize = tmp.getRectangle();

        this.mapBorder = new ShapeRenderer();

        this.bg = new int[1];
        bg[0] = map.getLayers().getIndex("background");
        this.l1 = new int[2];
        l1[0] = map.getLayers().getIndex("layer_up");
        l1[1] = map.getLayers().getIndex("layer_down");

        Array<RectangleMapObject> objects1 = map.getLayers().get("textures").getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < objects1.size; i++) {
            physX.addTextures(objects1.get(i));
        }
        Array<PolygonMapObject> objects2 = map.getLayers().get("textures").getObjects().getByType(PolygonMapObject.class);
        for (int i = 0; i < objects2.size; i++) {
            physX.addTextures(objects2.get(i));
        }

    }

    private void loadHero(){
        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("setting").getObjects().get("hero");
        this.heroBody = physX.addObject(tmp, "hero", true);

        this.character = new CharAnimation(Animation.PlayMode.LOOP, heroBody.getPosition().x, heroBody.getPosition().y);
    }

    private void loadSetting(){

        MapObjects mapObjects = map.getLayers().get("setting").getObjects();
        for (MapObject obj:
             mapObjects) {
             if (obj.getName()!=null && !obj.getName().equals("hero")) {

                physX.addObject((RectangleMapObject) obj, obj.getName(), false);
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

         if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            dispose();
            game.setScreen(new MenuScreen(game));
         }

         updateCamera();

         ScreenUtils.clear(Color.SKY);

         renderMap();
         if (img!=null) {
             batch.begin();
             batch.draw(img, heroBody.getPosition().x, heroBody.getPosition().y);
             batch.end();
         }
         if (isPlaying) {
             applyForceToHero();

             physX.step();
             physX.debugDraw(camera);

             renderHero();

             destroyBodies();
             activateActions();
         }
    }

    private void activateActions() {
        if (action!=null) {
            switch (action) {
                case TRAP: trapWasActivated();
                            break;
                case WIN: gameWin();
                    break;
                case DEATH: gameOver();
                    break;
                case COIN_PICK: break;
            }
            action = null;
        }

    }

    private void gameWin() {

        this.isPlaying = false;
        this.img = new Texture("win.png");

        backgroundMusic.stop();
        soundEffect = Gdx.audio.newMusic(Gdx.files.internal("sounds/win_sound.mp3"));
        soundEffect.play();

    }

    private void gameOver() {
        this.isPlaying = false;
        this.img = new Texture("game-over.png");

        backgroundMusic.stop();
        soundEffect = Gdx.audio.newMusic(Gdx.files.internal("sounds/death-sound.mp3"));
        soundEffect.play();
    }

    private void trapWasActivated() {
        soundEffect = Gdx.audio.newMusic(Gdx.files.internal("sounds/woosh.mp3"));
        soundEffect.play();

        Vector2 currentVector = heroBody.getLinearVelocity();
        heroBody.applyForceToCenter(new Vector2( -1000000 * currentVector.x, -1000000 * currentVector.y),
                                        true);
    }


    private void applyForceToHero() {
        if (character.getCurrentStatus()!=CharStatus.JUMP
                && character.getCurrentStatus()!=CharStatus.DUCK) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))    heroBody.applyForceToCenter(new Vector2(-5000000, 0), true);
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))   heroBody.applyForceToCenter(new Vector2(5000000, 0), true);

            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))      heroBody.applyForceToCenter(new Vector2(0, 25000000), true);
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))    heroBody.applyForceToCenter(new Vector2(0, -5000000), true);
        }

    }

    public void destroyBodies(){
        for (Body body:
                destroyList) {
            physX.destroyBody(body);
        }
        destroyList.clear();
    }

    private void updateCamera() {
        //zoom
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_SUBTRACT))    camera.zoom +=0.1f;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ADD))    camera.zoom -=0.1f;

        camera.position.x = heroBody.getPosition().x;
        camera.position.y = heroBody.getPosition().y;
        camera.update();
    }


    private void renderHero() {

        character.setDeltaTime(Gdx.graphics.getDeltaTime());
        character.setCurrentPosition(heroBody.getPosition().x, heroBody.getPosition().y);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        character.drawCharacter(batch);
        batch.end();
    }

    private void renderMap(){
        mapRenderer.setView(camera);
        mapRenderer.render(bg);
        mapRenderer.render(l1);

        mapBorder.setProjectionMatrix(camera.combined);
        mapBorder.begin(ShapeRenderer.ShapeType.Line);
        mapBorder.setColor(Color.GOLD);
        mapBorder.rect(mapSize.x, mapSize.y, mapSize.width, mapSize.height);
        mapBorder.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        character.dispose();
        backgroundMusic.dispose();
        if (soundEffect!=null) soundEffect.dispose();
        if (img!=null) img.dispose();
    }
}
