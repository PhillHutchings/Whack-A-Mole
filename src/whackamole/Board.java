/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whackamole;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;

/**
 *
 * @author hutch
 */
public class Board extends JPanel implements Sprites, MouseListener, MouseMotionListener{
    
    Clouds[] clouds = new Clouds[4];   
    Clouds[] startclouds = new Clouds[2];
    MoleHill[] moleHill = new MoleHill[12];
    JLabel[] hScores = new JLabel[3];           //array for high score labels
      
    static Mole mole;
    private Mole[] startMoles = new Mole[3];
    private MoleHill[] startMoleHills = new MoleHill[3];
    
    private JButton start;
    private JLabel scoreBoardLabel;
    private JLabel countDownLabel;
    private JLabel roundLabel;
    private JLabel timeLabel;
    private JLabel highScoreMain;
    private JLabel highScore1;
    private JLabel highScore2;
    private JLabel highScore3;
    private JLabel scoreText;
    
    private JTextField enterScore;
    
    private ImageIcon ii;
    private Image title;
    
    private ImageIcon ii2;
    private Image scoreBoard;
    
    private ImageIcon ii3;
    private Image blood;
    
    private ImageIcon ii4;
    private Image grass;
    
    private ImageIcon ii5;
    private Image flower1;
    
    private ImageIcon ii6;
    private Image fence;
    
    private ImageIcon ii7;
    private Image roundSign;
    
    private ImageIcon ii8;
    private Image highScoreboard;
    
    private ImageIcon ii9;
    private Image scoreTxt;
    
    private int round = 1;
    private int gameTime = 30000;
    
    private int score;
    private int countdown = 3;
    
    private boolean startScreen = true;
    private boolean firstTime = true;
    
    private boolean gameScreen = false;
    private boolean inGame = false;    
    private boolean endGame = false;
    private boolean diggingNoise = true;            //to start and stop digging noise withount multi calls
    
    private int mouseX;
    private int mouseY;
 
    private String userDir = System.getProperty("user.dir");
    private File backGroundFile = new File(userDir + "/src/whackamole/images/Background.png");
    private File startBackGroundFile = new File(userDir + "/src/whackamole/images/startBackground.png");
    private File endBackGroundFile = new File(userDir + "/src/whackamole/images/endBackGroundFile.png");
    
    private ImageIcon digGif;
    
    private Clip digSound;
    private ExecutorService exGPM;
   
