public class FrameImage {
	int[] colorTable;
	int width;
	int height;

	public FrameImage(int[] colorTable, int width, int height) {
		super();
		this.colorTable = colorTable;
		this.width = width;
		this.height = height;
	}

	public int getSize() {
		return width * height;
	}

	public int[] getColorTable() {
		return colorTable;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
