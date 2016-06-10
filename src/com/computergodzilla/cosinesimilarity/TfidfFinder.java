package com.computergodzilla.cosinesimilarity;

import java.io.IOException;

import org.apache.commons.math.linear.OpenMapRealVector;
import org.apache.lucene.store.LockObtainFailedException;

public class TfidfFinder {
	
	private VectorGenerator vecGen; //vector generator instance
	private static DocVector[] docVec; //document vectors array
	

	public TfidfFinder() {
		try {
			this.vecGen = new VectorGenerator();
			vecGen.GetAllTerms();
			TfidfFinder.docVec = vecGen.GetDocumentVectors(); // getting document vectors
																		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public  DocVector[] findTfIdfs() throws IOException {

		OpenMapRealVector idfVector = findIdfs(); // getting idf vector
		double allTermsCount = docVec[0].getTerms().size(); // finding all
																// terms count

		for (int i = 0; i < docVec.length; i++) {
			for (int k = 0; k < allTermsCount; k++) {

				// System.out.println("For Doc: " + i + " Term: " + k );

				// System.out.println("Vector before the change: " +
				// docVector[i]);

				// System.out.println("idf: " + idfVector.getEntry(k));
				// System.out.println("tf: " +
				// docVector[i].getVector().getEntry(k));

				double tfIdf = idfVector.getEntry(k) * docVec[i].getVector().getEntry(k);

				// System.out.println("tfIdf: " + tfIdf);

				docVec[i].getVector().setEntry(k, tfIdf);

				// System.out.println("Vector after the change: " +
				// docVector[i]);
				// System.out.println("Doc-vec changed: " +
				// docVector[i].getVector().getEntry(k));
			}
			
			//docVec[i].normalize();

		}
		return docVec;

	}

	public static OpenMapRealVector findIdfs() throws LockObtainFailedException, IOException {

		// System.out.println(docVector[0]);
		// System.out.println(docVector[1]);
		// System.out.println(docVector[2]);
		// System.out.println(docVector[3]);

		double allTermsCount = docVec[0].getTerms().size();

		OpenMapRealVector idfVector = new OpenMapRealVector((int) allTermsCount);

		double zero = 0;

		for (int k = 0; k < docVec[0].getTerms().size(); k++) {  //checking how many documents have that term

			double count = 0;

			for (int i = 0; i < docVec.length; i++) {

				double checkIfNotZero = docVec[i].getVector().getEntry(k);
				
				// System.out.println(checkIfNotZero);

				if (checkIfNotZero != zero) {
					count++;
				}

			}

			// System.out.println("count: " + count);
			// System.out.println("length: " + (double) docVector.length);
			// System.out.println(1 + Math.log((double) docVector.length /
			// count));

			double result = 1 + Math.log((double) docVec.length / count);

			idfVector.setEntry(k, (double) result);
			// System.out.println("idf: " + idfVector.getEntry(k));

			//do I need to normalize idfs vector?
			
			
		}

		/*for(int i=0; i<idfVector.getDimension(); i++){
			
			System.out.println("Before normalize"+ i +":");
			System.out.println(idfVector.getEntry(i));
			
		}*/
		
		//idfVector= normalize(idfVector);
		
		/*for(int i=0; i<idfVector.getDimension(); i++){
			
			System.out.println("After normalize"+ i +":");
			System.out.println(idfVector.getEntry(i));
			
		}*/
		
		System.out.println("stop");
		return idfVector;

	}
	
	 public static OpenMapRealVector normalize(OpenMapRealVector vec) {
	        double sum = vec.getL1Norm();
	        vec = (OpenMapRealVector) vec.mapDivide(sum);
	        return vec;
	    }

}
