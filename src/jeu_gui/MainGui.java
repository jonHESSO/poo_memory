/*
 * Project : poo_memory
 * Author : Jonathan Schnyder
 * Created : Jun 20, 2017
 */

package jeu_gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class MainGui
{

	public static void main(String[] args)
	{
		//creation d'une nouvelle frame
		JFrame frame = new JFrame() ;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//creation d'un nouveau jeu
		PlateauPanel jeu = new PlateauPanel(6) ;
		
		//ajout du jeu � la frame
		frame.add(jeu,BorderLayout.NORTH) ;
		
		//creation d'un bouton nouveau jeu 
		JButton newGame = new JButton("Nouvelle partie");
		newGame.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				jeu.nouveauJeu();
			}
		});
		
		//ajout du bouteau nouveau jeu
		frame.add(newGame, BorderLayout.SOUTH);
		
		//pack et affichage
		frame.pack();
		frame.setVisible(true);

	}

}
