package caiquessantos.com.github.cm.model;

/**
 * 
 * Classe para fazer a adição dos vizinhos o local marcado, marcar e desmarcar uma casa, minar uma casa, abrir uma casa e checar se o vizinho dessa casa está seguro para abri-lo tambem
 * reiniciar o tabuleiro e mostrar o resultado de uma jogadada. 
 * 
 * 
 * @author Caique Santos Santana
 * @version 1.0
 * @since Release 01 da aplicação
 * */


import java.util.ArrayList;
import java.util.List;

public class Field {
	
	private boolean aberto = false;
	private boolean minado = false;
	private boolean marcado = false;
	private final int linha;
	private final int coluna;
	private boolean descoberto = false;
	private boolean protegido = false;
	private List<Field> vizinhos = new ArrayList<>();
	private List<CampoObserver> observadores = new ArrayList<>();
	
	
	/**
	 * Construtor da classe 'Field'
	 * @param linha - linha o tabuleiro
	 * @param coluna = coluna do tabuleiro.
	 * 
	 * */
	Field(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}
	
	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}
	
	/**
	 * Método que faz o registro dos observers da classe 'Field'
	 * 
	 * @param CampoObserver - O observer do campo
	 * */
	public void RegistrarObservador(CampoObserver O) {
		observadores.add(O);
	}
	
	/**
	 * 
	 * Notifica a todos os observers registrados que um evento aconteceu
	 *  
	 * @param CampoObserver - O observer do campo
	 * */
	private void notificarObservadores(EventoNoCampo evento) {
		observadores.stream().forEach(o -> o.EventoOcorreu(this, evento));
	}
	
	/**
	 * 
	 * Método para a adição de vizinhos ao endereço de campo escolhido, podendo esse ser um vizinho horizontal, vertical, diagonal ou não ser um vizinho.
	 * 
	 * @author Caíque Santos Santana
	 * @param  supostoVizinho - Endereço no tabuleiro que será analisado para ser adicionado ou não como vizinho o endereço escolhido
	 * @return Boolean - Informa se o parâmetro passado foi ou não considerado um vizinho.
	 * 
	 * */ 
	boolean adicionarVizinho(Field supostoVizinho) {
		
		boolean linhaDiferente = linha != supostoVizinho.linha;
		boolean colunaDiferente = coluna != supostoVizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		int deltaLinha = Math.abs(linha-supostoVizinho.linha);
		int deltaColuna = Math.abs(coluna-supostoVizinho.coluna);
		int deltaSomatorio = deltaLinha + deltaColuna;
		
		if (deltaSomatorio == 1 && !diagonal) {
			vizinhos.add(supostoVizinho);
			return true;
		} else if (deltaSomatorio == 2 && diagonal) {
			vizinhos.add(supostoVizinho);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Método para marcar e desmarcar uma casa no tabuleiro
	 * 
	 * @author Caíque Santos Santana
	 * */
	public void marcarEdesmarcar() {
		if (!aberto) {
			marcado = !marcado;
		}
		if (marcado) {
			notificarObservadores(EventoNoCampo.MARCAR);
		} else {
			notificarObservadores(EventoNoCampo.DESMARCAR);
		}
	}
	
	/**
	 *Método que funciona como o get para o atributo 'marcado'
	 *
	 *@author Caíque Santos Santana
	 *@return Boolean - Valor do atributo 'marcado'.
	 * */
	public boolean isMarcado() {
		return marcado;
	}
	
	/**
	 * Método para minar uma casa no tabuleiro
	 * 
	 * @author Caíque Santos Santana
	 * */
	void minar() {
		minado = true;
	}
	
	/**
	 *Método que funciona como o get para o atributo 'minado'
	 *
	 *@author Caíque Santos Santana
	 *@return Boolean - Valor do atributo 'minado'.
	 * */
	public boolean isMinado() {
		return minado;
	}
	
	/**
	 * Método para abrir uma casa no tabuleiro. Caso a casa aberta contenha uma mina, ele interrompe a execução e lança uma exceção. Caso o contrário ele abre todos os
	 * vizinhos que estejam seguros.
	 * 
	 * @author Caíque Santos Santana
	 * @return Boolean - True caso a casa seja aberta e false caso não seja.
	 * */
	public boolean abrir() {
		if (!aberto && !marcado) {
			if (minado) {
				notificarObservadores(EventoNoCampo.EXPLODIR);
				return true;
			}
			setAberto(true);
			
			if (vizinhoSeguro()) {
				vizinhos.forEach(v -> v.abrir());
			}
			return true;
		} else {
			return false;

		}
	}
	
	
	/**
	 * Set e 'aberto', faz a valiação para abrir um campo na interface gráfica.
	 * */
	void setAberto(boolean aberto) {
		this.aberto = aberto;
		if (aberto) {
			notificarObservadores(EventoNoCampo.ABRIR);
		}
	}

	/**
	 *Método que funciona como o get para o atributo 'aberto'
	 *
	 *@author Caíque Santos Santana
	 *@return Boolean - Valor do atributo 'aberto'.
	 * */
	public boolean isAberto() {
		return aberto;
	}
	
	/**
	 * Método para checar se o vizinho está ou não minado.
	 * 
	 * @author Caíque Santos Santana
	 * @return Boolean - True caso o vizinho checado não esteja minado e false caso o contrário.
	 * */
	public boolean vizinhoSeguro() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}
	
	/**
	 * Método para checar se o objativo da casa atual foi alcançado, ou seja, se ela foi aberta e não tinha mina ou se ela foi marcada e tinha minha.
	 * 
	 * @author Caíque Santos Santana
	 * @return Boolean - True caso o objetivo da casa atual seja alcancado e false caso o contrário.
	 * */
	boolean objetivoAlcancado() {
		descoberto = !minado && aberto;
		protegido = minado && marcado;
		return descoberto || protegido;
	}
	
	/**
	 * Método para contar quantas minas existem próximas ao vizinho.
	 * 
	 * @author Caíque Santos Santana
	 * @return Long - A quantidade de minas que existem próximas ao vizinho.
	 * */
	public int minasNosVizinhos() {
		return (int) vizinhos.stream().filter(v -> v.minado).count();
	}
	
	/**
	 * Método para reiniciar os atributos do jogo e consequentemente o jogo.
	 * 
	 * @author Caíque Santos Santana
	 * */
	void reiniciar() {
		minado = false;
		aberto = false;
		marcado = false;
		notificarObservadores(EventoNoCampo.REINICIAR);
	}

}
