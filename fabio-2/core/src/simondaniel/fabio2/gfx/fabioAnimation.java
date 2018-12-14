package simondaniel.fabio2.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class fabioAnimation {
	
	private static final int HIT = 0, WALK = 1, CROUCH = 2, STAND_UP = 3, JUMP_UP = 4, JUMP_DOWN = 5, SIDE = 6;
	
	float elapsedTime;
	int current;
	Animation<TextureRegion>[] animations;

	private boolean halted;
	private int haltAt = -1;
	
	Sprite sprite;
	
	boolean isIdle;

	@SuppressWarnings("unchecked")
	public fabioAnimation() {
		
		sprite = new Sprite();
		
		this.animations = new Animation[7];
		
		String[] animNames = new String[] {"hit", "walk", "crouch", "jump", "side"};
		
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("gfx/atlases/fabio/fabio2.atlas"));
		
		Array<AtlasRegion> region = atlas.getRegions();
		for(AtlasRegion r : region) {
			System.out.println("r: " + r.name);
		}
		
		Animation<TextureRegion> animation = new Animation<TextureRegion>(1f / 3f, atlas.findRegions("hit"));
		animation.setPlayMode(PlayMode.LOOP);
		animation.setFrameDuration(0.14f);
		this.animations[HIT] = animation;
		
		animation = new Animation<TextureRegion>(1f / 3f, atlas.findRegions("walk"));
		animation.setPlayMode(PlayMode.LOOP);
		animation.setFrameDuration(0.2f);
		this.animations[WALK] = animation;
		
		animation = new Animation<TextureRegion>(1f / 3f, atlas.findRegions("crouch"));
		animation.setPlayMode(PlayMode.NORMAL);
		animation.setFrameDuration(0.12f);
		this.animations[CROUCH] = animation;
		
		animation = new Animation<TextureRegion>(1f / 3f, atlas.findRegions("crouch"));
		animation.setPlayMode(PlayMode.REVERSED);
		animation.setFrameDuration(0.12f);
		this.animations[STAND_UP] = animation;
		
		animation = new Animation<TextureRegion>(1f / 3f, atlas.findRegions("jump"));
		animation.setPlayMode(PlayMode.NORMAL);
		animation.setFrameDuration(0.09f);
		this.animations[JUMP_UP] = animation;
		
		animation = new Animation<TextureRegion>(1f / 3f, atlas.findRegions("jump"));
		animation.setPlayMode(PlayMode.REVERSED);
		animation.setFrameDuration(0.09f);
		this.animations[JUMP_DOWN] = animation;
		
		animation = new Animation<TextureRegion>(1f / 3f, atlas.findRegions("side"));
		animation.setPlayMode(PlayMode.LOOP);
		animation.setFrameDuration(0.15f);
		this.animations[SIDE] = animation;

		current = SIDE;
		Animation<TextureRegion> currentAnimation = animations[current];
		sprite.setRegion(currentAnimation.getKeyFrames()[0]);
		sprite.setSize(currentAnimation.getKeyFrames()[0].getRegionWidth(), currentAnimation.getKeyFrames()[0].getRegionHeight());
		sprite.setOriginCenter();
	}

	public void update(float delta) {
		if (!halted) {
			elapsedTime += delta;
			
			TextureRegion frame = animations[current].getKeyFrame(elapsedTime);
			sprite.setRegion(frame);
			switch (current) {
			case JUMP_DOWN:
				if(animations[current].isAnimationFinished(elapsedTime)) {
					startIdle();
					
				}
				
				break;
			case HIT:
				if(animations[current].isAnimationFinished(elapsedTime/2)) {
					startWalk();
				}
			default:
				break;
			}
			//setSize(getRegionWidth()*1.0f, getRegionHeight()*0.65f);
			//setOriginCenter();
		
			if (animations[current].getKeyFrameIndex(elapsedTime) == haltAt) {
				halted = true;
			}
		}

	}

	public void setPosition(float x, float y) {
		sprite.setPosition(x - sprite.getOriginX(), y - sprite.getOriginY());
	}
	
	public void setPosition(Vector2 position) {
		setPosition(position.x, position.y);
	}
	
	public void drawCentered(SpriteBatch sb) {
		sprite.draw(sb);
	}
	
	public void startWalk() {
		if(current == JUMP_UP || current == JUMP_DOWN) return;
		elapsedTime = 0;
		current = WALK;
	}

	public void startCrouch() {
		elapsedTime = 0;
		current = CROUCH;
	}
	
	public void jumpUp() {
		elapsedTime = 0;
		current = JUMP_UP;
	}
	
	public void fallDown() {
		elapsedTime = 0;
		current = JUMP_DOWN;
	}
	
	public void performHit() {
		elapsedTime = 0;
		current = HIT;
	}
	
	public void startIdle() {
		isIdle = true;
	}
	
	public void runOnce(int i, boolean loop) {
		if (i < 0 || i >= animations.length)
			return;
		elapsedTime = 0;
		haltAt = -1;
		halted = false;
		current =  i;
		animations[i].setPlayMode(loop? PlayMode.LOOP : PlayMode.NORMAL);
	}

	public void haltAnimation() {
		halted = true;
	}

	public void haltAnimation(int frame) {
		haltAt = frame;
	}

	public void drawOutline(ShapeRenderer sr) {
		
		sr.rect(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
	}

	public void setDirection(boolean isLeft) {
		sprite.setFlip(isLeft, false);
	}

	public void setScale(float f) {
		sprite.setScale(f);
	}

	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}


	
}
