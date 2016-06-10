package com.computergodzilla.cosinesimilarity;

import java.io.IOException;

import org.apache.lucene.store.LockObtainFailedException;

/**
 * Class to calculate cosine similarity
 */
public class CosineSimilarity {    
	
    public double getCosineSimilarity(DocVector d1,DocVector d2) {
        double cosinesimilarity;
        try {
            cosinesimilarity = (d1.vector.dotProduct(d2.vector))
                    / (d1.vector.getNorm() * d2.vector.getNorm());
        } catch (Exception e) {
            return 0.0;
        }
        return cosinesimilarity;
    }
	
	
    public void writeCosineSimilarity() throws LockObtainFailedException, IOException
    {
       
       VectorGenerator vectorGenerator = new VectorGenerator();
       vectorGenerator.GetAllTerms();       
       DocVector[] docVector = vectorGenerator.GetDocumentVectors(); // getting document vectors
       for(int i = 0; i < docVector.length; i++)
       {
           double cosineSimilarity = getCosineSimilarity(docVector[0], docVector[i]);
           System.out.println("Cosine Similarity Score between document 0 and "+i+"  = " + cosineSimilarity);
       }    
       
       
    }
    
    
    
    
}