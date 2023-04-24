package project.particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import project.InputHandler;
import project.ParticleComponent;
import project.gfx.Bitmap;

public class ParticleManager {

	public static final Random random = new Random();
	public List<Particle> particles = new ArrayList<Particle>();
	public int color = 0xFF7F00;
	
	public int x = ParticleComponent.WIDTH / 2;
	public int y = ParticleComponent.HEIGHT / 2;
	
	public ParticleManager(int count, int color) {
		this(count);
		this.color = color;
	}
	
	public ParticleManager(int count) {
		for(int i = 0; i < count; i++) {
			add(new Particle());
		}
	}
	
	public void add(Particle p) {
		this.particles.add(p);
		p.init(this);
	}
	
	public void tick(InputHandler input) {
		this.x = input.mx;
		this.y = input.my;
		
		for(Particle p : this.particles) {
			p.tick();
		}
	}
	
	public void render(Bitmap screen) {
		for(Particle p : this.particles) {
			screen.setPixel(p.x, p.y, this.color);
		}
	}
	
	public void setColor(int color) { this.color = color; }
}