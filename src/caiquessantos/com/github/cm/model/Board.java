package caiquessantos.com.github.cm.model;

/**
 * 
 * Classe para fazer a chamdada dos métodos da classe 'Field' para associar os vizinhos de uma casa, marcar e desmarcar uma casa, reiniciar o tabuleiro, 
 * abrir uma casa, criar os campos e Sortear as minas.
 * 
 * @author Caique Santos Santana
 * @version 1.0
 * @since Release 01 da aplicação
 * */

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Board implements CampoObserver {
	
	private final int linhas;
	private final int colunas;
	private final int minas;
	
	private final List<Field> campos = new ArrayList<>();
	private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();
	
	/**
	 * Construtor da classe 'Board' que faz a chamada dos métodos criaCampos, associarVizinhos e SortearMinas logo quando é executado.
	 * 
	 * @param linha - linha o tabuleiro.
	 * @param coluna = coluna do tabuleiro.
	 * @param minas - minas do tabuleiro.
	 * 
	 * 
	 * */
	public Board(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		criarCampos();
		associarVizinhos();
		sortearMinas();
	}
	
	
	public int getLinhas() {
		return linhas;
	}


	public int getColunas() {
		return colunas;
	}


	public int getMinas() {
		return minas;
	}
	
	public void ParaCadaCampo(Consumer<Field> funcao) {
		campos.forEach(funcao);
	}


	/**
	 * Método que faz o registro dos observers da classe 'Board'
	 * 
	 * @param CampoObserver - O observer do campo
	 * */
	public void resgistrarObserver(Consumer<ResultadoEvento> o) {
		observadores.add(o);
	}
	
	/**
	 * 
	 * Notifica a todos os observers registrados que um evento aconteceu
	 *  
	 * @param CampoObserver - O observer do campo
	 * */
	private void notificarObservadores(boolean resultado) {
		observadores.stream().forEach(o -> o.accept(new ResultadoEvento(resultado)));
	}
	
	/**
	 * Método que chama o método de abrir uma casa de acordo com a linha e coluna desejada.
	 * 
	 * @author Caíque Santos Santana
	 * @param linha - linha desejada.
	 * @param coluna - coluna desejada. 
	 * */
	public void abrir(int linha, int coluna) {
		campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst().ifPresent(c -> c.abrir());
	}
	
	/**
	 * Método que chama o método de marcar ou desmarcar uma casa de acordo com a linha e coluna desejada.
	 * 
	 * @author Caíque Santos Santana
	 * @param linha - linha desejada.
	 * @param coluna - coluna desejada. 
	 * */
	public void marcarEdesmarcar(int linha, int coluna) {
		campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst().ifPresent(c -> c.marcarEdesmarcar());
	}
	
	/**
	 * Método para criar os campos linha a linha e coluna a coluna do tabuleiro.
	 * 
	 * @author Caíque Santos Santana
	 * */
	private void criarCampos(){
		for (int linha = 0; linha < linhas; linha++) {
			for (int coluna = 0; coluna < colunas; coluna++) {
					Field campo = new Field(linha, coluna);
					campo.RegistrarObservador(this);
					campos.add(campo);
			}
		}	
	}
	
	/**
	 * Método para associar cada endereço de linha e coluna como vizinho de uma casa específica, trocando de casa a cada iteracão.
	 * 
	 * @author Caíque Santos Santana
	 * */
	private void associarVizinhos() {
		for (Field field1 : campos) {
			for (Field field2 : campos) {
				field1.adicionarVizinho(field2);
			}
		}
	}
	
	/**
	 * Método para sortear as casas que as minas serão geradas chamando o mkétodo minar() de acordo com o valor aleatoriamente gerado.
	 * 
	 * @author Caíque Santos Santana
	 * */
	private void  sortearMinas() {
		
		long minasColocadas = 0;
		Predicate<Field> minado = f ->f.isMinado();
		
		do {
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			minasColocadas = campos.stream().filter(minado).count();
		} while (minasColocadas < minas);
	}
	
	/**
	 * Método para chamar o método que testa se o objetivo foi alcançado.
	 * 
	 * @author Caíque Santos Santana
	 * */
	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	
	/**
	 * Método para chamar o método de reiniciar o tabuleiro.
	 * 
	 * @author Caíque Santos Santana
	 * */
	public void reiniciar() {
		campos.stream().forEach(r -> r.reiniciar());
		sortearMinas();
	}

	@Override
	public void EventoOcorreu(Field campo, EventoNoCampo evento) {
		if (evento == EventoNoCampo.EXPLODIR) {
			mostrarMinas();
			notificarObservadores(false);
		} else if (objetivoAlcancado()) {
			notificarObservadores(true);
		}
		
	}	
	
	/**
	 * Método que mostra todas as minas no tabuleiro após a derrota do user.
	 * */
	private void mostrarMinas() {
		campos.stream().filter(c -> c.isMinado()).filter(c -> !c.isMarcado())
		.forEach(c -> c.setAberto(true));
	}

}
