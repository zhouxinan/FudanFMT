import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GIFDecoder {
	FileInputStream fileInputStream;
	DataInputStream dataInputStream;
	int[] globalColorTable;
	private final int GIF_HEADER_LENGTH = 6;
	FrameImage image;

	public GIFDecoder(String gifFilename) throws FileNotFoundException {
		super();
		this.fileInputStream = new FileInputStream(gifFilename);
		this.dataInputStream = new DataInputStream(fileInputStream);
	}

	public void decode() throws IOException {
		// Check if GIF header is valid.
		byte[] gifHeader = new byte[GIF_HEADER_LENGTH];
		dataInputStream.read(gifHeader);
		String gifHeaderString = new String(gifHeader);
		if (!gifHeaderString.startsWith("GIF")) {
			System.out.println("This is not a valid GIF file!");
			return;
		}
		System.out.println("This is a valid GIF file, version: " + gifHeaderString);
		if (gifHeaderString.charAt(4) != '9') {
			System.out.println("This GIF version is not supported in this program!");
			return;
		}
		// Read width and height from logical screen descriptor
		int width = readLittleEndianShort();
		System.out.println("Width: " + width);
		int height = readLittleEndianShort();
		System.out.println("Height: " + height);
		// Read four pieces of information.
		byte nextByte = dataInputStream.readByte();
		int globalColorTableFlag = nextByte >> 7 & 0x1;
		int colorResolution = nextByte >> 4 & 0x7;
		int sortFlag = nextByte >> 3 & 0x1;
		int sizeOfGlobalColorTable = nextByte & 0x7;
		System.out.println("GlobalColorTableFlag: " + globalColorTableFlag);
		System.out.println("ColorResolution: " + colorResolution);
		System.out.println("SortFlag: " + sortFlag);
		System.out.println("SizeOfGlobalColorTable: " + sizeOfGlobalColorTable);
		// Read BackgroundColorIndex.
		int backgroundColorIndex = dataInputStream.readUnsignedByte();
		System.out.println("BackgroundColorIndex: " + backgroundColorIndex);
		// Read PixelAspectRatio.
		int pixelAspectRatio = dataInputStream.readUnsignedByte();
		System.out.println("PixelAspectRatio: " + pixelAspectRatio);
		// If globalColorTableFlag is 1, load global color table.
		if (globalColorTableFlag == 1) {
			sizeOfGlobalColorTable = 1 << (sizeOfGlobalColorTable + 1);
			globalColorTable = new int[sizeOfGlobalColorTable];
			loadColorTable(globalColorTable);
		}

		while (true) {
			int firstByte = dataInputStream.readUnsignedByte();
			if (firstByte == 0x3B) {
				System.out.println("Reached trailer.");
				break;
			} else if (firstByte == 0x21) {
				int secondByte = dataInputStream.readUnsignedByte();
				if (secondByte == 0xFF) {
					System.out.println("This is an application extension.");
					// Skip application sub-block
					readBlockSizeAndSkip();
					// Skip data sub-block
					readBlockSizeAndSkip();
					// Skip block terminator
					dataInputStream.skip(1);
				} else if (secondByte == 0xF9) {
					System.out.println("This is a graphic control extension.");
					// Skip graphic control sub-block
					readBlockSizeAndSkip();
					// Skip block terminator
					dataInputStream.skip(1);
				} else if (secondByte == 0xFE) {
					System.out.println("This is a comment extension.");
					// Skip comment sub-block
					readBlockSizeAndSkip();
					// Skip block terminator
					dataInputStream.skip(1);
				}
			} else if (firstByte == 0x2C) {
				System.out.println("This is an image descriptor.");
				// Read imageLeftPosition, imageTopPosition, imageWidth,
				// imageHeight of current frame image.
				int imageLeftPosition = readLittleEndianShort();
				System.out.println("imageLeftPosition: " + imageLeftPosition);
				int imageTopPosition = readLittleEndianShort();
				System.out.println("imageTopPosition: " + imageTopPosition);
				int imageWidth = readLittleEndianShort();
				System.out.println("imageWidth: " + imageWidth);
				int imageHeight = readLittleEndianShort();
				System.out.println("imageHeight: " + imageHeight);
				// Read m, i, s, r, pixel
				byte misrpixel = dataInputStream.readByte();
				int m = misrpixel >> 7 & 0x1;
				System.out.print("m:" + m);
				int i = misrpixel >> 6 & 0x1;
				System.out.print(" i:" + i);
				int s = misrpixel >> 5 & 0x1;
				System.out.print(" s:" + s);
				int r = misrpixel >> 3 & 0x3;
				System.out.print(" r:" + r);
				int pixel = misrpixel & 0x7;
				System.out.print(" pixel:" + pixel + "\n");
				// If m is 1, use local color table. Else, use global color
				// table.
				if (m == 1) {
					int sizeOflocalColorTable = 1 << (pixel + 1);
					int[] localColorTable = new int[sizeOflocalColorTable];
					loadColorTable(localColorTable);
					image = new FrameImage(localColorTable, imageWidth, imageHeight);
				} else {
					image = new FrameImage(globalColorTable, imageWidth, imageHeight);
				}
				// Decode the image of the current frame.
				LZWDecoder lzwDecoder = new LZWDecoder(image, dataInputStream);
				lzwDecoder.decode();
			}
		}
	}

	// This function is to read a little-endian short integer.
	public int readLittleEndianShort() throws IOException {
		int small = dataInputStream.readUnsignedByte();
		int big = dataInputStream.readUnsignedByte();
		return (small + big * 256);
	}

	// This function is to load color table.
	public void loadColorTable(int[] colorTable) throws IOException {
		int sizeOfColorTable = colorTable.length;
		for (int i = 0; i < sizeOfColorTable; i++) {
			int red = dataInputStream.readUnsignedByte();
			int green = dataInputStream.readUnsignedByte();
			int blue = dataInputStream.readUnsignedByte();
			colorTable[i] = (0xFF << 24) | (red << 16) | (green << 8) | blue;
		}
	}

	// This function is read block size and skip the block immediately.
	public void readBlockSizeAndSkip() throws IOException {
		int blockSize = dataInputStream.readUnsignedByte();
		System.out.println("BlockSize: " + blockSize);
		dataInputStream.skip(blockSize);
	}
}
