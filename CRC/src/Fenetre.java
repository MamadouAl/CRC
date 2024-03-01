import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Fenetre extends JFrame {
	private JTextField message;
	private JComboBox<String> polynomeGen;
	private JButton boutonValider;

	public Fenetre() {
		setTitle("TP CRC");
		setSize(400, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		initComposant();
	}

	/**
	 * Initialisation des composants
	 */
	private void initComposant() {
		JPanel panneauBoutons = new JPanel();
		JPanel panneauSaisi = new JPanel();
		JPanel radio = new JPanel();

		panneauBoutons.setLayout(new FlowLayout(FlowLayout.CENTER));
		panneauSaisi.setLayout(new GridLayout(3, 2));
		setLayout(new BorderLayout());

		message = new JTextField();
		polynomeGen = new JComboBox<>(new String[] { "CRC-4", "CRC-12", "CRC-16", "CRC-CCITT", "CRC-32" });
		boutonValider = new JButton("Valider");

		panneauSaisi.add(new JLabel("Entrez une trame:"));
		panneauSaisi.add(message);
		panneauSaisi.add(new JLabel("Polynôme générateur:"));
		panneauSaisi.add(polynomeGen);

		JRadioButton calculer = new JRadioButton("Calculer");
		JRadioButton verifier = new JRadioButton("Verifier");
		ButtonGroup groupe = new ButtonGroup();

		groupe.add(calculer);
		groupe.add(verifier);
		radio.add(calculer);
		radio.add(verifier);
		add(radio, BorderLayout.CENTER);

		panneauBoutons.add(boutonValider);
		panneauBoutons.add(Box.createHorizontalGlue());
		add(panneauBoutons, BorderLayout.SOUTH);
		add(panneauSaisi, BorderLayout.NORTH);

		// Configuration de l'action du bouton une fois clické
		boutonValider.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String trame = message.getText();
				int polynome = polynomeGen.getSelectedIndex() + 1;
				int[] poly;

				switch (polynome) {
				case 1:
					poly = new int[] { 1, 0, 1, 1, 0 };
					break;
				case 2:
					poly = new int[] { 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 };
					break;
				case 3:
					poly = new int[] { 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1 };
					break;
				case 4:
					poly = new int[] { 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 };
					break;
				default:
					poly = new int[] { 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 0,
							1, 1, 0, 0, 1, 1 };
					break;
				}
				if (trame.isEmpty()) {
					JOptionPane.showMessageDialog(Fenetre.this, "Veuillez entrer une chaine binaire", "Erreur",
							JOptionPane.ERROR_MESSAGE);
				} else {
					JPanel panneau2 = new JPanel();
					JPanel panneau = new JPanel() {
						@Override
						protected void paintComponent(Graphics g) {
							super.paintComponent(g);
							setBackground(Color.WHITE);
							Font font = new Font("SansSerif", Font.PLAIN, 14);
							g.setFont(font);
							if (calculer.isSelected()) {
								// Je calcule
								CodeCRC calculerCRC = new CodeCRC(trame, poly);
								calculerCRC.calculCRC(g);
							} else if (verifier.isSelected()) {
								// Je verifie
								CodeCRC calculerCRC = new CodeCRC(trame, poly);
								calculerCRC.verifieCRC(g);

							} else {
								JOptionPane.showMessageDialog(Fenetre.this, "Veuillez entrer une option", "Erreur",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					};
					JScrollPane scrollPane = new JScrollPane(panneau);
					scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

					// Création de la fenêtre affichant les étapes
					JFrame fenetre = new JFrame("CRC : Les Etapes ");
					fenetre.setSize(450, 600);
					fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					fenetre.setLayout(new BorderLayout());
					fenetre.add(scrollPane, BorderLayout.CENTER);
					fenetre.add(panneau2, BorderLayout.SOUTH);
					fenetre.setLocationRelativeTo(null);
					fenetre.setVisible(true);
				}
			}
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(Fenetre::new);
	}
}
