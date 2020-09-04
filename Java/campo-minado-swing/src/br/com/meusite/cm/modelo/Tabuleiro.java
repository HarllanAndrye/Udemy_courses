package br.com.meusite.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador {
	
	private final int linhas;
	private final int colunas;
	private final int qtdMinas;
	
	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>(); 

	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.qtdMinas = minas;
		
		gerarCampos();
		associarOsVizinhos();
		sortearMinas();
	}
	
	// Registra o observador para o mesmo receber alguma notificação de evento
	public void registrarObservadores(Consumer<ResultadoEvento> observador) {
		observadores.add(observador);
	}
	
	// Todos observadores serão notificados de que ocorreu um evento
	private void notificarObservadores(boolean resultado) {
		observadores.stream()
			.forEach(o -> o.accept(new ResultadoEvento(resultado)));
	}

	public void abrir(int linha, int coluna) {
		// Procurar na lista (campos) a linha e colunas passadas como parâmetro.
		campos.parallelStream()
			.filter(c -> c.getLinha() == linha && c.getColuna() == coluna) // procurar a linha e coluna
			.findFirst() // obtem o campo encontrado
			.ifPresent(c -> c.abrir()); // chama o método abrir
	}
	
	public void alternarMarcacao(int linha, int coluna) {
		campos.parallelStream()
			.filter(c -> c.getLinha() == linha && c.getColuna() == coluna) // procurar a linha e coluna
			.findFirst() // obtem o campo encontrado
			.ifPresent(c -> c.alternarMarcacao()); // chama o método para marcar o campo
	}
	
	private void gerarCampos() {
		for (int linha = 0; linha < this.linhas; linha++) {
			for (int coluna = 0; coluna < this.colunas; coluna++) {
				Campo campo = new Campo(linha, coluna);
				// Registrando o tabuleiro como observador para cada campo criado.
				// O tabuleiro é o "interessado" por cada evendo que ocorre em cada campo.
				campo.registrarObservadores(this);
				campos.add(campo);
			}
		}
	}
	
	private void associarOsVizinhos() {
		for(Campo c1: campos) {
			for(Campo c2: campos) {
				c1.adicionarVizinho(c2);
			}
		} 
	}
	
	private void sortearMinas() {
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		
		do {
			// Math.random(): retorna um valor entre 0.0 e 1.0
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			minasArmadas = campos.stream().filter(minado).count();
		} while (minasArmadas < qtdMinas);
	}
	
	public boolean objetivoAlcancado() {
		// Se todos os campos tiverem com o objetivo alcançado, então terminou o jogo.
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	
	public void reiniciar() {
		// Reinicia cada campo
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	}
	
	/*public List<Campo> getCampos() {
		return campos;
	}*/
	
	// Método onde é passada uma função (lambda) que irá ser utilizada em cada campo
	public void paraCadaCampo(Consumer<Campo> funcao) {
		campos.forEach(funcao);
	}
	
	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		if (evento == CampoEvento.EXPLODIR) {
			mostrarMinas();
			notificarObservadores(false);
		} else if (objetivoAlcancado()) {
			notificarObservadores(true);
		}
	}
	
	private void mostrarMinas() {
		// Só irá abrir os campos que contém mina.
		// O campos marcados não irão abrir.
		campos.stream()
			.filter(c -> c.isMinado())
			.filter(c -> !c.isMarcado()) // Não obter os campos marcados
			.forEach(c -> c.setAberto(true));
	}

	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}
	
}
