package UI;
import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import ServerClient.Main;

public class MainMenu {
	
	private Display display;
	private Shell shell;
	
	// config variables, set by button presses / menu selects
	private int mode; // 1=local, 2=server-create, 3=server-join
	private String game_file;
	private String ai;
	
	private final int width = 400, height = 400;
	
	public MainMenu() {
		this.display = new Display();
	}
	
	public void run() {
		// layouts and layout data
        GridLayout comp_layout = new GridLayout();
        comp_layout.numColumns = 3;
        //comp_layout.makeColumnsEqualWidth = true;
		
		GridData fill_cells = new GridData();
		fill_cells.horizontalAlignment = GridData.FILL;
		fill_cells.horizontalSpan = 3;
		fill_cells.grabExcessHorizontalSpace = true;
		fill_cells.widthHint = width;
		
		GridData full_buttons = new GridData();
		full_buttons.horizontalAlignment = GridData.FILL;
		full_buttons.grabExcessHorizontalSpace = true;
		
		// shell setup
		shell = new Shell(display);
		Rectangle screen_size = display.getClientArea();
        shell.setBounds(screen_size.width/2 - width/2, screen_size.height/2 - height/2, width, height);
        shell.setLayout(comp_layout);
        shell.setText("Chess");
       
        // title
        Label title = new Label(shell, SWT.NONE);
        title.setFont(new Font(display, "Comic Sans", 56, SWT.BOLD));
        title.setText("Chess");
        title.setLayoutData(fill_cells);
        title.setAlignment(SWT.CENTER);
        
        // game type submenu
        Label mode_label = new Label(shell, SWT.NONE);
        mode_label.setText("Select game mode:");
        
        Button local_button = new Button(shell, SWT.NONE);
        local_button.setText("Local");
        local_button.setLayoutData(full_buttons);
        
        Button network_button = new Button(shell, SWT.NONE);
        network_button.setText("Networked");
        network_button.setLayoutData(full_buttons);
        
        // ai submenu
        Label ai_menu = new Label(shell, SWT.NONE);
        ai_menu.setText("Select AI:");
        ai_menu.setEnabled(false);
        
        Combo ai_select = new Combo(shell, SWT.DROP_DOWN);
        ai_select.setItems(new String[] {"None", "Noob", "Easy", "Hard"});
        ai_select.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        ai_select.setEnabled(false);
        
        local_button.addListener(SWT.Selection, new Listener() {
        	public void handleEvent(Event e) {
        		ai_menu.setEnabled(true);
        		ai_select.setEnabled(true);
        		network_button.setEnabled(false);
        		mode = 1;
        	}
        });
        
        // server stuff submenu
        Label server_label = new Label(shell, SWT.NONE);
        server_label.setText("Create server or join existing?");
        server_label.setEnabled(false);
        
        Button create_game = new Button(shell, SWT.NONE);
        create_game.setText("Create");
        create_game.setLayoutData(full_buttons);
        create_game.setEnabled(false);
                
        Button join_game = new Button(shell, SWT.NONE);
        join_game.setText("Join");
        join_game.setLayoutData(full_buttons);
        join_game.setEnabled(false);
        
        network_button.addListener(SWT.Selection, new Listener() {
        	public void handleEvent(Event e) {
        		server_label.setEnabled(true);
        		create_game.setEnabled(true);
        		join_game.setEnabled(true);
        		local_button.setEnabled(false);
        	}
        });
        
        // load game submenu
        Label load_label = new Label(shell, SWT.NONE);
        load_label.setText("Load game or start new?");
        load_label.setEnabled(false);
        
        Button load_game = new Button(shell, SWT.NONE);
        load_game.setText("Load");
        load_game.setLayoutData(full_buttons);
        load_game.setEnabled(false);
        
        Button new_game = new Button(shell, SWT.NONE);
        new_game.setText("New");
        new_game.setLayoutData(full_buttons);
        new_game.setEnabled(false);
        
        ai_select.addListener(SWT.Selection, new Listener() {
        	public void handleEvent(Event e) {
        		load_label.setEnabled(true);
        		load_game.setEnabled(true);
        		new_game.setEnabled(true);
        		ai = ai_select.getText();
        	}
        });
        
        create_game.addListener(SWT.Selection, new Listener() {
        	public void handleEvent(Event e) {
        		load_label.setEnabled(true);
        		load_game.setEnabled(true);
        		new_game.setEnabled(true);
        		join_game.setEnabled(false);
        		mode = 2;
        	}
        });
        
        // game file selector
        Label file_label = new Label(shell, SWT.NONE);
        file_label.setText("Select game to load:");
        file_label.setEnabled(false);
        
	    Combo file_select = new Combo(shell, SWT.DROP_DOWN);	    		
	    file_select.setItems(getGameFileNames()); // read saved games from folder
	    file_select.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
	    file_select.setEnabled(false);
	    
	    load_game.addListener(SWT.Selection, new Listener() {
        	public void handleEvent(Event e) {
        		file_label.setEnabled(true);
        		file_select.setEnabled(true);
        		new_game.setEnabled(false);
        	}
        });
	    
	    // start button
	    Button start_button = new Button(shell, SWT.NONE);
	    start_button.setText("Start");
	    start_button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 3, 1));
	    start_button.setEnabled(false);
	    
	    join_game.addListener(SWT.Selection, new Listener() {
        	public void handleEvent(Event e) {
        		start_button.setEnabled(true);
        		create_game.setEnabled(false);
        		mode = 3;
        	}
        });
	    
	    new_game.addListener(SWT.Selection, new Listener() {
        	public void handleEvent(Event e) {
        		load_game.setEnabled(false);
        		start_button.setEnabled(true);
        	}
        });
	    
	    file_select.addListener(SWT.Selection, new Listener() {
        	public void handleEvent(Event e) {
        		start_button.setEnabled(true);
        		game_file = file_select.getText();
        	}
        });
	    
	    start_button.addListener(SWT.Selection, new Listener() {
        	public void handleEvent(Event e) {
        		Main.setConfig(game_file, ai, mode);
        		shell.dispose();
        	}
        });
        
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
	}
	
	/*
	public static void main(String[] args) {
		MainMenu menu = new MainMenu();
		menu.run();
	}
	*/
	
	private String[] getGameFileNames() {
		File game_file_dir = new File("games");
		File[] list_of_files = game_file_dir.listFiles();
		
		String[] file_names = new String[list_of_files.length];
		for (int i = 0; i < list_of_files.length; i++) {
			file_names[i] = list_of_files[i].getName();
		}
		
		return file_names;
	}
}
