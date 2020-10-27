package risk.LevelCreator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class CountryDrawPad extends JPanel implements MouseListener {
    private BufferedImage image;
    private Graphics2D graphics2D;

    private int oldX = -1, oldY = -1, startX, startY;

    public CountryDrawPad(){
        setDoubleBuffered(true);
        addMouseListener(this);
    }


    public void paintComponent(Graphics g){
        if(image == null){
            // image = createImage(getSize().width, getSize().height);
            image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            graphics2D = (Graphics2D)image.getGraphics();
            graphics2D.setStroke(new BasicStroke(2));
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            //graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
            clear();
        }
        g.drawImage(image, 0, 0, null);
    }


    public void clear(){
        oldX = -1;
        oldY = -1;
        graphics2D.setPaint(MapColor.BACKGROUND_COLOR.getColor());
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(MapColor.BORDER_COLOR.getColor());
        repaint();
    }

    public void finishDrawing(){
        randomFractal(oldX, oldY, startX, startY);
        repaint();
    }

    public void floodFill(){

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (oldX != -1) {
            randomFractal(oldX, oldY, e.getX(), e.getY());
            repaint();
        } else {
            startX = e.getX();
            startY = e.getY();
        }
        oldX = e.getX();
        oldY = e.getY();
    }

    public void randomFractal(int lX, int lY, int rX, int rY){
        final int BORDER_DETAIL = 4;
        int midX, midY;
        int delta;

        if (Math.abs(rX-lX) <= BORDER_DETAIL && Math.abs(rY-lY) <= BORDER_DETAIL)
            graphics2D.drawLine(lX, lY, rX, rY);
        else{
            midX = (lX + rX)/2;
            midY = (lY + rY)/2;
            delta = (int) ((Math.random() - 0.5) * (rX - lX));

            if (Math.abs(rX-lX) > Math.abs(rY - lY))
                midY += delta;
            else
                midX += delta;

            randomFractal(midX, midY, rX, rY);
            randomFractal(lX, lY, midX, midY);
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
