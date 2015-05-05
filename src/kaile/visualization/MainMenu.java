package kaile.visualization;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import kaile.sax.SAXFeatureSelect2;

import com.CDM2012.FeatureSelect;

public class MainMenu {
	private static String fileName = null;
	private static int choiceCase = 0;

	public static void CreateJFrame(String title) {
		// TODO Auto-generated method stub
		final JFrame frame = new JFrame(title);
		Container container = frame.getContentPane();
		final GridBagLayout gb = new GridBagLayout();
		container.setLayout(gb);
		container.setBackground(Color.CYAN);
		final JPanel panel = new JPanel();
		final JTextField jtfFilePath = new JTextField(20);
		jtfFilePath.setEditable(false);
		final JButton buttonFilePath = new JButton("SELECT FILE");
		buttonFilePath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fd = new JFileChooser();
				fd.showOpenDialog(null);
				File f = fd.getSelectedFile();
				if (f != null) {
					fileName = f.toString();
					jtfFilePath.setText(fileName);
				}
			}
		});
		final JLabel labelK = new JLabel("k:");
		final JTextField jtfK = new JTextField(5);
		final JLabel labelQl = new JLabel("ql:");
		final JTextField jtfQl = new JTextField(5);
		final JLabel labelW = new JLabel("w:");
		final JTextField jtfW = new JTextField(5);
		final JLabel labelA = new JLabel("a:");
		final JTextField jtfA = new JTextField(5);
		Choice choice = new Choice();
		choice.add("Novel Algorithm");
		choice.add("CDM2012 Algorithm");
		choice.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					if (evt.getItem().toString().equals("CDM2012 Algorithm")) {
						choiceCase = 1;
						jtfW.setText("");
						jtfA.setText("");
						jtfW.setEditable(false);
						jtfA.setEditable(false);
					}
					if (evt.getItem().toString().equals("Novel Algorithm")) {
						choiceCase = 0;
						jtfW.setEditable(true);
						jtfA.setEditable(true);
					}
				}
			}
		});
		JButton buttonGo = new JButton("GO");
		buttonGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fileName == null) {
					JOptionPane.showMessageDialog(null, "No Input File!");
				} else {
					if (jtfK.getText().trim().equals("")) {
						JOptionPane.showMessageDialog(null,
								"The Input Parameters are not complete!");
					} else {
						int k = Integer.parseInt(jtfK.getText());
						if (choiceCase == 0) {
							if (jtfQl.getText().trim().equals("")
									|| jtfW.getText().trim().equals("")
									|| jtfA.getText().trim().equals("")) {
								JOptionPane
										.showMessageDialog(null,
												"The Input Parameters are not complete!");
							} else {
								int ql = Integer.parseInt(jtfQl.getText());
								int w = Integer.parseInt(jtfW.getText());
								int a = Integer.parseInt(jtfA.getText());
								SAXFeatureSelect2.runNovelAlgorithm(fileName,
										ql, w, a, k);
							}
						} else {
							if (jtfQl.getText().trim().equals("")) {
								JOptionPane.showMessageDialog(null,
										"The Input Parameter is not complete!");
								JOptionPane.showMessageDialog(null,
										"Shapelet Selecting Finished!");
							} else {
								int ql = Integer.parseInt(jtfQl.getText());
								FeatureSelect.runCDM2012Algorithm(fileName, ql,
										k);
								JOptionPane.showMessageDialog(null,
										"Shapelet Selecting Finished!");
							}
						}
					}
				}
			}
		});
		panel.add(buttonFilePath);
		panel.add(jtfFilePath);
		panel.add(labelK);
		panel.add(jtfK);
		panel.add(choice);
		panel.add(labelQl);
		panel.add(jtfQl);
		panel.add(labelW);
		panel.add(jtfW);
		panel.add(labelA);
		panel.add(jtfA);
		panel.add(buttonGo);
		container.add(panel);
		frame.setSize(1200, 200);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		CreateJFrame("Unsupervised-Shapelet Selecting Algorithm");
	}

}
