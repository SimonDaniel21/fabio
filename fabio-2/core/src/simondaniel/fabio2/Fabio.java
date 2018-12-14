package simondaniel.fabio2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import simondaniel.fabio2.gfx.AnimatedSprite;

public class Fabio {
	Body body;
	
	AnimatedSprite as;
	
	private Direction dir;
	private State state;
	
	public Fabio(World w) {
		
		dir = Direction.RIGHT;
		state = State.STANDING;
		
		
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("gfx/atlases/fabio/fabio2.atlas"));
		
		
		as = new AnimatedSprite(atlas, new String[] {"hit", "walk", "crouch", "jump", "side"});
		Array<AtlasRegion> region = atlas.getRegions();
		for(AtlasRegion r : region) {
			System.out.println("r: " + r.name);
		}
		
		BodyDef bDef = new BodyDef();
		bDef.position.x = 0;
		bDef.position.y = 3;
		bDef.fixedRotation = true;
		bDef.type = BodyType.DynamicBody;
		body = w.createBody(bDef);
		CircleShape cs = new CircleShape();
		cs.setRadius(0.9f);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = cs;
		fdef.restitution = 0;
		body.createFixture(fdef);
		
		setState(State.STANDING);
	}
	
	private void setState(State s) {
		switch (s) {
		case CROUCH:
			as.runAnimation(2);
			break;
		case HIT:
			as.runAnimation(0);
			break;
		case JUMP:
			as.runOnce(3, false);
			break;
		case STANDING:
			as.runAnimation(4);
			break;
		case WALKING:
			as.runAnimation(1);

		default:
			break;
		}
	}
	int i = 400;

	public void update(float delta) {
		as.update(delta);
		i++;
		
		if(i > 200 && Gdx.input.isKeyJustPressed(Keys.W)) {
			i = 0;
		}
		
		if(i == 0) {
			setState(State.JUMP);
		}
		
		if(i == 14) {
			body.setLinearVelocity(body.getLinearVelocity().x, 6);
			
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			body.applyForceToCenter(new Vector2(-10,0), true);
			dir = Direction.LEFT;
			setState(State.WALKING);
		}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			body.applyForceToCenter(new Vector2(0,0), true);
			setState(State.CROUCH);
		}
		if(Gdx.input.isKeyPressed(Keys.D)) {
			body.applyForceToCenter(new Vector2(10,0), true);
			dir = Direction.RIGHT;
			setState(State.WALKING);
		}
		
		as.setPosition(body.getPosition().cpy().add(0, 0.4f));
	}
	public Vector2 getPosition() {
		return body.getPosition();
	}
	public void draw(SpriteBatch batch) {
		as.setScale(1/MyGame.PPM);
		as.setFlip(dir == Direction.LEFT, false);
		as.draw(batch);
	}
	
	private enum State{
		WALKING, STANDING, HIT, JUMP, CROUCH;
	}
	
	private enum Direction{
		LEFT, RIGHT;
	}
}
