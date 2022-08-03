/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whackamole;

import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author hutch
 */
public class Clouds {
    
    private Image image;
    private int x;
    private int y;
    private int width;
    private int height;
    int movement = 0;
    
    public Clouds(int x, int y){
        
        this.x = getXPos();
        this.y = getYPos();
        loadImage();
        
    }
    
    public final void loadImage(){
        
        ImageIcon ii = new ImageIcon(getClass().getResource("images/cloud.png"));
        
        image = ii.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);

    }
    
    public Image getImage(){
        
        return image;
    }
    
        
    public final int getXPos(){
              
       Random random = new Random();      
       
       return random.ints(-1000, -200)
               .findFirst()
               .getAsInt();
    }
    
    public final int getYPos(){
        
       Random random = new Random();      
       
       return random.ints(0, 100)
               .findFirst()
               .getAsInt();
    }
    
    public int getX(){
        
        return x;
    }
    
       public int getY(){
        
        return y;
    }
       
    public void setX(){

        x = getXPos();
    }

      public void setY(){

        x = getYPos();
    }

    public int move(){

        return movement ++;
    }

    public void setMove(){

        movement = 0;
    }
}
