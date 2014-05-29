package com.voetsjoeba.imdb.renamer.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

/**
 * Serializable wrapper object for BufferedImages. Maintains an internal BufferedImage that is saved to/restored from PNG as this
 * object is (de)serialized.
 * 
 * @author Jeroen De Ridder
 */
@SuppressWarnings("serial")
public class SerializableBufferedImage implements Serializable {
	
	private BufferedImage image = null;
	
	public SerializableBufferedImage() {
		super();
	}
	
	public SerializableBufferedImage(BufferedImage im) {
		this();
		setImage(im);
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		ImageIO.write(getImage(), "png", new MemoryCacheImageOutputStream(out));
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		setImage(ImageIO.read(new MemoryCacheImageInputStream(in)));
	}
	
}
