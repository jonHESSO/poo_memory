/*
 * Project : poo_memory
 * Author : Jonathan Schnyder
 * Created : Jun 20, 2017
 */

package jeu_gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * le panel qui correspond au jeu. Les cartes sont des JButton modifi�es
 * qui lancent un actionlistener contenant une methode de comparaison
 * des deux cartes selectionn�es. Il prend en param�tre la @taille du jeu
 * (doit etre un nombre pair, sinon on a un nombre impair de cartes).
 * Toutes les cartes sont cr��es en paires avec des valeurs identiques
 * puis dispos�es al�atoirement dans une matrice de @taille par @taille
 */
public class PlateauPanel extends JPanel implements ActionListener
{
	//le plateau de jeu est un carr� de [taille] par [taille]
	private int taille ;
	//la matrice qui contient toutes les cartes
	private CarteLabel[][] grilleDeCarte ;
	//le nombre de paires
	private int nombreDePaires ;

	//le jeu compare toujours deux cartes, carte1 et carte2 permettent de les stocker temporairement
	private CarteLabel carte1 ;
	private CarteLabel carte2 ;
	
	//on utilise des icones contenues dans le r�pertoire icons
	private String iconDirectory = "icons" ;
	File directory = new File(iconDirectory) ;
	
	//on stock les icones dans une arraylist d'imageicon
	private ArrayList<ImageIcon> iconList ;

	//un timer est necessaire afin d'attendre avant que les cartes affich�es soit a nouveau cach�es
	private Timer timer =new Timer(1200, this);

	//constructeur, prend en paramt�tre la taille du plateau de jeu
	public PlateauPanel(int taille)
	{
		//jpanel standard
		super() ;
		//[taille] doit �tre pair, car sinon on a un nombre impair de cartes
		if (taille % 2 != 0){
			throw new IllegalArgumentException("Pas un nombre pair") ;
		}
		//on stock la taille, duh
		this.taille = taille ;
		//on definit le nombre de paires
		nombreDePaires = (taille * taille) / 2 ;
		
		//on definit la taille du plateau
		Dimension dimensions = new Dimension(100*taille,100*taille) ;
		setPreferredSize(dimensions);
		setLayout(new GridLayout(taille,taille));

		//on recup�re les icones pour le memory
		getIcons(nombreDePaires) ;
		
		//on lance un nouveau jeu
		nouveauJeu();
	}
	
	//methode g�n�rant les icones � partir du r�pertoire d'icones
	private void getIcons(int nombreDePaires)
	{
		//on initialise l'arraylist
		iconList = new ArrayList<>() ;
		//on initialise un compteur indiquant l'index du fichier qu'on teste
		int i = 0 ;
		//on cr�� une array contenant les r�f�rences vers les fichiers contenus dans le r�pertoire
		File[] files = directory.listFiles() ;
		//tant qu'on a pas autant d'icones qu'on a de paires
		while (iconList.size() < nombreDePaires)
		{
			//on essaie de cr�er une icone � partir du fichier � l'index i
			try
			{
				ImageIcon tIcon = new ImageIcon(files[i].getPath()) ;
				iconList.add(tIcon);
			}
			catch (Exception e)
			{
				//dommage
			}
			
			//on passe au fichier suivant, mais on d�passe pas le nombre de fichiers existants
			i = (i+1)%files.length ;
		}
	}

	//methode pour lancer un nouveau jeu
	public void nouveauJeu()
	{
		timer.stop();
		//on enleve tout ce qu'il y avait avant
		removeAll() ;
		//on initialise les valeurs de carte1 et carte2 � null
		carte1 = null ;
		carte2 = null ;
		//on cr�� une nouvelle matrice vide
		grilleDeCarte = new CarteLabel[taille][taille] ;
		//on cr�� des paires jusqu'� remplir la matrice
		for (int i = 0 ; i < nombreDePaires ; i++)
		{
			//chaque paire a un animal diff�rent
			ImageIcon animal = iconList.get(i) ;
			//pour chaque paire, on cr�� deux cartes identiques
			for (int j = 0 ; j< 2 ; j++)
			{
				//creation d'une nouvelle carte
				CarteLabel tCarte = new CarteLabel(animal) ;
				//ajout d'un actionlistener
				tCarte.addActionListener(new CardListener());
				//trouvage d'une case libre dans la matrice
				int[] index = celluleAleatoireLibre() ;
				int ligne = index[0] ;
				int colonne = index[1] ;
				//ajout de la carte dans la matrice
				grilleDeCarte[ligne][colonne] = tCarte ;
			}
		}

		//ajout de toutes les cartes au panel
		for (int i = 0; i < grilleDeCarte.length; i++)
		{
			for (int j = 0; j < grilleDeCarte[0].length; j++)
			{
				add(grilleDeCarte[i][j])  ;
			}
		}
		//on refresh l'affichage
		revalidate() ;
		repaint() ;
	}

