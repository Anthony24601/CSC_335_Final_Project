/**
File: XTankUI.java
Author: Anthony Hsu and Miles Gendreau
Course: CSC 335
Purpose: A class that creates a UI window and draws the GameModel

Public Methods:
.run() runs the UI and draws the GameModel
XTankUI objects are instantiated with a player object
*/

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ChessUI extends Thread
{
    private Player player;
    private PaintEvent paint_canvas;
    private Canvas canvas;
    private Display display;
    private Shell shell;
    private Board board;
    public static Font font;
    boolean update;
    
    ArrayList<String> possible_moves;
    
    /**
     * XTankUI constructor
     * @param player is a Player
     */
    public ChessUI(Player player)
    {
        this.player = player;
        display = new Display();
        font = new Font(display, "Comic Sans", 56, SWT.BOLD);
        update = false;
        possible_moves = new ArrayList<>();
    }
    
    /**
     * runs the UI and draws the GameModel
     * @return None
     */
    public void run()
    {
        shell = new Shell(display);
        shell.setText("Chess");
        shell.setLayout(new FillLayout());
        shell.setSize(800, 820);
        canvas = new Canvas(shell, SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED);
        
        canvas.addPaintListener(event -> {
            paint_canvas = event;
            board = player.getBoard();
            event.gc.fillRectangle(canvas.getBounds());
            if (board != null) {
            	for (int row = 1; row <= 8; row++) {
            		for (int col = 1; col <= 8; col++) {
            			draw_piece(board.get(row,col),row,col);
            		}
            	}
            }
            highlight(possible_moves);
          /*
          Color yellow = new Color(255,255,0);
          event.gc.setBackground(yellow);
          event.gc.setAlpha(100);
          event.gc.fillRectangle(0,0,100,100);
          */
        });

        canvas.addMouseListener(new MouseListener() {
            public void mouseDown(MouseEvent e) {
            	int col = e.x/100;
    			int row = e.y/100;
    			player.move((char)('a' + col) + "" + (row+1));
    			canvas.redraw();
            } 
            public void mouseUp(MouseEvent e) {} 
            public void mouseDoubleClick(MouseEvent e) {} 
        });

        canvas.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
            }
            public void keyReleased(KeyEvent e) {}
        });
    
        shell.open();
        while (!shell.isDisposed()) {
        	if (update) { 
        		canvas.redraw();
        		update = false;
        	}
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();      
    }
    
    public void update() {
    	update = true;
    	display.wake();
    }
    
    public void updatePossibles(ArrayList<String> possible) {
    	possible_moves = possible;
    }
    
    // Private Methods ----------------------------
    
    /**
     * prints You died on the screen
     * @return None
     */
    private void printGameOver() {
    	paint_canvas.gc.setFont(font);
    	paint_canvas.gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
    	paint_canvas.gc.drawText("You Died!", 250, 250);
    }
    
    /**
     * draws a piece
     * @param t is a Tank Object
     * @return None
     */
    private void draw_piece(Piece p, int row, int col) {
        Image img = new Image(display,p.getPicture(row, col));
        //if want left and right, use this
        //paint_canvas.gc.drawImage(img, 0, 0, img.getBounds().width, img.getBounds().height, (row-1)*100, (col-1)*100, 100, 100);
        //if want up and down, use this
        paint_canvas.gc.drawImage(img, 0, 0, img.getBounds().width, img.getBounds().height, (col-1)*100, (row-1)*100, 100, 100);
		img.dispose();
    }
    
    private void highlight(ArrayList<String> possible) {
    	
    	//Updown Orientation with white on top
    	for (String square: possible) {
    		//System.out.println(square);
			int row = (square.charAt(1)-'0'-1)*100;
			int col = (square.charAt(0)-'a')*100;
			Color yellow = new Color(255,255,0);
			paint_canvas.gc.setBackground(yellow);
			paint_canvas.gc.setAlpha(100);
			paint_canvas.gc.fillRectangle(col,row,100,100);
		}
    }
}


