/*
 * Project : poo_memory
 * Author : Jonathan Schnyder
 * Created : Jun 20, 2017
 */

package jeu_gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * cette classe repr�sente une carte de memory, c'est un JButton modifie
 * A la creation le bouton affiche un caract�re blanc, et stock la valeur
 * de l'animal qu'il contient. La carte contient des methodes pour afficher
 * et cacher la valeur de la carte, et un boolean servant de flag pour savoir
 * si la carte a �t� trouv�e ou non
 */
public class CarteLabel extends JButton
{
	//contient la valeur de la carte
	private ImageIcon animal ;
	//et le flag indiquand si elle a �t� trouv�e
	private boolean retournee ;
	
	
	//constructeur, prend en param�tre un string repr�santant l'animal de la carte
	public CarteLabel(ImageIcon animal)
	{
		//cr�� un JButton vide
		super() ;
		
		//stock les valeurs, et initialise le flag
		this.animal = animal ;
		this.retournee = false ;
				
	}
	
	//retourne la valeur de la carte
	public String getAnimal()
	{
		return animal.toString() ;
	}
	
	//retourne la valeur du flag trouv� ou non 
	public boolean estRetournee()
	{
		return retournee ;
	}
	
	//affiche la valeur de la carte et indique qu'elle est trouv�e
	public void retourner()
	{
		this.retournee = true ;
		this.setIcon(animal);
	}
	
	//affiche la valeur de la carte
	public void afficher()
	{
		this.setIcon(animal);
	}
	
	//cache la valeur de la carte
	public void cacher()
	{
		this.setIcon(null);
	}

}
