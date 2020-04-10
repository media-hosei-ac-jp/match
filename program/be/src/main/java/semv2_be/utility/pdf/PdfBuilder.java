package jp.co.sogeninc.semv2_be.utility.pdf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;

/**
 * PDF作成クラス
 * TODO 未使用
 */
@Service
public class PdfBuilder {

//	@Autowired
//	ExternalizedConfig config;
//
//	/**
//	 * HTMLよりPDF作成
//	 *
//	 * @param htmlStr:
//	 *            HTML形式の文字列
//	 *
//	 * @return 作成したpdfファイルをBase64で
//	 */
//	public String createFromHTML(String htmlStr) {
//
//		String baseIndata = "";
//		try {
//
//			createTemp();
//
//			// 一時フォルダに空のPDFファイル作成
//			Date now = new Date();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//			String fileName = config.getTempFolder() + sdf.format(now) + ".pdf";
//			File file = new File(fileName);
//			java.io.OutputStream oT = new FileOutputStream(file);
//
//			Document doc = new Document(PageSize.A4, 20, 20, 0, 0);
//			PdfWriter writer = PdfWriter.getInstance(doc, oT);
//			// Font font = new Font(BaseFont.createFont("HeiseiMin-W3",
//			// "UniJIS-UCS2-HW-H", false), 15, Font.BOLD);
//
//			// HTMLよりpdf編集
//			doc.open();
//
//			String fontname = config.getJapaneseFontName();
//			XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider();
//			fontProvider.register(fontname, "Serif");
//			String cssText = "body {font-family: Serif;}";
//
//			java.io.InputStream is = new ByteArrayInputStream(htmlStr.getBytes());
//			java.io.InputStream cssIs = new ByteArrayInputStream(cssText.getBytes());
//			XMLWorkerHelper.getInstance().parseXHtml(writer, doc, is, cssIs, Charset.forName("UTF-8"), fontProvider);
//			doc.close();
//			oT.close();
//			writer.close();
//
//			// PDFファイルをBase64にエンコード
//			File inf = new File(fileName);
//			FileInputStream fi = new FileInputStream(inf);
//			byte[] indata = new byte[(int) inf.length()];
//			fi.read(indata);
//			fi.close();
//
//			// // フォルダ削除
//			// folderAlldelete(inf);
//
//			// ==================================//
//			// Base64に //
//			// ==================================//
//			baseIndata = Base64.encodeBytes(indata);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return baseIndata;
//	}
//
//	/**
//	 * HTMLよりPDF作成（複数ページ）
//	 *
//	 * @param htmlStr:
//	 *            HTML形式の文字列
//	 *
//	 * @return 作成したpdfファイルをBase64で
//	 */
//	public String createFromHTML(List<String> htmlStr) {
//
//		String baseIndata = "";
//		try {
//
//			createTemp();
//
//			// 一時フォルダに空のPDFファイル作成
//			Date now = new Date();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//			String fileName = config.getTempFolder() + sdf.format(now) + ".pdf";
//			File file = new File(fileName);
//			java.io.OutputStream oT = new FileOutputStream(file);
//
//			Document doc = new Document(PageSize.A4, 20, 20, 0, 0);
//			PdfWriter writer = PdfWriter.getInstance(doc, oT);
//			// Font font = new Font(BaseFont.createFont("HeiseiMin-W3",
//			// "UniJIS-UCS2-HW-H", false), 15, Font.BOLD);
//
//			// HTMLよりpdf編集
//			doc.open();
//
//			String fontname = config.getJapaneseFontName();
//			XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider();
//			fontProvider.register(fontname, "Serif");
//			String cssText = "body {font-family: Serif;}";
//
//			// 1つのhtmlStrでページ分割
//			for (String html : htmlStr) {
//				java.io.InputStream is = new ByteArrayInputStream(html.getBytes());
//				java.io.InputStream cssIs = new ByteArrayInputStream(cssText.getBytes());
//				XMLWorkerHelper.getInstance().parseXHtml(writer, doc, is, cssIs, Charset.forName("UTF-8"),
//						fontProvider);
//				doc.newPage();
//			}
//			doc.close();
//			oT.close();
//			writer.close();
//
//			// PDFファイルをBase64にエンコード
//			File inf = new File(fileName);
//			FileInputStream fi = new FileInputStream(inf);
//			byte[] indata = new byte[(int) inf.length()];
//			fi.read(indata);
//			fi.close();
//
//			 // フォルダ削除
//			 folderAlldelete(inf);
//
//			// ==================================//
//			// Base64に //
//			// ==================================//
//			baseIndata = Base64.encodeBytes(indata);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return baseIndata;
//	}
//
//	/**
//	 * 一時フォルダ作成
//	 */
//	private void createTemp() {
//		try {
//			File folder = new File(config.getTempFolder());
//			if (!folder.exists()) {
//				folder.mkdir();
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	// /**
//	// * 一時フォルダ削除
//	// */
//	// private void deleteTemp() {
//	// try {
//	// File folder = new File(tempFolder);
//	// if (folder.exists()) {
//	// folder.deleteOnExit();
//	// }
//	//
//	// } catch (Exception e) {
//	// e.printStackTrace();
//	// }
//	// }
//
//	/*
//	 * ファイルおよびディレクトリを削除する
//	 */
//	private void folderAlldelete(File f) {
//		/*
//		 * ファイルまたはディレクトリが存在しない場合は何もしない
//		 */
//		if (f.exists() == false) {
//			return;
//		}
//
//		if (f.isFile()) {
//			/*
//			 * ファイルの場合は削除する
//			 */
//			f.delete();
//
//		} else if (f.isDirectory()) {
//			/*
//			 * ディレクトリの場合は、すべてのファイルを削除する
//			 */
//
//			/*
//			 * 対象ディレクトリ内のファイルおよびディレクトリの一覧を取得
//			 */
//			File[] files = f.listFiles();
//
//			/*
//			 * ファイルおよびディレクトリをすべて削除
//			 */
//			for (int i = 0; i < files.length; i++) {
//				/*
//				 * 自身をコールし、再帰的に削除する
//				 */
//				folderAlldelete(files[i]);
//			}
//			/*
//			 * 自ディレクトリを削除する
//			 */
//			f.delete();
//		}
//	}
//
//	// private void encodeBase64(String inFileName, String outFileName) {
//	// try {
//	// // ==================================//
//	// // Base64エンコードファイルを読み込む//
//	// // ==================================//
//	// File inf = new File(inFileName);
//	// FileInputStream fi = new FileInputStream(inf);
//	// byte[] indata = new byte[(int) inf.length()];
//	// fi.read(indata);
//	// fi.close();
//	//
//	// // ==================================//
//	// // Base64園コード //
//	// // ==================================//
//	// String baseIndata = Base64.encodeBytes(indata);
//	// // byte[] baseIndata =
//	// // org.apache.commons.codec.binary.Base64.encodeBase64(indata);
//	//
//	// // ==================================//
//	// // Base64デコード //
//	// // ==================================//
//	// byte[] outdata = Base64.decode(baseIndata);
//	// // byte[] outdata =
//	// // org.apache.commons.codec.binary.Base64.decodeBase64(baseIndata);
//	//
//	// // ==================================//
//	// // 結果書き出し //
//	// // ==================================//
//	// File outf = new File(outFileName);
//	// FileOutputStream fo = new FileOutputStream(outf);
//	// fo.write(outdata);
//	// fo.close();
//	// PdfWriter pdfwriter = PdfWriter.getInstance(new Document(PageSize.A4, 50,
//	// 50, 50, 50), fo);
//	// pdfwriter.close();
//	// } catch (Exception e) {
//	// e.printStackTrace();
//	// }
//	// }

}
