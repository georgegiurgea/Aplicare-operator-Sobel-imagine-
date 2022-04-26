package package1;
public class Buffer {
	
	private int[][] pixels = null;
	private boolean disponibil = false;
	
	public synchronized int[][] getPixels() {
		while (!disponibil) {
			try {
				wait();
				// Asteapta producatorul sa puna o valoare
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//dupa ce a pus o valoare se notifica threadurile prin notifyall
		//si se returneaza matricea de pixeli
		disponibil = false;
		notifyAll();
		return this.pixels;
	}
	
	public synchronized void setPixels(int[][] pixels) {
		while (disponibil) {
			try {
				wait();
				// Asteapta consumatorul sa ia o valoare
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//dupa ce consumatorul a luat o valoare se returneaza matrice de pixeli
		//disponibilitatea se face true si se notifica prin notifyall
		this.pixels = pixels;
		disponibil = true;
		notifyAll();
	}
}
