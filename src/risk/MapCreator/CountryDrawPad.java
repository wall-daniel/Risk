package risk.MapCreator;

import risk.Enums.MapColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.util.ArrayList;


public class CountryDrawPad extends JPanel implements MouseListener {
    private BufferedImage image;
    private Graphics2D graphics2D;
    private Polygon polygon;

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

    public Polygon imgToPolygon(){
        Polygon imgPolygon = new Polygon();
        int n = 0;
        for (int x = 0; x < image.getWidth(); x++){
            for (int y = 0; y < image.getHeight(); y++){
                if (image.getRGB(x,y) == MapColor.BORDER_COLOR.getColor().getRGB()){
                    imgPolygon.addPoint(x,y);
                }
            }
        }
        return imgPolygon;
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
        final double BORDER_DEVIATION = 0.5;

        int midX, midY;
        int delta;

        if (Math.abs(rX-lX) <= BORDER_DETAIL && Math.abs(rY-lY) <= BORDER_DETAIL)
            graphics2D.drawLine(lX, lY, rX, rY);
        else{
            midX = (lX + rX)/2;
            midY = (lY + rY)/2;
            delta = (int) ((Math.random() - BORDER_DEVIATION) * (rX - lX));
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

        Image img = TransformColorToTransparency();
        image = ImageToBufferedImage(img, image.getWidth(), image.getHeight());

        BufferedImage imag = image.getSubimage(startX, startY, endX - startX, endY - startY);
        image = imag;
    }

    private BufferedImage ImageToBufferedImage(Image image, int width, int height) {
        BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return dest;
    }

    private Image TransformColorToTransparency() {
        // Primitive test, just an example
        final int r1 = MapColor.BACKGROUND_COLOR.getColor().getRed();
        final int g1 = MapColor.BACKGROUND_COLOR.getColor().getGreen();
        final int b1 = MapColor.BACKGROUND_COLOR.getColor().getBlue();

        ImageFilter filter = new RGBImageFilter() {
            public final int filterRGB(int x, int y, int rgb) {
                int r = (rgb & 0xFF0000) >> 16;
                int g = (rgb & 0xFF00) >> 8;
                int b = rgb & 0xFF;
                if (r == r1 && g == g1 && b == b1) {
                    return rgb & 0xFFFFFF;
                }
                return rgb;
            }
        };
        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
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
