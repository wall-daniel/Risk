package risk.LevelCreator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


class DrawingPad extends JPanel implements MouseListener {
    private BufferedImage image;
    private Graphics2D graphics2D;

    private DrawingEnum drawingStatus;
    private Color drawColor = Color.GRAY;
    private Color existingLineColor = Color.BLACK;

    private int oldX = -1, oldY = -1;


    public DrawingPad() {
        setDrawingStatus(DrawingEnum.WAITING);

    }

    public void paintComponent(Graphics g){
        if(image == null){
           // image = createImage(getSize().width, getSize().height);
            image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            graphics2D = (Graphics2D)image.getGraphics();
            graphics2D.setStroke(new BasicStroke(1));
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            //graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
            clear();
        }
        g.drawImage(image, 0, 0, null);
    }

    public void clear(){
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(drawColor);
        repaint();
    }


    public void setDrawingStatus(DrawingEnum drawingStatus){
        this.drawingStatus = drawingStatus;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (oldX != -1) {
            randomFractal(oldX, oldY, e.getX(), e.getY());
            repaint();
        }
        oldX = e.getX();
        oldY = e.getY();
    }

    public void randomFractal(int lX, int lY, int rX, int rY){
        final int STOP = 4;
        int midX, midY;
        int delta;

        if (Math.abs(rX-lX) <= STOP)
            graphics2D.drawLine(lX, lY, rX, rY);
        else{
            midX = (lX + rX)/2;
            midY = (lY + rY)/2;
            delta = (int) ((Math.random() - 0.5) * Math.abs(rX - lX));
            midY +=delta;
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
