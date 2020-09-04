package br.com.meusite.calc.visao;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Calculadora extends JFrame {

	public Calculadora() {
		organizarLayout();
		
		setSize(232, 322);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // Abrir aplica��o no centro da tela
		setVisible(true);
	}
	
	private void organizarLayout() {
		// BorderLayout divide a tela em Norte, Sul, Leste, Oeste e Centro.
		setLayout(new BorderLayout());
		
		Display display = new Display();
		display.setPreferredSize(new Dimension(233, 60)); // Definindo o tamanho do display
		add(display, BorderLayout.NORTH); // M�todo add() � da classe JFrame, que a classe atual est� herdando
		
		Teclado teclado = new Teclado();
		add(teclado, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		new Calculadora();
	}
	
}
