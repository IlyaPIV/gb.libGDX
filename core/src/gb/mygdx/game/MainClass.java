package gb.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import gb.mygdx.game.screens.MenuScreen;

public class MainClass extends Game {

    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }


    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
