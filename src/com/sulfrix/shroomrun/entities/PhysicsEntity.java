package com.sulfrix.shroomrun.entities;

import com.sulfrix.shroomrun.Entity;
import com.sulfrix.shroomrun.lib.BoundingBox;
import processing.core.PVector;

public abstract class PhysicsEntity extends Entity {

    public double gravityMult = 1.0;

    /**
     * clock-wise, which sides have a collision
     */
    public boolean[] collisionSides = {false, false, false, false};

    public PhysicsEntity(PVector pos, BoundingBox bb) {
        super(pos, bb);
    }

    public void update(double timescale) {
        DoPhysics(timescale, 5);
    }

    public void DoPhysics(double ts, int substeps) {
        double timescale = 1.0/substeps;
        for (int i = 0; i < substeps; i++) {
            PhysicsStep(timescale*ts);
        }
    }

    void PhysicsStep(double timescale) {
        DoCollision(timescale);
        ApplyGravity(timescale);
        ApplyVelocity(timescale);
    }

    void DoCollision(double timescale) {
        for (Entity e: world.entities) {
            if (e != this) {
                if (e.collisionEnabled) {
                    if (velocity.y != 0) {
                        int vertSign = 1;
                        if (velocity.y < 0) {
                            vertSign = -1;
                        }

                        BoundingBox bb = new BoundingBox(boundingBox.width/1.2f, boundingBox.height);
                        PVector pos = PVector.add(position, new PVector(0, (float)(velocity.y*timescale)));

                        // vertical
                        if (BoundingBox.touching(bb, pos, e.boundingBox, e.position)) {
                            velocity.y = 0;
                            if (vertSign == 1) {
                                collisionSides[2] = true;
                                position.y = e.position.y - (e.boundingBox.height/2 + boundingBox.height/2);
                            } else {
                                collisionSides[0] = true;
                                position.y = e.position.y + (e.boundingBox.height/2 + boundingBox.height/2);
                            }
                            e.collide(this);



                        } else {
                            collisionSides[2] = false;
                            collisionSides[0] = false;
                        }


                    }
                    if (velocity.x != 0) {
                        int horizSign = 1;
                        if (velocity.x < 0) {
                            horizSign = -1;
                        }

                        BoundingBox bb = new BoundingBox(boundingBox.width, boundingBox.height/1.2f);
                        PVector pos = PVector.add(position, new PVector((float)(velocity.x*timescale), 0));

                        // horizontal
                        if (BoundingBox.touching(bb, pos, e.boundingBox, e.position)) {
                            velocity.x = 0;
                            if (horizSign == 1) {
                                collisionSides[1] = true;
                                position.x = e.position.x - (e.boundingBox.width/2 + boundingBox.width/2);
                            } else {
                                collisionSides[3] = true;
                                position.x = e.position.x + (e.boundingBox.width/2 + boundingBox.width/2);
                            }
                            e.collide(this);
                            break;
                        } else {
                            collisionSides[1] = false;
                            collisionSides[3] = false;
                        }
                    }
                } else if (e.isTrigger) {
                    if (BoundingBox.touching(boundingBox, position, e.boundingBox, e.position)) {
                        e.collide(this);
                    }
                }

            }
        }
    }

    void ApplyVelocity(double timescale) {
        position.add(PVector.mult(velocity, (float) timescale));
    }

    void ApplyGravity(double timescale) {
        velocity.add(new PVector(0, (float)(world.gravity*timescale*gravityMult)));
    }
}
