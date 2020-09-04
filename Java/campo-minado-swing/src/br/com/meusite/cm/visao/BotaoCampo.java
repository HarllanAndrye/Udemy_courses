package br.com.meusite.cm.visao;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import br.com.meusite.cm.modelo.Campo;
import br.com.meusite.cm.modelo.CampoEvento;
import br.com.meusite.cm.modelo.CampoObservador;

@SuppressWarnings("serial")
public class BotaoCampo extends JButton implements CampoObservador, MouseListener {
	
	private final Color BG_PADRAO = new Color(184, 184, 184);
	//private final Color BG_MARCAR = new Color(8, 179, 247);
	private final Color BG_EXPLODIR = new Color(189, 66, 68);
	private final Color TEXTO_VERDE = new Color(0, 100, 0);
	
	private Campo campo;
	
	public BotaoCampo(Campo campo) {
		this.campo = campo;
		
		setBackground(BG_PADRAO); // Alterando a cor do botão
		setBorder(BorderFactory.createBevelBorder(0)); // Alterando as bordas
		
		// Registrando o evento do mouse
		addMouseListener(this);
		
		// Registrando a classe como interessada em escutar os eventos do campo
		campo.registrarObservadores(this);
	}
	
	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		switch (evento) {
			case ABRIR:
				aplicarEstiloAbrir();
				break;
			case MARCAR:
				aplicarEstiloMarcar();
				break;
			case EXPLODIR:
				aplicarEstiloExplodir();
				break;
			default:
				aplicarEstilopadrao();
		}
		
		// "Forçando" o componente a ser "redesenhado" (atualizado) na tela
		SwingUtilities.invokeLater(() -> {
			repaint();
			validate();
		});
	}
	
	private void aplicarEstiloAbrir() {
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		// Se o campo estiver minado, altera a cor do mesmo e interrompe o fluxo deste método.
		if (campo.isMinado()) {
			setBackground(BG_EXPLODIR);
			return;
		}
		
		setBackground(BG_PADRAO);
		
		// Saber o número de minas na vizinhança e alterar a cor do texto de acordo com a quantidade de minas
		switch (campo.minasNaVizinhanca()) {
		case 1:
			setForeground(TEXTO_VERDE); // Cor da letra (texto)
			break;
		case 2:
			setForeground(Color.BLUE);
			break;
		case 3:
			setForeground(Color.YELLOW);
			break;
		case 4:
		case 5:
		case 6:
			setForeground(Color.RED);
			break;
		default:
			setForeground(Color.PINK);
		}
		
		// Se a vizinhança não for segura, mostra a quantidade de minas, caso contrário, não mostra nada.
		String valor = !campo.vizinhancaSegura() ? campo.minasNaVizinhanca() + "" : "";
		setText(valor);
	}

	private void aplicarEstiloMarcar() {
		/*setBackground(BG_MARCAR);
		setForeground(Color.BLACK);
		setText("M");*/
		
		try {
			Image img = ImageIO.read(getClass().getResource("/br/com/meusite/cm/images/flag_.png"));
			setIcon(new ImageIcon(img));
		} catch (Exception e) {
			System.out.println("Exceção: " + e.getMessage());
		}
	}

	private void aplicarEstiloExplodir() {
		/*setBackground(BG_EXPLODIR);
		setForeground(Color.WHITE);
		setText("X");*/
		
		try {
			Image img = ImageIO.read(getClass().getResource("/br/com/meusite/cm/images/mine_.png"));
			setIcon(new ImageIcon(img));
		} catch (Exception e) {
			System.out.println("Exceção: " + e.getMessage());
		}
	}

	private void aplicarEstilopadrao() {
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createBevelBorder(0));
		setText("");
		setIcon(null); // Remover as imagens da mina e bandeira.
	}
	
	// Interface dos eventos do mouse

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) { // 1 = botão esquerdo do mouse
			campo.abrir();
		} else {
			campo.alternarMarcacao();
		}
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

}
