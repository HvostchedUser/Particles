package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // write your code here
        GameWindow gw=new GameWindow();
        gw.beginDrawing();
        //gw.friction=0.7;
        gw.friction=0.7;
        gw.size=15;
        //gw.g=20;
        gw.g=40;
        gw.middling=0.001;
        //gw.generateRandom(500,20);
        gw.generateRandom(700,20);//1000 20
        //gw.borders=true;
        while(true){
            try {
                gw.step();
            }catch (Exception e){

            }
        }
    }
}