package LevelCreator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


class DrawingPad extends JComponent {
    private BufferedImage image;
    private Graphics2D graphics2D;
    private int currentX , currentY , oldX , oldY, startX, startY;

    private DrawingEnum drawingStatus;
    private Color drawColor = Color.GRAY;
    private Color existingLineColor = Color.BLACK;

    private ArrayList<Point> pointArrayList = new ArrayList<Point>();
    private boolean finishedShape;

    public DrawingPad(){
        setDrawingStatus(DrawingEnum.WAITING);

        setDoubleBuffered(true);
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                if (drawingStatus!=DrawingEnum.WAITING) {
                    oldX = e.getX();
                    oldY = e.getY();
                    pointArrayList.add(e.getPoint());
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (drawingStatus!=DrawingEnum.WAITING) {
                    System.out.println("MOUSE RELEASED");

                    if (finishedShape){ //if shape was finished, chop off line from start to intersection
                        System.out.println("SHAPE FINISHED");

                        graphics2D.setPaint(Color.white);
                        int start = pointArrayList.indexOf(pointArrayList.get(pointArrayList.size()-1));
                        System.out.println("INTERSECTION AT: " + pointArrayList.get(pointArrayList.size()-1) + " " + start);
                        for (int i = 0; i < start; i++){
                            Point p1 = pointArrayList.get(i);
                            Point p2 = pointArrayList.get(i+1);
                            graphics2D.drawLine(p1.x, p1.y, p2.x, p2.y);
                        }
                        System.out.println("DELETED TO " + start);

                        graphics2D.setPaint(drawColor);
                    } else { //if shape was not finished, draw line from end to start
                        System.out.println("SHAPE NOT FINISHED");
                        Point p1 = pointArrayList.get(0);
                        graphics2D.drawLine(e.getX(), e.getY(), p1.x, p1.y);
                    }

                    repaint();
                    for (Point p: pointArrayList)
                        System.out.println(p);


                    pointArrayList.clear();
                    drawingStatus = DrawingEnum.WAITING;
                }


            }
        });


        addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseDragged(MouseEvent e) {
                currentX = e.getX();
                currentY = e.getY();
                if (graphics2D != null) {
                    if (drawingStatus == DrawingEnum.CONTINENTS) {
                        if (!finishedShape){
                            if (image.getRGB(currentX, currentY) == drawColor.getRGB()){
                                finishedShape = true;
                                System.out.println("SHAPE CLOSED");
                            }
                            pointArrayList.add(e.getPoint());
                            graphics2D.drawLine(oldX, oldY, currentX, currentY);
                           
                        }
                    }else{ //countries


                    }
                }
                repaint();
                oldX = currentX;
                oldY = currentY;
            }
        });
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
        finishedShape = false;
    }
}
