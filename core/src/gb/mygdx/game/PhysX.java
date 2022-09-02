package gb.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PhysX {
    private final World world;
    private final Box2DDebugRenderer debugRenderer;

    public PhysX() {
        this.world = new World(new Vector2(0, -9.81f), true);
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

    public Body addObject(RectangleMapObject obj){
        Rectangle rect = obj.getRectangle();
        String type = (String) obj.getProperties().get("BodyType");
        float gravityScale = (Float) obj.getProperties().get("gravityScale");


        BodyDef def = new BodyDef();
        FixtureDef fixDef = new FixtureDef();               // текстура
        PolygonShape polygonShape = new PolygonShape();

        def.type = BodyDef.BodyType.valueOf(type);
        def.position.set(rect.x + rect.width/2, rect.y + rect.height/2);
        def.gravityScale = gravityScale;

        polygonShape.setAsBox(rect.width/2, rect.height/2); //так надо - половинные значения в параметры

        fixDef.shape = polygonShape;
        fixDef.friction = 0.3f;            // трение (0 - лёд)
        fixDef.density = 1;             // плотность
        fixDef.restitution = 0;         // упругость

        Body body;
        body = world.createBody(def);
        body.createFixture(fixDef).setUserData("поверхность");

        polygonShape.dispose();

        return body;
    }
}
