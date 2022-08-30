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
import com.badlogic.gdx.utils.ScreenUtils;
import gb.mygdx.game.CharAnimation;
import gb.mygdx.game.MainClass;

public class GameScreen implements Screen {

    private MainClass game;
    private SpriteBatch batch;
    private CharAnimation character;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private final float STEP = 10f;
    private Rectangle mapSize;
    private ShapeRenderer mapBorder;


    public GameScreen(MainClass game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.character = new CharAnimation("fighter.png", 9, 5, Animation.PlayMode.LOOP,
                                           Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f + 40);
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.map = new TmxMapLoader().load("maps/gamemap.tmx");
        this.mapRenderer = new OrthogonalTiledMapRenderer(map);

        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("obj").getObjects().get("camera");
        camera.position.x = tmp.getRectangle().x;
        camera.position.y = tmp.getRectangle().y;

        tmp = (RectangleMapObject) map.getLayers().get("obj").getObjects().get("border");
        mapSize = tmp.getRectangle();

        this.mapBorder = new ShapeRenderer();


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)
                        && mapSize.x < camera.position.x - 1)    camera.position.x -= STEP;
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)
                        && (mapSize.x + mapSize.width) > (camera.position.x + 1))   camera.position.x += STEP;
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)
                        && mapSize.y > camera.position.y - 1)      camera.position.y += STEP;
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)
                        && (mapSize.y - mapSize.height) < (camera.position.y + 1))    camera.position.y -= STEP;
        //zoom
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_SUBTRACT))    camera.zoom +=0.1f;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ADD))    camera.zoom -=0.1f;



        camera.update();

        ScreenUtils.clear(Color.SKY);

        character.setDeltaTime(Gdx.graphics.getDeltaTime());
        character.checkChangingOfDirection();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        character.drawCharacter(batch);
        batch.end();


        mapBorder.setProjectionMatrix(camera.combined);
        mapBorder.begin(ShapeRenderer.ShapeType.Line);
        mapBorder.setColor(Color.GOLD);
        mapBorder.rect(mapSize.x, mapSize.y, mapSize.width, mapSize.height);
        mapBorder.end();

        mapRenderer.setView(camera);
        mapRenderer.render();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            dispose();
            game.setScreen(new MenuScreen(game));
        }
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
      //  map.dispose();
    }
}
