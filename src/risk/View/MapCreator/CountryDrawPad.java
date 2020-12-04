package risk.View.MapCreator;

import risk.Enums.MapColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.*;


public class CountryDrawPad extends JPanel implements MouseListener {
    private BufferedImage image;
    private Graphics2D graphics2D;
    private Polygon polygon;

    private int oldX = -1, oldY = -1, startX, startY;

    int borderDetail = 1000;
    final double BORDER_DEVIATION = 0.5;

    public CountryDrawPad(){
        polygon = new Polygon();
        setDoubleBuffered(true);
        addMouseListener(this);
    }

    public void paintComponent(Graphics g){
        if(image == null){
            image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            graphics2D = (Graphics2D)image.getGraphics();
            graphics2D.setStroke(new BasicStroke(5));
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            clear();
        }
        g.drawImage(image, 0, 0, null);
    }

    public void clear(){
        reset();
        graphics2D.setPaint(MapColor.BACKGROUND_COLOR.getColor());
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(MapColor.BORDER_COLOR.getColor());
        repaint();
    }

    public void reset(){
        oldX = -1;
        oldY = -1;
        polygon = new Polygon();
    }

    public void closeShape(){
        randomFractal(oldX, oldY, startX, startY);
        repaint();
    }

    public Polygon getPolygon(){
        int deltaX = 0, deltaY = 0;

        deltaXLoop:
        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++)
                if (image.getRGB(x,y) == MapColor.BORDER_COLOR.getColor().getRGB()){
                    deltaX = x;
                    break deltaXLoop;
                }

        deltaYLoop:
        for (int y = 0; y < image.getHeight(); y++)
            for (int x = 0; x < image.getWidth(); x++)
                if (image.getRGB(x,y) == MapColor.BORDER_COLOR.getColor().getRGB()){
                    deltaY = y;
                    break deltaYLoop;
                }

        polygon.translate(-deltaX+10, -deltaY+10);
        return polygon;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (oldX == -1) {
            startX = e.getX();
            startY = e.getY();
            polygon.addPoint(startX, startY);
        } else {
            randomFractal(oldX, oldY, e.getX(), e.getY());
            repaint();
        }

        oldX = e.getX();
        oldY = e.getY();

    }

    public void setBorderDetail(int borderDetail){
        this.borderDetail = borderDetail;
    }

    public void randomFractal(int lX, int lY, int rX, int rY){

        int midX, midY;
        int delta;

        if (Math.abs(rX-lX) <= borderDetail && Math.abs(rY-lY) <= borderDetail) {
            graphics2D.drawLine(lX, lY, rX, rY);
            polygon.addPoint(rX, rY);
        }else{
            midX = (lX + rX)/2;
            midY = (lY + rY)/2;
            delta = (int) ((Math.random() - BORDER_DEVIATION) * (rX - lX));
            if (Math.abs(rX-lX) > Math.abs(rY - lY))
                midY += delta;
            else
                midX += delta;

            randomFractal(lX, lY, midX, midY);
            randomFractal(midX, midY, rX, rY);
        }
    }


    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
