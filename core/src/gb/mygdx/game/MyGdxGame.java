package gb.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	private int clk;

	private CharAnimation character;

	
	@Override
	public void create () {
		batch = new SpriteBatch();

		character = new CharAnimation("fighter.png", 9, 5, Animation.PlayMode.LOOP,
							Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f + 40);

	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);

		character.setDeltaTime(Gdx.graphics.getDeltaTime());
//		float mouseX = Gdx.input.getX() - animation.getFrame().getRegionWidth()/2;
//		float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY() - animation.getFrame().getRegionHeight()/ 2;

		if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			clk++;
		}

		checkChangingOfDirection();

		Gdx.graphics.setTitle("Clicked " + clk + " times! [Direction to left = " + character.isDirectionLeft() + "]");

		batch.begin();
		character.drawCharacter(batch);
		batch.end();
	}

	private void checkChangingOfDirection(){
		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) character.setDirectionLeft(true);
		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) character.setDirectionLeft(false);
	}



	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		character.dispose();
	}
}
