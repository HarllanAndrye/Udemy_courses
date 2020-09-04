package br.com.meusite.cm.visao;

import javax.swing.JFrame;

import br.com.meusite.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class TelaPrincipal extends JFrame {
	
	public TelaPrincipal() {
		Tabuleiro tabuleiro = new Tabuleiro(16, 30, 50);
		
		add(new PainelTabuleiro(tabuleiro)); // Adicionando o "painelTabuleiro" a tela inicial.
		
		setTitle("Campo Minado");
		setSize(690, 438); // Tamanho da janela
		setLocationRelativeTo(null); // Colocando a janela centralizada
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Informa que o botão "x" deve finalizar a aplicação
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new TelaPrincipal();
	}

}
