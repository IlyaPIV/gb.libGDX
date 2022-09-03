package gb.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PhysX {
    private final World world;
    private final Box2DDebugRenderer debugRenderer;

    public PhysX() {
        //this.world = new World(new Vector2(0, -9.81f), true);
        this.world = new World(new Vector2(0, -13), true);
        world.setContactListener(new MyContactListener());
        this.debugRenderer = new Box2DDebugRenderer();
    }

    public void setGravity(Vector2 gravity){
        world.setGravity(gravity);
    }

    public void step(){
        world.step(1/60f, 3, 3);
    }

    public void debugDraw(OrthographicCamera camera){
        debugRenderer.render(world, camera.combined);
    }

    public void dispose(){
        world.dispose();
        debugRenderer.dispose();
    }

    public Body addObject(RectangleMapObject obj, String objName, boolean addGhost){
        return prepareBody(obj, objName,0.5f, 1f, 0.1f, addGhost);
    }

    public void addTextures(RectangleMapObject obj){
        prepareBody(obj, "textures", 0.4f, 1f, 0f, false);
    }

    public void addTextures(PolygonMapObject obj){
       // prepareBody(obj, "textures", 0.4f, 1f, 0f, false);
    }

    public Body prepareBody(RectangleMapObject object, String objName,
                            float friction, float density, float restitution, boolean addSensor) {
        Rectangle rect = object.getRectangle();
        String type = (String) object.getProperties().get("BodyType");
        float gravityScale = (Float) object.getProperties().get("gravityScale");

        BodyDef def = new BodyDef();
        FixtureDef fixDef = new FixtureDef();               // текстура

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(rect.width/2, rect.height/2); //так надо - половинные значения в параметры

        fixDef.shape = polygonShape;
        fixDef.friction = friction;            // трение (0 - лёд)
        fixDef.density = density;             // плотность
        fixDef.restitution = restitution;         // упругость

        def.type = BodyDef.BodyType.valueOf(type);
        def.position.set(rect.x + rect.width/2, rect.y + rect.height/2);
        def.gravityScale = gravityScale;

        Body body;
        body = world.createBody(def);
        body.createFixture(fixDef).setUserData(objName);
        if (addSensor) {
            polygonShape.setAsBox(rect.width/10, rect.height/10,
                                        new Vector2(0, -rect.width/2), 0);
            body.createFixture(fixDef).setUserData(objName+"_sensor");
            body.getFixtureList().get(body.getFixtureList().size-1).setSensor(true);
        }

        polygonShape.dispose();
        return body;
    }

    public void destroyBody(Body body){
        world.destroyBody(body);
    }
}
