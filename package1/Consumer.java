package package1;

public class Consumer extends Thread{
	private Buffer buffer;
	private int w,h;
	public Consumer(Buffer buffer) {
		this.buffer = buffer;
		this.w = getWidthImag();//latime imagine
		this.h = getHeightImg();//lungime imagine
	}
	
	
	@Override
	public void run() {
		
		
		for (int i = 0; i < 4; i++) {
			int[][] Bloc_pixels = new int[w/4][h];//se creaza spatiul pentru a stoca matricea primita
			//se pune blocul in matricea auxiliara 
			Bloc_pixels = buffer.getPixels();
			//se pune pixeli din buffer in imaginea finala
			for (int j = 0; j < h; j++) {
				for (int k = w/4 * i; k < w/4 * (i + 1); k++) {
					Main.setRGBImgGray(k, j, Bloc_pixels[k - (w/4 * i)][j]);
				}
			}
			
			System.out.println("Consumatorul a preluat sfertul cu numarul " + (i + 1) + " al imaginii.");
			
			try {
				//se executa sleep din cerinta de 1 secunda 
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

