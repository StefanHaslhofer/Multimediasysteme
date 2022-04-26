package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import pixels.Pixel;

public class Subsampling implements Filter {

	@Override
	public String getFilterName() {
		return "Subsampling";
	}

	@Override
	public Image runFilter(BufferedImage image, Map<String, Parameter> parameters) {
		int rate = parameters.get("rate").getValue();
		BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	
		for (int x = 0; x < image.getWidth(); x+=rate) {
		    for (int y = 0; y < image.getHeight(); y+=rate) {
		    	// iterate over current block
		    	// current block has boundaries (x,y) to (x+rate,y+rate)
		    	for (int i = x; i < x + rate && i < image.getWidth(); i++) {
				    for (int j = y; j < y + rate && j < image.getHeight(); j++) {
				    	bi.setRGB(i, j, image.getRGB(x, y));
				    }
		    	}
		    	
		    	if(y + rate >= image.getHeight()) {
		    		break;
		    	}
		    }
		    
		    // break out of loop if x + rate is larger than width
		    // otherwise we get an java.lang.ArrayIndexOutOfBoundsException
		    if(x + rate >= image.getWidth()) {
	    		break;
	    	}
		}
		
		return bi;
	}

	@Override
	public List<Parameter> getParameters() {
		Parameter p = new Parameter("rate", 4, 1, 8);
		return Arrays.asList(p);
	}

}
