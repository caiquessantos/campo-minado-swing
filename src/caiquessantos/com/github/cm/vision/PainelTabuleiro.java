package caiquessantos.com.github.cm.vision;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import caiquessantos.com.github.cm.model.Board;

@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel {

	public PainelTabuleiro(Board tabuleiro) {
		
		setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));
		
		tabuleiro.ParaCadaCampo(c -> add(new BotaoCampo(c)));
		
		tabuleiro.resgistrarObserver(e -> {
			
			SwingUtilities.invokeLater(() -> {
				
				if (e.isGanhou()) {
					JOptionPane.showMessageDialog(this, "Ganhou!!!");
				} else {
					JOptionPane.showMessageDialog(this, "Perdeu!!!");
				}
				
				tabuleiro.reiniciar();
			});
		});
	}
}
