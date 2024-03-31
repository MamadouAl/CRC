# Transmission bande passante 
## Sujet
Il s’agit d’implanter un programme JAVA permettant de tracer le signal électrique émis lors de
l’envoi d’une chaîne binaire en fonction du code choisi (NRZ, Manchester, Manchester différentiel,
Miller).
1. Votre interface devra permettre de saisir une chaîne binaire et de choisir le code à utiliser
pour le signal électrique.
2. En fonction de la chaîne binaire et du code choisi, vous devez tracer le bon signal.

# Les Codes Cycliques de Redondance (CRC)
## Sujet 
Il s’agit d’implanter un programme JAVA permettant de calculer ou de vérifier un code CRC.
1. Écrire une méthode qui à partir d’un mot donné sous forme d’une chaîne de caractères
donnera lorsque c’est possible le code CRC à lui adjoindre et le mot complet résultant de
cette adjonction. Cette méthode devra permettre de présenter les étapes ayant aboutit à
ce résultat.
2. Écrire une méthode qui à partir d’un mot complet toujours donné sous forme d’une chaîne
de caractères vérifiera si ce mot contient ou ne contient pas d’erreurs. Cette méthode devra
permettre de présenter les étapes ayant aboutit au résultat.
3. Écrire un programme principal qui permettra à un utilisateur de saisir des suites de bits
puis d’effectuer des vérifications sur des trames complètes ou de calculer les codes CRC
correspondant à la chaîne de bit saisie.
### Début d’analyse du programme
On considère deux fonctionalités principales au programme :
• le calcul d’un code CRC,
• la vérification d’un message utilisant un code CRC.
Pour chaque opération, il faudra que l’utilisateur choississent le polynôme générateur à utiliser.

# Code de Hamming
## Sujet 
l s’agit d’implanter un programme JAVA permettant de calculer les codes de Hamming.
1. Écrire une méthode qui à partir d’un mot donné sous forme d’une chaîne de caractères donnera
lorsque c’est possible le code à lui adjoindre et le mot de Hamming résultant de cette adjonction.
2. Écrire une méthode qui à partir d’un mot de Hamming toujours donné sous forme d’une chaîne de
caractères vérifiera si ce mot contient ou ne contient pas d’erreurs. Cette méthode devra permettre
de présenter les étapes ayant aboutit au résultat.
3. Écrire un programme principal qui permettra à un utilisateur de saisir des suites de bits puis
d’effectuer des vérifications sur des codes de Hamming ou de calculer les mots de Hamming corre-
spondant à la chaîne saisie.
### Début d’analyse du programme
On considère deux fonctionalités principales au programme :
• le calcul d’un code de Hamming,
• la vérification d’un message utilisant Hamming comme code détecteur d’erreur.
L’utilisateur saisira un mot sous forme d’une suite de bits. Cette suite devra posséder une longueur bien
précise pour correspondre à un mot de Hamming. Un mot de Hamming est de la forme "x-y", x étant la
longueur totale du mot et y la longueur du message émis (sans le code de contrôle).
Pour i=1,2,3,... ,la longueur total du mot de Hamming est x=2^i − 1, la longueur du message est y=(2^i − 1) − i et la longueur du code contrôle de parité est i.

# Routage 
## Sujet 
Il s’agit d’implanter un programme JAVA permettant de donner le chemin le plus court dans
une topologie réseau donnée. On imagine que l’on dispose d’un réseau dans lequel se trouvent
des machines et des commutateurs avec plusieurs interfaces réseaux.
1. Votre programme devra permettre de saisir le graphe correspondant à la topologie réseau.
2. L’utilisateur choisira deux machines dans votre réseau puis le programme permettra de
trouver l’ensemble des nœuds intermédiaires ainsi que les interfaces utilisées pour se rendre
d’une extrémité à l’autre.
3. Le résultat pourra être donnée sous forme textuelle et/ou éventuellement graphique.
4. Finalement, votre programme devra permettre d’établir les tables de routage au niveau de
chaque commutateur et les tables de circuits virtuels entre deux machines (on donnera une
liste de liaison à établir dans l’ordre).

# Source 
M. Duvallet, Professeur de Réseaux, Université du Havre.
https://eureka.univ-lehavre.fr/course/view.php?id=733



