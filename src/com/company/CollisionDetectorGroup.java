package com.company;

import javax.media.j3d.*;
import java.util.Enumeration;

public class CollisionDetectorGroup extends Behavior {


    private boolean inCollision = false;
    private Group group;

    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;


    public CollisionDetectorGroup(Group group_given, Bounds bounds) {
        group = group_given;
        group_given.setCollisionBounds(bounds);
        inCollision = false;
    }

    public void initialize() {
        wEnter = new WakeupOnCollisionEntry(group);
        wExit = new WakeupOnCollisionExit(group);
        wakeupOn(wEnter);
    }

    public void processStimulus(Enumeration criteria) {
        inCollision = !inCollision;
        WakeupCriterion theCriterion = (WakeupCriterion) criteria.nextElement();

        if (inCollision) {

            System.out.println("Collided with " + group.getUserData());
            wakeupOn(wExit);
        }
        else {
            System.out.println("nie ma kolizji");
            wakeupOn(wEnter);
        }
    }

}
