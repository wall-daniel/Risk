package risk.LevelCreator;

import risk.Country;

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
            image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            graphics2D = (Graphics2D)image.getGraphics();
            graphics2D.setStroke(new BasicStroke(1));
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

    public void reset(){
        oldX = -1;
        oldY = -1;
    }


    public void closeShape(){
        randomFractal(oldX, oldY, startX, startY);
        repaint();
    }

    public void fill(){

        graphics2D.setPaint(MapColor.FILL_COLOR.getColor());
        for (int x = 0; x < image.getWidth(); x++){
            boolean fill = false;
            int initialY = 0;
            for (int y = 0; y < image.getHeight(); y++){
               if (image.getRGB(x, y) == MapColor.BORDER_COLOR.getColor().getRGB()) {
                   if (fill)
                       graphics2D.drawLine(x, initialY, x, y-1);

                   if (image.getRGB(x, y+1) == MapColor.BACKGROUND_COLOR.getColor().getRGB()) {
                       fill = !fill;
                       initialY = y;
                   }
               }
            }
        }


        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (oldX == -1) {
            startX = e.getX();
            startY = e.getY();
        } else {
            randomFractal(oldX, oldY, e.getX(), e.getY());
            repaint();
        }
        oldX = e.getX();
        oldY = e.getY();
    }

    public void randomFractal(int lX, int lY, int rX, int rY){
        final int BORDER_DETAIL = 4;
        int midX, midY;
        int delta, deltaY;

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

    //process image for finish
    public void finish(){
        /*
        int startX = 0, startY = 0, endX = image.getWidth() - 1, endY = image.getHeight() - 1;

        startYLoop:
        for (; startY < image.getHeight(); startY++)
            for (int x = 0; x < image.getWidth(); x++)
                if (image.getRGB(x, startY) != MapColor.BACKGROUND_COLOR.getColor().getRGB())
                    break startYLoop;

        startXLoop:
        for (; startX < image.getWidth(); startX++)
            for (int y = 0; y < image.getHeight() ; y++)
                if (image.getRGB(startX, y) != MapColor.BACKGROUND_COLOR.getColor().getRGB())
                    break startXLoop;

        endYLoop:
        for (; endY > 1; endY--)
            for (int x = 0; x < image.getWidth(); x++)
                if (image.getRGB(x, endY) != MapColor.BACKGROUND_COLOR.getColor().getRGB())
                    break endYLoop;

        endXLoop:
        for (; endX > 1; endX--)
            for (int y = 0; y < image.getHeight(); y++)
                if (image.getRGB(endX, y) != MapColor.BACKGROUND_COLOR.getColor().getRGB())
                    break endXLoop;

        */

        graphics2D.setPaint(MapColor.TRANSPARENT_COLOR.getColor());

        for (int x = 0; x < image.getWidth(); x++){
            boolean fill = true;
            int initialY = 0;
            for (int y = 0; y < image.getHeight(); y++){
                if (image.getRGB(x, y) == MapColor.BORDER_COLOR.getColor().getRGB() || image.getRGB(x, y) == MapColor.FILL_COLOR.getColor().getRGB() ) {
                    if (fill)
                        graphics2D.drawLine(x, initialY, x, y-1);

                    if (fill && image.getRGB(x, y+1) == MapColor.FILL_COLOR.getColor().getRGB()) {
                        fill = false;
                    }
                    if (!fill && image.getRGB(x, y+1) == MapColor.BACKGROUND_COLOR.getColor().getRGB()){
                        fill = true;
                        initialY = y;
                    }
                }

                if (y == image.getHeight() - 1)
                    graphics2D.drawLine(x, initialY, x, y);
            }
        }

        /*




        BufferedImage img = image.getSubimage(startX, startY, endX - startX, endY - startY); //fill in the corners of the desired crop location here
        image = img;

*/

    }


    public BufferedImage getImage(){
        return image;
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
