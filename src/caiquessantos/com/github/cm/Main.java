package caiquessantos.com.github.cm;

import javax.swing.JFrame;

import caiquessantos.com.github.cm.model.*;
import caiquessantos.com.github.cm.vision.*;
@SuppressWarnings("serial")
public class Main extends JFrame {

	public Main() {
		Board tabuleiro = new Board(16, 30, 50);
		add(new PainelTabuleiro(tabuleiro));
		setTitle("Campo Minado");
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(690, 438);
		setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		new Main();

	}

}
