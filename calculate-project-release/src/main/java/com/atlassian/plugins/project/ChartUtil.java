//package com.atlassian.plugins.project;
//
//import java.awt.Graphics;
//import java.awt.image.BufferedImage;
//import java.io.BufferedOutputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//
//import javax.imageio.ImageIO;
//import javax.servlet.http.HttpSession;
//import javax.swing.JProgressBar;
//import javax.xml.bind.DatatypeConverter;
//
//import net.sf.hibernate.Hibernate;
//
//import org.jfree.chart.ChartRenderingInfo;
//import org.jfree.chart.ChartUtilities;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.encoders.EncoderUtil;
//import org.jfree.chart.encoders.ImageFormat;
//import org.jfree.chart.plot.CategoryPlot;
//import org.jfree.util.Log;
//
//import com.atlassian.jira.charts.jfreechart.ChartHelper;
//
//public class ChartUtil {
//
//	
//	
//	public static String saveChartAsPNG(JProgressBar chart, int width,
//			int height, ChartRenderingInfo info, HttpSession session)
//			throws IOException {
//
//		if (chart == null) {
//			throw new IllegalArgumentException("Null 'chart' argument.");
//		}
//		String tempDirName = System.getProperty("java.io.tmpdir");
//		if (tempDirName == null) {
//			throw new RuntimeException("Temporary directory system property "
//					+ "(java.io.tmpdir) is null.");
//		}
//
//		File tempDir = new File(tempDirName);
//		if (!tempDir.exists()) {
//			tempDir.mkdirs();
//		}
//
//		String prefix = "jfreechart-";
//		if (session == null) {
//			prefix = "jfreechart-onetime-";
//		}
//		File tempFile = File.createTempFile(prefix, ".png", tempDir);
//
//		saveChartAsPNG(tempFile, chart, width, height, info);
//
//		BufferedImage image = ImageIO.read(tempFile);
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		ImageIO.write(image, "png", baos);
//		byte[] res = baos.toByteArray();
//		return "data:image/png;base64,"
//				+ DatatypeConverter.printBase64Binary(res);
//	}
//	
//	
//	public static void saveChartAsPNG(File file, JProgressBar chart,
//            int width, int height, ChartRenderingInfo info)
//        throws IOException {
//
//        if (file == null) {
//            throw new IllegalArgumentException("Null 'file' argument.");
//        }
//        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
//        try {
//            writeChartAsPNG(out, chart, width, height, info);
//        }
//        finally {
//            out.close();
//        }
//    }
//	  public static void writeChartAsPNG(OutputStream out, JProgressBar chart,
//	            int width, int height,  ChartRenderingInfo info)
//	            throws IOException {
//
//	        if (chart == null) {
//	            throw new IllegalArgumentException("Null 'chart' argument.");
//	        }
//	        int w = chart.getSize().width;
//	        int h = chart.getSize().height;
//Log.error("------"+w+"-----"+h+"======"+chart.getX()+"======"+chart.getY());
//
//	        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//	        Graphics g = bufferedImage.getGraphics();
//	        g.drawRect(chart.getX(), chart.getY(), width, height);
//	       /* BufferedImage bufferedImage
//	                = chart.createBufferedImage(width, height, info);*/
//	        EncoderUtil.writeBufferedImage(bufferedImage, ImageFormat.PNG, out);
//	    }
//	
//}
