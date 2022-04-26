package filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import pixels.Pixel;

public class SepiaFilter implements Filter {

	@Override
	public String getFilterName() {
		return "Sepia Filter";
	}

	@Override
  public Image runFilter(BufferedImage image, Map<String, Parameter> parameters) {
    BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

    Pixel p;
	for (int x = 0; x <image.getWidth(); x++) {
	    for (int y = 0; y < image.getHeight(); y++) {
	        p  = new Pixel(image.getRGB(x, y)); // create Pixel object from raw value
	        int sepiaR = (int) ((p.getR()*0.393) + (p.getG()*0.769) + (p.getB()*0.189));
	        int sepiaG = (int) ((p.getR()*0.349) + (p.getG()*0.686) + (p.getB()*0.168));
	        int sepiaB = (int) ((p.getR()*0.272) + (p.getG()*0.534) + (p.getB()*0.131));

	        bi.setRGB(x, y, Pixel.generateRaw(sepiaR,sepiaG,sepiaB,p.getAlpha())); // set pixel of new image
	    }
	}
    
    
	return bi;

	}

	@Override
	public List<Parameter> getParameters() {
		return Arrays.asList();
	}
}
