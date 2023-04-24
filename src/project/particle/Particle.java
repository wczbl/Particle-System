package project.particle;

import java.util.Random;

import project.ParticleComponent;

public class Particle {

	public static final Random random = new Random();
	public double lifeTime;
	public double age;
	public int x;
	public int y;
	public double angle;
	public double angleA;
	public int speed;
	
	protected ParticleManager pm;
	
	public void init(ParticleManager pm) { 
		this.pm = pm; 
		reset();
	}
	
	public void reset() {
		this.lifeTime = random.nextDouble();
		this.age = random.nextDouble() * 0.05;
		this.angle = random.nextDouble() * Math.PI * 2;
		this.angleA = random.nextDouble() * 0.05;
		this.speed = random.nextInt(2) + 1;
		this.x = this.pm.x;
		this.y = this.pm.y;
	}
	
	public void tick() {
		this.angle += this.angleA;
		if(this.angle > 2 * Math.PI) this.angle = 0;
		
		this.lifeTime -= this.age;
		if(this.lifeTime < 0) reset();
		
		if(this.x < 3 || this.y < 3 || this.x >= ParticleComponent.WIDTH - 4 || this.y >= ParticleComponent.HEIGHT - 4) {
			reset();
		} 			
		
		
		this.x += Math.round(Math.cos(this.angle) * this.speed);
		this.y += Math.round(Math.sin(this.angle) * this.speed);
	}

}