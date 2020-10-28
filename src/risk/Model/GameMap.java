package risk.Model;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class GameMap extends JFrame {

    private ArrayList<Integer> xCoords;
    private ArrayList<Integer> yCoords;
    public HashMap<String, Polygon> map;

    public GameMap() {
        super("Map");

        xCoords = new ArrayList<Integer>();
        yCoords = new ArrayList<Integer>();


        map = new HashMap<>();

        map.put("Canada", new Polygon(new int[] {87,153,147,123,84}, new int[] {191,190,227,258,244}, 5));
        map.put("Sweden", new Polygon(new int[] {250,350,425,475,550,75,50}, new int[] {150,100,125,225,250,375,300}, 7));
        map.put("Russia", new Polygon (new int[] {312, 474, 486, 395}, new int[] {401, 356, 425, 480}, 4));


        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("resources/risk.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage finalImg = img;
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(finalImg, 0, 0, this); // see javadoc for more info on the parameters

                g.setColor(Color.BLUE);
                for (Polygon country : map.values()) {
                    g.drawPolygon(country);
                }
            }
        };
        add(p);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

                for (String countryName : map.keySet()) {
                    if (map.get(countryName).contains(mouseEvent.getX(), mouseEvent.getY())) {
                        System.out.println("Clicked in poly " + countryName);
                    }
                }

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                xCoords.add(mouseEvent.getX() - getInsets().left);
                yCoords.add(mouseEvent.getY() - getInsets().top);
                Polygon newCountry = new Polygon(xCoords.stream().mapToInt(i->i).toArray(), yCoords.stream().mapToInt(i->i).toArray(), xCoords.size());
                map.put("temp", newCountry);
                repaint();

                System.out.println("map.put(\"\", new Polygon (new int[] {" + xCoords.toString().substring(1, xCoords.toString().length() - 1) + "}, new int[] {" + yCoords.toString().substring(1, yCoords.toString().length() - 1) + "}, " + xCoords.size() + "));");
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

        setSize(img.getWidth(), img.getHeight());
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public static void main(String[] args) {
        GameMap map = new GameMap();
    }
}