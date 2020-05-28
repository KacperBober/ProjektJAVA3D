package com.company;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;

import javax.imageio.ImageIO;
import javax.media.j3d.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.sun.j3d.utils.geometry.*;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import javax.media.j3d.Transform3D;
import javax.vecmath.*;

import static javax.media.j3d.Background.SCALE_FIT_ALL;
import static javax.media.j3d.ImageComponent.FORMAT_RGB;

public class Robot_3D extends JFrame implements ActionListener {
    JButton start = new JButton();
    JButton dzwiek = new JButton();
    JButton reset_kamery = new JButton();
    JButton zacznij_nagrywanie = new JButton();
    JButton zakoncz_nagrywanie = new JButton();
    JButton odtworz_nagranie = new JButton();

    Canvas3D comp = createCanvas3D(new Dimension(1000, 700));
    BoundingSphere bounds;
    SimpleUniverse simpleU;
    OrbitBehavior orbit; // musi być widoczny dla simpleU

    Robot_3D() {
        super("Robot_3D Sebastian Krajna Kacper Bober");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);

        add(BorderLayout.NORTH, stworzPanelPrzyciskow());
        add(BorderLayout.WEST, dodanieInstrukcji());
        add(BorderLayout.CENTER, comp);

        pack();
        // tworzenie sceny
        BranchGroup scena = createSceneGraph(true);

        simpleU = new SimpleUniverse(comp);
        // tworzenie poruszania sceny  
        createOrbitPlatform();
        simpleU.addBranchGraph(scena);

    }

    public BranchGroup createSceneGraph(boolean isInteractive) {
        
        BranchGroup tworzona_scena = new BranchGroup();

        // poczatkowe polozenie kamery
        // TransformGroup objTrans = new TransformGroup(); 
        // objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        // Transform3D t3dTrans = new Transform3D();
        // t3dTrans.setTranslation(new Vector3d(2, -0.5, -2));
        // objTrans.setTransform(t3dTrans);
        // tworzona_scena.addChild(objTrans);

        dodanieZiemi(tworzona_scena);
        dodanieSwiatla(tworzona_scena);

        // opcjonalnie można dodać przycisk w menu czy ma być interaktywne i na
        // podstawie tego wywoływać tę funkcję
        /*
         * if (isInteractive) { }
         */

        return tworzona_scena;
    }

    public void dodanieSwiatla(BranchGroup tworzona_scena){
        Color3f ambientColor = new Color3f(0.3f, 0.3f, 0.3f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        tworzona_scena.addChild(ambientLightNode);

        // Set up the directional lights
        Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
        Vector3f light1Direction = new Vector3f(1.0f, 1.0f, 1.0f);
        Color3f light2Color = new Color3f(1.0f, 1.0f, 0.9f);
        Vector3f light2Direction = new Vector3f(-1.0f, -1.0f, -1.0f);

        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        tworzona_scena.addChild(light1);

        DirectionalLight light2 = new DirectionalLight(light2Color, light2Direction);
        light2.setInfluencingBounds(bounds);
        tworzona_scena.addChild(light2);
    }

    public void dodanieZiemi(BranchGroup tworzona_scena){
        Appearance wyglad_ziemia = new Appearance();
        TextureLoader loader = new TextureLoader("resources\\trawka.jpg", null);
        ImageComponent2D image = loader.getImage();
        Texture2D trawka = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
        trawka.setImage(0, image);
        trawka.setBoundaryModeS(Texture.WRAP);
        trawka.setBoundaryModeT(Texture.WRAP);
        wyglad_ziemia.setTexture(trawka);
        Point3f[] coords = new Point3f[4];
        for (int i = 0; i < 4; i++)
            coords[i] = new Point3f();

        Point2f[] tex_coords = new Point2f[4];
        for (int i = 0; i < 4; i++)
            tex_coords[i] = new Point2f();

        coords[0].y = 0.0f;
        coords[1].y = 0.0f;
        coords[2].y = 0.0f;
        coords[3].y = 0.0f;

        coords[0].x = 3.5f;
        coords[1].x = 3.5f;
        coords[2].x = -3.5f;
        coords[3].x = -3.5f;

        coords[0].z = 3.5f;
        coords[1].z = -3.5f;
        coords[2].z = -3.5f;
        coords[3].z = 3.5f;

        tex_coords[0].x = 0.0f;
        tex_coords[0].y = 0.0f;

        tex_coords[1].x = 10.0f;
        tex_coords[1].y = 0.0f;

        tex_coords[2].x = 0.0f;
        tex_coords[2].y = 10.0f;

        tex_coords[3].x = 10.0f;
        tex_coords[3].y = 10.0f;

        // ziemia

        QuadArray qa_ziemia = new QuadArray(4, GeometryArray.COORDINATES | GeometryArray.TEXTURE_COORDINATE_2);
        qa_ziemia.setCoordinates(0, coords);

        qa_ziemia.setTextureCoordinates(0, tex_coords);

        Shape3D ziemia = new Shape3D(qa_ziemia);
        ziemia.setAppearance(wyglad_ziemia);

        tworzona_scena.addChild(ziemia);
    }

    /** Creates Vieving platform with orbit behaviour **/
    public ViewingPlatform createOrbitPlatform() {

        ViewingPlatform viewingPlatform = simpleU.getViewingPlatform();
        viewingPlatform.setNominalViewingTransform();
        orbit = new OrbitBehavior(simpleU.getCanvas());
        bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        orbit.setSchedulingBounds(bounds);
        orbit.setTranslateEnable(false);

        orbit.setRotFactors(0.5, 0.5);
        orbit.setReverseRotate(true);

        viewingPlatform.setViewPlatformBehavior(orbit);
        return viewingPlatform;
    }

    /** Creates Canvas3D object with given dimensions **/
    public static Canvas3D createCanvas3D(Dimension dim) {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        canvas3D.setPreferredSize(dim);
        return canvas3D;
    }

    /** Creates JPanel and initializes buttons **/
    public JPanel stworzPanelPrzyciskow() {
        JPanel panel_menu = new JPanel(new FlowLayout());
        start.setText("Start");
        start.addActionListener(this);

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

    /** Creates JPanel with robot instructions **/
    public static JPanel dodanieInstrukcji() {
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon("resources\\istrukcja_robota.jpg"));
        JPanel panel_instrukcji = new JPanel(new FlowLayout());
        panel_instrukcji.add(label);
        return panel_instrukcji;
    }

    /** Obsluga zdarzen **/
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == reset_kamery) {
            Transform3D przesuniecie_obserwatora = new Transform3D();
            przesuniecie_obserwatora.set(new Vector3f(0.0f, 1.5f, 15.0f));
            simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);
        }
    }

    public static void main(String[] args) {
        new Robot_3D();
    }

}
