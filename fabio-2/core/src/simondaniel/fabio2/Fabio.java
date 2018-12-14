package simondaniel.fabio2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import actions.Action;
import actions.ActionPool;
import actions.ActionSequence;
import actions.WaitAction;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import simondaniel.fabio2.gfx.fabioAnimation;

public class Fabio {
	Body body;
	
	fabioAnimation as;
	
	private Direction dir;
	private State state;
	
	ActionPool actions;
	
	public Fabio(World w) {
		
		actions = new ActionPool();
		
		dir = Direction.RIGHT;
		state = State.STANDING;
		
		
		
		as = new fabioAnimation();
		
		
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
	
	State lastState;
	
	private void setState(State s) {
		switch (s) {
		case CROUCH:
			as.startCrouch();
			break;
		case HIT:
			as.performHit();
			break;
		case JUMP:
			as.jumpUp();
			break;
		case FALL:
			if(lastState != State.FALL)
				as.fallDown();
			break;
		case STANDING:
			as.startIdle();
			break;
		case WALKING:
			if(lastState != State.WALKING)
				as.startWalk();

		default:
			break;
		}
		lastState = s;
	}
	int i = 400;

	public void update(float delta) {
		as.update(delta);
		i++;
		
		if(i > 100 && Gdx.input.isKeyJustPressed(Keys.W)) {
			i = 0;
		}
		
		if(i == 0) {
			setState(State.JUMP);
		}
		
		if(i == 14) {
			body.setLinearVelocity(body.getLinearVelocity().x, 6);
		}
		if( i == 80) {
			setState(State.FALL);
		}
		boolean idle = true;
		if(Gdx.input.isKeyPressed(Keys.A)) {
			//body.applyForceToCenter(new Vector2(-10,0), true);
			body.setLinearVelocity(-3, body.getLinearVelocity().y);
			dir = Direction.LEFT;
			setState(State.WALKING);
			idle = false;
		}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			//setState(State.CROUCH);
			//idle = false;
			actions.runAction(new ActionSequence(new WaitAction(4.3f),
					new Action() {
						
						@Override
						public void update(float delta) {
							System.err.println("action executed");
						}
						
						@Override
						public boolean isFinished() {
							return true;
						}
					}));
		}
		if(Gdx.input.isKeyPressed(Keys.D)) {
			body.setLinearVelocity(3, body.getLinearVelocity().y);
			dir = Direction.RIGHT;
			setState(State.WALKING);
			idle = false;
		}
		if(idle)
			setState(State.STANDING);
		
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)) {
	
			setState(State.HIT);
		}
		
		actions.update(delta);
		
		as.setPosition(body.getPosition().cpy().add(0, 0.4f));
	}
	public Vector2 getPosition() {
		return body.getPosition();
	}
	public void draw(SpriteBatch batch) {
		as.setScale(1/MyGame.PPM);
		as.setDirection(dir == Direction.LEFT);
		as.draw(batch);
	}
	
	private enum State{
		WALKING, STANDING, HIT, JUMP, FALL, CROUCH, STAND_UP;
	}
	
	private enum Direction{
		LEFT, RIGHT;
	}
}
