package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class GameWindow extends JFrame{
    boolean drawing=false;
    Thread t=new Thread(new Runnable() {
        @Override
        public void run() {
            while(drawing) {
                try {
                    redraw();
                    Thread.sleep(20);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        }
    });
    //JFrame j=new JFrame("image");
    //BufferedImage img=new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
    JLabel board=new JLabel("LOADING...");
    double[][] tabled;
    double[][] table;
    Color[] col;
    ArrayList<Particle> ps=new ArrayList<>();
    Random r=new Random();
    double friction=0;
    double g=1;
    double middling=0.15;
    boolean borders=false;
    int size=40;
    int w=1920;
    int h=1000;
    BufferedImage field=new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d= (Graphics2D) field.getGraphics();
    public GameWindow(){
        //setUndecorated(true);
        //setOpacity(0.5f);
        //l.setOpaque(false);
        add(board);
        setBackground(new Color(255,255,255,255));
        pack();
        setResizable(false);
        setSize(w,h+35);
        setVisible(true);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    public GameWindow setImage(BufferedImage img){
        //img=im;
        //if(getWidth()!=img.getWidth()||getHeight()!=img.getHeight()) {
        //    setSize(img.getWidth(), img.getHeight());
        //}
        board.setIcon(new ImageIcon(img));
        repaint();
        //j.repaint();
        return this;
    }



    public void generateRandom(int amount,int types) {
        //friction=r.nextDouble();
        col=new Color[types];
        table=new double[types][types];
        tabled=new double[types][types];
        System.out.println("Friction is "+friction);
        for (int i = 0; i < types; i++) {
            col[i]=Color.getHSBColor(r.nextFloat(),1,1);
            for (int j = 0; j < types; j++) {
                table[i][j]=r.nextDouble()*2d-1d;
                tabled[i][j]=(r.nextDouble()*12d*size+size*1d);

            }
        }
        ps.clear();
        for (int i = 0; i < amount; i++) {
            Particle part=new Particle();
            part.vx=0;
            part.vy=0;
            part.x=r.nextInt(w*3)-w;
            part.y=r.nextInt(h*3)-h;
            part.type=r.nextInt(types);
            part.id=i;
            ps.add(part);
        }
    }
    public void beginDrawing(){
        drawing=true;
        t.start();
    }
    public void stopDrawing(){
        drawing=false;
    }

    public void step() {
        for (Particle t:ps) {
            boolean collide=false;
            t.vx*=friction;
            t.vy*=friction;
            for(Particle n:ps){
                double ddx=(t.x-n.x);
                double ddy=(t.y-n.y);
                double dds=Math.sqrt(ddx*ddx+ddy*ddy);
                if(t.id!=n.id){
                    double dx=(t.x-n.x);
                    double dy=(t.y-n.y);
                    //double s=Math.sqrt(dx*dx+dy*dy);
                    double s=Math.sqrt(dx*dx+dy*dy);
                    if(s!=0) {
                        double f = (table[t.type][n.type] / (s)) * g;
                        dx = dx / s;
                        dy = dy / s;
                        if(t.type!=n.type) {
                            //if(true){
                            if(table[t.type][n.type]<0) {
                                if (s > tabled[t.type][n.type]) {
                                    t.vx += f * dx;
                                    t.vy -= f * dy;
                                } else {
                                    t.vx -= f * dx;
                                    t.vy += f * dy;
                                }
                            }else{
                                t.vx += f * dx;
                                t.vy -= f * dy;
                            }
                        }
                        if(dds<size){
                            ddx = ddx / dds;
                            ddy = ddy / dds;
                            collide=true;
                            double ds=size-dds;
                            t.x+=ddx*ds/10;
                            t.y+=ddy*ds/10;
                            n.x-=ddx*ds/10;
                            n.y-=ddy*ds/10;
                            t.vx+=ddx*ds/10;
                            t.vy-=ddy*ds/10;
                            n.vx-=ddx*ds/10;
                            n.vy+=ddy*ds/10;
                        }else{

                        }
                    }else{
                        t.x-=1;
                        t.y-=1;
                    }
                }
            }
            if(borders) {
                if (t.x > w - size / 2) {
                    if (t.vx > 0) t.vx = -t.vx;
                    t.x = w - size / 2;
                }
                if (t.x < 0 + size / 2) {
                    if (t.vx < 0) t.vx = -t.vx;
                    t.x = size / 2;
                }
                if (t.y > h - size / 2) {
                    if (t.vy < 0) t.vy = -t.vy;
                    t.y = h - size / 2;
                }
                if (t.y < 0 + size / 2) {
                    if (t.vy > 0) t.vy = -t.vy;
                    t.y = size / 2;
                }
            }
            t.vx+=-(t.x-w/2)*middling;
            t.vy+=(t.y-h/2)*middling;
            t.x+=t.vx;
            t.y-=t.vy;
        }
        //redraw();
    }

    private void redraw() {
        g2d.setPaint(new Color(0,0,0,255));
        g2d.fillRect(0,0,w,h);
        for (Particle t:ps) {
            if(t.x>-size&&t.x<w+size&&t.y>-size&&t.y<h+size) {
                //g2d.setPaint(new Color(col[t.type].getRed(),col[t.type].getGreen(),col[t.type].getBlue(),127));
                //g2d.fillOval((int) (t.x - (size)), (int) (t.y - (size)), size*2, size*2);
                /*int i=size*3;
                g2d.setPaint(new Color(col[t.type].getRed(),col[t.type].getGreen(),col[t.type].getBlue(),50));
                g2d.fillOval((int) (t.x - (i / 2)), (int) (t.y - (i / 2)), i, i);
                g2d.setPaint(col[t.type]);
                double sz=Math.abs(t.vx-t.vy);
                g2d.fillOval((int)(t.x- (sz / 2)),(int)(t.y- (sz / 2)),(int)(sz),(int)(sz));
                */
                g2d.setPaint(col[t.type]);
                g2d.fillOval((int)(t.x- (size / 2)),(int)(t.y- (size / 2)),(int)(size),(int)(size));
                //g2d.setStroke(new BasicStroke(size,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
                //g2d.drawLine((int)(t.x),(int)(t.y),(int)(t.x-t.vx),(int)(t.y+t.vy));
            }
        }
        setImage(field);
    }
}
