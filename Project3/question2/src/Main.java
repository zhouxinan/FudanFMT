import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException {
		// Get GIF filename.
		System.out.println("Please input GIF filename: ");
		Scanner input = new Scanner(System.in);
		String gifFilename = input.nextLine();
		input.close();
		// Decode GIF.
		GIFDecoder gifDecoder = new GIFDecoder(gifFilename);
		gifDecoder.decode();
	}
}