	//trouvaaillage d'une case libre dans la matrice, retourne l'index sous forme [row,column]
	private int[] celluleAleatoireLibre() 
	{
		//initialisation de l'index a null
		int[] index = null ;

		//variables pour g�n�rer un numero de ligne et de colonne al�atoire
		int randomRow ;
		int randomColumn ;

		//booleen indiquant si une case a �t� trouv�e
		boolean caseOccupee = true ;

		//tant qu'aucune case n'a �t� trouv�e, on g�n�re un numero de ligne / colonne
		while (caseOccupee == true){
			
			//generation d'un numero de ligne et de colonne al�atoire
			randomRow = (int) (Math.random()*taille) ;
			randomColumn = (int) (Math.random()*taille) ;

			//si la case est libre
			if (grilleDeCarte[randomRow][randomColumn] == null)
			{
				//on r�cup�re l'index de la case, et on indique que la case est libre
				index = new int[2] ;
				index[0] = randomRow  ;
				index[1] = randomColumn ;
				caseOccupee = false ;
			}
		}
		
		//on retourne l'index de la case
		return  index ;
	}


	//indique si le jeu est fini
	private boolean estFini()
	{
		//on part du principe que c'est fini, sauf si
		boolean estFini = true ;
		for (int i = 0; i < grilleDeCarte.length; i++) 
		{
			for (int j = 0; j < grilleDeCarte[0].length; j++) 
			{
				//sauf si n'importe quelle case n'a pas �t� retourn�e
				if(grilleDeCarte[i][j].estRetournee()==false)
				{
					//du coup c'est pas fini, hein
					estFini = false ;
				}
			}
		}
		//fini ou pas ? who knows
		return estFini ;
	}


	//methode tout slip, to String
	public String toString()
	{
		String tableau = "" ;
		for (int i = 0; i < grilleDeCarte.length; i++) 
		{
			for (int j = 0; j < grilleDeCarte[0].length; j++) 
			{
				String valeur = "   x   " ;
				if(grilleDeCarte[i][j].estRetournee()==true)
				{
					valeur = grilleDeCarte[i][j].toString() ;
				}
				tableau += valeur + "  " ;
			}
			tableau += "\n" ;
		}
		tableau += "\n" ;
		return tableau ;
	}


	//le listener des cartes quand elles sont cliqu�es.
	private class CardListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			//si l'�tat est fini, on fait rien
			if (estFini()==true)
			{
				return;
			}

			//si la carte est d�ja retourn�e, on fait rien
			if(((CarteLabel) e.getSource()).estRetournee()==true)
			{
				return ;
			}
			
			//si la carte selectionn�e est la carte1, on fait rien
			if((CarteLabel) e.getSource() == carte1)
			{
				return ;
			}
			
			//si la carte2 n'est pas vide, �a veut dire que le timer n'as pas encore fini et vid� les carte1 et carte2
			//du coup on fait rien
			if(carte2 != null)
			{
				return ;
			}

			//si la carte1 est vide, �a veut dire qu'on affiche la premiere carte
			if (carte1 == null)
			{
				//on stock la r�f�rence de la carte cliqu�e dans carte1, et on l'affiche
				carte1 = (CarteLabel) e.getSource() ;
				carte1.afficher();
			}
			//si la carte1 n'est pas vide, �a veut dire qu'on affiche la carte 2, et on verifie si �a forme une paire
			else
			{
				//on stock la r�f�rence de la carte cliqu�e dans carte2, et on l'affiche
				carte2 = (CarteLabel) e.getSource() ;
				carte2.afficher();

				//on r�cup�re les valeurs des deux cartes
				String animal1 = carte1.getAnimal() ;
				String animal2 = carte2.getAnimal() ;
				
				//on compare les deux valeurs, si elles correspondent :
				if (animal1.equals(animal2))
				{
					//on retourne les deux cartes (elles ont l'�tat trouv�)
					carte1.retourner();
					carte2.retourner();
					
					//on vide les ref�rences de carte1 et carte2
					carte1 = null ;
					carte2 = null ;
					
					//on teste si le jeu est fini, si oui on affiche un petit message
					if (estFini()==true)
					{
						JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(PlateauPanel.this),"Bravo vous avez tout trouv�","Gagn�",1);
					}
				}
				//si les deux valeurs ne correspondent pas, on lance le timer
				else
				{
					timer.start();

				}
			}
		}
	}
	
	//listener du timer, quand le timer arrive � x secondes on cache les deux cartes cliqu�es, et on vide
	//les r�f�rences de carte1 et carte2. On arrete aussi le timer, il va etre relanc� � la prochaine
	//paire fausse.
	public void actionPerformed(ActionEvent ev){
		if(ev.getSource()==timer){
			carte1.cacher();
			carte2.cacher();
			carte1 = null ;
			carte2 = null ;
			timer.stop();
		}
	}

}
