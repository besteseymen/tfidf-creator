// Indexer.java
package com.computergodzilla.cosinesimilarity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.commongrams.CommonGramsFilter;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.hunspell.Dictionary;
import org.apache.lucene.analysis.hunspell.HunspellStemFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * Class to create Lucene Index from files. Remember this class will only index
 * files inside a folder. If there are multiple folder inside the source folder
 * it will not index those files.
 * 
 * It will only index text files
 */
public class Indexer {

	private final File sourceDirectory;
	private final File indexDirectory;
	private static String fieldName;

	public Indexer() {
		this.sourceDirectory = new File(Configuration.SOURCE_DIRECTORY_TO_INDEX);
		this.indexDirectory = new File(Configuration.INDEX_DIRECTORY);
		fieldName = Configuration.FIELD_CONTENT;
	}

	/**
	 * Method to create Lucene Index Keep in mind that always index text value
	 * to Lucene for calculating Cosine Similarity. You have to generate tokens,
	 * terms and their frequencies and store them in the Lucene Index.
	 * 
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 * @throws IOException
	 */
	public void index() throws CorruptIndexException, LockObtainFailedException, IOException {
		Directory dir = FSDirectory.open(indexDirectory);
				
		//Analyzer analyzer = new StartsWithCapitalAnalyzer();
				
		 Analyzer analyzer = new Analyzer() {
			@Override
			  protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
			    final StandardTokenizer src = new StandardTokenizer(reader);
			    TokenStream tok = new StandardFilter(src);
			    tok = new LowerCaseFilter( tok);
			    tok = new StopFilter( tok, StandardAnalyzer.STOP_WORDS_SET);
			    //tok = new PorterStemFilter(tok);
			    tok = new KStemFilter(tok);
			    //tok = new SnowballFilter(tok, "English");
			   
			    InputStream aff;
			    InputStream dic;
			    Dictionary dictionary = null;
				try {
					aff = new FileInputStream(new File("/Users/beste/Desktop/en_US/en_US.aff"));
					dic = new FileInputStream(new File("/Users/beste/Desktop/en_US/en_US.dic"));
					dictionary = new Dictionary(aff, dic);
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
		        
			      //tok = new HunspellStemFilter(tok, dictionary);
			    
			    return new TokenStreamComponents(src, tok);
			  } 
			 };
		
		//Analyzer analyzer = new StandardAnalyzer(StandardAnalyzer.STOP_WORDS_SET); // using stop words
		//Analyzer analyzer = new EnglishAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);

		if (indexDirectory.exists()) {
			iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		} else {
			// Add new documents to an existing index:
			iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		}

		IndexWriter writer = new IndexWriter(dir, iwc);
		
		int counter=0;

		for (File f : sourceDirectory.listFiles()) {
			
			counter++;   //checking for different files with diff encoding
			//System.out.println(counter);
			//System.out.println(f.getName().toString());

			if (f.getName().toString().equalsIgnoreCase(".DS_Store")) { // control for hidden file in macOS

				System.out.println(f.getName().toString() + " is not using");
			}

			if (f.getName().toString().equalsIgnoreCase(".")) { // control for hidden file in macOS

				System.out.println(f.getName().toString() + " is not using");
			}
			
			if (f.getName().toString().equalsIgnoreCase("..")) { // control for hidden file in macOS

				System.out.println(f.getName().toString() + " is not using");
			}
			else {
				Document doc = new Document();
				FieldType fieldType = new FieldType();
				fieldType.setIndexed(true);
				fieldType.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
				fieldType.setStored(true);
				fieldType.setStoreTermVectors(true);
				fieldType.setTokenized(true);
				Field contentField = new Field(fieldName, getAllText(f), fieldType);
				doc.add(contentField);

				// Added by Beste
				doc.add(new StringField("docName", f.getName().toString(), Field.Store.YES));  
				//add file name to the index

				writer.addDocument(doc);
			}

		}
		writer.close();
	}

	/**
	 * Method to get all the texts of the text file. Lucene cannot create the
	 * term vectors and tokens for reader class. You have to index its text
	 * values to the index. It would be more better if this was in another
	 * class. I am lazy you know all.
	 * 
	 * @param f
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String getAllText(File f) throws FileNotFoundException, IOException {
		String textFileContent = "";
	

		for (String line : Files.readAllLines(Paths.get(f.getAbsolutePath()))) {
			textFileContent += line;
		}
		return textFileContent;
	}

}