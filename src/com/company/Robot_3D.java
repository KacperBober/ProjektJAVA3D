package com.company;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;

import javax.imageio.ImageIO;
import javax.media.j3d.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
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

public class Robot_3D extends JFrame implements ActionListener, KeyListener {

    Canvas3D comp = createCanvas3D(new Dimension(1000, 700));
    BoundingSphere bounds;
    SimpleUniverse simpleU;
    OrbitBehavior orbit; // musi być widoczny dla simpleU

    JButton start = new JButton();
    JButton dzwiek = new JButton();
    JButton reset_kamery = new JButton();
    JButton zacznij_nagrywanie = new JButton();
    JButton zakoncz_nagrywanie = new JButton();
    JButton odtworz_nagranie = new JButton();

    Robot_3D() {
        super("Robot_3D Sebastian Krajna Kacper Bober");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);

        add(BorderLayout.NORTH, stworzPanelPrzyciskow());
        add(BorderLayout.WEST, dodanieInstrukcji());
        add(BorderLayout.CENTER, comp);

        comp.addKeyListener(this);

        pack();
        // tworzenie sceny
        bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        BranchGroup scena = createSceneGraph(true);


        simpleU = new SimpleUniverse(comp);
        // tworzenie poruszania sceny
        createOrbitPlatform();
        simpleU.addBranchGraph(scena);

    }

    public BranchGroup createSceneGraph(boolean isInteractive) {

        BranchGroup tworzona_scena = new BranchGroup();

        dodanieZiemi(tworzona_scena);
        dodanieSwiatla(tworzona_scena);

        Appearance wyglad_mury = new Appearance();
        wyglad_mury.setColoringAttributes(new ColoringAttributes(139f, 0f, 139f, ColoringAttributes.NICEST));


        TransformGroup wieza_p = new TransformGroup();
        Transform3D przesuniecie_wiezy = new Transform3D();
        przesuniecie_wiezy.set(new Vector3f(0.0f, 0.0f, 0.0f));
        wieza_p.setTransform(przesuniecie_wiezy);

        tworzona_scena.addChild(wieza_p);
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
        TransformGroup t_podloga = new TransformGroup();
        Transform3D t3d_podloga = new Transform3D();
        t3d_podloga.setTranslation(new Vector3f(0.0f, -0.0f, 1.0f));
        t3d_podloga.rotY(Math.PI);
        t3d_podloga.setScale(1.5);

        ObjectFile loader = new ObjectFile();
        Scene podloga_wczytanie = null;
        File file = new java.io.File("resources/podloga.obj");
        try {
            podloga_wczytanie = loader.load(file.toURI().toURL());
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }

        t_podloga.setTransform(t3d_podloga);
        t_podloga.addChild(podloga_wczytanie.getSceneGroup());
        tworzona_scena.addChild(t_podloga);

        TransformGroup t_podstawka = new TransformGroup();
        t3d_podloga.setTranslation(new Vector3f(0.0f, -0.0f, 1.0f));
        t3d_podloga.rotY(Math.PI);
        t3d_podloga.setScale(1.5);

        Scene podstawka = null;

        File file_1 = new java.io.File("resources/podstawka.obj");
        try {
            podstawka = loader.load(file_1.toURI().toURL());
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }

        t_podstawka.setTransform(t3d_podloga);
        t_podstawka.addChild(podstawka.getSceneGroup());
        tworzona_scena.addChild(t_podstawka);

        TransformGroup t_pierwszy_obraczacz = new TransformGroup();
        t3d_podloga.setTranslation(new Vector3f(0.0f, -0.0f, 1.0f));
        t3d_podloga.rotY(Math.PI);
        t3d_podloga.setScale(1.5);

        Scene pierwszy_obrot = null;

        File file_2 = new java.io.File("resources/pierwszy_obraczacz.obj");
        try {
            pierwszy_obrot = loader.load(file_2.toURI().toURL());
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }

        t_pierwszy_obraczacz.setTransform(t3d_podloga);
        t_pierwszy_obraczacz.addChild(pierwszy_obrot.getSceneGroup());
        tworzona_scena.addChild(t_pierwszy_obraczacz);

        TransformGroup t_pierwsze_ramie = new TransformGroup();
        t3d_podloga.setTranslation(new Vector3f(0.0f, -0.0f, 1.0f));
        t3d_podloga.rotY(Math.PI);
        t3d_podloga.setScale(1.5);

        Scene s_pierwsze_ramie = null;

        File file_3 = new java.io.File("resources/pierwsze_ramie.obj");
        try {
            s_pierwsze_ramie = loader.load(file_3.toURI().toURL());
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }

        t_pierwsze_ramie.setTransform(t3d_podloga);
        t_pierwsze_ramie.addChild(s_pierwsze_ramie.getSceneGroup());
        tworzona_scena.addChild(t_pierwsze_ramie);

        TransformGroup t_drugie_ramie = new TransformGroup();
        t3d_podloga.setTranslation(new Vector3f(0.0f, -0.0f, 1.0f));
        t3d_podloga.rotY(Math.PI);
        t3d_podloga.setScale(1.5);

        Scene s_drugie_ramie = null;

        File file_4 = new java.io.File("resources/drugie_ramie.obj");
        try {
            s_drugie_ramie = loader.load(file_4.toURI().toURL());
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }

        t_drugie_ramie.setTransform(t3d_podloga);
        t_drugie_ramie.addChild(s_drugie_ramie.getSceneGroup());
        tworzona_scena.addChild(t_drugie_ramie);
    }

    /** Creates Vieving platform with orbit behaviour **/
    public ViewingPlatform createOrbitPlatform() {

        ViewingPlatform viewingPlatform = simpleU.getViewingPlatform();
        viewingPlatform.setNominalViewingTransform();
        orbit = new OrbitBehavior(simpleU.getCanvas());
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
            przesuniecie_obserwatora.set(new Vector3f(0.0f, 0.1f, 12.0f));

            simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);
        }
    }

    public void keyPressed(KeyEvent e){
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP: 
                System.out.println("tak");
                break;
            case KeyEvent.VK_RIGHT:
                System.out.println("w prawo");
                break;
            case 'a': 
            System.out.println("a");
            break;
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        new Robot_3D();
    }

}
