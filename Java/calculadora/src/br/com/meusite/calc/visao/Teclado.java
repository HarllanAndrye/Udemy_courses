package br.com.meusite.calc.visao;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import br.com.meusite.calc.modelo.Memoria;

@SuppressWarnings("serial")
public class Teclado extends JPanel implements ActionListener {
	
	private final Color COR_CINZA_ESCURO = new Color(68, 68, 68);
	private final Color COR_CINZA_CLARO = new Color(99, 99, 99);
	private final Color COR_LARANJA = new Color(242, 163, 60);
	
	public Teclado() {
		// GridBagLayout: é mais flexivel na construção do layout, pois, por exemplo, podemos colocar o botão ocupando mais de uma coluna.
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		setLayout(layout);
		
		// Faz com que os botões oculpem todo espaço da tela em igual tamanho
		c.weightx = 1;
		c.weighty = 1;
		
		// Faz com que os elementos preencham todos espaços vazios da tela (todos lados)
		c.fill = GridBagConstraints.BOTH;
		
		// Linha 1
		c.gridwidth = 2; // Faz com que o botão oculpe 3 espaços na grade
		adicionarbotao("AC", COR_CINZA_ESCURO, c, 0, 0);
		c.gridwidth = 1;
		adicionarbotao("%", COR_LARANJA, c, 2, 0);
		adicionarbotao("/", COR_LARANJA, c, 3, 0);
		
		// Linha 2
		adicionarbotao("7", COR_CINZA_CLARO, c, 0, 1);
		adicionarbotao("8", COR_CINZA_CLARO, c, 1, 1);
		adicionarbotao("9", COR_CINZA_CLARO, c, 2, 1);
		adicionarbotao("*", COR_LARANJA, c, 3, 1);
		
		// linha 3
		adicionarbotao("4", COR_CINZA_CLARO, c, 0, 2);
		adicionarbotao("5", COR_CINZA_CLARO, c, 1, 2);
		adicionarbotao("6", COR_CINZA_CLARO, c, 2, 2);
		adicionarbotao("-", COR_LARANJA, c, 3, 2);
		
		// linha 4
		adicionarbotao("1", COR_CINZA_CLARO, c, 0, 3);
		adicionarbotao("2", COR_CINZA_CLARO, c, 1, 3);
		adicionarbotao("3", COR_CINZA_CLARO, c, 2, 3);
		adicionarbotao("+", COR_LARANJA, c, 3, 3);
		
		// linha 5
		//c.gridwidth = 2;
		adicionarbotao("±", COR_CINZA_CLARO, c, 0, 4);
		adicionarbotao("0", COR_CINZA_CLARO, c, 1, 4);
		//c.gridwidth = 1;
		adicionarbotao(",", COR_CINZA_CLARO, c, 2, 4);
		adicionarbotao("=", COR_LARANJA, c, 3, 4);
	}

	private void adicionarbotao(String texto, Color cor, GridBagConstraints c, int x, int y) {
		// x e y são as coordenadas na grade de onde será adicionado o componente (botão)
		c.gridx = x;
		c.gridy = y;
		
		Botao botao = new Botao(texto, cor);
		botao.addActionListener(this);
		
		add(botao, c);
	}
	
	// Para cada botão é chamado esse método
	@Override
	public void actionPerformed(ActionEvent e) {
		// getSource(): obtem a origem do evento.
		// Verifica e o evento é uma instancia de JButton
		if (e.getSource() instanceof JButton) {
			JButton botao = (JButton) e.getSource();
			Memoria.getInstancia().processarComando(botao.getText());
		}
	}

}
