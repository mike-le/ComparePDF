package comparepdf;

import java.awt.BorderLayout;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level; //added
import java.util.logging.Logger; //added
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class mainClass 
{
    public static void main(String[] args) throws PrinterException 
    {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter the name of the PDF: ");
        String s = reader.next(); 
        System.out.println("Enter the name of the PDF 2: ");
        String s2 = reader.next(); 
        reader.close();
        
        PdfManager pdfmanager = new PdfManager();
        pdfmanager.setFilePath("/Users/Michael/Desktop/test/" + s);
        
        PdfManager pdfmanager2 = new PdfManager();
        pdfmanager2.setFilePath("/Users/Michael/Desktop/test/" + s2);
        
        try 
        {
            File f = new File("/Users/Michael/Desktop/test/" + s + ".txt");
            String textFromFile = pdfmanager.ToText();
            
            File f2 = new File("/Users/Michael/Desktop/test/" + s2 + ".txt");
            String textFromFile2 = pdfmanager2.ToText();
            
            Document report1 = new Document(textFromFile, f);
            report1.createFrequencyTable();
            report1.sortByComparator();       
            
            Document report2 = new Document(textFromFile2, f2);
            report2.createFrequencyTable();
            report2.sortByComparator();  
            
            double[] freqIndex = report2.compareIndex(report1, report2);
            System.out.println("New words added: " + freqIndex[0]);
            System.out.println("Deleted words: " + freqIndex[1]);
            
            printJTable(report1.getMap());
            printJTable(report2.getMap());
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(mainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  

    private static void printTable(Map<String,Integer> map)
    {
        for (String word : map.keySet()) 
        {
            String key = word.toString();
            String value = map.get(word).toString();
            System.out.println(key + ": " + value);
        }
    }
    
    private static void printJTable(Map<String, Integer> map) throws PrinterException
    {
        String[] columnNames = {"Word", "Frequency"};
        Object[][] tableData = new Object[map.keySet().size()][2];

        int index = 0;
        for (String word : map.keySet()) 
        {
            int frequency = map.get(word);
            tableData[index][0] = word;
            tableData[index][1] = frequency;
            index++;
        }
        
        JTable table = new JTable(tableData, columnNames);
        
        JFrame frame = new JFrame("JTable Test Display");

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JScrollPane tableContainer = new JScrollPane(table);

        panel.add(tableContainer, BorderLayout.CENTER);
        frame.getContentPane().add(panel);

        frame.pack();
        frame.setVisible(true);
    }
    
}

