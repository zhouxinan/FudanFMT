import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LZWDecoder {
	// Standard GIF max dictionary size in bits
	final int MAX_DICTIONARY_BIT_SIZE = 12;
	// Image of current frame.
	FrameImage image;
	DataInputStream dataInputStream;
	// Image size (width * height)
	int imageSize;
	// The original code size
	int lzwMinimumCodeSize;
	// Current code size
	int codeSize;
	// Color table of the image
	int[] colorTable;
	int dictionarySize;
	int clearCode;
	int stopCode;
	int[][] dictionary;
	// Size of the sub-block
	int subblockSize;
	// Position of current byte in the sub-block
	int subblockByteCount;
	// Current byte in the sub-block
	int currentByte;
	// Position of the bit to start to read from in currentByte.
	int bitPointer;
	// How many frame images have been processed.
	static int frameImageCount;
	// Pixel color buffer.
	int[] pixelColor;
	// The index of the pixel to write.
	int pixelIndex;

	public LZWDecoder(FrameImage image, DataInputStream dataInputStream) throws IOException {
		super();
		this.image = image;
		this.dataInputStream = dataInputStream;
		this.imageSize = image.getSize();
		this.lzwMinimumCodeSize = dataInputStream.readUnsignedByte() + 1;
		this.codeSize = lzwMinimumCodeSize;
		this.colorTable = image.getColorTable();
		this.dictionarySize = colorTable.length + 2;
		this.clearCode = colorTable.length;
		this.stopCode = colorTable.length + 1;
		this.subblockSize = dataInputStream.readUnsignedByte();
		this.currentByte = dataInputStream.readUnsignedByte();
		this.dictionary = new int[1 << MAX_DICTIONARY_BIT_SIZE][];
		this.subblockByteCount = 1;
		this.bitPointer = 0;
		this.pixelColor = new int[imageSize];
	}

	public void decode() throws IOException {
		// Initialize dictionary.
		for (int i = 0; i < colorTable.length; i++) {
			int[] temp = { i };
			this.dictionary[i] = temp;
		}
		// LZW algorithm.
		// Set newCode to clearCode as default value.
		int newCode = clearCode;
		while (true) {
			int oldCode = newCode;
			newCode = getNextCode();
			if (newCode == stopCode) {
				break;
			} else if (newCode == clearCode) {
				resetDictionary();
			} else if (oldCode == clearCode) {
				decode(oldCode, newCode, false, false);
			} else {
				if (newCode < dictionarySize) {
					decode(oldCode, newCode, false, true);
				} else {
					decode(oldCode, oldCode, true, true);
				}
				// Increase code size if necessary.
				if (codeSize < MAX_DICTIONARY_BIT_SIZE && dictionarySize >= (1 << codeSize)) {
					codeSize++;
				}
			}
		}
		saveImageToBmpFile(pixelColor);
	}

	// This function is to get next code from the input stream.
	public int getNextCode() throws IOException {
		int numberOfBitsGot = 0;
		int code = 0;
		// Loop until "codeSize" number of bits are got.
		while (numberOfBitsGot < codeSize) {
			int currentByteCopy = currentByte;
			// Calculate how many bits to get in the current byte.
			int numberOfBitsToGet = 8 - bitPointer;
			if (numberOfBitsToGet > (codeSize - numberOfBitsGot)) {
				numberOfBitsToGet = codeSize - numberOfBitsGot;
				// Create bit mask.
				int bitMask = 0xff >> (8 - bitPointer - numberOfBitsToGet);
				currentByteCopy &= bitMask;
			} else {
				currentByteCopy >>= bitPointer;
			}
			// Add currentByteCopy to code.
			code |= currentByteCopy << numberOfBitsGot;
			numberOfBitsGot += numberOfBitsToGet;
			bitPointer += numberOfBitsToGet;
			if (bitPointer >= 8) {
				// Reset bit bitPointer.
				bitPointer = 0;
				// When the current sub-block is finished, read the next sub-block
				if (subblockByteCount >= subblockSize) {
					subblockSize = dataInputStream.readUnsignedByte();
					// Reset subblockByteCount.
					subblockByteCount = 0;
				}
				currentByte = dataInputStream.readUnsignedByte();
				subblockByteCount++;
			}
		}
		return code;
	}

	// This function is to reset dictionary.
	public void resetDictionary() {
		dictionarySize = colorTable.length + 2;
		codeSize = lzwMinimumCodeSize;
	}

	// This function is to save frame image to BMP file.
	private void saveImageToBmpFile(int[] colorData) throws IOException {
		int width = image.getWidth();
		int height = image.getHeight();
		// Let the output filename be "Frame*.bmp"
		File file = new File("Frame" + frameImageCount + ".bmp");
		frameImageCount++;
		DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = bufferedImage.getRaster();
		raster.setDataElements(0, 0, width, height, colorData);
		ImageIO.write(bufferedImage, "bmp", dataOutputStream);
	}

	// This function is to decode the image on receiving a new code.
	public void decode(int oldCode, int codeToOutput, boolean isToOutputCHAR, boolean isToCreateNewEntry) {
		for (int i = 0; i < dictionary[codeToOutput].length; i++) {
			pixelColor[pixelIndex++] = colorTable[dictionary[codeToOutput][i]];
		}
		int CHAR = dictionary[codeToOutput][0];
		if (isToOutputCHAR) {
			pixelColor[pixelIndex++] = colorTable[CHAR];
		}
		if (isToCreateNewEntry) {
			int[] newEntry = new int[dictionary[oldCode].length + 1];
			System.arraycopy(dictionary[oldCode], 0, newEntry, 0, dictionary[oldCode].length);
			newEntry[newEntry.length - 1] = CHAR;
			dictionary[dictionarySize] = newEntry;
			dictionarySize++;
		}
	}
}