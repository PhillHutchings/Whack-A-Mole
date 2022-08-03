/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whackamole;

import java.awt.Image;
import java.awt.Rectangle;
import java.time.Instant;
import java.util.Random;
import javax.swing.ImageIcon;
import java.time.Duration;

/**
 *
 * @author hutch
 */
public class Mole implements Sprites{
    
    private Image image;
    private int x;
    private int y;
    public int yMovement;
    private int originalY;
    private int height;
    private int width;
    private boolean raising = true;
    private boolean hit;
    static int speed = 1;
    
    public Mole(int x, int y, int width, int height, boolean hit){
        
        this.x = x;
        this.y = y;
        this.width = MOLE_WIDTH + width;
        this.height = MOLE_HEIGHT + height;
        this.hit = hit;
        setOriginalY();
        loadImage();
        
    }
    
    /**
     * loads mole image
     */
    
    private void loadImage(){
        
        ImageIcon ii = new ImageIcon(getClass().getResource("images/Mole.png"));
        
        image = ii.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

    }
    
    /**
     * moves the mole up and down
     * @return 
     */
    
    public int move(){
       
       if(yMovement < getHeight() && raising == true){
            
            yMovement -= speed;
            
            if(yMovement <= -getHeight() /3 - 10){
                                
                raising = false;

            }
        }  
        
        if(raising == false){

            yMovement += speed;
            
            if(yMovement >= 0){
                
                
                raising = true;
                               
                Board.mole = newMole();       

            }
        }
                
        return yMovement;
    }
    
    /**
     * chooses new mole when mole hit
     * @return 
     */
    
    public static Mole newMole(){
        
        int[] holeLevel = new int[]{790, 580, 430};

        int randomLevel = holeLevel[getRandomLevel()];
        int[] holes;
        int randomHole;

        switch (randomLevel){

            case 790:       //bottom

                holes = new int[]{35, 225, 415, 605};
                randomHole = holes[getRandomHole()];

                return new Mole(randomHole, randomLevel ,0 , 0, false);      //generates random position for mole

            case 580:           //middle

                holes = new int[]{100, 260, 420, 580};
                randomHole = holes[getRandomHole()];

                return new Mole(randomHole, randomLevel , -40 , -40, false);      //generates random position for mole

            case 430:               //top

                holes = new int[]{170, 290, 410, 530};
                randomHole = holes[getRandomHole()];

                return new Mole(randomHole, randomLevel , -80 , -80, false);      //generates random position for mole

            default:
                
                return new Mole(35, 790 ,0 , 0, false); 
        }        
    
    }
    
    /**
     * gets mole image
     * @return 
     */
    
    public Image getImage(){
        
        return image;
    }
    
    /**
     * gets x position
     * @return 
     */
        
    public int getX(){
        
        return x;
    }
    
   /**
     * gets current y position
     * @return 
     */
    
    public int getY(){
        
        return y + yMovement;
    }
    
    /**
     * sets the original y
     */
    
    public final void setOriginalY(){
        
        originalY = y;
    }
    
    /**
     * gets moles original starting place
     * @return 
     */
    
    public int getOriginalY(){
        
        return originalY;
    }
        
    /**
     * gets mole position
     * @return 
     */
    
    public Rectangle getBounds(){
        
        return new Rectangle(getX(), getY(), width, height);
    }
    
    /**
     * gets width
     * @return 
     */
    
    public int getWidth(){
        
        return width;
    }
    
    /**
     * gets height
     * @return 
     */
    
    public int getHeight(){
        
        return height;
    }
    
    /**
     * gets hit status
     * @return 
     */
    
    public boolean isHit(){
        
        return hit;
    }
    
    /**
     * if mole clicked mole hit updated;
     */
    
    public void hit(){
        
        hit = true;
    }
    
    /**
     * picks random number for array entry of holes available
     * @return 
     */
    
    public static int getRandomHole(){
        
        Random random = new Random();
                        
        return random.ints(0, 4).findFirst().getAsInt();
        
    }
    
    /**
     * picks random number for array entry of levels available
     * @return 
     */
    
    public static int getRandomLevel(){
        
        Random random = new Random();
                        
        return random.ints(0, 3).findFirst().getAsInt();
        
    }
    
}
