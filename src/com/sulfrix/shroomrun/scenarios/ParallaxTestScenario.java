package com.sulfrix.shroomrun.scenarios;

import com.sulfrix.sulfur.Scenario;
import com.sulfrix.sulfur.entity.Camera;
import com.sulfrix.shroomrun.entities.Tile;
import processing.core.PVector;

public class ParallaxTestScenario extends Scenario {
    Camera camera;

    @Override
    public void init() {
        camera = new Camera(new PVector(0, 0));
        world.AddEntity(camera);
        world.camera = camera;
        var tile1 = new Tile(new PVector(60, 60), "rock.png");
        world.AddEntity(tile1);
        tile1.ZPos = 0;
        var tile2 = new Tile(new PVector(60, 60), "rock.png");
        world.AddEntity(tile2);
        tile2.parallax = 4;
        tile2.ZPos = 4;

        var tile3 = new Tile(new PVector(60, 60), "rock.png");
        world.AddEntity(tile3);
        tile3.parallax = 8;
        tile3.ZPos = 8;
        for (int i = 0; i < 100; i++) {
            var tile = new Tile(new PVector(30*i, 0), "rock.png");
            tile.parallax = 4;
            tile.ZPos = 4;
            world.AddEntity(tile);
        }
    }

    @Override
    public void update(double timescale) {
        if (input.KeyPressed(39)) {
            camera.position.x += 10 * timescale;
        }
        if (input.KeyPressed(37)) {
            camera.position.x -= 10 * timescale;
        }
        if (input.KeyPressed(38)) {
            camera.position.y -= 10 * timescale;
        }
        if (input.KeyPressed(40)) {
            camera.position.y += 10 * timescale;
        }
        super.update(timescale);
    }
}
