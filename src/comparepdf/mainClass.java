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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;

public class mainClass 
{
    // String Metric Algorithms
    // Longest Common Subsequence
    public static void main(String[] args) throws PrinterException 
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE); 
                JFrame frame = new JFrame("FileChooserDemo");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                //Add content to the window.
                frame.add(new FileChooserDemo());

                //Display the window.
                frame.pack();
                frame.setVisible(true);
            }
        });
           
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter the name of the PDF: ");
        String s = reader.next(); 
        System.out.println("Enter the name of the PDF 2: ");
        String s2 = reader.next(); 
        reader.close();
       
        PdfManager pdfmanager = new PdfManager();
        pdfmanager.setFilePath("./PDF/" + s + ".pdf");
        
        PdfManager pdfmanager2 = new PdfManager();
        pdfmanager2.setFilePath("./PDF/" + s2 + ".pdf");
        
        try 
        {
            File f = new File("./PDF/" + s + ".txt");
            String textFromFile = pdfmanager.ToText();
            
            File f2 = new File("./PDF/" + s2 + ".txt");
            String textFromFile2 = pdfmanager2.ToText();
            
            Document report1 = new Document(textFromFile, f);
            report1.createFrequencyTable();
            report1.sortByComparator();   
            
            Document report2 = new Document(textFromFile2, f2);
            report2.createFrequencyTable();
            report2.sortByComparator();  
            
            String[] wordArr1 = report1.getWordArray();
            String[] wordArr2 = report2.getWordArray();
            int[][] LCSmatrix = report1.LCSLength(wordArr1, wordArr2);
            System.out.println(report1.printLCS(LCSmatrix, wordArr1, wordArr2, wordArr1.length - 1, wordArr2.length - 1));
            
            
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
    
    public static class FileChooserDemo extends JPanel implements ActionListener {
    
    static private final String newline = "\n";
    JButton openButton, saveButton;
    JTextArea log;
    JFileChooser fc;
 
    public FileChooserDemo() {
        super(new BorderLayout());
 
        //Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
 
        //Create a file chooser
        fc = new JFileChooser();
 
        //Uncomment one of the following lines to try a different
        //file selection mode.  The first allows just directories
        //to be selected (and, at least in the Java look and feel,
        //shown).  The second allows both files and directories
        //to be selected.  If you leave these lines commented out,
        //then the default mode (FILES_ONLY) will be used.
        //
        //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
 
        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        openButton = new JButton("Open a File...",
                                 createImageIcon("images/Open16.gif"));
        openButton.addActionListener(this);
 
        //Create the save button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        saveButton = new JButton("Save a File...",
                                 createImageIcon("images/Save16.gif"));
        saveButton.addActionListener(this);
 
        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);
 
        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }
 
    public void actionPerformed(ActionEvent e) {
 
        //Handle open button action.
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(FileChooserDemo.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {             
                PdfManager pdfmanager = new PdfManager();

                try 
                {
                    File f = fc.getSelectedFile();
                    log.append("Opening: " + f.getName() + newline);
                    String textFromFile = pdfmanager.ToText(f);

                    Document report1 = new Document(textFromFile, f);
                    report1.createFrequencyTable();
                    report1.sortByComparator();   

                    String[] wordArr1 = report1.getWordArray();
                    String[] wordArr2 = report1.getWordArray();
                    int[][] LCSmatrix = report1.LCSLength(wordArr1, wordArr2);
                    System.out.println(report1.printLCS(LCSmatrix, wordArr1, wordArr2, wordArr1.length - 1, wordArr2.length - 1));

                    printJTable(report1.getMap());
                } 
                catch (IOException | PrinterException ex) 
                {
                    log.append("Failed to open file" + newline);
                    Logger.getLogger(mainClass.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
 
        //Handle save button action.
        } else if (e.getSource() == saveButton) {
            int returnVal = fc.showSaveDialog(FileChooserDemo.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would save the file.
                log.append("Saving: " + file.getName() + "." + newline);
            } else {
                log.append("Save command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        }
    }
 
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = FileChooserDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }  
}
    
}

