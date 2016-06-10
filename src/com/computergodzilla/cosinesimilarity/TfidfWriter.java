package com.computergodzilla.cosinesimilarity;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map.Entry;

public class TfidfWriter {
	
	 public static void writeTfIdfsToFile() throws IOException{
	    	
		 	TfidfFinder tfidfFinder = new TfidfFinder();
		 
	    	DocVector[] docVector = tfidfFinder.findTfIdfs();
	    	
	    	Writer writer = new BufferedWriter(new OutputStreamWriter(
		              new FileOutputStream("denemeTfIdf.txt", false), "utf-8"));
	    	
	    	System.out.println( "Total Word Count: "+ AllTerms.getAllTerms().size());
	    	
	    	
	    	//remove comment out lines for seeing the terms on the console 
	    	
	    	  /*for(Entry<String,Integer> s : AllTerms.getAllTerms().entrySet())
	          {        
	             System.out.println(s.getKey());  //writing all the terms to the console
	          }*/
	    	  	    	
	    	for(int i = 0; i < docVector.length; i++)
	        {
	    	   writer.write(docVector[i].getDocName());  //writing doc name
	    	   writer.write(System.getProperty( "line.separator" ));
	    	   writer.write(docVector[i].toString());  //writing tfidfs
	    	   writer.write(System.getProperty( "line.separator" ));
	        }
	    	
	    	 writer.flush();
	    	 writer.close();
	    }

}
