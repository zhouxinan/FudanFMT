import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LZWDecoder {
	List<FrameImage> imageList;
	FileInputStream fileInputStream;
	DataInputStream dataInputStream;
	DataOutputStream dataOutputStream;
	int[][] globalColorTable = null;
	public final int GIF_HEADER_LENGTH = 6;
	int imageDataCount;

	public LZWDecoder(FileInputStream fileInputStream) throws FileNotFoundException {
		super();
		this.fileInputStream = fileInputStream;
		dataInputStream = new DataInputStream(fileInputStream);
		dataOutputStream = new DataOutputStream(new FileOutputStream("a.gif"));
		imageList = new ArrayList<FrameImage>();
	}

	public void decode() throws IOException {
		byte[] gifHeader = new byte[GIF_HEADER_LENGTH];
		dataInputStream.read(gifHeader);
		String gifHeaderString = new String(gifHeader);
		if (!gifHeaderString.startsWith("GIF")) {
			System.out.println("This is not a valid GIF file!");
			return;
		}
		System.out.println("This is a valid GIF file, version: " + gifHeaderString);
		if (!(gifHeaderString.charAt(4) == '9')) {
			System.out.println("This GIF version is not supported in this program!");
			return;
		}
		int width = readLittleEndianShort();
		System.out.println("Width: " + width);
		int height = readLittleEndianShort();
		System.out.println("Height: " + height);
		byte logicalScreenDescriptor = dataInputStream.readByte();
		int globalColorTableFlag = logicalScreenDescriptor >> 7 & 0x1;
		int colorResolution = logicalScreenDescriptor >> 4 & 0x7;
		int sortFlag = logicalScreenDescriptor >> 3 & 0x1;
		int sizeOfGlobalColorTable = logicalScreenDescriptor & 0x7;
		System.out.println("GlobalColorTableFlag: " + globalColorTableFlag);
		System.out.println("ColorResolution: " + colorResolution);
		System.out.println("SortFlag: " + sortFlag);
		System.out.println("SizeOfGlobalColorTable: " + sizeOfGlobalColorTable);
		int backgroundColorIndex = dataInputStream.readUnsignedByte();
		System.out.println("BackgroundColorIndex: " + backgroundColorIndex);
		int pixelAspectRatio = dataInputStream.readUnsignedByte();
		System.out.println("PixelAspectRatio: " + pixelAspectRatio);
		if (globalColorTableFlag == 1) {
			sizeOfGlobalColorTable = 1 << (sizeOfGlobalColorTable + 1);
			globalColorTable = new int[sizeOfGlobalColorTable][3];
			loadColorTable(globalColorTable, sizeOfGlobalColorTable);
		}
		try {
			while (true) {
				int firstByte = dataInputStream.readUnsignedByte();
				if (firstByte == 0x3B) {
					System.out.println("Reached trailer.");
					break;
				} else if (firstByte == 0x21) {
					System.out.println("Extension data read.");
					int secondByte = dataInputStream.readUnsignedByte();
					if (secondByte == 0xFF) {
						System.out.println("This is an application extension.");
						dataInputStream.skip(12);
						int blockSize = dataInputStream.readUnsignedByte();
						System.out.println("BlockSize: " + blockSize);
						dataInputStream.skip(blockSize);
						dataInputStream.skip(1);
					} else if (secondByte == 0xF9) {
						System.out.println("This is a graphic control extension.");
						int blockSize = dataInputStream.readUnsignedByte();
						System.out.println("BlockSize: " + blockSize);
						dataInputStream.skip(blockSize);
						dataInputStream.skip(1);
					}
				} else if (firstByte == 0x2C) {
					System.out.println("This is an image descriptor.");
					int imageLeftPosition = readLittleEndianShort();
					System.out.println("imageLeftPosition: " + imageLeftPosition);
					int imageTopPosition = readLittleEndianShort();
					System.out.println("imageTopPosition: " + imageTopPosition);
					int imageWidth = readLittleEndianShort();
					System.out.println("imageWidth: " + imageWidth);
					int imageHeight = readLittleEndianShort();
					System.out.println("imageHeight: " + imageHeight);
					byte misrpixel = dataInputStream.readByte();
					int m = misrpixel >> 7 & 0x1;
					System.out.println("m:" + m);
					int i = misrpixel >> 6 & 0x1;
					System.out.println("i:" + i);
					int s = misrpixel >> 5 & 0x1;
					System.out.println("s:" + s);
					int r = misrpixel >> 3 & 0x3;
					System.out.println("r:" + r);
					int pixel = misrpixel & 0x7;
					System.out.println("pixel:" + pixel);
					if (m == 1) {
						int sizeOflocalColorTable = 1 << (pixel + 1);
						int[][] localColorTable = new int[sizeOflocalColorTable][3];
						loadColorTable(localColorTable, sizeOflocalColorTable);
						FrameImage image = new FrameImage(localColorTable, imageWidth, imageHeight);
						imageList.add(image);
					} else {
						FrameImage image = new FrameImage(globalColorTable, imageWidth, imageHeight);
						imageList.add(image);
					}
					int lzwMinimumCodeSize = dataInputStream.readUnsignedByte();
				}
			}
		} catch (EOFException e) {
			// TODO Auto-generated catch block
			System.out.println("End of file.");
		}
	}

	public int readLittleEndianShort() throws IOException {
		int small = dataInputStream.read();
		int big = dataInputStream.read();
		return (small + big * 256);
	}

	public void loadColorTable(int[][] colorTable, int sizeOfColorTable) throws IOException {
		for (int i = 0; i < sizeOfColorTable; i++) {
			colorTable[i][0] = dataInputStream.readUnsignedByte();
			colorTable[i][1] = dataInputStream.readUnsignedByte();
			colorTable[i][2] = dataInputStream.readUnsignedByte();
		}
	}
}
