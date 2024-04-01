package edu.diallo;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Routage {
	private static Map<String, Map<String, Integer>> graphe = new HashMap<>();
	private static JTextArea textArea;

	/**
	 * Permet de créer un commutateur
	 */
	public static void creerCommutateur() {
		String nomCommutateur = JOptionPane.showInputDialog("Entrez le nom du commutateur:");
		if (nomCommutateur != null && !nomCommutateur.isEmpty()) {
			if (graphe.containsKey(nomCommutateur)) {
				afficherMessage("Un commutateur avec le même nom existe déjà.");
			} else {
				graphe.put(nomCommutateur, new HashMap<>());
				afficherMessage("Commutateur créé : " + nomCommutateur);
			}
		}
	}

	/**
	 * Permet d'ajouter une liaison entre deux commutateurs
	 */
	public static void ajouterLiaison() {
		if (graphe.size() < 2) {
			afficherMessage("Au moins deux commutateurs doivent être créés pour ajouter une liaison.");
			return;
		}
		String[] commutateurs = graphe.keySet().toArray(new String[0]);
		String commutateurSource = (String) JOptionPane.showInputDialog(null, "Choisissez le commutateur source:",
				"Ajouter liaison", JOptionPane.QUESTION_MESSAGE, null, commutateurs, commutateurs[0]);
		if (commutateurSource == null) {
			return; // L'utilisateur a annulé
		}
		String commutateurDestination = (String) JOptionPane.showInputDialog(null,
				"Choisissez le commutateur destination:", "Ajouter liaison", JOptionPane.QUESTION_MESSAGE, null,
				commutateurs, commutateurs[0]);
		if (commutateurDestination == null) {
			return; // L'utilisateur a annulé
		}
		if (commutateurSource.equals(commutateurDestination)) {
			afficherMessage("Les commutateurs source et destination ne peuvent pas être les mêmes.");
			return;
		}
		if (graphe.get(commutateurSource).containsKey(commutateurDestination)) {
			afficherMessage("Une liaison entre ces deux commutateurs existe déjà.");
			return;
		}
		String costString = JOptionPane.showInputDialog("Entrez le coût de la liaison:");
		if (costString == null) {
			return; // L'utilisateur a annulé
		}
		int cost;
		try {
			cost = Integer.parseInt(costString);
		} catch (NumberFormatException e) {
			afficherMessage("Veuillez entrer un nombre entier pour le coût.");
			return;
		}
		if (cost <= 0) {
			afficherMessage("Le coût de la liaison doit être un nombre positif.");
			return;
		}
		graphe.get(commutateurSource).put(commutateurDestination, cost);
		graphe.get(commutateurDestination).put(commutateurSource, cost);
		afficherMessage("Liaison ajoutée entre " + commutateurSource + " et " + commutateurDestination
				+ " avec un coût de " + cost);
	}

	/**
	 * Permet de supprimer un commutateur
	 */
	public static void supprimerCommutateur() {
		if (!graphe.isEmpty()) {
			String[] commutateurs = graphe.keySet().toArray(new String[0]);
			String nomCommutateur = (String) JOptionPane.showInputDialog(null, "Choisissez le commutateur à supprimer:",
					"Supprimer commutateur", JOptionPane.QUESTION_MESSAGE, null, commutateurs, commutateurs[0]);
			if (nomCommutateur != null) {
				graphe.remove(nomCommutateur);
				for (Map<String, Integer> voisins : graphe.values()) {
					voisins.remove(nomCommutateur);
				}
				afficherMessage("Commutateur supprimé : " + nomCommutateur);
			}
		} else {
			afficherMessage("Aucun commutateur à supprimer.");
		}
	}

	/**
	 * Permet de supprimer une liaison entre deux commutateurs
	 */
	public static void supprimerLiaison() {
		if (graphe.size() < 2) {
			afficherMessage("Au moins deux commutateurs doivent être créés pour supprimer une liaison.");
			return;
		}
		String[] commutateurs = graphe.keySet().toArray(new String[0]);
		String commutateurSource = (String) JOptionPane.showInputDialog(null, "Choisissez le commutateur source:",
				"Supprimer liaison", JOptionPane.QUESTION_MESSAGE, null, commutateurs, commutateurs[0]);
		if (commutateurSource == null) {
			return; // L'utilisateur a annulé
		}
		String commutateurDestination = (String) JOptionPane.showInputDialog(null,
				"Choisissez le commutateur destination:", "Supprimer liaison", JOptionPane.QUESTION_MESSAGE, null,
				commutateurs, commutateurs[0]);
		if (commutateurDestination == null) {
			return; // L'utilisateur a annulé
		}
		if (!graphe.get(commutateurSource).containsKey(commutateurDestination)) {
			afficherMessage("Il n'y a pas de liaison entre " + commutateurSource + " et " + commutateurDestination);
			return;
		}
		graphe.get(commutateurSource).remove(commutateurDestination);
		graphe.get(commutateurDestination).remove(commutateurSource);
		afficherMessage("Liaison supprimée entre " + commutateurSource + " et " + commutateurDestination);
	}

	/**
	 * Trouve le chemin le plus court entre deux commutateurs
	 */
	public static void trouverCheminLePlusCourt() {
		if (graphe.size() < 2) {
			afficherMessage("Au moins deux commutateurs doivent être créés pour trouver un chemin.");
			return;
		}
		String[] commutateurs = graphe.keySet().toArray(new String[0]);
		String commutateurDepart = (String) JOptionPane.showInputDialog(null, "Choisissez le commutateur de départ:",
				"Trouver chemin le plus court", JOptionPane.QUESTION_MESSAGE, null, commutateurs, commutateurs[0]);
		if (commutateurDepart == null) {
			return; // L'utilisateur a annulé
		}
		String commutateurDestination = (String) JOptionPane.showInputDialog(null,
				"Choisissez le commutateur de destination:", "Trouver chemin le plus court",
				JOptionPane.QUESTION_MESSAGE, null, commutateurs, commutateurs[0]);
		if (commutateurDestination == null) {
			return; // L'utilisateur a annulé
		}

		List<String> cheminPlusCourt = trouverCheminPlusCourt(graphe, commutateurDepart, commutateurDestination);
		if (cheminPlusCourt.isEmpty()) {
			afficherMessage("Aucun chemin trouvé entre les deux commutateurs.");
		} else {
			StringBuilder message = new StringBuilder();
			message.append("Chemin le plus court entre ").append(commutateurDepart).append(" et ")
					.append(commutateurDestination).append(" : ");
			message.append(String.join(" -> ", cheminPlusCourt));
			afficherMessage(message.toString());
		}
	}

	/**
	 * Trouve le chemin le plus court entre deux commutateurs
	 */
	private static List<String> trouverCheminPlusCourt(Map<String, Map<String, Integer>> graph,
			String commutateurDepart, String commutateurDestination) {
		Queue<String> queue = new LinkedList<>();
		Map<String, String> parentMap = new HashMap<>();
		Map<String, Integer> distanceMap = new HashMap<>();
		Set<String> visited = new HashSet<>();

		queue.add(commutateurDepart);
		visited.add(commutateurDepart);
		parentMap.put(commutateurDepart, null);
		distanceMap.put(commutateurDepart, 0);

		while (!queue.isEmpty()) {
			String noeud = queue.poll();
			if (noeud.equals(commutateurDestination)) {
				return construireChemin(parentMap, commutateurDepart, commutateurDestination);
			}
			for (String voisin : graph.get(noeud).keySet()) {
				int newDistance = distanceMap.get(noeud) + graph.get(noeud).get(voisin);
				if (!visited.contains(voisin) || newDistance < distanceMap.get(voisin)) {
					queue.add(voisin);
					visited.add(voisin);
					parentMap.put(voisin, noeud);
					distanceMap.put(voisin, newDistance);
				}
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Construit le chemin à partir de la table parentMap
	 */
	private static List<String> construireChemin(Map<String, String> parentMap, String start, String end) {
		List<String> chemin = new ArrayList<>();
		String current = end;
		while (current != null) {
			chemin.add(current);
			current = parentMap.get(current);
		}
		Collections.reverse(chemin);
		return chemin;
	}

	/**
	 * Affiche les tables de routage pour chaque commutateur
	 */
	public static void afficherTablesDeRoutages() {
		JFrame frame = new JFrame("Tables de routage");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(400, 300);

		JPanel panel = new JPanel(new BorderLayout());

		textArea = new JTextArea(10, 30);
		textArea.setEditable(false);

		if (!graphe.isEmpty()) {
			StringBuilder result = new StringBuilder();
			for (String commutateur : graphe.keySet()) {
				result.append("Table de routage pour le commutateur ").append(commutateur).append(" :\n");

				for (String destination : graphe.keySet()) {
					if (!destination.equals(commutateur)) {
						result.append(destination).append(" : ");

						List<List<String>> lesChemins = new ArrayList<>();
						trouverTousChemins(commutateur, destination, new ArrayList<>(), lesChemins);

						// Création d'une liste pour stocker les voisins avec leur poids total
						List<VoisinAvecPoids> voisinAvecLeurPoids = new ArrayList<>();

						for (List<String> chemin : lesChemins) {
							if (chemin.size() > 1) {
								String voisin = chemin.get(1);
								int totalPoids = calculerPoidsTotalPath(chemin);

								// Vérifie si le voisin existe déjà dans la liste
								Optional<VoisinAvecPoids> voisinExiste = voisinAvecLeurPoids.stream()
										.filter(n -> n.voisin.equals(voisin)).findFirst();

								if (voisinExiste.isPresent()) {
									// Met à jour le poids total si le nouveau chemin est plus léger
									if (totalPoids < voisinExiste.get().poidsTotal) {
										voisinExiste.get().poidsTotal = totalPoids;
									}
								} else {
									// Ajoute le voisin à la liste
									voisinAvecLeurPoids.add(new VoisinAvecPoids(voisin, totalPoids));
								}
							}
						}

						// Trie des voisins en fonction de leur poids total
						voisinAvecLeurPoids.sort(Comparator.comparingInt(n -> n.poidsTotal));

						// Récupération des noms des voisins triés
						List<String> VoisinsTries = voisinAvecLeurPoids.stream()
								.map(n -> n.voisin + "(" + n.poidsTotal + ")").collect(Collectors.toList());

						result.append(VoisinsTries).append("\n");
					}
				}
				result.append("\n");
			}
			textArea.setText(result.toString());
		} else {
			textArea.setText("Aucun commutateur pour afficher les tables de routage.");
		}

		JScrollPane scrollPane = new JScrollPane(textArea);
		panel.add(scrollPane, BorderLayout.CENTER);

		frame.getContentPane().add(panel);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	// Classe utilitaire pour stocker le voisin avec son poids total
	static class VoisinAvecPoids {
		String voisin;
		int poidsTotal;

		public VoisinAvecPoids(String neighbor, int totalWeight) {
			this.voisin = neighbor;
			this.poidsTotal = totalWeight;
		}
	}

	// Fonction pour calculer le poids total d'un chemin
	public static int calculerPoidsTotalPath(List<String> path) {
		int totalWeight = 0;
		for (int i = 0; i < path.size() - 1; i++) {
			String source = path.get(i);
			String destination = path.get(i + 1);
			totalWeight += graphe.get(source).get(destination);
		}
		return totalWeight;
	}

	/**
	 * Trouve tous les chemins entre deux commutateurs
	 */
	public static void trouverTousChemins(String source, String destination, List<String> path,
			List<List<String>> allPaths) {
		path.add(source);

		if (source.equals(destination)) {
			allPaths.add(new ArrayList<>(path));
		} else {
			Map<String, Integer> voisins = graphe.get(source);
			for (String voisin : voisins.keySet()) {
				if (!path.contains(voisin)) {
					trouverTousChemins(voisin, destination, path, allPaths);
				}
			}
		}
		path.remove(path.size() - 1);
	}

	/**
	 * Affiche un message dans une boîte de dialogue
	 */
	private static void afficherMessage(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

	/**
	 * Retourne le graphe
	 */
	public static Map<String, Map<String, Integer>> getGraph() {
		return graphe;
	}
}
