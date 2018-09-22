package comparepdf;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfManager {

    private PDFParser parser;
    private PDFTextStripper pdfStripper;
    private PDDocument pdDoc;
    private COSDocument cosDoc;

    private String Text;
    private String filePath;
    private File file;

    public PdfManager() {
        this.pdfStripper = null;
        this.pdDoc = null;
        this.cosDoc = null;
    }

    public String ToText() throws IOException {
        //System.out.println(file.getAbsolutePath());
        pdDoc = PDDocument.load(file);
        pdfStripper = new PDFTextStripper();            
        
        pdDoc.getNumberOfPages();
        pdfStripper.setStartPage(1);
        pdfStripper.setEndPage(pdDoc.getNumberOfPages());
        Text = pdfStripper.getText(pdDoc); //text file => string
        pdDoc.close();
        return Text; //returns contents of the file
    }
    
    public String ToText(File f) throws IOException {
        pdDoc = PDDocument.load(f.getAbsoluteFile());
        pdfStripper = new PDFTextStripper();
        
        pdDoc.getNumberOfPages();
        pdfStripper.setStartPage(1);
        pdfStripper.setEndPage(pdDoc.getNumberOfPages());
        Text = pdfStripper.getText(pdDoc); //text file => string
        //pdDoc.close();
        return Text; //returns contents of the file
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public void removePage(int n)
    {
        try {   
            file = new File(filePath);
            PDDocument document = PDDocument.load(file);
            document.removePage(0);
            document.save("./PDF/new.pdf");
            document.close();
        } catch (IOException ex) {
            Logger.getLogger(PdfManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
