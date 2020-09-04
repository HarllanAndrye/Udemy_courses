package br.com.meusite.calc.modelo;

import java.util.ArrayList;
import java.util.List;

// Classe Singleton
public class Memoria {

	// Criando um enumerador dentro da pr�pria classe
	private enum TipoComando {
		ZERAR, NUMERO, DIV, MULT, SUB, SOMA, IGUAL, VIRGULA, SINAL, PORC;
	};
	
	private static final Memoria instancia = new Memoria();
	
	private final List<MemoriaObservador> observadores = new ArrayList<>();
	
	private TipoComando ultimaOperacao = null;
	private boolean substituir = false; // Usado para saber se deve ou n�o substituir o texto atual do display quando clicar em uma opera��o
	private String textoAtual = "";
	private String textoBuffer = "";
	
	// Singleton � um padr�o de projeto de software. 
	// Este padr�o garante a exist�ncia de apenas uma inst�ncia de uma classe, mantendo um ponto global de acesso ao seu objeto.
	private Memoria() {}
	
	public static Memoria getInstancia() {
		return instancia;
	}

	public String getTextoAtual() {
		return textoAtual.isEmpty() ? "0" : textoAtual;
	}

	// M�todo que ir� registrar todos observadores
	public void adicionarObservador(MemoriaObservador observador) {
		observadores.add(observador);
	}
	
	// M�todo para processar o comanda da calculadora
	public void processarComando(String texto) {
		
		TipoComando tipo = detectarTipoComando(texto);
		
		if (tipo == null) {
			return;
		} else if (tipo == TipoComando.ZERAR) {
			textoAtual = "";
			textoBuffer = "";
			substituir = false;
			ultimaOperacao = null;
		} else if (tipo == TipoComando.SINAL) {
			if (!textoAtual.contains("-")) {
				textoAtual = "-" + textoAtual;
			} else {
				textoAtual = textoAtual.substring(1); // Retira apenas o 1� caracter, que no caso � o "-"
			}
		} else if (tipo == TipoComando.NUMERO || tipo == TipoComando.VIRGULA) {
			textoAtual = substituir ? texto : textoAtual + texto;
			substituir = false;
		} else {
			if (tipo == TipoComando.PORC) {
				Double numeroAtual = Double.parseDouble(textoAtual.replace(",", "."));
				Double numeroBuffer = Double.parseDouble(textoBuffer.replace(",", "."));
				
				if (ultimaOperacao == TipoComando.SOMA || ultimaOperacao == TipoComando.SUB) {
					numeroAtual = (numeroAtual * numeroBuffer) / 100;
				} else {
					numeroAtual = numeroAtual / 100;
				}
				
				textoAtual = Double.toString(numeroAtual).replace(".", ",");
			}
			
			substituir = true;
			textoAtual = obterResultadoOperacao(); // Calcula primeiro para depois substituir os valores nas pr�ximas vari�veis
			textoBuffer = textoAtual;
			// ultimaOperacao = tipo;
			ultimaOperacao = tipo == TipoComando.PORC ? null : tipo;
		}

		// Sempre que houver uma mudan�a no texto, notificar todos observadores
		observadores.forEach(o -> o.valorAlterado(getTextoAtual()));
	}

	private String obterResultadoOperacao() {
		if (ultimaOperacao == null || ultimaOperacao == TipoComando.IGUAL) {
			return textoAtual;
		} 
		
		Double numeroBuffer = Double.parseDouble(textoBuffer.replace(",", "."));
		Double numeroAtual = Double.parseDouble(textoAtual.replace(",", "."));
		
		double resultado = 0;
		
		if (ultimaOperacao == TipoComando.SOMA) {
			resultado = numeroBuffer + numeroAtual;
		} else if (ultimaOperacao == TipoComando.SUB) {
			resultado = numeroBuffer - numeroAtual;
		} else if (ultimaOperacao == TipoComando.MULT) {
			resultado = numeroBuffer * numeroAtual;
		} else if (ultimaOperacao == TipoComando.DIV) {
			resultado = numeroBuffer / numeroAtual;
		}
		
		String resultadoString = Double.toString(resultado).replace(".", ",");
		// Utilizado para verifica se o n�mero termina com ",0", se sim, retira e mostra apenas o n�mero inteiro
		boolean isInteiro = resultadoString.endsWith(",0");
		
		return isInteiro ? resultadoString.replace(",0", "") : resultadoString;
	}

	private TipoComando detectarTipoComando(String texto) {
		// Evitar colocar zeros � esquerda
		if (texto.isEmpty() && texto == "0") {
			return null;
		}
		
		// Verificando se o texto passado como par�metro � um n�mero
		try {
			Integer.parseInt(texto);
			return TipoComando.NUMERO;
		} catch (NumberFormatException e) {
			// Quando n�o for n�mero...
			
			if ("AC".equalsIgnoreCase(texto)) {
				return TipoComando.ZERAR;
			} else if ("/".equalsIgnoreCase(texto)) {
				return TipoComando.DIV;
			} else if ("*".equalsIgnoreCase(texto)) {
				return TipoComando.MULT;
			} else if ("-".equalsIgnoreCase(texto)) {
				return TipoComando.SUB;
			} else if ("+".equalsIgnoreCase(texto)) {
				return TipoComando.SOMA;
			} else if ("%".equalsIgnoreCase(texto)) {
				return TipoComando.PORC;
			} else if (",".equalsIgnoreCase(texto) && !textoAtual.contains(",")) {
				// Se j� existir v�rgula, n�o entra nesse IF
				return TipoComando.VIRGULA;
			} else if ("�".equalsIgnoreCase(texto)) {
				return TipoComando.SINAL;
			} else if ("=".equalsIgnoreCase(texto)) {
				return TipoComando.IGUAL;
			}
		}
		
		return null;
	}
	
}
