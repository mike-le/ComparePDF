package comparepdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Document 
{
    private HashMap<String, Integer> frequencyTable;
    private Map<String, Integer> sortedTable;
    private File file;
    private String filePath;
    private int numberofWords;
    
    public Document(String fileContents, File targetFile) throws FileNotFoundException 
    {
        frequencyTable = new HashMap<String, Integer>();
        file = targetFile;
        filePath = targetFile.getPath();
        PrintWriter writer = new PrintWriter(targetFile.getPath());
        Pattern p = Pattern.compile("[\\w']+");
        Matcher m = p.matcher(fileContents);
        while (m.find()) 
        {
            String trimmedWord = fileContents.substring(m.start(), m.end());
            if (!trimmedWord.contains("_") && trimmedWord.length() > 1) 
            {
                writer.print(trimmedWord + " ");
            }
        }
        writer.close();
    }
    
    public void createFrequencyTable() throws FileNotFoundException {
        Scanner s = new Scanner(file);
        while (s.hasNext()) {
            String nextWord = s.next().toLowerCase();
            if (containsNumber(nextWord) != true) {
                if (frequencyTable.containsKey(nextWord)) {
                    frequencyTable.replace(nextWord, frequencyTable.get(nextWord) + 1);
                } else {
                    frequencyTable.put(nextWord, 1);
                }
            }
        }
        s.close();
    }
    
    public Map<String, Integer> getMap()
    {
        return sortedTable;
    }
    
    public int getNumberOfWords()
    {
        return numberofWords;
    }
    
    public double[] compareIndex(Document oldIndex, Document newIndex)
    {
        double newWords = 0;
        double deletedWords = 0;
        Map<String, Integer> oldEntry = oldIndex.getMap();
        Map<String, Integer> newEntry = newIndex.getMap();
        
        for (Map.Entry<String, Integer> pdf1 : oldEntry.entrySet())
        {
            for(Map.Entry<String, Integer> pdf2 : newEntry.entrySet())
            {
                if(pdf1.getKey() == pdf2.getKey())
                {
                    newWords = newWords + (double) pdf2.getValue() - (double) pdf1.getValue();
                    oldEntry.remove(pdf1.getKey());
                    newEntry.remove(pdf2.getKey());
                }
            }
        }
        
        for (Map.Entry<String, Integer> addedWords : newEntry.entrySet())
        {
            newWords = newWords + (double) addedWords.getValue();
        }
        
        for (Map.Entry<String, Integer> removedWords : oldEntry.entrySet())
        {
            deletedWords = deletedWords + (double) removedWords.getValue();
        }
        double[] temp = {newWords, deletedWords};
        return temp;
    }
    
    private static boolean containsNumber(String s) throws FileNotFoundException
    {
        if (s.contains("0") || s.contains("1") || s.contains("2") || s.contains("3")
                || s.contains("4") || s.contains("5") || s.contains("6") || s.contains("7") || s.contains("8")
                || s.contains("9")) 
        {
            return true;
        }
        return false;
    }
    
    public void sortByComparator()
    {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(frequencyTable.entrySet());
        
        //Sort the list based on values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
            {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        
        //Maintaining insertion order with the help of a LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list)
        {
            numberofWords = numberofWords + entry.getValue();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        
        this.sortedTable = sortedMap;       
    }
    
}
