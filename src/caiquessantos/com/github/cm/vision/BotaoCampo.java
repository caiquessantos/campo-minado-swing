package caiquessantos.com.github.cm.vision;


import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import caiquessantos.com.github.cm.model.CampoObserver;
import caiquessantos.com.github.cm.model.EventoNoCampo;
import caiquessantos.com.github.cm.model.Field;

@SuppressWarnings("serial")
public class BotaoCampo extends JButton implements CampoObserver, MouseListener {

	private final Color bg_padrao  = new Color(184,184,184);
	private final Color bg_marcar  = new Color(8,179,247);
	private final Color bg_explodir  = new Color(189,66,68);
	private final Color bg_verde = new Color(0,100,0);
	
	private Field campo;
	
	public BotaoCampo(Field campo) {
		this.campo = campo;
		setBackground(bg_padrao);
		setOpaque(true);
		setBorder(BorderFactory.createBevelBorder(0));
		addMouseListener(this);
		campo.RegistrarObservador(this);
	}

	@Override
	public void EventoOcorreu(Field campo, EventoNoCampo evento) {
		switch (evento) {
		case ABRIR:
			estiloAbrir();
			break;
		case MARCAR:
			estiloMarcar();
			break;
		case EXPLODIR:
			estiloExplodir();
			break;
		default:
			estiloPadrao();
			break;
		}
		
	}

	private void estiloPadrao() {
		setBackground(bg_padrao);
		setBorder(BorderFactory.createBevelBorder(0));
		setText("");
		
	}

	private void estiloExplodir() {
		setBackground(bg_explodir);
		setForeground(Color.black);
		setText("💣");

		
	}

	private void estiloMarcar() {
		if (!campo.isMarcado()) {
			estiloPadrao();
		} else if (campo.isMarcado()) {
			setBackground(bg_marcar);
			setForeground(Color.black);
			setText("⚠");			
		}
	}

	private void estiloAbrir() {
		setBackground(bg_padrao);
		setBorder(BorderFactory.createLineBorder(Color.gray));
		
		if (campo.isMinado()) {
			setBackground(bg_explodir);
			return;
		}
		
		switch (campo.minasNosVizinhos()) {
		case 1:
			setForeground(bg_verde);
			break;
		case 2:
			setForeground(Color.blue);
			break;
		case 3:
			setForeground(Color.yellow);
			break;
		case 4:
		case 5:
		case 6:
			setForeground(Color.red);
			break;
		default:
			setForeground(Color.pink);
			break;
		}
		
		String valor = !campo.vizinhoSeguro() ? campo.minasNosVizinhos() + "" : "";
		setText(valor);
	}
	
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) {
			campo.abrir();
		} else {
			campo.marcarEdesmarcar();
		}
	}
	public void mouseClicked(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		
	}
	
	
}