    public Board(){
              
        setLayout(null);
        
        ii = new ImageIcon(getClass().getResource("images/Title.png"));
        title = ii.getImage().getScaledInstance(500, 110, Image.SCALE_SMOOTH);
        
        ii2 = new ImageIcon(getClass().getResource("images/ScoreBoard.png"));
        scoreBoard = ii2.getImage().getScaledInstance(270, 150, Image.SCALE_SMOOTH);
        
        ii3 = new ImageIcon(getClass().getResource("images/bloodSplat.png"));
        blood = ii3.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        
        digGif = new ImageIcon(getClass().getResource("images/digging.gif"));
        
        ii4 = new ImageIcon(getClass().getResource("images/grass.png"));
        grass = ii4.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        
        ii5 = new ImageIcon(getClass().getResource("images/flower1.png"));
        flower1 = ii5.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        
        ii6 = new ImageIcon(getClass().getResource("images/fence.png"));
        fence = ii6.getImage().getScaledInstance(BOARD_WIDTH, 300, Image.SCALE_SMOOTH);
        
        ii7 = new ImageIcon(getClass().getResource("images/roundSign.png"));
        roundSign = ii7.getImage().getScaledInstance(180, 200, Image.SCALE_SMOOTH);
        
        ii8 = new ImageIcon(getClass().getResource("images/highScoreBoard.png"));
        highScoreboard = ii8.getImage().getScaledInstance(380, 340, Image.SCALE_SMOOTH);
        
        ii9 = new ImageIcon(getClass().getResource("images/scoreTxt.png"));
        scoreTxt = ii9.getImage().getScaledInstance(250, 100, Image.SCALE_SMOOTH);
        
        Border border = BorderFactory.createLineBorder(Color.BLUE, 2);
         
        scoreBoardLabel = new JLabel("", SwingConstants.CENTER);
        scoreBoardLabel.setBounds(325, 200, 115 , 80);
        scoreBoardLabel.setFont(new Font("Serif", Font.BOLD, 25));
        scoreBoardLabel.setForeground(Color.YELLOW);
     
        countDownLabel = new JLabel("", SwingConstants.CENTER);
        countDownLabel.setBounds(325, 200, 115 , 80);
        countDownLabel.setFont(new Font("Serif", Font.BOLD, 45));
        countDownLabel.setForeground(Color.BLACK);
        
        roundLabel = new JLabel("", SwingConstants.CENTER);
        roundLabel.setBounds(557, 148, 125 , 30);
        roundLabel.setFont(new Font("Kristen", Font.BOLD, 18));
        roundLabel.setForeground(Color.BLACK);   
        
        timeLabel = new JLabel("", SwingConstants.CENTER);
        timeLabel.setBounds(557, 168, 125 , 60);
        timeLabel.setFont(new Font("Kristen", Font.BOLD, 25));
        timeLabel.setForeground(Color.BLACK);
              
        highScoreMain = new JLabel(" High Scores", SwingConstants.CENTER);
        highScoreMain.setBounds(28, 145, 200 , 35);
        highScoreMain.setFont(new Font("Kristen", Font.BOLD, 25));
        highScoreMain.setForeground(Color.BLACK);
        
        highScore1 = new JLabel("", SwingConstants.CENTER);
        highScore1.setBounds(34, 190, 200 , 35);
        highScore1.setFont(new Font("Kristen", Font.BOLD, 25));
        highScore1.setForeground(Color.BLACK);
        
        highScore2 = new JLabel("", SwingConstants.CENTER);
        highScore2.setBounds(28, 238, 200 , 35);
        highScore2.setFont(new Font("Kristen", Font.BOLD, 25));
        highScore2.setForeground(Color.BLACK);
        
        highScore3 = new JLabel("", SwingConstants.CENTER);
        highScore3.setBounds(34, 289, 200 , 35);
        highScore3.setFont(new Font("Kristen", Font.BOLD, 25));
        highScore3.setForeground(Color.BLACK);
        
        enterScore = new JTextField();
        enterScore.setBounds(330, 430, 120, 40);
        enterScore.setFont(new Font("Kristen", Font.BOLD, 25));
        enterScore.setForeground(Color.BLACK);
        enterScore.setHorizontalAlignment(JTextField.CENTER);
        
        scoreText = new JLabel();
        scoreText.setBounds(290, 320, 200, 100);
        scoreText.setFont(new Font("Kristen", Font.BOLD, 50));
        scoreText.setForeground(Color.ORANGE);
        scoreText.setHorizontalAlignment(JLabel.CENTER);

                       
        start = new JButton("START");
        start.setBounds(300, 300, 180 , 60);
        start.setFocusPainted(false);
        start.setFont(new Font("Serif", Font.BOLD, 25));
        start.setForeground(Color.WHITE);
               
        start.setBackground(new Color(102,68,0));
        start.addActionListener(new play());

        this.add(countDownLabel);
        countDownLabel.setVisible(false);
        
        this.add(scoreBoardLabel);
        scoreBoardLabel.setVisible(false);
        
        this.add(roundLabel);
        roundLabel.setVisible(false);
        
        this.add(timeLabel);
        timeLabel.setVisible(false);
        
        this.add(highScoreMain);
        highScoreMain.setVisible(false);
        
        this.add(highScore1);
        highScore1.setVisible(false);
        
        this.add(highScore2);
        highScore2.setVisible(false);
        
        this.add(highScore3);
        highScore3.setVisible(false);
        
        this.add(enterScore);
        enterScore.setVisible(false);
        
        this.add(scoreText);
        scoreText.setVisible(false);
        
        this.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        
        initiateStartMoles();
        initiateClouds();
        initiateMoleHills();
        creatHighScoreFile();

        hScores[0] = highScore1;                //high score array
        hScores[1] = highScore2;
        hScores[2] = highScore3;
              
        addKeyListener();   
    }
    
    /**
     * key listener for eng screen enter name text field
     */
    
