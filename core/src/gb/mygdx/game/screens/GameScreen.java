package gb.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import gb.mygdx.game.character.CharAnimation;
import gb.mygdx.game.MainClass;
import gb.mygdx.game.PhysX;

public class GameScreen implements Screen {

    private final MainClass game;
    private final SpriteBatch batch;
    private final PhysX physX;
    private CharAnimation character;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private final float STEP = 15f;
    private Rectangle mapSize;
    private ShapeRenderer mapBorder;
    private int[] bg;
    private int[] l1;
    private Body heroBody;


    public GameScreen(MainClass game) {
        this.game = game;
        this.physX = new PhysX();
        this.batch = new SpriteBatch();

        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1.60f;

        loadGameMap();
        loadHero();

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

        Array<RectangleMapObject> objects = map.getLayers().get("textures").getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < objects.size; i++) {
            physX.addObject(objects.get(i));
        }

    }

    private void loadHero(){
        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("setting").getObjects().get("hero");
        this.heroBody = physX.addObject(tmp);

        this.character = new CharAnimation(Animation.PlayMode.LOOP, heroBody.getPosition().x, heroBody.getPosition().y);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))    heroBody.applyForceToCenter(new Vector2(-1000000, 0), true);
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))   heroBody.applyForceToCenter(new Vector2(1000000, 0), true);
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))      heroBody.applyForceToCenter(new Vector2(0, 5000000), true);
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))    heroBody.applyForceToCenter(new Vector2(0, -1000000), true);
        //zoom
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_SUBTRACT))    camera.zoom +=0.1f;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ADD))    camera.zoom -=0.1f;



        camera.position.x = heroBody.getPosition().x;
        camera.position.y = heroBody.getPosition().y;
        camera.update();

        ScreenUtils.clear(Color.SKY);

        renderMap();

        physX.step();
        physX.debugDraw(camera);

        renderHero();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            dispose();
            game.setScreen(new MenuScreen(game));
        }
    }

    private void renderHero() {
        //character.changeCharStatus(heroBody.getLinearVelocity());   // вектор ускрения
        System.out.println(heroBody.getLinearVelocity());
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

    }
}
