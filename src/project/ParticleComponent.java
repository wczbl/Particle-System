package project;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;

import project.gfx.Bitmap;
import project.particle.ParticleManager;

public class ParticleComponent extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	public static final Random random = new Random();
	public static final String TITLE = "Particle System";
	public static final int WIDTH = 240;
	public static final int HEIGHT = 180;
	public static final int SCALE = 3;
	private boolean running;
	
	public Bitmap screenBitmap;
	public ParticleManager pm;
	public InputHandler input;
	
	public synchronized void start() {
		if(this.running) return;
		this.running = true;
		new Thread(this).start();
	}
	
	public void run() {
		init();
		long lastTime = System.nanoTime();
		double delta = 0.0;
		double nsPerTick = 1000000000.0 / 60.0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		int ticks = 0;
		while(this.running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			
			while(delta >= 1) {
				delta--;
				tick();
				ticks++;
			}			
			
			render();
			swap();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("frames: " + frames + " ticks: " + ticks);
				frames = 0;
				ticks = 0;
			}
			
			try {
				Thread.sleep(2L);
			} catch (InterruptedException e) {
				// ignore
			}
		}
	}
	
	public void init() {
		this.screenBitmap = new Bitmap(WIDTH, HEIGHT);
		this.pm = new ParticleManager(400, random.nextInt());
		this.input = new InputHandler(this);
	}
	
	public void tick() {  
		this.input.tick();
		this.pm.tick(this.input); 
		if(this.input.mouseClicked) this.pm.setColor(random.nextInt());
	}
	
	public void swap() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(2);
			requestFocus();
			return;
		}
				
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		int xOffset = (getWidth() - WIDTH * SCALE) / 2;
		int yOffset = (getHeight() - HEIGHT * SCALE) / 2;
		
		g.drawImage(this.screenBitmap.getImage(), xOffset, yOffset, WIDTH * SCALE, HEIGHT * SCALE, null);
		
		g.dispose();
		bs.show();
	}
	
	public void render() {
		this.pm.render(this.screenBitmap);
		this.screenBitmap.smooth(2);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Dimension d = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		ParticleComponent ps = new ParticleComponent();
		frame.setTitle(TITLE);
		ps.setMinimumSize(d);
		ps.setMaximumSize(d);
		ps.setPreferredSize(d);
		frame.setLayout(new BorderLayout());
		frame.add(ps, BorderLayout.CENTER);
		frame.pack();
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		ps.start();
	}
	
}