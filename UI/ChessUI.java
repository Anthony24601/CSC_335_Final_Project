package UI;
/**
File: XTankUI.java
Author: Anthony Hsu and Miles Gendreau
Course: CSC 335
Purpose: A class that creates a UI window and draws the GameModel
XTankUI objects are instantiated with a player object
*/

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import Game.Board;
import PiecePackage.Piece;
import ServerClient.Player;
import ServerClient.Main;

public class ChessUI extends Thread
{
    private static final String MOVE_SOUND_FILE = "sounds/move.wav";

    private Player player;
    private PaintEvent paint_canvas;
    private Canvas canvas;
    private Display display;
    private Shell shell;
    private Board board;
    private SoundEffect moveSound;
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
        moveSound = new SoundEffect(MOVE_SOUND_FILE);
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
            if(player.getModel()!=null)
                player.getModel().checkDraws();
            event.gc.fillRectangle(canvas.getBounds());
            if (board != null) {
            	for (int row = 1; row <= 8; row++) {
            		for (int col = 1; col <= 8; col++) {
            			draw_piece(board.get(row,col),row,col);
            		}
            	}
            }
            highlight(possible_moves);
            
            //Win Condition
            if (player.getModel() != null) {
            	if (player.getModel().getIsOver()) {
            		if (!player.getModel().getHasCheckmate()) {
            			printDraw();
            		}
            		else if (player.getType().equals("Local")) {
            			printWin();
            		} else {
            			if ((player.getModel().isWhitesTurn() && player.getColor() == Piece.WHITE) ||
            					(!player.getModel().isWhitesTurn() && player.getColor() == Piece.BLACK)) {
                    		printLived();
                    	} else {
                    		printDied();
                    	}
            		}
                }
            }
        });

        canvas.addMouseListener(new MouseListener() {
            public void mouseDown(MouseEvent e) {
            	int col = e.x/100;
    			int row = 8-e.y/100;
    			if (player.getModel() != null && !player.getModel().getIsOver()) {
    				player.move((char)('a' + col) + "" + row);
        			canvas.redraw();
    			}
            }
            public void mouseUp(MouseEvent e) {}
            public void mouseDoubleClick(MouseEvent e) {}
        });

        canvas.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
            }
            public void keyReleased(KeyEvent e) {}
        });
        
        
        //---- main menu
  		Menu menuBar, logMenu;
  		MenuItem logMenuHeader;
  		MenuItem exitItem, saveItem, forfeitItem;

  		menuBar = new Menu(shell, SWT.BAR);

  		logMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
  		logMenuHeader.setText("Options");
  		logMenu = new Menu(shell, SWT.DROP_DOWN);
  		logMenuHeader.setMenu(logMenu);

  		saveItem = new MenuItem(logMenu, SWT.PUSH);
  		saveItem.setText("Save");
  		exitItem = new MenuItem(logMenu, SWT.PUSH);
  		exitItem.setText("Exit");
  		forfeitItem = new MenuItem(logMenu, SWT.PUSH);
  		forfeitItem.setText("Forfeit");

  		exitItem.addSelectionListener(new SelectionListener() {
  	    	public void widgetSelected(SelectionEvent event) {
  	    		shell.close();
  	    		display.dispose();
  	    	}
  	    	public void widgetDefaultSelected(SelectionEvent event) {
  	    		shell.close();
  	    		display.dispose();
  	    	}
  	    });
  		
  		saveItem.addSelectionListener(new SelectionListener() {
  	    	public void widgetSelected(SelectionEvent event) {
  	    		player.saveGame("game");
  	    		System.out.println("game saved!");
  	    	}
  	    	public void widgetDefaultSelected(SelectionEvent event) {
  	    		;
  	    	}
  	    });
  		
  		forfeitItem.addSelectionListener(new SelectionListener() {
  	    	public void widgetSelected(SelectionEvent event) {
  	    		player.move("Forfeit");
    			canvas.redraw();
  	    	}
  	    	public void widgetDefaultSelected(SelectionEvent event) {
  	    		;
  	    	}
  	    });
        
  		shell.setMenuBar(menuBar);
  		
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
        player.finished = true;
        display.dispose();
    }

    public void update() {
        update = true;
    	display.wake();
        moveSound();
    }

    public void updatePossibles(ArrayList<String> possible) {
    	possible_moves = possible;
    }

    public void moveSound() {
        moveSound.play();
    }

    // Private Methods ----------------------------

    /**
     * prints You died on the screen
     * @return None
     */
    private void printDied() {
    	paint_canvas.gc.setFont(font);
    	paint_canvas.gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
    	paint_canvas.gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
    	paint_canvas.gc.drawText("You Died!", 275, 300);
    }
    
    /**
     * prints You died on the screen
     * @return None
     */
    private void printLived() {
    	paint_canvas.gc.setFont(font);
    	paint_canvas.gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
    	paint_canvas.gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
    	paint_canvas.gc.drawText("You Lived!", 275, 300);
    }
    
    /**
     * prints You died on the screen
     * @return None
     */
    private void printDraw() {
    	paint_canvas.gc.setFont(font);
    	paint_canvas.gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
    	paint_canvas.gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
    	paint_canvas.gc.drawText("DRAW!!!", 275, 300);
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
        paint_canvas.gc.drawImage(img, 0, 0, img.getBounds().width, img.getBounds().height, (col-1)*100, (8-row)*100, 100, 100);
		img.dispose();
    }

    private void highlight(ArrayList<String> possible) {
    	//Updown Orientation with white on top
    	for (String square: possible) {
    		//System.out.println(square);
			int row = (8-(square.charAt(1)-'0'))*100;
			int col = (square.charAt(0)-'a')*100;
			Color yellow = new Color(255,255,0);
			paint_canvas.gc.setBackground(yellow);
			paint_canvas.gc.setAlpha(100);
			paint_canvas.gc.fillRectangle(col,row,100,100);
		}
    }
    
    private void printWin() {
    	paint_canvas.gc.setFont(font);
    	paint_canvas.gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
    	paint_canvas.gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
    	if (player.getModel().isWhitesTurn()) {
    		paint_canvas.gc.drawText("White Won!", 275, 300);
    	} else {
    		paint_canvas.gc.drawText("Black Won!", 275, 300);
    	}
    }
}
