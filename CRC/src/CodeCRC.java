import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Classe pour le calcul et la vérification du code CRC.
 * @author Diallo 
 */
public class CodeCRC {
	private String message;
	private int[] polynome;

	public CodeCRC(String msg, int[] polyGen) {
		this.message = msg;
		this.polynome = polyGen;
	}

	/**
	 * Methode calcul du code CRC
	 * @param g L'objet Graphics pour l'affichage graphique des étapes de calcul.
	 */
	public void calculCRC(Graphics g) {
		String poly = getPolynom();
		g.setColor(Color.BLACK);
		int y = 20;

		g.drawString("Etapes de calcul du code CRC", 10, y);
		y += 30;
		g.drawString(balance(), 10, y);
		y += 15;
		g.setColor(Color.RED);
		g.drawString(poly, 10, y);
		g.setColor(Color.BLACK);
		y += 15;
		g.drawLine(10, y, 120, y);
		y += 15;
		g.drawString(emission(balance(), poly), 10, y);
		y += 20;
		if (removeZeros(emission(balance(), poly)).length() < polynome.length)
			return;
		else {
			String result = removeZeros(emission(balance(), poly));
			int size = removeZeros(emission(balance(), poly)).length();
			while (size >= polynome.length) {
				y += 15;
				g.drawString(result, 10, y);
				y += 15;
				g.setColor(Color.RED);
				g.drawString(poly, 10, y);
				g.setColor(Color.BLACK);
				y += 15;
				g.drawLine(10, y, 120, y);
				y += 15;
				g.drawString(emission(result, poly), 10, y);
				y += 25;

				size = removeZeros(emission(result, poly)).length();
				result = removeZeros(emission(result, poly));
			}
			g.setColor(Color.RED);
			g.drawString("Le CRC est : " + result, 10, y);
			g.drawString("Le mot à transmettre est : " + message + " " + result, 10, y + 15);
		}
	}

	/**
	 * Methode de verification du code CRC
	 * @param g L'objet Graphics pour l'affichage graphique des étapes de calcul.
	 */
	public void verifieCRC(Graphics g) {
		String poly = getPolynom();
		g.setColor(Color.BLACK);
		int y = 20;
		g.drawString("Etapes de vérification du code CRC", 10, y);
		y += 30;
		g.drawString(message, 10, y);
		y += 15;
		g.setColor(Color.RED);
		g.drawString(poly, 10, y);
		g.setColor(Color.BLACK);
		y += 15;
		g.drawLine(10, y, 120, y);
		y += 15;
		g.drawString(emission(message, poly), 10, y);
		y += 20;
		if (removeZeros(emission(balance(), poly)).length() < polynome.length) {
			g.setColor(Color.BLUE);
			g.drawString("Aucune erreur détectée. Le message transmis est correct.", 10, y);
			return;
		} else {
			String result = removeZeros(emission(message, poly));
			int taille = removeZeros(emission(message, poly)).length();
			while (taille >= polynome.length) {
				y += 15;
				g.drawString(result, 10, y);
				y += 15;
				g.setColor(Color.RED);
				g.drawString(poly, 10, y);
				g.setColor(Color.BLACK);
				y += 15;
				g.drawLine(10, y, 120, y);
				y += 15;
				g.drawString(emission(result, poly), 10, y);
				y += 25;

				taille = removeZeros(emission(result, poly)).length();
				result = removeZeros(emission(result, poly));
			}

			g.setColor(Color.BLUE);
			if (result.isEmpty() || result.equals("0")) {
				g.drawString("Aucune erreur détectée. Le message transmis est correct.", 10, y);
			} else {
				g.setColor(Color.RED);
				g.drawString("Le CRC est incorrect. Erreurs détectées.", 10, y);
			}
		}
	}

	/**
	 * Obtenir le polynôme sous forme de chaîne de caractères.
	 */
	private String getPolynom() {
		String res = "";
		for (int i = 0; i < polynome.length; i++) {
			res += polynome[i];
		}
		return res;
	}

	/**
	 * Permet de supprimer les zéros non significatifs d'une chaîne de caractères.
	 */
	private static String removeZeros(String result) {
		int i = 0;
		String newMessage = "";
		while (i < result.length() && result.charAt(i) == '0')
			i++;
		for (int j = i; j < result.length(); j++)
			newMessage += result.charAt(j);
		return newMessage;
	}

	/**
	 * Méthode pour équilibrer la trame en ajoutant des zéros à son début selon la
	 * taille du polynôme.
	 */
	public String balance() {
		return message + prefixZeros();
	}

	/**
	 * Permet d'effectuer l'émission en appliquant l'opération XOR entre la trame et
	 * le polynôme.
	 */
	private static String emission(String trame, String polynome) {
		String res = "";
		int i = 0;
		int j = 0;
		while ((i < trame.length()) && (j < polynome.length())) {
			char a = trame.charAt(i);
			char b = polynome.charAt(j);
			res += calculXOR(a, b);
			i++;
			j++;
		}

		if (i >= trame.length()) {
			for (int k = j; k < polynome.length(); k++) {
				res += polynome.charAt(k);
			}
		}
		if (j >= polynome.length()) {
			for (int k = i; k < trame.length(); k++) {
				res += trame.charAt(k);
			}
		}
		return res;
	}

	/*
	 * Effectue l'opération XOR (OU exclusif) entre deux caractères.
	 */
	private static char calculXOR(char x, char y) {
		if (x == y)
			return '0';
		else
			return '1';
	}

	/*
	 * Permet d'obtenir le nombre de zéros nécessaires pour compléter la trame en
	 * fonction de la taille du polynôme.
	 */
	public String prefixZeros() {
		int taille = polynome.length;
		switch (taille) {
		case 5:
			return "0000";
		case 13:
			return "000000000000";
		case 17:
			return "0000000000000000";
		case 33:
			return "00000000000000000000000000000000";
		default:
			return "";
		}
	}
}
