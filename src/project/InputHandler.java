package project;

import java.awt.Canvas;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputHandler implements MouseListener, MouseMotionListener {

	public int mx;
	public int my;
	
	public boolean mouseClicked;
	public boolean mousePressed;
	private boolean _mousePressed;
	
	public InputHandler(Canvas canvas) {
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
	}
	
	public void tick() {
		this.mouseClicked = !this.mousePressed && this._mousePressed;
		this.mousePressed = this._mousePressed;
	}
	
	public void mouseDragged(MouseEvent e) {
		this.mx = e.getX() / ParticleComponent.SCALE;
		this.my = e.getY() / ParticleComponent.SCALE;
	}

	public void mouseMoved(MouseEvent e) {
		this.mx = e.getX() / ParticleComponent.SCALE;
		this.my = e.getY() / ParticleComponent.SCALE;		
	}

	public void mouseClicked(MouseEvent e) {
		this.mx = e.getX() / ParticleComponent.SCALE;
		this.my = e.getY() / ParticleComponent.SCALE;		
	}

	public void mousePressed(MouseEvent e) {
		this.mx = e.getX() / ParticleComponent.SCALE;
		this.my = e.getY() / ParticleComponent.SCALE;		
		this._mousePressed = true;
	}

	public void mouseReleased(MouseEvent e) {
		this.mx = e.getX() / ParticleComponent.SCALE;
		this.my = e.getY() / ParticleComponent.SCALE;				
		this._mousePressed = false;
	}

	public void mouseEntered(MouseEvent e) {		
		this.mx = e.getX() / ParticleComponent.SCALE;
		this.my = e.getY() / ParticleComponent.SCALE;		
	}

	public void mouseExited(MouseEvent e) {
		this.mx = e.getX() / ParticleComponent.SCALE;
		this.my = e.getY() / ParticleComponent.SCALE;				
	}

}