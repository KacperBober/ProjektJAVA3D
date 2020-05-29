package com.company;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import javax.media.j3d.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import javax.media.j3d.Transform3D;
import javax.vecmath.*;

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

    TransformGroup tg_podloga = new TransformGroup();
    TransformGroup tg_podstawka = new TransformGroup();
    TransformGroup tg_pierwszy_obraczacz = new TransformGroup();
    TransformGroup tg_pierwsze_ramie = new TransformGroup();
    TransformGroup tg_drugie_ramie = new TransformGroup();
    TransformGroup tg_pochylacz_chwytaka = new TransformGroup();
    TransformGroup tg_obraczacz_chwytaka = new TransformGroup();
    TransformGroup tg_chwytak = new TransformGroup();

    Transform3D t3d_podloga = new Transform3D();
    Transform3D t3d_podstawka = new Transform3D();
    Transform3D t3d_pierwszy_obraczacz = new Transform3D();
    Transform3D t3d_pierwsze_ramie = new Transform3D();
    Transform3D t3d_drugie_ramie = new Transform3D();
    Transform3D t3d_pochylacz_chwytaka = new Transform3D();
    Transform3D t3d_obracacz_chwytaka = new Transform3D();
    Transform3D t3d_chwytak = new Transform3D();

    Transform3D t3d_podloga_nag = new Transform3D();
    Transform3D t3d_podstawka_nag = new Transform3D();
    Transform3D t3d_pierwszy_obraczacz_nag = new Transform3D();
    Transform3D t3d_pierwsze_ramie_nag = new Transform3D();
    Transform3D t3d_drugie_ramie_nag = new Transform3D();
    Transform3D t3d_pochylacz_chwytaka_nag = new Transform3D();
    Transform3D t3d_obracacz_chwytaka_nag = new Transform3D();
    Transform3D t3d_chwytak_nag = new Transform3D();

    boolean nagrywanie;
    Vector<KeyEvent> nagrane_przyciski = new Vector<KeyEvent>();

    boolean key_a;
    boolean key_d;
    boolean key_w;
    boolean key_s;
    boolean key_r;
    boolean key_f;
    boolean key_t;
    boolean key_g;
    boolean key_y;
    boolean key_h;
    boolean key_u;
    boolean key_j;

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

        BranchGroup glowna_scena = new BranchGroup();

        dodanieZiemi(glowna_scena);
        dodanieSwiatla(glowna_scena);

        return glowna_scena;
    }

    public void dodanieZiemi(BranchGroup glowna_scena) {

        Scene podloga_wczytanie = wczytajPlikRamienia("resources/podloga.obj");

        tg_podloga.setTransform(t3d_podloga);
        tg_podloga.addChild(podloga_wczytanie.getSceneGroup());
        glowna_scena.addChild(tg_podloga);

        /////////////////////////////////////////////////////////////////////

        Scene podstawka = wczytajPlikRamienia("resources/podstawka.obj");

        tg_podstawka.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg_podstawka.setTransform(t3d_podstawka);
        tg_podstawka.addChild(podstawka.getSceneGroup());
        glowna_scena.addChild(tg_podstawka);

        /////////////////////////////////////////////////////////////////////

        Scene pierwszy_obrot = wczytajPlikRamienia("resources/pierwszy_obraczacz.obj");

        tg_pierwszy_obraczacz.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg_pierwszy_obraczacz.setTransform(t3d_pierwszy_obraczacz);
        tg_pierwszy_obraczacz.addChild(pierwszy_obrot.getSceneGroup());
        tg_podstawka.addChild(tg_pierwszy_obraczacz);

        /////////////////////////////////////////////////////////////////////

        Scene s_pierwsze_ramie = wczytajPlikRamienia("resources/pierwsze_ramiev2.obj");

        tg_pierwsze_ramie.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg_pierwsze_ramie.addChild(s_pierwsze_ramie.getSceneGroup());

        Transform3D t3d_przesuniecie = new Transform3D();
        t3d_przesuniecie.set(new Vector3f(0, 0.62f, -0.13f)); // przesuwam obiekt z orgin na miejsce
        t3d_pierwsze_ramie.mul(t3d_przesuniecie);

        tg_pierwsze_ramie.setTransform(t3d_pierwsze_ramie);

        tg_pierwszy_obraczacz.addChild(tg_pierwsze_ramie);

        /////////////////////////////////////////////////////////////////////

        Scene s_drugie_ramie = wczytajPlikRamienia("resources/drugie_ramiev2.obj");

        tg_drugie_ramie.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg_drugie_ramie.addChild(s_drugie_ramie.getSceneGroup());

        t3d_przesuniecie.set(new Vector3f(0.04f, 0.75f, -0.02f)); // przesuwam obiekt z orgin na miejsce
        t3d_drugie_ramie.mul(t3d_przesuniecie);

        tg_drugie_ramie.setTransform(t3d_drugie_ramie);
        tg_pierwsze_ramie.addChild(tg_drugie_ramie);

        /////////////////////////////////////////////////////////////////////

        Scene s_pochylacz_chwytaka = wczytajPlikRamienia("resources/pochylacz_chwytakav2.obj");

        tg_pochylacz_chwytaka.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg_pochylacz_chwytaka.addChild(s_pochylacz_chwytaka.getSceneGroup());

        t3d_przesuniecie.set(new Vector3f(-0.05f, 0.00f, -0.75f)); // przesuwam obiekt z orgin na miejsce
        t3d_pochylacz_chwytaka.mul(t3d_przesuniecie);

        tg_pochylacz_chwytaka.setTransform(t3d_pochylacz_chwytaka);
        tg_drugie_ramie.addChild(tg_pochylacz_chwytaka);

        /////////////////////////////////////////////////////////////////////

        Scene s_obracacz_chwytaka = wczytajPlikRamienia("resources/obraczacz_chwytakav2.obj");

        tg_obraczacz_chwytaka.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg_obraczacz_chwytaka.addChild(s_obracacz_chwytaka.getSceneGroup());

        t3d_przesuniecie.set(new Vector3f(0.1f, -0.01f, -0.2f)); // przesuwam obiekt z orgin na miejsce
        t3d_obracacz_chwytaka.mul(t3d_przesuniecie);

        tg_obraczacz_chwytaka.setTransform(t3d_obracacz_chwytaka);
        tg_pochylacz_chwytaka.addChild(tg_obraczacz_chwytaka);

        /////////////////////////////////////////////////////////////////////

        Scene s_chwytak = wczytajPlikRamienia("resources/chwytakv2.obj");

        tg_chwytak.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg_chwytak.addChild(s_chwytak.getSceneGroup());

        t3d_przesuniecie.set(new Vector3f(0.0f, 0.00f, -0.18f)); // przesuwam obiekt z orgin na miejsce
        t3d_chwytak.mul(t3d_przesuniecie);

        tg_chwytak.setTransform(t3d_chwytak);
        tg_obraczacz_chwytaka.addChild(tg_chwytak);

    }

    /** Creates Vieving platform with orbit behaviour **/
    public ViewingPlatform createOrbitPlatform() {

        ViewingPlatform viewingPlatform = simpleU.getViewingPlatform();
        viewingPlatform.setNominalViewingTransform();
        orbit = new OrbitBehavior(simpleU.getCanvas());
        orbit.setSchedulingBounds(bounds);
        orbit.setTranslateEnable(false);

        orbit.setRotFactors(0.5, 0.1);
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
        zacznij_nagrywanie.addActionListener(this);

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

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == reset_kamery) {
            Transform3D przesuniecie_obserwatora = new Transform3D();
            przesuniecie_obserwatora.set(new Vector3f(0.0f, 0.0f, 12.0f));

            simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);
        }

        if (e.getSource() == zacznij_nagrywanie) {
            
            nagrane_przyciski.clear();
            
            t3d_pierwszy_obraczacz_nag.set(t3d_pierwszy_obraczacz);
            t3d_pierwsze_ramie_nag.set(t3d_pierwsze_ramie);
            t3d_drugie_ramie_nag.set(t3d_drugie_ramie);
            t3d_pochylacz_chwytaka_nag.set(t3d_pochylacz_chwytaka);
            t3d_obracacz_chwytaka_nag.set(t3d_obracacz_chwytaka);
            t3d_chwytak_nag.set(t3d_chwytak);

            nagrywanie = true;
        }

        if (e.getSource() == zakoncz_nagrywanie) {
            nagrywanie = false;
            System.out.println("false");
        }
        
        if (e.getSource() == odtworz_nagranie) {

            t3d_pierwszy_obraczacz.set(t3d_pierwszy_obraczacz_nag);
            t3d_pierwsze_ramie.set(t3d_pierwsze_ramie_nag);
            t3d_drugie_ramie.set(t3d_drugie_ramie_nag);
            t3d_pochylacz_chwytaka.set(t3d_pochylacz_chwytaka_nag);
            t3d_obracacz_chwytaka.set(t3d_obracacz_chwytaka_nag);
            t3d_chwytak.set(t3d_chwytak_nag);

            tg_pierwszy_obraczacz.setTransform(t3d_pierwszy_obraczacz_nag);
            tg_pierwsze_ramie.setTransform(t3d_pierwsze_ramie_nag);
            tg_drugie_ramie.setTransform(t3d_drugie_ramie_nag);
            tg_pochylacz_chwytaka.setTransform(t3d_pochylacz_chwytaka_nag);
            tg_obraczacz_chwytaka.setTransform(t3d_obracacz_chwytaka_nag);
            tg_chwytak.setTransform(t3d_chwytak_nag);

            for (int i = 0; i < nagrane_przyciski.size(); i++) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                keyPressed(nagrane_przyciski.elementAt(i));
                keyReleased(nagrane_przyciski.elementAt(i));
            }
        }
    }

    public void wykonajRuch() {
        Transform3D akcja = new Transform3D();
        if (key_a == true) {
            akcja.rotY(Math.PI / 180);
            t3d_pierwszy_obraczacz.mul(akcja);
            tg_pierwszy_obraczacz.setTransform(t3d_pierwszy_obraczacz);
        }
        if (key_d == true) {
            akcja.rotY(-Math.PI / 180);
            t3d_pierwszy_obraczacz.mul(akcja);
            tg_pierwszy_obraczacz.setTransform(t3d_pierwszy_obraczacz);
        }
        if (key_w == true) {
            akcja.rotX(Math.PI / 180);
            t3d_pierwsze_ramie.mul(akcja);
            tg_pierwsze_ramie.setTransform(t3d_pierwsze_ramie);
        }
        if (key_s == true) {
            akcja.rotX(-Math.PI / 180);
            t3d_pierwsze_ramie.mul(akcja);
            tg_pierwsze_ramie.setTransform(t3d_pierwsze_ramie);
        }
        if (key_r == true) {
            akcja.rotX(Math.PI / 180);
            t3d_drugie_ramie.mul(akcja);
            tg_drugie_ramie.setTransform(t3d_drugie_ramie);
        }
        if (key_f == true) {
            akcja.rotX(-Math.PI / 180);
            t3d_drugie_ramie.mul(akcja);
            tg_drugie_ramie.setTransform(t3d_drugie_ramie);
        }
        if (key_t == true) {
            akcja.rotX(Math.PI / 180);
            t3d_pochylacz_chwytaka.mul(akcja);
            tg_pochylacz_chwytaka.setTransform(t3d_pochylacz_chwytaka);
        }
        if (key_g == true) {
            akcja.rotX(-Math.PI / 180);
            t3d_pochylacz_chwytaka.mul(akcja);
            tg_pochylacz_chwytaka.setTransform(t3d_pochylacz_chwytaka);
        }
        if (key_y == true) {
            akcja.rotY(Math.PI / 180);
            t3d_obracacz_chwytaka.mul(akcja);
            tg_obraczacz_chwytaka.setTransform(t3d_obracacz_chwytaka);
        }
        if (key_h == true) {
            akcja.rotY(-Math.PI / 180);
            t3d_obracacz_chwytaka.mul(akcja);
            tg_obraczacz_chwytaka.setTransform(t3d_obracacz_chwytaka);
        }
        if (key_u == true) {
            akcja.rotZ(Math.PI / 180);
            t3d_chwytak.mul(akcja);
            tg_chwytak.setTransform(t3d_chwytak);
        }
        if (key_j == true) {
            akcja.rotZ(-Math.PI / 180);
            t3d_chwytak.mul(akcja);
            tg_chwytak.setTransform(t3d_chwytak);
        }
    }

    public void keyPressed(KeyEvent e) {
        int przycisk = e.getKeyCode();
        switch (przycisk) {
            case KeyEvent.VK_A:
                key_a = true;
                break;
            case KeyEvent.VK_D:
                key_d = true;
                break;
            case KeyEvent.VK_W:
                key_w = true;
                break;
            case KeyEvent.VK_S:
                key_s = true;
                break;
            case KeyEvent.VK_R:
                key_r = true;
                break;
            case KeyEvent.VK_F:
                key_f = true;
                break;
            case KeyEvent.VK_T:
                key_t = true;
                break;
            case KeyEvent.VK_G:
                key_g = true;
                break;
            case KeyEvent.VK_Y:
                key_y = true;
                break;
            case KeyEvent.VK_H:
                key_h = true;
                break;
            case KeyEvent.VK_U:
                key_u = true;
                break;
            case KeyEvent.VK_J:
                key_j = true;
                break;
        }
        if (nagrywanie == true) {
            nagrane_przyciski.add(e);
        }
        wykonajRuch();
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                key_a = false;
                break;
            case KeyEvent.VK_D:
                key_d = false;
                break;
            case KeyEvent.VK_W:
                key_w = false;
                break;
            case KeyEvent.VK_S:
                key_s = false;
                break;
            case KeyEvent.VK_R:
                key_r = false;
                break;
            case KeyEvent.VK_F:
                key_f = false;
                break;
            case KeyEvent.VK_T:
                key_t = false;
                break;
            case KeyEvent.VK_G:
                key_g = false;
                break;
            case KeyEvent.VK_Y:
                key_y = false;
                break;
            case KeyEvent.VK_H:
                key_h = false;
                break;
            case KeyEvent.VK_U:
                key_u = false;
                break;
            case KeyEvent.VK_J:
                key_j = false;
                break;
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public Scene wczytajPlikRamienia(String filename) {
        ObjectFile loader = new ObjectFile();
        Scene s = null;
        File file = new java.io.File(filename);
        try {
            s = loader.load(file.toURI().toURL());
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        return s;
    }

    /** Creates JPanel with robot instructions **/
    public static JPanel dodanieInstrukcji() {
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon("resources\\istrukcja_robota.jpg"));
        JPanel panel_instrukcji = new JPanel(new FlowLayout());
        panel_instrukcji.add(label);
        return panel_instrukcji;
    }

    public void dodanieSwiatla(BranchGroup glowna_scena) {
        Color3f ambientColor = new Color3f(0.0f, 0.0f, 0.0f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        glowna_scena.addChild(ambientLightNode);

        // Set up the directional lights
        Color3f light1Color = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f light1Direction = new Vector3f(1.0f, 1.0f, 1.0f);
        Color3f light2Color = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f light2Direction = new Vector3f(-1.0f, -1.0f, -1.0f);

        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        glowna_scena.addChild(light1);

        DirectionalLight light2 = new DirectionalLight(light2Color, light2Direction);
        light2.setInfluencingBounds(bounds);
        glowna_scena.addChild(light2);
    }

    public static void main(String[] args) {
        new Robot_3D();
    }

}
