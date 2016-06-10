package com.computergodzilla.cosinesimilarity;

import java.io.IOException;

import org.apache.commons.math.linear.OpenMapRealVector;

import org.apache.lucene.store.LockObtainFailedException;

public class Test {
    
    public static void main(String[] args) throws LockObtainFailedException, IOException
    {
    	long startTime = System.currentTimeMillis();
    	indexAllDocs();
    	
    	TfidfWriter.writeTfIdfsToFile();
    	long estimatedTime = System.currentTimeMillis() - startTime;
    	
    	System.out.println("Estimated time: " + estimatedTime);
        //getCosineSimilarity();
    }
    
    
    public static void normalize(OpenMapRealVector idfVector) {
        double sum = idfVector.getL1Norm();
        idfVector = (OpenMapRealVector) idfVector.mapDivide(sum);
    }
       
    
    public static void indexAllDocs() throws LockObtainFailedException, IOException
    {
    	Indexer index = new Indexer();
        index.index();
    }
}