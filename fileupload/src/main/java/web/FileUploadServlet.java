package web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.log4j.Logger;

@WebServlet("/fileupload")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 205242440643911308L;
	final static Logger LOG = Logger.getLogger(FileUploadServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// String applicationPath = request.getServletContext().getRealPath("");
		// //applicationPath + File.separator + UPLOAD_DIR;
		// String uploadFilePath = applicationPath + File.separator +
		// UPLOAD_DIR;

		InputStream input = getServletContext().getResourceAsStream("/WEB-INF/app.properties");
		Properties props = new Properties();
		props.load(input);
		String uploaddir = props.getProperty("upload.dir");

		LOG.info("upload dir property: " + uploaddir);
		
		File fileuploaddir = new File(uploaddir);
		if (!fileuploaddir.exists()) {
			fileuploaddir.mkdirs();
		}
		LOG.info("upload file directory: " + fileuploaddir.getAbsolutePath());

		String fileName = null;
		for (Part part : request.getParts()) {
			fileName = getFileName(part);
			part.write(uploaddir + File.separator + fileName);
		}

		request.setAttribute("message", fileName + " File uploaded successfully!");
		getServletContext().getRequestDispatcher("/response.jsp").forward(request, response);
	}

	private String getFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		System.out.println("content-disposition header= " + contentDisp);
		String[] tokens = contentDisp.split(";");
		for (String token : tokens) {
			if (token.trim().startsWith("filename")) {
				return token.substring(token.indexOf("=") + 2, token.length() - 1);
			}
		}
		return "";
	}
}