package com.company;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.universe.SimpleUniverse;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.media.j3d.*;

import javax.swing.*;
import javax.vecmath.Color3f;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

public class mySimpleUniverse extends JFrame implements ActionListener {
    private Canvas3D canvas3D;

    public BranchGroup createSceneGraph(boolean isInteractive, boolean isRandom) {
        // Create the root of the branch graph
        BranchGroup objRoot = new BranchGroup();

        // Create the TransformGroup node and initialize it to the
        // identity. Enable the TRANSFORM_WRITE capability so that
        // our behavior code can modify it at run time. Add it to
        // the root of the subgraph.
        TransformGroup objTrans = new TransformGroup();
        Transform3D t3dTrans = new Transform3D();
        t3dTrans.setTranslation(new Vector3d(0, 0, -1));
        objTrans.setTransform(t3dTrans);

        TransformGroup objRot = new TransformGroup();
        objRot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objRoot.addChild(objTrans);
        objTrans.addChild(objRot);


        // Create a simple Shape3D node; add it to the scene graph.
        // issue 383: changed the cube to a text, so that any graphical problem related to Yup can be seen.
        Font3D f3d = new Font3D(new Font("dialog", Font.PLAIN, 1),
                new FontExtrusion());
        Text3D text = new Text3D(f3d, "JCanvas3D",
                new Point3f( -2.3f, -0.5f, 0.f));

        Shape3D sh = new Shape3D();
        Appearance app = new Appearance();
        Material mm = new Material();
        mm.setLightingEnable(true);
        app.setMaterial(mm);
        sh.setGeometry(text);
        sh.setAppearance(app);

        objRot.addChild( sh );

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
                100.0);

        // Set up the ambient light
        Color3f ambientColor = new Color3f(0.3f, 0.3f, 0.3f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        objRoot.addChild(ambientLightNode);

        // Set up the directional lights
        Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
        Vector3f light1Direction  = new Vector3f(1.0f, 1.0f, 1.0f);
        Color3f light2Color = new Color3f(1.0f, 1.0f, 0.9f);
        Vector3f light2Direction  = new Vector3f(-1.0f, -1.0f, -1.0f);

        DirectionalLight light1
                = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        objRoot.addChild(light1);

        DirectionalLight light2
                = new DirectionalLight(light2Color, light2Direction);
        light2.setInfluencingBounds(bounds);
        objRoot.addChild(light2);


        if (true == isInteractive) {
            MouseRotate mr = new MouseRotate(canvas3D, objRot);
            mr.setSchedulingBounds(bounds);
            mr.setSchedulingInterval(1);
            objRoot.addChild(mr);
        } else {
            // Create a new Behavior object that will perform the
            // desired operation on the specified transform and add
            // it into the scene graph.
            Transform3D yAxis = new Transform3D();

            // rotation speed is randomized a bit so that it does not go at the same speed on every canvases,
            // which will make it more natural and express the differences between every present universes
            Alpha rotationAlpha = null;

            if (true == isRandom) {
                int duration = Math.max(2000, (int) (Math.random() * 8000.));
                rotationAlpha = new Alpha(-1,
                        (int) ((double) duration * Math.random()), 0, duration,
                        0, 0);
            } else {
                rotationAlpha = new Alpha(-1, 4000);
            }

            RotationInterpolator rotator = new RotationInterpolator(rotationAlpha,
                    objRot, yAxis, 0.0f, (float) Math.PI * 2.0f);

            rotator.setSchedulingBounds(bounds);
            objRoot.addChild(rotator);
        }

        return objRoot;
    }
    public void actionPerformed (ActionEvent e){
        ;
    }
}
