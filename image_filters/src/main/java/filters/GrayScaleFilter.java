package filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import pixels.Pixel;

public class GrayScaleFilter implements Filter {
	@Override
	public String getFilterName() {
		return "Grayscale Filter";
	}

	@Override
	public Image runFilter(BufferedImage image, Map<String, Parameter> parameters) {
		BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		Pixel p;
		for (int x = 0; x <image.getWidth(); x++) {
		    for (int y = 0; y < image.getHeight(); y++) {
		        p  = new Pixel(image.getRGB(x, y)); // create Pixel object from raw value
		        int avg = (p.getR()+p.getG()+p.getB())/3; // calculate average
		        bi.setRGB(x, y, Pixel.generateRaw(avg ,avg ,avg, p.getAlpha())); // set pixel of new image
		    }
		}

		return bi;
	}

	@Override
	public List<Parameter> getParameters() {
		return Arrays.asList();
	}
}
