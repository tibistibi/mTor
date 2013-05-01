package nl.bhit.mtor.server.webapp.action;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import nl.bhit.mtor.server.webapp.util.ImageUtils;

import com.opensymphony.xwork2.Preparable;

public class QRCodeLoginAction extends BaseAction implements Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6583950769381263089L;
	
	private static final int QR_WIDTH = 100;
	private static final int QR_HEIGHT = 100;
	private static final String QR_CODE_CONTENT_TYPE = "image/png";
	
	@Override
	public void prepare() throws Exception {
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String buildQRCode() {
		
		BufferedImage biQR = ImageUtils.buildQRCode("Hello QR World!!!!!", QR_WIDTH, QR_HEIGHT, ImageUtils.QRImageFormat.PNG);
		if (biQR == null) {
			LOG.error("Error building QR image with message: " + "asdasd");
			return null;
		}

		getResponse().setContentType(QR_CODE_CONTENT_TYPE);
		try {
			OutputStream os = getResponse().getOutputStream();
			ImageIO.write(biQR, ImageUtils.QRImageFormat.PNG.getFormat(), os);
			os.flush();
		} catch (IOException ioe) {
			LOG.error(ioe.getMessage(), ioe);
		}
		
		return NONE;
	}
	
	private String buildURLLoginMsg() {
		StringBuilder sbURL = new StringBuilder();
		getRequest().getSession().getId();
		return NONE;
	}
	
}
