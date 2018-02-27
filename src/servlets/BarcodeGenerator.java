package servlets;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BarcodeGenerator
 */
@WebServlet("/BarcodeGenerator")
public class BarcodeGenerator extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BarcodeGenerator() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		executeServlet(request, response);
	}

	private void executeServlet(HttpServletRequest request,
			HttpServletResponse response) {
		
		
		
		
		
		
//		File img = new File(getServletContext().getRealPath("images/download.png"));
//		BufferedImage buffImg = new BufferedImage(390, 390,
//				BufferedImage.TYPE_INT_ARGB);
//		try {
//			buffImg = ImageIO.read(img);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		response.setContentType("image/png");
//		BufferedImage bi = getQRCodeWithOverlay(buffImg);
//		OutputStream out;
//		try {
//			out = response.getOutputStream();
//			ImageIO.write(bi, "png", out);
//			out.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	public static BufferedImage getQRCodeWithOverlay(BufferedImage qrcode) {
		BufferedImage scaledOverlay = scaleOverlay(qrcode);

		Integer deltaHeight = qrcode.getHeight() - scaledOverlay.getHeight();
		Integer deltaWidth = qrcode.getWidth() - scaledOverlay.getWidth();

		BufferedImage combined = new BufferedImage(qrcode.getWidth(),
				qrcode.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) combined.getGraphics();
		g2.drawImage(qrcode, 0, 0, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		g2.drawImage(scaledOverlay, Math.round(deltaWidth / 2),
				Math.round(deltaHeight / 2), null);
		return combined;
	}

	private static BufferedImage scaleOverlay(BufferedImage qrcode) {
		Integer scaledWidth = Math.round(qrcode.getWidth() * 2);
		Integer scaledHeight = Math.round(qrcode.getHeight() * 2);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		File f = new File("c:/64.png");
		System.out.println(f.exists());
		Image image = toolkit.getImage(f.getAbsolutePath());
		BufferedImage imageBuff = new BufferedImage(scaledWidth, scaledHeight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = imageBuff.createGraphics();
		g.drawImage(image.getScaledInstance(scaledWidth, scaledHeight,
				BufferedImage.SCALE_SMOOTH), 11, 11, new Color(0, 0, 0), null);
		g.dispose();
		return imageBuff;
	}

	public static void main(String[] args) {
		File img = new File("WebContent/images/download.png");
		BufferedImage buffImg = new BufferedImage(390, 390,
				BufferedImage.TYPE_INT_ARGB);
		try {
			buffImg = ImageIO.read(img);
			getQRCodeWithOverlay(buffImg);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		executeServlet(request, response);
	}

}
