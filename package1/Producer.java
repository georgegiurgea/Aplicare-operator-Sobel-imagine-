package package1;
import java.awt.Color;

public class Producer extends Thread{
	private Buffer buffer;
	private int w,h;
	
	public Producer(Buffer buffer) {
		this.buffer = buffer;
		this.w = getWidthImag();//latime imagine
		this.h = getHeightImg();//lungime imagine
	}
	
	@Override
	public void run() {
		
		
		for (int i = 0; i < 4; i++) {
			int[][] Bloc_pixeli_gri = new int[w/4][h];
			//se creaza spatiul pentru a stoca matricea primita
			for (int j = 0; j < h; j++) {
				for (int k = w/4 * i; k < w/4 * (i + 1); k++) {
					
					//se ia valorile culorilor in imaginea color 
					Color culoare = new Color(Main.ImgColor.getRGB(k, j));
					//se face media valorilor 
					int medie = (culoare.getRed() + culoare.getBlue() + culoare.getGreen()) / 3;
					//se reface culoarea doar ca toate valorile pixelilor vor fi media lor
					culoare = new Color(medie, medie, medie);
					//se pun pixeli noi calculati 
					Bloc_pixeli_gri[k - (w/4 * i)][j] = culoare.getRGB();
				}
			}
			//punem pixeli alb-negru in buffer
			buffer.setPixels(Bloc_pixeli_gri);
			
			System.out.println("Producatorul a pus sfertul cu numarul " + (i + 1) + " al imaginii.");
			//timpul de 1 secunda specificat in cerinte
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public int getWidthImag()
	{
		return Main.ImgColor.getWidth();
	}
	public int getHeightImg()
	{
		return Main.ImgColor.getHeight();
	}
}

