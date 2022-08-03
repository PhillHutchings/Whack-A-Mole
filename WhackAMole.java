/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whackamole;

import javax.swing.JFrame;

/**
 *
 * @author hutch
 */
public class WhackAMole extends JFrame implements Sprites{

    Board board = new Board();
    
    public WhackAMole(){
       
        init();
        
   }
    
    private void init(){
        
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Whack-A-Mole");
        add(board);
        addMouseListener(board);
        
        board.loadGame();
  
        setVisible(true);
        setResizable(false);
        
    }

    public static void main(String[] args) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                
                new WhackAMole();
            }
        });
    }
    
}
