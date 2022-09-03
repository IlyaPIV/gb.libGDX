package gb.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.*;
import gb.mygdx.game.screens.GameScreen;

public class MyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("hero") || tmpB.equals("hero")) {
                if (tmpB.equals("trap")) { GameScreen.action = GameAction.TRAP; }
                if (tmpA.equals("trap")) { GameScreen.action = GameAction.TRAP; }
                if (tmpB.equals("death")) { GameScreen.action = GameAction.DEATH; }
                if (tmpA.equals("death")) { GameScreen.action = GameAction.DEATH; }
                if (tmpA.equals("finish")) { GameScreen.action = GameAction.WIN; }
                if (tmpB.equals("finish")) { GameScreen.action = GameAction.WIN; }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
