package tpHamming;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Hamming extends JFrame {
	private JTextField binaire;
	private JButton executer;
	private JRadioButton radio1;
	private JRadioButton radio2;
	private ButtonGroup buttonGroup;
	private JTextArea resultatTextArea;

	public Hamming() {
		super("Code de Hamming");
		init();
		setSize(450, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void init() {
		resultatTextArea = new JTextArea(10, 30);
		resultatTextArea.setEditable(false);
		Font police = new Font(resultatTextArea.getFont().getName(), Font.BOLD, 13);
		resultatTextArea.setFont(police);

		JPanel panneauSaisie = new JPanel();
		JPanel panneauRadio = new JPanel();
		JPanel panneauBouton = new JPanel();

		panneauSaisie.setLayout(new BoxLayout(panneauSaisie, BoxLayout.LINE_AXIS));
		panneauBouton.setLayout(new FlowLayout(FlowLayout.CENTER));

		binaire = new JTextField(20);
		JLabel label = new JLabel("Saisir le code binaire ");
		panneauSaisie.add(label);
		panneauSaisie.add(binaire);

		executer = new JButton("Exécuter");
		radio1 = new JRadioButton("Calculer le code");
		radio2 = new JRadioButton("Vérifier le code");
		buttonGroup = new ButtonGroup();
		buttonGroup.add(radio1);
		buttonGroup.add(radio2);

		panneauRadio.add(radio1);
		panneauRadio.add(radio2);
		panneauRadio.add(resultatTextArea);
		panneauBouton.add(executer);

		add(panneauSaisie, BorderLayout.NORTH);
		add(panneauRadio, BorderLayout.CENTER);
		add(panneauBouton, BorderLayout.SOUTH);

		executer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				executer();
			}
		});
	}

	private void executer() {
		String codeBinaire = binaire.getText();

		if (radio1.isSelected()) {
			if (codeBinaire == "") {
				JOptionPane.showInternalMessageDialog(this, "Votre code est incorrect !" + codeBinaire);
			}
			calculHamming(codeBinaire);
		} else if (radio2.isSelected()) {
			verifieHamming(codeBinaire);
		} else {
			JOptionPane.showMessageDialog(this, "Veuillez sélectionner une option.");
		}
	}

	/**
	 * Calcule le code de Hamming
	 */
	private void calculHamming(String mot) throws IllegalArgumentException {
		int r = 0;
		int n = mot.length();

		while ((n + r + 1) > Math.pow(2, r)) {
			r++;
		}
		int j = 0;
		
		//Vérification de la longueur de la chaîne binaire
		if (n != ((int) Math.pow(2, r) - 1) - r) {
			JOptionPane.showMessageDialog(Hamming.this,
					"Longueur invalide pour la chaîne binaire. \n"
							+ "La longueur doit être de 4 pour un code de Hamming 4/7 "
							+ "ou de 11 pour un code de Hamming 11/15 ou 26 pour un code Hamming 26/31.",
					"Erreur", JOptionPane.ERROR_MESSAGE);
			throw new IllegalArgumentException(
					"Longueur invalide pour la chaîne binaire.La longueur doit être de 4 pour un code de Hamming 4/7 ou de 11 pour un code de Hamming 11/15 ou 26 pour un code Hamming 26/31.");
		}
		int[] hammingCode = new int[r + n]; 

		// Remplissage les bits de parité
		for (int i = 1; i <= hammingCode.length; i++) {
			if (i == (int) Math.pow(2, j)) {
				j++;
			} else {
				hammingCode[i - 1] = Character.getNumericValue(mot.charAt(mot.length() - 1));
				mot = mot.substring(0, mot.length() - 1);
			}
		}
		//
		for (int parite = 0; parite < hammingCode.length; parite++) {
			int ph = (int) Math.pow(2, parite);
			if (ph == (parite + 1)) {
				int startIndex = ph - 1;
				int i = startIndex;
				int[] toXor = new int[hammingCode.length];

				while (i < hammingCode.length) {
					System.arraycopy(hammingCode, i, toXor, i, ph);
					i += 2 * ph;
				}

				for (int z = 1; z < toXor.length; z++) {
					hammingCode[startIndex] ^= toXor[z];
				}
			}
		}
		String res = "";
		for (int bit : hammingCode) {
			res += bit;
		}
		resultatTextArea.setText("Le Code Hamming est : " + new StringBuilder(res).reverse().toString());
	}

	/**
	 * Vérifie si le code de Hamming est correct
	 */
	private void verifieHamming(String trame) {
		String hammingCodeStr = new StringBuilder(trame).reverse().toString();
		int[] hammingCode = new int[hammingCodeStr.length()];
		for (int i = 0; i < hammingCodeStr.length(); i++) {
			hammingCode[i] = Integer.parseInt(String.valueOf(hammingCodeStr.charAt(i)));
		}

		int n = hammingCode.length;
		int r = 0;

		while ((n + r + 1) > Math.pow(2, r)) {
			r++;
		}

		int[] errorBits = new int[r];
		StringBuilder sb = new StringBuilder("\n");
		String hming = new StringBuilder(hammingCodeStr).reverse().toString();
		sb.append("Le message de Hamming = " + hming + "\n\n");

		for (int parite = 0; parite < r - 1; parite++) {
			int expo = (int) Math.pow(2, parite);
			int sum = 0;
			sb.append("C" + parite + " vaut ");

			for (int j = expo - 1; j < n; j += 2 * expo) {
				for (int k = j; k < j + expo && k < n; k++) {
					sb.append(hammingCode[k] + " + ");
					sum += hammingCode[k];
				}
			}

			sb.delete(sb.length() - 2, sb.length()); // Supprimer le dernier "+"
			sb.append(" = " + (sum % 2) + "\n");
			errorBits[parite] = sum % 2;
		}
		int errorPosition = calculPosition(errorBits);

		if (errorPosition == 0) {
			sb.append("\n -> Il n'y a pas d'erreurs :)");
		} else {
			sb.append("\nPosition possible de l'erreur : " + errorPosition);
		}
		resultatTextArea.setText(sb.toString());
	}

	/**
	 * Calcule la position de l'erreur
	 */
	private static int calculPosition(int[] errorBits) {
		int position = 0;
		for (int i = 0; i < errorBits.length; i++) {
			position += errorBits[i] * Math.pow(2, i);
		}
		return position;
	}

	public static void main(String... args) {
		SwingUtilities.invokeLater(Hamming::new);
	}
}
