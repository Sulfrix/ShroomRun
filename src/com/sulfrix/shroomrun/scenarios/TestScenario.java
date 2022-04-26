package com.sulfrix.shroomrun.scenarios;

import com.sulfrix.sulfur.Scenario;
import com.sulfrix.sulfur.entity.Camera;
import com.sulfrix.shroomrun.entities.PhysicsTest;
import com.sulfrix.shroomrun.entities.Tile;
import processing.core.PGraphics;
import processing.core.PVector;

public class TestScenario extends Scenario {

    public Camera testCam;

    public PhysicsTest testEnt;

    @Override
    public void init() {
        testCam = new Camera(new PVector(0, -30));
        world.AddEntity(testCam);
        world.camera = testCam;
        testEnt = new PhysicsTest(new PVector(0, -30));
        world.AddEntity(testEnt);
        for (int o = 0; o < 1; o++) {
            for (int i = 0; i < 100; i++) {
                //world.AddEntity(new Tile(new PVector(i * 30, (float) ((Math.ceil((i*5.0)/30.0)+o)*30)), "rock.png"));
                world.AddEntity(new Tile(new PVector(i * 30, (float) ((Math.ceil((i*-5.0)/30.0)+o+3)*30)), "rock.png"));
                world.AddEntity(new Tile(new PVector(i * 30, (float) ((Math.ceil((i*-5.0)/30.0)+o-2)*30)), "rock.png"));
                //world.AddEntity(new Tile(new PVector(i * 30, o*30, -i*1), "rock.png"));
            }
        }
    }

    @Override
    public void update(double timescale) {
        if (!input.KeyPressed(32)) {
            if (input.KeyPressed(39)) {
                testCam.position.x += 10 * timescale;
            }
            if (input.KeyPressed(37)) {
                testCam.position.x -= 10 * timescale;
            }
            if (input.KeyPressed(38)) {
                testCam.position.y -= 10 * timescale;
            }
            if (input.KeyPressed(40)) {
                testCam.position.y += 10 * timescale;
            }
            testEnt.position = testCam.position;
        } else {
            testCam.position = testEnt.position;
        }

        super.update(timescale);
    }

    @Override
    public void draw(double timescale, PGraphics g) {
        g.push();
        //g.rotateY(((float)(input.mouseX-g.width/2))/100f);
        //testCam.position.x += input.mouseX-input.pmouseX;
        //testCam.position.y += input.mouseY-input.pmouseY;
        super.draw(timescale, g);
        g.pop();
        g.line(g.width/2f, 0, g.width/2f, g.height);
        g.line(0, g.height/2f, g.width, g.height/2f);
    }
}
