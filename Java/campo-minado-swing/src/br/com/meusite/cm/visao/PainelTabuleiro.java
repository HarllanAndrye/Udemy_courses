package br.com.meusite.cm.visao;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.meusite.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel {
	
	public PainelTabuleiro(Tabuleiro tabuleiro) {
		
		// Informa como organizar os componentes visuais na tela.
		// Nesse caso, será uma grade (tabuleiro) com linhas e colunas.
		setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));
		
		// Para cada campo será criado um botão do tipo JButton e adicionado (add) ao painel JPanel
		tabuleiro.paraCadaCampo(c -> add(new BotaoCampo(c)));
		
		// Registrando o tabuleiro (observador) para obter o resultado do jogo
		// e = ResultadoEvento
		tabuleiro.registrarObservadores(e -> {
			// Mostra para o usuário a mensagem só após a tela do jogo atualizar
			SwingUtilities.invokeLater(() -> {
				if (e.isGanhou()) {
					JOptionPane.showMessageDialog(this, "Ganhou :)");
				} else {
					JOptionPane.showMessageDialog(this, "Perdeu :(");
				}
				
				tabuleiro.reiniciar();
			});
		});
	}

}
