package edu.diallo;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Fenetre extends JFrame {

	private JButton creerButton;
	private JButton deleteButton;
	private JButton ajoutLiaisonButton;
	private JButton removeLiaisonButton;
	private JButton cheminButton;
	private JButton tableRoutageButton;
	private JTextField cout;
	private JTextArea graphe;

	public Fenetre() {
		super("TP - Routage  - Diallo Mamadou Aliou");
		initComponents();
		setSize(600, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Initialisation des composants de la fenêtre
	 */
	public void initComponents() {
		setLayout(new BorderLayout());

		JPanel ajout = new JPanel();
		JPanel suppr = new JPanel();
		JPanel affichage = new JPanel();

		JLabel enTete = new JLabel("Gestion des commutateurs et des liaisons");
		enTete.setHorizontalAlignment(JLabel.CENTER);
		add(enTete, BorderLayout.NORTH);

		// Buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(0, 3)); // 0 lignes (pour s'adapter à la hauteur), 3 colonnes

		creerButton = new JButton("Créer un site");
		creerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Routage.creerCommutateur();
				updateGraphArea();
			}
		});
		buttonPanel.add(creerButton);
		buttonPanel.add(Box.createHorizontalStrut(10));

		ajoutLiaisonButton = new JButton("Ajouter une liaison");
		ajoutLiaisonButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Routage.ajouterLiaison();
				updateGraphArea();
			}
		});
		buttonPanel.add(ajoutLiaisonButton);
		ajout.add(buttonPanel);

		deleteButton = new JButton("Supprimer un site");
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Routage.supprimerCommutateur();
				updateGraphArea();
			}
		});
		buttonPanel.add(deleteButton);
		buttonPanel.add(Box.createHorizontalStrut(10));

		removeLiaisonButton = new JButton("Supprimer liaison");
		removeLiaisonButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Routage.supprimerLiaison();
				updateGraphArea();
			}
		});
		buttonPanel.add(removeLiaisonButton);
		suppr.add(buttonPanel);

		cheminButton = new JButton("Trouver chemin le plus court");
		cheminButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Routage.trouverCheminLePlusCourt();
			}
		});
		buttonPanel.add(cheminButton);
		buttonPanel.add(Box.createHorizontalStrut(10));

		tableRoutageButton = new JButton("Afficher tables de routage");
		tableRoutageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Routage.afficherTablesDeRoutages();
			}
		});
		buttonPanel.add(tableRoutageButton);
		affichage.add(buttonPanel, BorderLayout.SOUTH);

		add(ajout, BorderLayout.WEST);
		add(suppr, BorderLayout.CENTER);
		add(affichage, BorderLayout.CENTER);

		// Affiche Cout de la liaison
		JPanel costPanel = new JPanel();
		costPanel.add(new JLabel("Coût de la liaison :"));
		cout = new JTextField(5);
		costPanel.add(cout);
		add(costPanel, BorderLayout.SOUTH);

		// Affichage du graphe
		graphe = new JTextArea();
		graphe.setRows(10); // augmente la hauteur de la zone de texte
		graphe.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(graphe);
		add(scrollPane, BorderLayout.SOUTH);
	}

	/**
	 * Met à jour la zone de texte affichant le graphe
	 */
	private void updateGraphArea() {
		StringBuilder graphText = new StringBuilder();
		graphText.append("Votre Graphe :\n");
		for (Map.Entry<String, Map<String, Integer>> entry : Routage.getGraph().entrySet()) {
			graphText.append(entry.getKey()).append(" : ");
			for (Map.Entry<String, Integer> neighbor : entry.getValue().entrySet()) {
				graphText.append(neighbor.getKey()).append("(").append(neighbor.getValue()).append(") ");
			}
			graphText.append("\n");
		}
		graphe.setText(graphText.toString());
	}
}
