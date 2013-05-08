package nl.bhit.mtor.server.webapp.action;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import nl.bhit.mtor.model.User;
import nl.bhit.mtor.server.webapp.util.ImageUtils;
import nl.bhit.mtor.util.CryptographicUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class QRCodeLoginAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6583950769381263089L;
	
	private static final int QR_WIDTH = 200;
	private static final int QR_HEIGHT = 200;
	private static final String QR_CODE_CONTENT_TYPE = "image/png";
	
	private Boolean jsonQRCodeScanned;
	
	/**
	 * Builds a QR code image and send it in the response.
	 * 
	 * @return
	 */
	public String buildQRCode() {
		String encodedMsg = getQRToken();
		if (StringUtils.isBlank(encodedMsg)) {
			return null;
		}
		
		BufferedImage biQR = ImageUtils.buildQRCode(encodedMsg, QR_WIDTH, QR_HEIGHT, ImageUtils.QRImageFormat.PNG);
		if (biQR == null) {
			LOG.error("Error building QR image with message: " + encodedMsg);
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
	
	/**
	 * Check if user has scanned the QR code.
	 * 
	 * @return
	 * 			True if QR code has scanned. False otherwise.
	 */
	public String isQRScanned() {
		User user = userManager.getAndCheckQRUser(getQRToken());
		setJsonQRCodeScanned(user != null);
		
		return SUCCESS;
	}
	
	/**
	 * Log in the user when he/she has scanned the QR code.
	 * 
	 * @return
	 * 			SUCCESS if we can do the log in. FALSE if we cannot get a user with this QR token.
	 */
	public String doQRLogin() {
		User user = userManager.getAndCheckQRUser(getQRToken());
		if (user == null) {
			return CANCEL;
		}
		
		user.setQrToken("");
		userManager.save(user);
		
        // log user in automatically
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getConfirmPassword(), user.getAuthorities());
        auth.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        return SUCCESS;
	}
	
	/**
	 * Builds the QR token that will be transformed in a QR image.
	 * 
	 * @return
	 */
	private String getQRToken() {
		return CryptographicUtils.digestString(CryptographicUtils.DIGEST_ALGORITHM.SHA256, getRequest().getSession().getId());
	}
	
	/*
	 * Getters & Setters
	 */
	
	public Boolean getJsonQRCodeScanned() {
		return jsonQRCodeScanned;
	}

	public void setJsonQRCodeScanned(Boolean jsonQRCodeScanned) {
		this.jsonQRCodeScanned = jsonQRCodeScanned;
	}
	
}
