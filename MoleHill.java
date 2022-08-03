/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whackamole;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;


/**
 *
 * @author hutch
 */
public class MoleHill implements Sprites{
    
    private Image image;
    private int height;
    private int width;
    private int x;
    private int y;
    private int yMovement = 0;
    private int heightSpeed;
    private boolean raising = true;
    
    public MoleHill(int x, int y, int width, int height){
        
        this.x = x;
        this.y = y;
        this.width = MOLE_WIDTH + width;
        this .height = height;
        this.heightSpeed = (height / 10) /2;            //determines how fast mole hill raises up according to size to help get even rasing
        loadImage();
        
    }
                   
    /**
     * loads image
     * @return 
     */
    
    private void loadImage(){
        
        ImageIcon ii = new ImageIcon(getClass().getResource("images/MoleHill.png"));
        
        image = ii.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

    }
                
    /**
     * gets image
     * @return 
     */
    
    public Image getImage(){
        
        return image;
    }
    
    /**
     * moves the mole hill up to position
     * @return 
     */
    
    public int move(){
       
       if(yMovement < getHeight() && raising == true){
            
            yMovement -= heightSpeed ;
            
            if(yMovement <= -getHeight()){
                
                raising = false;

            }
        }  
                
        return yMovement;
    }
            
    /**
     * gets x
     * @return 
     */
    
    public int getX(){
        
        return x;
    }
                   
    /**
     * gets y
     * @return 
     */
    
      public int getY(){
        
        return y;
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
     * gets raising
     * @return 
     */
    
    public boolean getRaising(){
        
        return raising;
    }
                         
    /**
     * gets bounds
     * @return 
     */
    
    public Rectangle getBounds(){
        
        return new Rectangle(x, y + 30, width, height + 30);
    }
}
