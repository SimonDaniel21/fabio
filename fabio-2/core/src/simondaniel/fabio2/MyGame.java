package simondaniel.fabio2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import simondaniel.fabio2.gfx.AnimatedSprite;

public class MyGame extends ApplicationAdapter {
	
	public static final float PPM = 80;
	public static final int TILE_SIZE = 16;
	public static final float METERS_PER_TILE = 1f;
	public static final float TILED_MAP_UPSCALE = (PPM/TILE_SIZE) * METERS_PER_TILE;
	public static final float PPT = TILE_SIZE*TILED_MAP_UPSCALE;
	
	SpriteBatch batch;
	Texture img;
	World w;
	Box2DDebugRenderer dr;
	ShapeRenderer sr;
	OrthographicCamera cam;
	
	Fabio player;
	
	TiledMap map;
	TiledMapRenderer mapRenderer;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		sr.setAutoShapeType(true);
		img = new Texture("badlogic.jpg");
		
		
		w = new World(new Vector2(0, -9f), true);
		cam = new OrthographicCamera(Gdx.graphics.getWidth()/PPM, Gdx.graphics.getHeight()/PPM);
		
		player = new Fabio(w);
		
		System.out.println("Tiled upscale: " + PPT);
		
		dr = new Box2DDebugRenderer();

		map = new TmxMapLoader().load("maps/level1.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, TILED_MAP_UPSCALE / PPM);
		mapRenderer.setView(cam);
		
		Body body;
		PolygonShape shape = new PolygonShape();
		BodyDef bDef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth() / 2) / PPM * TILED_MAP_UPSCALE, (rect.getY() + rect.getHeight() / 2) / PPM * TILED_MAP_UPSCALE );

            body = w.createBody(bDef);

            shape.setAsBox(rect.getWidth() / 2 / PPM * TILED_MAP_UPSCALE, rect.getHeight() / 2 / PPM * TILED_MAP_UPSCALE);
            fdef.shape = shape;
            body.createFixture(fdef);
		}
		
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth() / 2) / PPM * TILED_MAP_UPSCALE, (rect.getY() + rect.getHeight() / 2) /PPM * TILED_MAP_UPSCALE);

            body = w.createBody(bDef);

            shape.setAsBox(rect.getWidth() / 2 / PPM * TILED_MAP_UPSCALE, rect.getHeight() / 2 / PPM * TILED_MAP_UPSCALE);
            fdef.shape = shape;
            body.createFixture(fdef);
}
	}
	
	int i = 0;
	int j = 0;
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		i++;
		
		
	
				
		w.step(Gdx.graphics.getDeltaTime(), 6, 2);
		cam.position.x = player.getPosition().x;
		cam.position.y = player.getPosition().y;
		cam.update();
		//System.out.println("body at " +  body.getPosition().x);
		
		player.update(Gdx.graphics.getDeltaTime());
		
		mapRenderer.setView(cam);
		mapRenderer.render();
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		//batch.draw(img, 0, 0);
	
		player.draw(batch);
		
		batch.end();
		
		sr.setProjectionMatrix(cam.combined);
		sr.begin();
		//as.drawOutline(sr);
		sr.end();
		
		dr.render(w, batch.getProjectionMatrix());
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
