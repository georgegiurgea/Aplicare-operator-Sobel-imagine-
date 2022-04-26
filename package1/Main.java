package package1;

import java.awt.image.BufferedImage;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import package2.ClasaInterfata;
import package2.NonAbstractClass;

public class Main extends NonAbstractClass {
	//declararea bufferedImage 
	public static BufferedImage ImgColor = null;
	public static BufferedImage ImgAlbNegru = null;
	
	public static void main(String[] args) throws IOException{
		//pentru citirea text din consola
		Scanner scan= new Scanner(System.in);
		String input = null;
		String output = null;
		// Third level inheritance
		//crearea de obiec din clasa NonAbstractClass
		NonAbstractClass obj1 = new NonAbstractClass();
		// crearea de obiect din clasa ClassInterface
		ClasaInterfata obj2 = new ClasaInterfata();
		//folosim bloc try catch pentru tratarea erorilor 
				try {
					//apelam metodele obiectului
					obj1.abstractDisplay();
					obj1.displayLevel();
					System.out.println();
					//citim in input linia de text din consola
					input = scan.nextLine();
					//apelam metodele obiectului				
					obj2.display();
					System.out.println();
					
					System.out.println();
					//citim in input linia de text din consola
					output = scan.nextLine();
					
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Adaugati argumente pentru rulare!");
				}
		
		
		
		
		
		
		// citirea imaginii
		//pentru cronometrarea programului
		
		long startTimeReading = System.nanoTime();
		
		//declaram tipul File pentru a citi imaginea
		File file = null;
		
		
		
		
		//folosim bloc try catch pentru citirea imaginii
		try {
			file = new File(input);
			ImgColor = ImageIO.read(file);
			System.out.println("Imaginea color a fost citita!");
			
		} catch(IOException e) {
			System.out.println("Error: " + e);
		}
		//se incheie etapa de citire a imagini
		long stopTimeReading = System.nanoTime();
		
		// incepe procesare de imaginie 
		long startTimeProcessing = System.nanoTime();
		//se declara o imagine noua cu dimensiunile imaginei originale
		ImgAlbNegru = new BufferedImage(ImgColor.getWidth(), ImgColor.getHeight(), ImgColor.getType());
	    //se instantiaza obiecte din clasa buffer ,producer si consumer
		Buffer buffer = new Buffer();
		Producer producer = new Producer(buffer);
		Consumer consumer = new Consumer(buffer);
		//dam startul thredurilor producer si consummer
		producer.start();		// start producer thread
		consumer.start();		// start consumer thread
		
		//trebuie sa asteptam ca thredul consumer sa termine ce are de facut si apoi sa trecem mai departe
		try {
			consumer.join();	// wait consumer thread to end
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
		//apelarea metodei care aplica filtrul sobel
		Main.procesare_filtru();	
		
		//se incheie etapa de procesare a imaginii
		long stopTimeProcessing = System.nanoTime();
		
		// incepe salvarea imaginii noi
		long startTimeSaving = System.nanoTime();
		//folosim bloc try catch pentru citirea imaginii
		try {
			file = new File(output);
			ImageIO.write(ImgAlbNegru, "bmp", file);
			System.out.println("Imaginea alb-negru a fost salvata cu succes!");
		} catch(IOException e) {
			System.out.println("Eroare: " + e);
		}
		//se incheie salvarea imaginii
		long stopTimeSaving = System.nanoTime();
		//calcularea timpului necesare de completarea a fiecarei etape
		//functia de display pentru timp de procesare
		obj1.diplayTime(stopTimeReading - startTimeReading, stopTimeProcessing - startTimeProcessing, stopTimeSaving - startTimeSaving);
		
	}

	private static void procesare_filtru() {
		//luam din imagine dimensiunile sale
		int x = ImgAlbNegru.getWidth();
        int y = ImgAlbNegru.getHeight();

        //facem o matrice de margini care va fi matrice auxiliara
        int[][] edgeColors = new int[x][y];
        //initializam valoarea maxima a gradientului cu -1 pentru a gasi mai tarziu valoarea maxima
        int maxGradient = -1;
        
        //parcurgem fiecare pixel in imagine
        for (int i = 1; i < x - 1; i++) {
            for (int j = 1; j < y - 1; j++) {
            	//se extrage valoare din imagine de coordonate (i,j) 
            	//se face & logic pentru a extrage doar 8 biti din cei 24
            	//deoarece tot cei 8 biti pentru RGB au aceeasi valoare
                int val00 = (ImgAlbNegru.getRGB(i - 1, j - 1))& 0xff;
                int val01 = (ImgAlbNegru.getRGB(i - 1, j))& 0xff;
                int val02 = (ImgAlbNegru.getRGB(i - 1, j + 1))& 0xff;

                int val10 = (ImgAlbNegru.getRGB(i, j - 1))& 0xff;
                int val11 = (ImgAlbNegru.getRGB(i, j))& 0xff;
                int val12 = (ImgAlbNegru.getRGB(i, j + 1))& 0xff;

                int val20 = (ImgAlbNegru.getRGB(i + 1, j - 1))& 0xff;
                int val21 = (ImgAlbNegru.getRGB(i + 1, j))& 0xff;
                int val22 = (ImgAlbNegru.getRGB(i + 1, j + 1))& 0xff;
                //facem convolutia cu valorile extrase 
                int gx =  ((-1 * val00) + (0 * val01) + (1 * val02)) 
                        + ((-2 * val10) + (0 * val11) + (2 * val12))
                        + ((-1 * val20) + (0 * val21) + (1 * val22));

                int gy =  ((-1 * val00) + (-2 * val01) + (-1 * val02))
                        + ((0 * val10) + (0 * val11) + (0 * val12))
                        + ((1 * val20) + (2 * val21) + (1 * val22));
                //calculam valoarea magnitudinii 
                double gval = Math.sqrt((gx * gx) + (gy * gy));
                //transformam valoarea magnitudinii in int 
                int g = (int) gval;
                //determinam maxima a magnitudinii
                if(maxGradient < g) {
                    maxGradient = g;
                }
                //punem valoarea magnitudinii in matricea auxiliara
                edgeColors[i][j] = g;
            }
        }
        //trebuie sa scalam valoarea lui g deoarece un pixel are valoarea maxima de 255 
        double scale = 255.0 / maxGradient;
        //parcurgem fiecare pixel din matricea auxialiara scalam valoarea lui g cu constanta gasita mai sus 
        //si apoi trebuie sa facem numarul de 8 biti ,salvat in matricea auxiliara, in un numar de 24 de biti
        //care sa aiba aceasi 8 biti pentru fiecare culoare
        //dupa care punem numarul de 24 biti format in imagine pe care trebuie sa o salvam
        for (int i = 1; i < x - 1; i++) {
            for (int j = 1; j < y - 1; j++) {
                int edgeColor = edgeColors[i][j];
                edgeColor = (int)(edgeColor * scale);
                edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;

                setRGBImgGray(i, j, edgeColor);
            }
        }
    }
	public static void setRGBImgGray(int x,int y, int col)
	{
		ImgAlbNegru.setRGB(x, y, col);
	}
	
}

