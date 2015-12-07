import java.io.FileInputStream;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		LZWDecoder lzwDecoder = new LZWDecoder(new FileInputStream("demo.gif"));
		lzwDecoder.decode();
	}
}
