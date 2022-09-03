import javax.swing.*;
import java.awt.*; 
import java.util.Random;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener {

    // declaring and initializing standard panel dimensions
    static final int SCREEN_WIDTH = 1000;
	static final int SCREEN_HEIGHT = 750;
	static final int UNIT_SIZE = 50;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
	static final int DELAY = 175;

    // snake can never be bigger than the game
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];

    // default snake length
	int bodyParts = 6;

    // declaring variables related to apples
	int applesEaten;
	int appleX;
	int appleY;
    
    // defualt direction that the snake is facing
    // initially the snake is also not moving
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel(){
        // set up the panel and start the game
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.green);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() {
        // generate an apple, run the game, and start the timer
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		
		if(running) {
            // draw apple
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			for(int i = 0; i< bodyParts;i++) {
                // draw snake head
				if(i == 0) {
					g.setColor(new Color(51, 153, 255));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
                // draw snake body
				else {
					g.setColor(new Color(51, 204, 255));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}			
			}
            // font and text
			g.setColor(Color.white);
			g.setFont( new Font("Arial",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
		
	}

	public void newApple(){
        // generates a new apple randomly on the panel
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}

	public void move() {
        // snake movement
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
        // snake movement depending on input
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		
	}

    // if an apple is eaten snake gets bigger, score increases, and a new apple is generated
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}

	public void checkCollisions() {
		//checks if head collides with the snakes body
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				running = false;
			}
		}
		//check if head touches the left border of the panel
		if(x[0] < 0) {
			running = false;
		}
		//check if head touches the right border of the panel
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//check if head touches the top border of the panel
		if(y[0] < 0) {
			running = false;
		}
		//check if head touches the bottom border of the pannel
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
        // if the game is dead the timer will stop
		if(!running) {
			timer.stop();
		}
	}

	public void gameOver(Graphics g) {
		// game over screen text
		g.setColor(Color.white);
		g.setFont( new Font("Arial",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());

        // more game over screen text
		g.setColor(Color.white);
		g.setFont( new Font("Arial",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
        // whenever the game is running we can move, check if we ate an apple
        // check if we ran into a border or the snakes body
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
        // if we die then we repaint 
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
        // in control of the snakes movement depending on user input
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
}

