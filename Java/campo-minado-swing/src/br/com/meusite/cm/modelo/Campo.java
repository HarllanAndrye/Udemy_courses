package br.com.meusite.cm.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {
	
	private final int linha;
	private final int coluna;
	
	private boolean aberto = false;
	private boolean minado = false;
	private boolean marcado = false;
	
	private List<Campo> vizinhos = new ArrayList<>();
	private List<CampoObservador> observadores = new ArrayList<>();
	
	Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}
	
	// Registra o observador para o mesmo receber alguma notificação de evento
	public void registrarObservadores(CampoObservador observador) {
		observadores.add(observador);
	}
	
	// Todos observadores serão notificados de que ocorreu um evento
	private void notificarObservadores(CampoEvento evento) {
		// this: é a própria classe "Campo" (classe atual)
		// Irá percorrer a lista e notifica cada observador
		observadores.stream()
			.forEach(o -> o.eventoOcorreu(this, evento));
	}
	
	boolean adicionarVizinho(Campo vizinho) {

		// Verifica se o vizinho esta na mesma linha ou não
		boolean linhaDiferente = this.linha != vizinho.linha;
		// Verifica se o vizinho esta na mesma coluna ou não
		boolean colunaDiferente = this.coluna != vizinho.coluna;
		// Verifica se o vizinho esta na diagonal
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		int deltaLinha = Math.abs(this.linha - vizinho.linha);
		int deltaColuna = Math.abs(this.coluna - vizinho.coluna);
		// deltaGeral deve ter o valor de 1 (se o vizinho estiver na mesma linha ou coluna) ou 2 (se o vizinho estiver na diagonal).
		// Valores diferentes de 1 ou 2, significa que o campo não é vizinho.
		int deltaGeral = deltaLinha + deltaColuna;
		
		if(deltaGeral == 1 && !diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else if(deltaGeral == 2 && diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else {
			return false;
		}
		
	}
	
	// Marca ou não o campo (no campo minado quando clica com o botão direito do mouse)
	public void alternarMarcacao() {
		// O campo não pode estar aberto para ser marcado.
		if (!this.aberto) {
			this.marcado = !this.marcado;
			
			if (marcado) {
				notificarObservadores(CampoEvento.MARCAR);
			} else {
				notificarObservadores(CampoEvento.DESMARCAR);
			}
		}
	}
	
	public boolean abrir() {
		
		// Só abre se estiver fechado e não marcado.
		if(!aberto && !marcado) {
			// Se tiver mina, notifica ao observadores sobre o evento e interrompe o fluxo.
			if(minado) {
				notificarObservadores(CampoEvento.EXPLODIR);
				return true;
			}
			
			setAberto(true);
			
			// Se a vizinhança for segura, continua abrindo campos vizinhos.
			if(vizinhancaSegura()) {
				// Chamada recursiva
				vizinhos.forEach(v -> v.abrir());
			}
			
			return true;
		} else {
			return false;
		}
		
	}
	
	// É verificada toda a vizinhança (diagonal, linha, coluna).
	public boolean vizinhancaSegura() {
		// Se nenhum vizinho estiver minado (com uma mina), então a vizinhança é segura.
		// Se retornar falso, a vizinhança não é segura (há mina).
		return vizinhos.stream().noneMatch(v -> v.minado);
	}
	
	void minar() {
		minado = true;
	}
	
	public boolean isMarcado() {
		return marcado;
	}
	
	public boolean isAberto() {
		return aberto;
	}
	
	void setAberto(boolean aberto) {
		this.aberto = aberto;
		
		if (aberto) {
			// Notificando que o campo foi aberto
			notificarObservadores(CampoEvento.ABRIR);
		}
	}

	public boolean isFechado() {
		return !isAberto();
	}
	
	public boolean isMinado() {
		return minado;
	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}
	
	// Se o campo marcado está correto, ou seja, não tem mina.
	boolean objetivoAlcancado() {
		// Se conseguiu abrir um campo e ele não "explodiu".
		boolean desvendado = !minado && aberto;
		// Se marcou um campo que contém uma mina (campo não aberto!)
		boolean protegido = minado && marcado;
		
		return desvendado || protegido;
	}
	
	// Retornar a quantidade de minas na vizinhança
	public int minasNaVizinhanca() {
		// Filtra todos vizinhos com mina e obtem a quantidade que o filtro retornou.
		return (int) vizinhos.stream().filter(v -> v.minado).count();
	}
	
	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
		
		// Notificando aos observadores o evento
		notificarObservadores(CampoEvento.REINICIAR);
	}

}
