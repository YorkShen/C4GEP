package ui.conf.view;

import javax.swing.JTabbedPane;

public class ProjectTabPane extends JTabbedPane{
	
	public ConfPanel confPanel;
	
	
	public ProjectTabPane(int position,final ConfPanel confPanel) {
		super(position);
		this.confPanel = confPanel;
		
		
		
	}
}