    public final void addKeyListener(){
        
        enterScore.addKeyListener(new KeyListener(){
            
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
                if(enterScore.getText().length() >= 3){
                    
                    enterScore.setText(enterScore.getText().substring(0,3));
                    
                }
             
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    
                    String name = enterScore.getText();

                    String getScore = score + "-" + name;

                    addToHighScores(getScore);
                    
                    enterScore.setText("");
                    
                    restartGame();  //takes back to start screen
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }

        });
    }
    
    /**
     * resets to start screen
     */
    
    public void restartGame(){
        
        inGame = false;             
        gameScreen = false;
        endGame = false;
        
        startScreen = true;
        firstTime = true;
        diggingNoise = true;
          
        Mole.speed = 1;

        enterScore.setVisible(false);
        scoreText.setVisible(false);

        resetClock();           //resets variables
        resetScore();
        resetRound();   
        
        updateScore();          //updates to labels
        updateRound();
        
        resetTime();                //resets countdown clock
        
        initiateStartMoles();
        initiateClouds();
        initiateMoleHills();

        start.setVisible(true);

        repaint();
        
    }

    /**
     * gets saved start screen background
     * @return 
     */
    
    public BufferedImage startBackground(){
        
        BufferedImage image = null;
        
        try {
            
            image = ImageIO.read(startBackGroundFile);
            
        } catch (IOException ex) {
            
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return image;
    }
       
    /**
     * gets saved game screen background
     * @return 
     */
    
    public BufferedImage background(){
        
        BufferedImage image = null;
        
        try {
            
            image = ImageIO.read(backGroundFile);
            
        } catch (IOException ex) {
            
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return image;
    }
        
    /**
     * gets saved end screen background
     * @return 
     */
    
    public BufferedImage endBackground(){
        
        BufferedImage image = null;
        
        try {
            
            image = ImageIO.read(endBackGroundFile);
            
        } catch (IOException ex) {
            
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return image;
    }
    
    /**
     * initiates moles on start screen array
     */
    
    public final void initiateStartMoles(){

        int x = 165;
        int y = 500;
        
        for(int i = 0 ; i < startMoles.length; i++){
            
            startMoles[i] = new Mole(x, y, 0, 0, false);
            x += startMoles[i].getWidth();
        }     
        
        x = 165;
        y = y + startMoles[0].getHeight() /2 + 25;
        
         for(int i = 0 ; i < startMoleHills.length; i++){
            
            startMoleHills[i] = new MoleHill(x, y, 0, 80);
            x += startMoles[i].getWidth();
        }
    }
    
    /**
     * initiates cloud array
     */
    
    public final void initiateClouds(){

        for(int i = 0 ; i < clouds.length; i++){      //main game clouds
            
            clouds[i] = new Clouds(0,0);

        }        
        
        for(int i = 0 ; i < startclouds.length; i++){            //start screen clouds
            
            startclouds[i] = new Clouds(0,0);

        } 
    }
    
     /**
     * initiates mole array
     */
    
    public final void initiateMoleHills(){

        int y = 420;
        int x = 135;
        int width = -10;
        int height = 35;
        
        for(int i = 0 ; i < 4; i++){            //top mole hills
            
            moleHill[i] = new MoleHill(x, y, width, height);
                          
            x += 120;
                                                               
        }    
        
        y = 570;
        x = 75;
        width = 20;
        height = 50;
        
        for(int j = 4 ; j < 8; j++){                //middle mole hills
            
            moleHill[j] = new MoleHill(x, y, width, height);
                
            x += 160;
                                                               
        } 
        
        y = 780;
        x = 15;
        width = 50;
        height = 80;
        
        for(int k = 8 ; k < 12; k++){               //bottom mole hills

            moleHill[k] = new MoleHill(x, y, width, height);
                         
            x += 190;
                                                               
        }     
     
    }

    
    /**
     *
     * @param g
     */
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        
        if(startScreen == true){
            
          g2d.drawImage(startBackground(), null, null);              //start screen

        }
        
        if(gameScreen == true){
                     
            g2d.drawImage(background(), null, null);             // before game playing
              
            drawClouds(g);
      
            drawTitleAndBoard(g);
            
            if(firstTime == true){
                
                moleHillRisingTimer(g);
                drawHideBar(g); 
                
            }else{
                
                drawHideBar(g);             //cannot be added to background as moles will show on top  
                drawMoleHills(g);
            }       
 
        }
        
        if(inGame == true){

            playing(g);                 //game in play
            
        }
        
        if(endGame == true){
            
            g2d.drawImage(endBackground(), null, null);          //end screen
            
            scoreText.setVisible(true);
            scoreText.setText(String.valueOf(score));

            enterScore.setVisible(true);
            
        }
    }
    
    /**
     * painting the start screen
     * @param g
     */
    
    public void startScreen(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(new Color(128,191,255));
        g2d.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);   //blue sky
        
        Ellipse2D.Double circle = new Ellipse2D.Double(40, 40, 80, 80);   //sun
        
        g2d.setColor(Color.YELLOW); 
        g2d.fill(circle);
               
        boolean cloudImage1 = false;
        boolean cloudImage2 = false;
        
        do{                         //start clouds
            
            cloudImage1 = g2d.drawImage(startclouds[0].getImage(), 90, 100, null);
            cloudImage2 = g2d.drawImage(startclouds[1].getImage(), 600, 100, null);
            
        }while(cloudImage1 == false || cloudImage2 == false);
        
        drawTitle(g);           //draws main title

        g2d.setColor(new Color(102,204,0));                 //green grass
        g2d.fillRect(0, 560, BOARD_WIDTH, BOARD_HEIGHT);
                        
        boolean fenceCheck1 = false;
        boolean fenceCheck2 = false;
        
        do{                         //draws fence
            
            fenceCheck1 = g2d.drawImage(fence, -105, 370, null);
            fenceCheck2 = g2d.drawImage(fence, fence.getWidth(null) / 2 + 8, 370, null);
            
        }while(fenceCheck1 == false);   //check image is loaded before save

        for(int i = 0 ; i < startMoles.length;){            //start screen moles
            
            boolean check = g2d.drawImage(startMoles[i].getImage(), startMoles[i].getX(), startMoles[i].getY(), null);
            
            if(check == true){          //check image is loaded before save
                
                i++;
            }
        }
          
        for(int i = 0 ; i < startMoleHills.length;){           //start screen mole hills

            boolean check = g2d.drawImage(startMoleHills[i].getImage(), startMoleHills[i].getX(), startMoleHills[i].getY(), this); 

            if(check == true){      //check image is loaded before save
                
                i++;
                
            }
        }

        boolean grassImage1 = false;
        boolean grassImage2 = false;
        boolean grassImage3 = false;       
  
        do{                                         //grass oimages
                   
            grassImage1 = g2d.drawImage(grass, 100, 700, null);      //check image is loaded before save
            grassImage2 = g2d.drawImage(grass, 500, 650, null);      //check image is loaded before save
            grassImage3 = g2d.drawImage(grass, 600, 500, null);      //check image is loaded before save
        
        }while(grassImage1 == false || grassImage2 == false || grassImage3 == false);
        
        boolean flowerImage1 = false;
        boolean flowerImage2 = false;
        
        do{                                         //flower images
            
            flowerImage1 = g2d.drawImage(flower1, 50, 600, null);        //check image is loaded before save
            flowerImage2 = g2d.drawImage(flower1, 600, 700, null);        //check image is loaded before save
            
        }while(flowerImage1 == false || flowerImage2 == false);
        
    }
            
    /**
     * painting the game screen
     * @param g
     */
    
    public void gameScreen(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(new Color(128,191,255));
        g2d.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);   //blue sky
                        
        Ellipse2D.Double circle = new Ellipse2D.Double(40, 40, 80, 80);   //sun
        
        g2d.setColor(Color.YELLOW); 
        g2d.fill(circle);
        
        drawFieldAndHill(g);        //draws field and hill
        drawScoreBoard(g);          //draw scoreboard
                 
        boolean fenceCheck1 = false;
        boolean fenceCheck2 = false;
        
        fence = ii6.getImage().getScaledInstance(BOARD_WIDTH, 260, Image.SCALE_SMOOTH);
        
        do{                         //draws fence
            
            fenceCheck1 = g2d.drawImage(fence, -105, 230, null);
            fenceCheck2 = g2d.drawImage(fence, fence.getWidth(null) / 2 + 8, 230, null);
            
        }while(fenceCheck1 == false);
                                      
        boolean grassImage1 = false;
        boolean grassImage2 = false;
        boolean grassImage3 = false;
                     
        do{                                         //grass images
            
            grassImage1 = g2d.drawImage(grass, 30, 480, null);      //check image is loaded before save
            grassImage2 = g2d.drawImage(grass, 550, 650, null);      //check image is loaded before save
            grassImage3 = g2d.drawImage(grass, 600, 300, null);      //check image is loaded before save
        
        }while(grassImage1 == false || grassImage2 == false || grassImage3 == false);
        
        boolean flowerImage1 = false;
        boolean flowerImage2 = false;
        
        do{                                         //flower images
            
            flowerImage1 = g2d.drawImage(flower1, 200, 680, null);        //check image is loaded before save
            flowerImage2 = g2d.drawImage(flower1, 500, 500, null);        //check image is loaded before save
            
       }while(flowerImage1 == false || flowerImage2 == false);            

    }
    
    /**
     * end screen to enter name for score
     * @param g 
     */
    
    public void endScreen(Graphics g){
                      
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(new Color(128,191,255));
        g2d.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);   //blue sky
        
        drawTitle(g);
        
        drawScoreTxt(g);
              
    }
            
    /**
     * painting the game screen
     * @param g
     */
    
    public void playing(Graphics g){
                
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(background(), null, null);
        drawClouds(g);        
        drawTitleAndBoard(g);

        drawMole(g);
        drawHideBar(g);
        drawMoleHills(g);
               
    }
       
    /**
     * draws main title
     * @param g 
     */
    
    public void drawTitle(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        boolean titleCheck = false;
        
        do{
            titleCheck = g2d.drawImage(title, 150, 70, null);         
            
        }while(titleCheck == false);
    }
    
    /**
     * draws scoreboard
     * @param g 
     */
    
    public void drawScoreBoard(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        boolean scoreCheck = false;
         
        do{
            
            scoreCheck = g2d.drawImage(scoreBoard, 250, 180, null);
            
        }while(scoreCheck == false);
    }
    
    /**
     * draws title, round board and high score board
     * @param g 
     */
    
    public void drawTitleAndBoard(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        boolean highScoreCheck = false;         //high score board
        
        do{
            
            highScoreCheck = g2d.drawImage(highScoreboard, -60, 105, null);
            
        }while(highScoreCheck == false);
        
        boolean titlecheck  = false;
        boolean roundBCheck = false;
           
        do{             //draw title and round board
            
            titlecheck = g2d.drawImage(title, 150, 20, null);       
            roundBCheck  = g2d.drawImage(roundSign, 530, 80, null);
            
        }while(titlecheck == false || roundBCheck  == false);
  
    }
       
    /**
     * draws clouds
     * @param g 
     */
    
    public void drawClouds(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        for(int i = 0 ; i < clouds.length; i++){             //drawing clouds

            g2d.drawImage(clouds[i].getImage(), clouds[i].getX() + clouds[i].move(), clouds[i].getY(), this);

            if(clouds[i].getX() + clouds[i].movement == BOARD_WIDTH + 200){

                clouds[i].setMove();              
                
            }
        }
    }
    
    /**
     * draws bars to hide moles + grass and flowers
     * @param g 
     */
    
    public void drawHideBar(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;            //new Color(102,204,0)
        
        g2d.setColor(new Color(102,204,0)); 
        
        g2d.fillRect(0, 445, BOARD_WIDTH, 50);            //top part for upper moles
        
        g2d.fillRect(0, 605, BOARD_WIDTH, 80);            //middlepart for middle moles
        
        g2d.fillRect(0, 830, BOARD_WIDTH, 50);   //bottom part for lower moles
 
    }
    
    /**
     * draws mole hills
     * @param g 
     */
    
    public void drawMoleHills(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        boolean moleHillCheck = false;
        
        for(int i = 0 ; i < moleHill.length;){           //mole hills
                        
            moleHillCheck  = g2d.drawImage(moleHill[i].getImage(), moleHill[i].getX(), moleHill[i].getY(), this); 
            
            if(moleHillCheck == true){
                i++;
            }

        }       
    }
    
    /**
     * timer to make the mole hills rise up
     * @param g 
     */
    
    public void moleHillRisingTimer(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        int raised = 0;
                
        for(int i = 0 ; i < moleHill.length; i++){           //mole hills

            g2d.drawImage(moleHill[i].getImage(), moleHill[i].getX(), moleHill[i].getY() + moleHill[i].getHeight() + moleHill[i].move(), this);
                       
            if(moleHill[i].getRaising() == false){

                raised ++;
                
                if(raised == 12){
                    
                    drawHideBar(g);
                    drawMoleHills(g);
                    
                    digSound.stop();
                    
                    gameScreen = false;
                    inGame = true;        
                    firstTime = false;

                    countDown();
                }               
            }  
        }  
    }
    
    
    /**
     * draws hills and fields
     * @param g 
     */
    
    public void drawFieldAndHill(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(new Color(30,179,0));                  //darker outlines
        g2d.drawOval(0, 300, BOARD_WIDTH, BOARD_HEIGHT);
        g2d.drawRect(-5, 500, BOARD_WIDTH, BOARD_HEIGHT);
        
        g2d.setColor(new Color(102,204,0));
        g2d.fillRect(0, 380, BOARD_WIDTH, BOARD_HEIGHT);            //hill and field
        g2d.fillOval(0, 300, BOARD_WIDTH, BOARD_HEIGHT);
    }
    
    /**
     * draws moles
     * @param g 
     */
    
    public void drawMole(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
   
        if(mole != null){
            
            if(gameTime == 0){
                
                mole = Mole.newMole();              //to hide mole when game time runs out otherwise will be still sticking ount
                
            }else{
            
                if(mole.isHit()){

                    g2d.drawImage(mole.getImage(), mole.getX(), mole.getY() + mole.yMovement , this);       //draws blood splat and still mole
                    g2d.drawImage(blood, mouseX - 40, mouseY -65, null);


                }else{

                    g2d.drawImage(mole.getImage(), mole.getX(), mole.getY() + mole.move(), this);           //draws moving mole

                }
            }
        }
    }
    
    /**
     * draws score text for end screen
     * @param g 
     */
    
    public void drawScoreTxt(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        boolean scoreTxtCheck = false;
         
        do{
            
            scoreTxtCheck = g2d.drawImage(scoreTxt, 270, 200, null);
            
        }while(scoreTxtCheck == false);
    }

    /**
     * saves the background
     */
    
    public final void saveBackground(){
        
        if(!backGroundFile.exists()){
            
            BufferedImage image = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT,BufferedImage.TYPE_INT_RGB);

            try {

                Graphics2D graphic = image.createGraphics();  
 
                gameScreen(graphic);    

                ImageIO.write(image, "png", backGroundFile);
              

            } catch (IOException ex) {

                Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
       
    /**
     * saves the background
     */
    
    public final void saveStartBackground(){
        
        if(!startBackGroundFile.exists()){
            
            BufferedImage image = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT,BufferedImage.TYPE_INT_RGB);

            try {

                Graphics2D graphic = image.createGraphics();  

                startScreen(graphic);    

                ImageIO.write(image, "png", startBackGroundFile);


            } catch (IOException ex) {

                Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
           
    /**
     * saves the background
     */
    
    public final void saveEndBackground(){
        
        if(!endBackGroundFile.exists()){
            
            BufferedImage image = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT,BufferedImage.TYPE_INT_RGB);

            try {

                Graphics2D graphic = image.createGraphics();  

                endScreen(graphic);    

                ImageIO.write(image, "png", endBackGroundFile);


            } catch (IOException ex) {

                Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * updates scoreboard with score
     */
    
    public void updateScore(){
        
        scoreBoardLabel.setText(String.valueOf(score));
    }
    
    /**
     * updates round on board
     */
    
    public void updateRound(){
          
        roundLabel.setText("ROUND " + round);
    }
    
    /**
     * updates game time
     */
    
    public void updateTime(){
        
        gameTime -= 50;
        timeLabel.setText(String.valueOf(gameTime / 1000));
    }
    
    /**
     * resets round time
     */
    
    public void resetTime(){
        
        gameTime = 30000; 
    }
    
    /**
     * resets the countdown
     */
    
    public void resetClock(){
        
        countdown = 3;
    }
    
    /**
     * resets the round back to 1
     */
    
    public void resetRound(){
        
        round = 1;
    }
    
    /**
     * sets round to next round
     */
    
    public void nextRound(){
        
        round++;
    }
    
    /**
     * resets the score to zero
     */
    
    public void resetScore(){
        
        score = 0;
    }
  
    /**
     * begins game hills rising
     */
    
    public void gameScreenTimer(){
        
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        
            Runnable task = () -> { 
                
                if(diggingNoise == true){
                    
                    digNoise();
                    
                    diggingNoise = false;
                }
                                              
                  repaint();
                  
                  if(inGame == true){
                      
                    ex.shutdown();
                    
                  }                             
            };
        
        ex.scheduleAtFixedRate(task, 3000, 10, TimeUnit.MILLISECONDS);
              
    }
    
    /**
     * begins game countdown
     */
    
    public void countDown(){
               
        updateRound();
        
        countDownLabel.setVisible(true);
        countDownLabel.setText(String.valueOf(countdown));
                     
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        
            Runnable task = () -> { 
                                              
                countdown-- ;
                countDownLabel.setText(String.valueOf(countdown)); 
                
                if(countdown == 0){

                    countDownLabel.setVisible(false);
                    scoreBoardLabel.setVisible(true);
                    
                    inGame = true;    
                    
                    mole = Mole.newMole();
                       
                    resetTime();
                    
                    playGame();
                    
                    ex.shutdownNow();
                }
                    
            };
        
        ex.scheduleAtFixedRate(task, 1000, 1000, TimeUnit.MILLISECONDS);
              
    }
    
    /**
     * game timer
     */
    
    public void playGame(){
                   
        ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();         

            Runnable play = () -> {
                
                if(gameTime == 0){     //checks score sets up for new level                   

                    if(round == 10){        //checks for last round to go to end screen
                                                
                        endGame = true;
                        inGame = false;
                        gameScreen = false;
                        
                        scoreBoardLabel.setVisible(false);
                        roundLabel.setVisible(false);
                        timeLabel.setVisible(false);
                        highScoreMain.setVisible(false);
                        highScore1.setVisible(false);            
                        highScore2.setVisible(false);
                        highScore3.setVisible(false);
                        
                        exGPM.shutdownNow();      //stops game music
       
                        es.shutdown();
                        
                        repaint();
     
                        
                    }else{                  //initiates next round
                        
                        Mole.speed ++;
                        
                        resetClock();
                        scoreBoardLabel.setVisible(false);
                        updateScore();   

                        nextRound();
                        countDown();

                        es.shutdownNow();
                    }
                    
                }else{

                    updateTime();
                    repaint();
                }
            };   

        es.scheduleAtFixedRate(play, 0, 50, TimeUnit.MILLISECONDS);
    }
    
       
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
                
        gunShot();          //makes gun shot noise
    
        try{
            
            for(int i = 0; i < moleHill.length; i++){
                  
                if(mole.getOriginalY() - 10 == moleHill[i].getY() && mole.getX() == moleHill[i].getX() + 20 || mole.getX() == moleHill[i].getX() + 25 || mole.getX() == moleHill[i].getX() + 35){         //gets moles mole hill    
                    
                    if(mole.getBounds().contains(e.getPoint()) && !moleHill[i].getBounds().contains(e.getPoint())){            //checks for hit on mole but not on mole hill or dirt below

                        if(mole.isHit() == false){                      //checks mole hasnt been hit before

                            mole.hit();                         //sets mole hit to true
                            moleNoise();            //makes mole noise

                            mouseX = e.getX();          //to get the point at which the blood splatter paints
                            mouseY = e.getY();

                            int level = mole.getOriginalY();        //works out how many points you get for each mole

                            switch(level){

                                case 790:

                                    score += 5 + (round * 2);

                                    break;

                                case 580:

                                    score += 15 + (round * 2);

                                    break;

                                case 430:

                                    score += 25 + (round * 2);

                                    break;
                            }

                            updateScore();

                            new javax.swing.Timer(500, new ActionListener(){            //timer to let dead mole stay for half a second before creating a new one

                                @Override
                                public void actionPerformed(ActionEvent e3) {            

                                    mole = Mole.newMole();

                                    ((Timer)e3.getSource()).stop();
                                }

                            }).start();
                        }
                    } 
                }
            }
                           
        }catch(NullPointerException ex){
            
            //do nothing to stop throwing on start screen
        }
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

    @Override
    public void mouseDragged(MouseEvent e) {
        
        //if(e == MouseEvent.m)
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
        
    }
    
    /**
     * class for start button
     */
    
    class play implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
               
            startScreen = false;
            gameScreen = true; 
            start.setVisible(false);
            
            roundLabel.setVisible(true);
            timeLabel.setVisible(true);
            highScoreMain.setVisible(true);
            highScore1.setVisible(true);
            highScore2.setVisible(true);
            highScore3.setVisible(true);
            
            sortHighScores();               //displays high scores
                       
            gameScreenTimer();          //sets 3 second delay before mole hill movement
                                                
            setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
            new ImageIcon(getClass().getResource("images/crosshairs.png")).getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH),
            new Point(0,0),"custom cursor"));;

        }          
    }
    
    /**
     *  makes gun shot noise
     */
    
    public void gunShot(){
        
        try{                                                   
            
            File gunShot = new File("src/whackamole/sounds/gunShot.wav");
            AudioInputStream aisGun = AudioSystem.getAudioInputStream(gunShot.toURI().toURL());
            
            Clip clip = AudioSystem.getClip();
            
            clip.open(aisGun);
            clip.start();
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            
        }     
    }
    
    /**
     * makes mole noise when hit
     */
    
    public void moleNoise(){
        
        try{                                                    //makes mole shot noise
            
            File moleSound = new File("src/whackamole/sounds/moleShot.wav");
            AudioInputStream aisMole = AudioSystem.getAudioInputStream(moleSound.toURI().toURL());
            
            Clip clip = AudioSystem.getClip();
            
            clip.open(aisMole);
            clip.start();
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
    /**
     * makes digging noise
     */
    
    public void digNoise(){
        
        try{                                                    //makes mole shot noise
            
            File digS = new File("src/whackamole/sounds/digging.wav");
            AudioInputStream aisDig = AudioSystem.getAudioInputStream(digS.toURI().toURL());
            
            digSound = AudioSystem.getClip();
                 
            digSound.open(aisDig);
            
            FloatControl fc =  (FloatControl) digSound.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = 0.1f;
            
            fc.setValue(20f * (float) Math.log10(volume));
            
            digSound.start();
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
    /**
     * plays game music
     */
    
    public void gamePlayMusic(){
             
        try {
        
            File gameMusic = new File("src/whackamole/sounds/gamePlayMusic.wav");
            AudioInputStream aisGame = AudioSystem.getAudioInputStream(gameMusic.toURI().toURL());

            final Clip clip = AudioSystem.getClip();
            
            clip.open(aisGame);
            
            FloatControl fc =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = 0.3f;
            
            fc.setValue(20f * (float) Math.log10(volume));
            
            exGPM = Executors.newSingleThreadExecutor();
            
            Runnable playMusic = () -> {
                
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                                
            };
            
            exGPM.submit(playMusic);


        } catch (MalformedURLException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex1) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex1);
            
        }
        
    }
    
    /**
     * loads game screens and noises then adds start button
     */
    
    public void loadGame(){
        
        gamePlayMusic();
        saveStartBackground();
        saveBackground();
        saveEndBackground();
        gunShot();
        moleNoise();
        this.add(start);
    }
    
    /**
     * creates the high score file
     */
    
    public final void creatHighScoreFile(){
        
        Path highScores = Paths.get("highScores.txt");
        
        if(!highScores.toFile().exists()){
            
            try {
                
                Files.createFile(highScores);
                
            } catch (IOException ex) {
                
                Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
       
    /**
     * adds score to high scores
     * @param highScore 
     */
    
    public void addToHighScores(String highScore){
        
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("highScores.txt", true))){
            
            bw.write(highScore);
            bw.newLine();
            
        } catch (IOException ex) {
            
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
    * reads high score text file
    */
    
    public final void sortHighScores(){
        
        Comparator<String> comp = (String::compareTo);
        String s = null;
        Set<String> scores = new TreeSet<>(new Comparator<String>() {
            
            @Override
            public int compare(String o1, String o2) {
                
                if(o1.contains("-") && o2.contains("-")){
                    
                    return String.format("%3s", o2.substring(0, o2.indexOf("-")-1)).replace(" ","0").compareTo(String.format("%3s", o1.substring(0, o1.indexOf("-")-1)).replace(" ","0"));
                    
                }else{
                    
                    return 0;
                }
            }
      
        });
         
        try(BufferedReader br = new BufferedReader(new FileReader("highScores.txt"))){
            
    
            while((s = br.readLine())!= null){
                
                scores.add(s);
                        
            }
            
  
        } catch (FileNotFoundException ex) {
            
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                    
        try {           //resets highscore file in order
            
            Files.delete(Paths.get("highScores.txt"));
            
        } catch (IOException ex) {
            
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        creatHighScoreFile();           //creates new high score file

        int i = 0;

        for(String o: scores){                  //reEnters highscores in order
              
            addToHighScores(o);

            if(i < 3){                  //puts top 3 in the high score labels

                hScores[i].setText(o);
                i++;
            }               
        }
    }
    
}
