package ui.algconfiguration.view;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;

import ui.alginputdataprocess.view.MainFrame;

public class FunctionView extends JPanel {

	public JTextField txtConstantListSize = new JTextField(),
			txtRandomConstantStart = new JTextField();
	public JButton btnDelFunction = new JButton(
			"\u5220\u9664\u6240\u9009\u51FD\u6570"),
			btnDelAllFunction = new JButton(
					"\u5220\u9664\u5168\u90E8\u51FD\u6570");
	public JComboBox comboBox = new JComboBox(),
			JComboBoxOfSelectdFunctions = new JComboBox();
	int addTime = 0;
	
	ArrayList<String> items = new ArrayList<String>();
	
	MainFrame parent;

	public FunctionView(MainFrame parent) {
		this.parent = parent;
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBackground(Color.WHITE);
		setBounds(155, 115, 631, 447);
		setVisible(false);
		setLayout(null);

		JLabel lblNewLabel_24 = new JLabel(
				"\u586B\u5199\u6240\u9700\u7684\u51FD\u6570\u96C6\u548C\u968F\u673A\u6570");
		lblNewLabel_24.setFont(new Font("����", Font.PLAIN, 18));
		lblNewLabel_24.setBounds(20, 20, 216, 25);
		add(lblNewLabel_24);

		JLabel lblNewLabel_18 = new JLabel(
				"\u53EF\u4F9B\u9009\u62E9\u7684\u51FD\u6570");
		lblNewLabel_18.setFont(new Font("����", Font.PLAIN, 15));
		lblNewLabel_18.setBounds(20, 60, 120, 25);
		add(lblNewLabel_18);

		comboBox.setBounds(160, 62, 141, 25);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isaddItem = true;
				if (addTime == 0) {
					items.add(comboBox.getSelectedItem().toString());
					JComboBoxOfSelectdFunctions.addItem(comboBox
							.getSelectedItem().toString());
					JComboBoxOfSelectdFunctions.setSelectedItem(comboBox
							.getSelectedItem().toString());
				} else {
					for (int i = 0; i < items.size(); i++) {
						String item = comboBox.getSelectedItem().toString();
						if (item.equals(items.get(i))) {
							isaddItem = false;
						}
					}
					if (isaddItem == true) {
						items.add(comboBox.getSelectedItem().toString());
						JComboBoxOfSelectdFunctions.addItem(comboBox
								.getSelectedItem().toString());
						JComboBoxOfSelectdFunctions.setSelectedItem(comboBox
								.getSelectedItem().toString());
					}
				}

				addTime++;

			}
		});
		add(comboBox);

		JLabel lblNewLabel_19 = new JLabel(
				"\u5DF2\u9009\u62E9\u7684\u51FD\u6570");
		lblNewLabel_19.setFont(new Font("����", Font.PLAIN, 15));
		lblNewLabel_19.setBounds(20, 100, 100, 25);
		add(lblNewLabel_19);

		btnDelFunction.setBounds(323, 102, 104, 20);
		add(btnDelFunction);
		btnDelFunction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object selectItem = JComboBoxOfSelectdFunctions
						.getSelectedItem();
				JComboBoxOfSelectdFunctions.removeItem(selectItem);
			}
		});

		btnDelAllFunction.setBounds(473, 102, 110, 20);
		add(btnDelAllFunction);
		btnDelAllFunction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBoxOfSelectdFunctions.removeAllItems();
			}
		});
		JComboBoxOfSelectdFunctions.setBounds(160, 102, 141, 25);
		add(JComboBoxOfSelectdFunctions);

	}

}
