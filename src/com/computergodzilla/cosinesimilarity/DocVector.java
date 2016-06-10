
package com.computergodzilla.cosinesimilarity;

import java.util.Map;
import org.apache.commons.math.linear.OpenMapRealVector;
import org.apache.commons.math.linear.RealVectorFormat;

/**
 *
 */
public class DocVector {

    public Map<String, Integer> terms; //keeping all the terms in all docs 
    public OpenMapRealVector vector;
    public String docName; //Added by Beste
    
    public String getDocName() {
		return docName;
	}

	public Map<String, Integer> getTerms() { //added by Beste
		return terms;
	}

	public OpenMapRealVector getVector() { //added by Beste
		return vector;
	}

	public DocVector(Map<String, Integer> terms , String docName) {
        this.terms = terms;
        this.vector = new OpenMapRealVector(terms.size());   
        this.docName = docName;
    }

    public void setEntry(String term, int freq) {
        if (terms.containsKey(term)) {
            int pos = terms.get(term);
            vector.setEntry(pos, (double) freq);
        }
    }

    public void normalize() {
        double sum = vector.getL1Norm();
        vector = (OpenMapRealVector) vector.mapDivide(sum);
    }

     @Override
    	public String toString() {
        RealVectorFormat formatter = new RealVectorFormat();
        return formatter.format(vector);
    }
    
    /* @Override
	public String toString() {
    	
    String str = ",sayi:";	
    RealVectorFormat formatter = new RealVectorFormat("{","}",str);
    return formatter.format(vector);
	}*/
}

