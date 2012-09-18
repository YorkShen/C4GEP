package ui.conf.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JList;
import javax.swing.JScrollPane;

import domain.core.algconfiguration.Function;

import ui.conf.model.MyPrompt;
import ui.conf.model.MyTextField;
import ui.conf.model.MyTitle;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.CardLayout;

public class GenePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5180414798333601078L;
	private JTextField individualNumtextField;
	private JTextField normalGeneNumTextField;
	private JTextField homeoticGeneTextField;
	private JTextField homeoticGeneHeaderTextField;
	private JTextField normalGeneHeaderTextField;
	private final ButtonGroup buttonGroup = new ButtonGroup();


	/**
	 * Create the panel.
	 */
	public GenePanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{105, 181, 225, 0};
		gridBagLayout.rowHeights = new int[]{30, 30, 30, 30, 30, 84, 30, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		JLabel titleLabel = new MyTitle("\u8BBE\u7F6E\u4E2A\u4F53\u548C\u57FA\u56E0\u53C2\u6570");
		GridBagConstraints gbc_titleLabel = new GridBagConstraints();
		gbc_titleLabel.gridwidth = 2;
		gbc_titleLabel.fill = GridBagConstraints.VERTICAL;
		gbc_titleLabel.insets = new Insets(0, 0, 5, 0);
		gbc_titleLabel.gridx = 1;
		gbc_titleLabel.gridy = 0;
		add(titleLabel, gbc_titleLabel);

		JLabel individualNumLabel = new MyPrompt("\u79CD\u7FA4\u5927\u5C0F");
		GridBagConstraints gbc_individualNumLabel = new GridBagConstraints();
		gbc_individualNumLabel.anchor = GridBagConstraints.WEST;
		gbc_individualNumLabel.fill = GridBagConstraints.VERTICAL;
		gbc_individualNumLabel.insets = new Insets(0, 0, 5, 5);
		gbc_individualNumLabel.gridx = 1;
		gbc_individualNumLabel.gridy = 1;
		add(individualNumLabel, gbc_individualNumLabel);

		individualNumtextField = new MyTextField();
		GridBagConstraints gbc_individualNumtextField = new GridBagConstraints();
		gbc_individualNumtextField.anchor = GridBagConstraints.WEST;
		gbc_individualNumtextField.fill = GridBagConstraints.VERTICAL;
		gbc_individualNumtextField.insets = new Insets(0, 0, 5, 0);
		gbc_individualNumtextField.gridx = 2;
		gbc_individualNumtextField.gridy = 1;
		add(individualNumtextField, gbc_individualNumtextField);

		JLabel normalGeneNumLabel = new MyPrompt("\u666E\u901A\u57FA\u56E0\u6570\u91CF");
		GridBagConstraints gbc_normalGeneNumLabel = new GridBagConstraints();
		gbc_normalGeneNumLabel.anchor = GridBagConstraints.WEST;
		gbc_normalGeneNumLabel.fill = GridBagConstraints.VERTICAL;
		gbc_normalGeneNumLabel.insets = new Insets(0, 0, 5, 5);
		gbc_normalGeneNumLabel.gridx = 1;
		gbc_normalGeneNumLabel.gridy = 2;
		add(normalGeneNumLabel, gbc_normalGeneNumLabel);

		normalGeneNumTextField = new MyTextField();
		GridBagConstraints gbc_normalGeneNumTextField = new GridBagConstraints();
		gbc_normalGeneNumTextField.anchor = GridBagConstraints.WEST;
		gbc_normalGeneNumTextField.fill = GridBagConstraints.VERTICAL;
		gbc_normalGeneNumTextField.insets = new Insets(0, 0, 5, 0);
		gbc_normalGeneNumTextField.gridx = 2;
		gbc_normalGeneNumTextField.gridy = 2;
		add(normalGeneNumTextField, gbc_normalGeneNumTextField);

		JLabel normalGeneHeaderLabel = new MyPrompt("\u666E\u901A\u57FA\u56E0\u5934\u957F");
		GridBagConstraints gbc_normalGeneHeaderLabel = new GridBagConstraints();
		gbc_normalGeneHeaderLabel.anchor = GridBagConstraints.WEST;
		gbc_normalGeneHeaderLabel.fill = GridBagConstraints.VERTICAL;
		gbc_normalGeneHeaderLabel.insets = new Insets(0, 0, 5, 5);
		gbc_normalGeneHeaderLabel.gridx = 1;
		gbc_normalGeneHeaderLabel.gridy = 3;
		add(normalGeneHeaderLabel, gbc_normalGeneHeaderLabel);

		normalGeneHeaderTextField = new MyTextField();
		GridBagConstraints gbc_normalGeneHeaderTextField = new GridBagConstraints();
		gbc_normalGeneHeaderTextField.anchor = GridBagConstraints.WEST;
		gbc_normalGeneHeaderTextField.fill = GridBagConstraints.VERTICAL;
		gbc_normalGeneHeaderTextField.insets = new Insets(0, 0, 5, 0);
		gbc_normalGeneHeaderTextField.gridx = 2;
		gbc_normalGeneHeaderTextField.gridy = 3;
		add(normalGeneHeaderTextField, gbc_normalGeneHeaderTextField);

		JLabel connectionTypeLabel = new MyPrompt("\u666E\u901A\u57FA\u56E0\u8FDE\u63A5\u65B9\u5F0F");
		GridBagConstraints gbc_connectionTypeLabel = new GridBagConstraints();
		gbc_connectionTypeLabel.anchor = GridBagConstraints.WEST;
		gbc_connectionTypeLabel.fill = GridBagConstraints.VERTICAL;
		gbc_connectionTypeLabel.insets = new Insets(0, 0, 5, 5);
		gbc_connectionTypeLabel.gridx = 1;
		gbc_connectionTypeLabel.gridy = 4;
		add(connectionTypeLabel, gbc_connectionTypeLabel);

		JPanel connectionTypePanel = new JPanel();
		GridBagConstraints gbc_connectionTypePanel = new GridBagConstraints();
		gbc_connectionTypePanel.insets = new Insets(0, 0, 5, 0);
		gbc_connectionTypePanel.fill = GridBagConstraints.BOTH;
		gbc_connectionTypePanel.gridx = 2;
		gbc_connectionTypePanel.gridy = 4;
		add(connectionTypePanel, gbc_connectionTypePanel);

		JRadioButton homeoticGeneRadioButton = new JRadioButton("\u4F7F\u7528\u540C\u6E90\u57FA\u56E0");
		connectionTypePanel.add(homeoticGeneRadioButton);
		buttonGroup.add(homeoticGeneRadioButton);

		JRadioButton connectionFuncRadioButton = new JRadioButton(
				"\u4F7F\u7528\u8FDE\u63A5\u51FD\u6570");
		connectionTypePanel.add(connectionFuncRadioButton);
		buttonGroup.add(connectionFuncRadioButton);

		JPanel connectionPanel = new JPanel();
		GridBagConstraints gbc_connectionPanel = new GridBagConstraints();
		gbc_connectionPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_connectionPanel.gridwidth = 2;
		gbc_connectionPanel.insets = new Insets(0, 0, 5, 0);
		gbc_connectionPanel.gridx = 1;
		gbc_connectionPanel.gridy = 5;
		add(connectionPanel, gbc_connectionPanel);
		connectionPanel.setLayout(new CardLayout(0, 0));

		JPanel homecticPanel = new JPanel();
		connectionPanel.add(homecticPanel, "name_99163667654329");
		GridBagLayout gbl_homecticPanel = new GridBagLayout();
		gbl_homecticPanel.columnWidths = new int[]{186, 236, 0};
		gbl_homecticPanel.rowHeights = new int[]{21, 0};
		gbl_homecticPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_homecticPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		homecticPanel.setLayout(gbl_homecticPanel);

		JLabel connectionFuncLabel = new MyPrompt("\u9009\u62E9\u8FDE\u63A5\u51FD\u6570");
		GridBagConstraints gbc_connectionFuncLabel = new GridBagConstraints();
		gbc_connectionFuncLabel.anchor = GridBagConstraints.WEST;
		gbc_connectionFuncLabel.insets = new Insets(0, 0, 0, 5);
		gbc_connectionFuncLabel.gridx = 0;
		gbc_connectionFuncLabel.gridy = 0;
		homecticPanel.add(connectionFuncLabel, gbc_connectionFuncLabel);

		JComboBox<Function> connectionComboBox = new JComboBox<Function>();
		GridBagConstraints gbc_connectionComboBox = new GridBagConstraints();
		gbc_connectionComboBox.anchor = GridBagConstraints.NORTHWEST;
		gbc_connectionComboBox.gridx = 1;
		gbc_connectionComboBox.gridy = 0;
		homecticPanel.add(connectionComboBox, gbc_connectionComboBox);
		connectionComboBox.setMaximumSize(new Dimension(110, 21));

		JPanel connectionFuncPanel = new JPanel();
		connectionPanel.add(connectionFuncPanel, "name_99163681758653");
		GridBagLayout gbl_connectionFuncPanel = new GridBagLayout();
		gbl_connectionFuncPanel.columnWidths = new int[]{185, 78, 0};
		gbl_connectionFuncPanel.rowHeights = new int[]{21, 0, 0};
		gbl_connectionFuncPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_connectionFuncPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		connectionFuncPanel.setLayout(gbl_connectionFuncPanel);

		JLabel homeoticGeneNumLabel = new MyPrompt("\u540C\u6E90\u57FA\u56E0\u6570\u91CF");
		GridBagConstraints gbc_homeoticGeneNumLabel = new GridBagConstraints();
		gbc_homeoticGeneNumLabel.anchor = GridBagConstraints.WEST;
		gbc_homeoticGeneNumLabel.insets = new Insets(0, 0, 5, 5);
		gbc_homeoticGeneNumLabel.gridx = 0;
		gbc_homeoticGeneNumLabel.gridy = 0;
		connectionFuncPanel.add(homeoticGeneNumLabel, gbc_homeoticGeneNumLabel);

		homeoticGeneTextField = new MyTextField();
		GridBagConstraints gbc_homeoticGeneTextField = new GridBagConstraints();
		gbc_homeoticGeneTextField.anchor = GridBagConstraints.NORTHWEST;
		gbc_homeoticGeneTextField.insets = new Insets(0, 0, 5, 0);
		gbc_homeoticGeneTextField.gridx = 1;
		gbc_homeoticGeneTextField.gridy = 0;
		connectionFuncPanel.add(homeoticGeneTextField, gbc_homeoticGeneTextField);

		JLabel homeoticGeneHeaderLabel = new MyPrompt("\u540C\u6E90\u57FA\u56E0\u5934\u957F");
		GridBagConstraints gbc_homeoticGeneHeaderLabel = new GridBagConstraints();
		gbc_homeoticGeneHeaderLabel.anchor = GridBagConstraints.WEST;
		gbc_homeoticGeneHeaderLabel.insets = new Insets(0, 0, 0, 5);
		gbc_homeoticGeneHeaderLabel.gridx = 0;
		gbc_homeoticGeneHeaderLabel.gridy = 1;
		connectionFuncPanel.add(homeoticGeneHeaderLabel, gbc_homeoticGeneHeaderLabel);

		homeoticGeneHeaderTextField = new MyTextField();
		GridBagConstraints gbc_homeoticGeneHeaderTextField = new GridBagConstraints();
		gbc_homeoticGeneHeaderTextField.anchor = GridBagConstraints.NORTHWEST;
		gbc_homeoticGeneHeaderTextField.gridx = 1;
		gbc_homeoticGeneHeaderTextField.gridy = 1;
		connectionFuncPanel.add(homeoticGeneHeaderTextField, gbc_homeoticGeneHeaderTextField);

		JLabel usedFuncLabel = new MyPrompt("\u6240\u7528\u7684\u51FD\u6570");
		GridBagConstraints gbc_usedFuncLabel = new GridBagConstraints();
		gbc_usedFuncLabel.anchor = GridBagConstraints.WEST;
		gbc_usedFuncLabel.fill = GridBagConstraints.VERTICAL;
		gbc_usedFuncLabel.insets = new Insets(0, 0, 0, 5);
		gbc_usedFuncLabel.gridx = 1;
		gbc_usedFuncLabel.gridy = 6;
		add(usedFuncLabel, gbc_usedFuncLabel);

		JScrollPane functionScrollPane = new JScrollPane();
		GridBagConstraints gbc_functionScrollPane = new GridBagConstraints();
		gbc_functionScrollPane.anchor = GridBagConstraints.WEST;
		gbc_functionScrollPane.gridx = 2;
		gbc_functionScrollPane.gridy = 6;
		add(functionScrollPane, gbc_functionScrollPane);

		JList<Function> functionList = new JList<Function>();
		functionList.setVisibleRowCount(4);
		functionScrollPane.setViewportView(functionList);
		functionScrollPane.setMaximumSize(new Dimension(126, 60));

	}

}
