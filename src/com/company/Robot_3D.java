package com.company;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;

import javax.media.j3d.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.Transform3D;
import javax.vecmath.*;

public class Robot_3D extends JFrame implements ActionListener {
    JButton start = new JButton();
    JButton dzwiek = new JButton();
    JButton reset_kamery = new JButton();
    JButton zacznij_nagrywanie = new JButton();
    JButton zakoncz_nagrywanie = new JButton();
    JButton odtworz_nagranie = new JButton();
    Canvas3D comp = createCanvas3D(new Dimension(1000, 700));
    SimpleUniverse simpleU;

    Robot_3D() {
        super("Robot_3D Sebastian Krajna Kacper Bober");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);


        setVisible(true);



        JPanel panelPrzyciski = stworzPanelPrzyciskow();
        add(BorderLayout.NORTH, panelPrzyciski);
        add(BorderLayout.WEST, addInstruction());
        add(BorderLayout.CENTER, comp);
        pack();     //dopasowuje ramke do elementow musi byc po dodaniu elementow

        BranchGroup scena = createSceneGraph(true);

        simpleU = new SimpleUniverse(comp);

        Transform3D przesuniecie_obserwatora = new Transform3D();
        przesuniecie_obserwatora.set(new Vector3f(0.0f,0.0f,10.0f));

        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);

       simpleU.addBranchGraph(scena);

    }

    public BranchGroup createSceneGraph(boolean isInteractive) {
        // Create the root of the branch graph
        BranchGroup tworzona_scena = new BranchGroup();

        // Create the TransformGroup node and initialize it to the
        // identity. Enable the TRANSFORM_WRITE capability so that
        // our behavior code can modify it at run time. Add it to
        // the root of the subgraph.
        TransformGroup objTrans = new TransformGroup();
        Transform3D t3dTrans = new Transform3D();
        t3dTrans.setTranslation(new Vector3d(0, 0, -1));
        objTrans.setTransform(t3dTrans);

        TransformGroup objRot = new TransformGroup(); //transformGroup tutaj tworzone do obslugi transformacji z myszki
        objRot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tworzona_scena.addChild(objTrans);
        objTrans.addChild(objRot);


        // Create a simple Shape3D node; add it to the scene graph.
        // issue 383: changed the cube to a text, so that any graphical problem related to Yup can be seen.

        //sekcja tworzenia napisu, tworzenie obiektów dobrze byłoby robić w oddzielnych funkcjach
        {
            Font3D f3d = new Font3D(new Font("dialog", Font.PLAIN, 1),
                    new FontExtrusion());
            Text3D text = new Text3D(f3d, "JCanvas3D",
                    new Point3f(-2.3f, -0.5f, 0.f));

            Shape3D sh = new Shape3D();
            Appearance app = new Appearance();
            Material mm = new Material();
            mm.setLightingEnable(true);
            app.setMaterial(mm);
            sh.setGeometry(text);
            sh.setAppearance(app);

            objRot.addChild(sh);
        }
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
                100.0);

        //sekcja oświetlenia
        {
            // Set up the ambient ligh
            Color3f ambientColor = new Color3f(0.3f, 0.3f, 0.3f);
            AmbientLight ambientLightNode = new AmbientLight(ambientColor);
            ambientLightNode.setInfluencingBounds(bounds);
            tworzona_scena.addChild(ambientLightNode);

            // Set up the directional lights
            Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
            Vector3f light1Direction = new Vector3f(1.0f, 1.0f, 1.0f);
            Color3f light2Color = new Color3f(1.0f, 1.0f, 0.9f);
            Vector3f light2Direction = new Vector3f(-1.0f, -1.0f, -1.0f);

            DirectionalLight light1
                    = new DirectionalLight(light1Color, light1Direction);
            light1.setInfluencingBounds(bounds);
            tworzona_scena.addChild(light1);

            DirectionalLight light2
                    = new DirectionalLight(light2Color, light2Direction);
            light2.setInfluencingBounds(bounds);
            tworzona_scena.addChild(light2);
        }

        //opcjonalnie można dodać przycisk w menu czy ma być interaktywne i na podstawie tego wywoływać tę funkcję
        if (isInteractive) {
            MouseWheelZoom m_zoom = new MouseWheelZoom(comp, objRot);
            m_zoom.setSchedulingBounds(bounds);
            tworzona_scena.addChild(m_zoom);
            MouseRotate mr = new MouseRotate(comp, objRot);
            mr.setSchedulingBounds(bounds);
            mr.setSchedulingInterval(1);
            tworzona_scena.addChild(mr);
        }

        return tworzona_scena;
    }
    /**Creates Canvas3D object with given dimensions **/
    public static Canvas3D createCanvas3D(Dimension dim) {
        GraphicsConfiguration config =
                SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        canvas3D.setPreferredSize(dim);
        return canvas3D;

    }

    /**Creates JPanel and initializes buttons **/
    public  JPanel stworzPanelPrzyciskow() {
        JPanel panel_menu = new JPanel(new FlowLayout());
        start.setText("Start");
        start.addActionListener(this );

        dzwiek.setText("Dzwiek");
        dzwiek.addActionListener(this);

        reset_kamery.setText("Reset Kamery");
        reset_kamery.addActionListener(this);

        zacznij_nagrywanie.setText("Rozpocznij nagrywanie");
        zakoncz_nagrywanie.addActionListener(this);

        zakoncz_nagrywanie.setText("Zakoncz nagrywanie");
        zakoncz_nagrywanie.addActionListener(this);

        odtworz_nagranie.setText("Odtworz nagranie");
        odtworz_nagranie.addActionListener(this);

        panel_menu.add(start);
        panel_menu.add(dzwiek);
        panel_menu.add(reset_kamery);
        panel_menu.add(zacznij_nagrywanie);
        panel_menu.add(zakoncz_nagrywanie);
        panel_menu.add(odtworz_nagranie);
        return panel_menu;
    }

    /**Creates JPanel with robot instructions**/
    public static JPanel addInstruction() {
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon("resources\\istrukcja_robota.jpg"));

        JPanel panel_instrukcji = new JPanel(new FlowLayout());
        panel_instrukcji.add(label);
        return panel_instrukcji;
    }

    /**Obsluga zdarzen **/
    public void actionPerformed(ActionEvent e){
            if(e.getSource() == reset_kamery) {
                Transform3D przesuniecie_obserwatora = new Transform3D();
                przesuniecie_obserwatora.set(new Vector3f(0.0f,0.0f,10.0f));
                simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);
            }
    }

    public static void main(String[] args) {
        new Robot_3D();
    }


}
