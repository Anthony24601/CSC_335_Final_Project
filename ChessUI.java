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
    
    /**
     * XTankUI constructor
     * @param player is a Player
     */
    public ChessUI(Player player)
    {
        this.player = player;
        display = new Display();
        font = new Font(display, "Comic Sans", 56, SWT.BOLD);
    }
    
    /**
     * runs the UI and draws the GameModel
     * @return None
     */
    public void run()
    {
        shell = new Shell(display);
        shell.setText("xtank");
        shell.setLayout(new FillLayout());
        shell.setSize(800, 820);
        canvas = new Canvas(shell, SWT.NO_BACKGROUND);
        
        canvas.addPaintListener(event -> {
            paint_canvas = event;
            board = player.getBoard();
            event.gc.fillRectangle(canvas.getBounds());
            if (board != null) {
            	for (int row = 0; row < 8; row++) {
            		for (int col = 0; col < 8; col++) {
            			draw_piece(board.get(row,col),row,col);
            		}
            	}
            }
        });

        canvas.addMouseListener(new MouseListener() {
            public void mouseDown(MouseEvent e) {
            	int col = e.x/100;
    			int row = e.y/100;
    			System.out.println(row + " " + col);
            } 
            public void mouseUp(MouseEvent e) {} 
            public void mouseDoubleClick(MouseEvent e) {} 
        });

        canvas.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
            }
            public void keyReleased(KeyEvent e) {}
        });
    
        //Runnable runnable = new Runner(player);
        //display.asyncExec(runnable);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        display.dispose();      
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
        paint_canvas.gc.drawImage(img, 0, 0, img.getBounds().width, img.getBounds().height, row*100, col*100, 100, 100);
		img.dispose();
    }
}

