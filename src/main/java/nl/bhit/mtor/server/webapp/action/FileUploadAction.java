package nl.bhit.mtor.server.webapp.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nl.bhit.mtor.Constants;

import org.apache.commons.io.IOUtils;
import org.apache.struts2.ServletActionContext;

/**
 * Sample action that shows how to do file upload with Struts 2.
 */
public class FileUploadAction extends BaseAction {
	
    private static final long serialVersionUID = -9208910183310010569L;
    
    private static final int BYTE_CHUNK_LENGTH = 8192;
    private static final int MAX_FILE_LENGTH = 2097152;
    
    private File file;
    private String fileContentType;
    private String fileFileName;
    private String name;

    /**
     * Upload the file
     * 
     * @return String with result (cancel, input or sucess)
     * @throws IOException
     *             if something goes wrong
     */
    public String upload() throws IOException{
        if (this.cancel != null) {
            return "cancel";
        }

        // the directory to upload to
        String uploadDir = ServletActionContext.getServletContext().getRealPath("/resources") + "/"
                + getRequest().getRemoteUser() + "/";

        // write the file to the file specified
        File dirPath = new File(uploadDir);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
        
        InputStream stream = null;
        OutputStream bos = null;
        try {
	        // retrieve the file data
	    	stream = new FileInputStream(file);
	        // write the file to the file specified
	    	bos = new FileOutputStream(uploadDir + fileFileName);
	        int bytesRead;
	        byte[] buffer = new byte[BYTE_CHUNK_LENGTH];
	        while ((bytesRead = stream.read(buffer, 0, BYTE_CHUNK_LENGTH)) != -1) {
	            bos.write(buffer, 0, bytesRead);
	        }
        } finally {
        	IOUtils.closeQuietly(bos);
        	IOUtils.closeQuietly(stream);
        }
        
        // place the data into the request for retrieval on next page
        getRequest().setAttribute("location", dirPath.getAbsolutePath() + Constants.FILE_SEP + fileFileName);

        String link = getRequest().getContextPath() + "/resources" + "/" + getRequest().getRemoteUser() + "/";

        getRequest().setAttribute("link", link + fileFileName);

        return SUCCESS;
    }

    /**
     * Default method - returns "input"
     * 
     * @return "input"
     */
    @Override
    public String execute() {
        return INPUT;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public String getFileFileName() {
        return fileFileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        if (getRequest().getMethod().equalsIgnoreCase("post")) {
            getFieldErrors().clear();
            if ("".equals(fileFileName) || file == null) {
                super.addFieldError("file", getText("errors.requiredField", new String[] {
                        getText("uploadForm.file") }));
            } else if (file.length() > MAX_FILE_LENGTH) {
                addActionError(getText("maxLengthExceeded"));
            }
        }
    }
}
