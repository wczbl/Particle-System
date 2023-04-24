package project.gfx;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bitmap {

	public final int w;
	public final int h;
	public int[] pixels;
	private BufferedImage image;
	public boolean flipped;
	
	public Bitmap(BufferedImage image) {
		this.image = image;
		this.w = image.getWidth();
		this.h = image.getHeight();
		this.pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	}
	
	public Bitmap(int w, int h) {
		this.w = w;
		this.h = h;
		this.image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		this.pixels = ((DataBufferInt)this.image.getRaster().getDataBuffer()).getData();
	}
		
	public void setPixel(int x, int y, int col) {
		if(x >= 0 && y >= 0 && x < this.w && y < this.h) {
			this.pixels[x + y * this.w] = col & 0xFFFFFF;
		}
	}
	
	public void draw(Bitmap b, int xp, int yp, int col) {
		int rr = (col >> 16) & 0xFF;
		int gg = (col >> 8) & 0xFF;
		int bb = (col >> 0) & 0xFF;
		
		int x0 = xp;
		int y0 = yp;
		int x1 = xp + b.w;
		int y1 = yp + b.h;
		
		rr = rr * 0x55 / 0xFF;
		gg = gg * 0x55 / 0xFF;
		bb = bb * 0x55 / 0xFF;
		
		if(x0 < 0) x0 = 0;
		if(y0 < 0) y0 = 0;
		if(x1 > this.w) x1 = this.w;
		if(y1 > this.h) y1 = this.h;
		
		for(int y = y0; y < y1; y++) {
			int yy = y - yp;
			for(int x = x0; x < x1; x++) {
				int xx = x - xp;
				
				int c = rr << 16 | gg << 8 | bb;
				
				int src = b.pixels[xx + yy * b.w];
				if(src == 0) continue;
				
				this.pixels[x + y * this.w] = src * c;
				
			}
		}
	}
	
	public int getPixel(int x, int y) {
		if(x >= 0 && y >= 0 && x < this.w && y < this.h) {
			return this.pixels[x + y * this.w];
		}
		
		return 0;
	}
	
	public void clear(int col) { Arrays.fill(this.pixels, col); }
	
	public BufferedImage getImage() { return this.image; }
	
	
	public void smooth(int iterations) {
		iterations = Math.max(1, iterations);
		for(int i = 0; i < iterations; i++) {
			for(int y = 1; y < this.h - 1; y++) {
				for(int x = 1; x < this.w - 1; x++) {
					List<Pixel> pixels = PixelCache.getPixels();
					
					pixels.add(new Pixel(this.pixels[x + y * this.w]));
					
					
					pixels.add(new Pixel(this.pixels[(x - 1) + y * this.w]));
					pixels.add(new Pixel(this.pixels[(x + 1) + y * this.w]));
					pixels.add(new Pixel(this.pixels[x + (y - 1) * this.w]));
					pixels.add(new Pixel(this.pixels[x + (y + 1) * this.w]));
					
					pixels.add(new Pixel(this.pixels[(x - 1) + (y - 1) * this.w]));
					pixels.add(new Pixel(this.pixels[(x - 1) + (y + 1) * this.w]));
					pixels.add(new Pixel(this.pixels[(x + 1) + (y - 1) * this.w]));
					pixels.add(new Pixel(this.pixels[(x + 1) + (y + 1) * this.w]));
					
					this.pixels[x + y * this.w] = Pixel.avgPixels(pixels).getColor();
				}				
			}
		}
	}
	
	public static class PixelCache {
		private static final List<Pixel> pixels = new ArrayList<Pixel>();
		
		
		
		public static List<Pixel> getPixels() {
			pixels.clear();
			return pixels;
		}
	}
	
	public static class Pixel {
		public int r;
		public int g;
		public int b;
		
		public Pixel(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}
		
		public Pixel(int col) {
			this.r = (col >> 16) & 0xFF;
			this.g = (col >> 8) & 0xFF;
			this.b = (col >> 0) & 0xFF;
		}
		
		public int getColor() {
			return ((this.r & 0xFF) << 16) | ((this.g & 0xFF) << 8) | ((this.b & 0xFF) << 0);
		}
		
		public static Pixel avgPixels(List<Pixel> pixels) {
			long rr = 0;
			long gg = 0;
			long bb = 0;
			
			for(Pixel pixel : pixels) {
				rr += pixel.r;
				gg += pixel.g;
				bb += pixel.b;
			}
			
			return new Pixel((int)rr / pixels.size(), (int)gg / pixels.size(), (int)bb / pixels.size());
			
		}
	}
}