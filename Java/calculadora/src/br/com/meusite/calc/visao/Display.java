package br.com.meusite.calc.visao;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.meusite.calc.modelo.Memoria;
import br.com.meusite.calc.modelo.MemoriaObservador;

@SuppressWarnings("serial")
public class Display extends JPanel implements MemoriaObservador {
	
	private final JLabel label;
	
	public Display() {
		// Registrar o Display para informar que ele está interessado nos eventos que ocorrerem
		// Sempre que o valor for alterado, recebe a notificação
		Memoria.getInstancia().adicionarObservador(this);
		
		setBackground(new Color(46, 49, 50));
		label = new JLabel(Memoria.getInstancia().getTextoAtual());
		label.setForeground(Color.WHITE);
		label.setFont(new Font("courier", Font.PLAIN, 30));

		// Gerenciador de layout.
		// FlowLayout(alinhamento, espaçoHorizontal, espaçoVertical)
		setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 25)); // Alinhar o texto a direita
		
		add(label);
	}
	
	@Override
	public void valorAlterado(String novoValor) {
		// Assim que altera o valor, o mesmo é alterado no display
		label.setText(novoValor);
	}

}
