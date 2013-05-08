package nl.bhit.mtor.server.webapp.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public final class ImageUtils {
	
	/**
	 * Formats of the QR code output.
	 * 
	 * @author miquel
	 *
	 */
	public enum QRImageFormat {
		
		JPEG("jpeg"),
		PNG("png");
		
		private String format;
		
		private QRImageFormat(String format) {
			this.format = format;
		}
		
		public String getFormat() {
			return this.format;
		}
	}
	
    /**
     * Checkstyle rule: utility classes should not have public constructor
     */
    private ImageUtils() {
    }
    
    /**
     * Builds new QR code with parameters provided.
     * 
     * @param encodedMsg			
     * 				Message that we want to encode.
     * @param width					
     * 				Width of the resulting QR image.
     * @param height				
     * 				Height of the resulting QR image.
     * @param imageFormat			
     * 				Format of the resulting QR image.
     * @return						
     * 				Image with message encoded.
     */
    public static BufferedImage buildQRCode(final String encodedMsg, final int width, final int height, final QRImageFormat imageFormat) {
    	final Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        
        final QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix;
        try {
			byteMatrix = qrCodeWriter.encode(encodedMsg, BarcodeFormat.QR_CODE, width, height, hintMap);
		} catch (WriterException e) {
			return null;
		}
        
        final BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bi.createGraphics();
        
        Graphics2D graphics = (Graphics2D)bi.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }

        return bi;
    }

}
