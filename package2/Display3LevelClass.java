package package2;

public class Display3LevelClass {
	
	public void displayLevel() {
		System.out.println("3 Introduceti numele imagini sursa sub forma nume.bmp");
	}
	public void diplayTime(double timecitire,double timeproc,double timesalvare )
	{
		System.out.println("Timpul pentru citire: " + (double) timecitire / 1000000000 + " secunde");
		System.out.println("Timpul pentru procesare: " + (double) timeproc / 1000000000 + " secunde");
		System.out.println("Timpul pentru salvare: " + (double) timesalvare / 1000000000 + " secunde");
	};
}
