package app.process;

import java.util.*;
import org.apache.log4j.Logger;

import app.App;
import app.helper.PropertiesCache;

import java.io.*;
import java.nio.file.Path;
import gate.*;
import gate.util.*;
import gate.util.persistence.PersistenceManager;
import gate.corpora.RepositioningInfo;

public class AnnieStandAlone {
//
//	/** The Corpus Pipeline application to contain ANNIE */
//	private CorpusController annieController;

	final static Logger LOG = Logger.getLogger(AnnieStandAlone.class);
	Path filepath;
    final String D = PropertiesCache.getInstance().getProperty("csv.delimeter");
    final String OUTPUTDIR = PropertiesCache.getInstance().getProperty("dir.output");
    final String OUTPUTEXT = PropertiesCache.getInstance().getProperty("output.extension");

	public AnnieStandAlone(Path filepath) {
		this.filepath = filepath;
	}

	/**
	 * Initialise the ANNIE system. This creates a "corpus pipeline" application
	 * that can be used to run sets of documents through the extraction system.
	 */
//	public void initAnnie() throws GateException, IOException {
//		LOG.debug("Initialising ANNIE...");
//
//		// load the ANNIE application from the saved state in plugins/ANNIE
//		File pluginsHome = Gate.getPluginsHome();
//		File anniePlugin = new File(pluginsHome, "ANNIE");
//		File annieGapp = new File(anniePlugin, "ANNIE_with_defaults.gapp");
//		annieController = (CorpusController) PersistenceManager.loadObjectFromFile(annieGapp);
//
//		LOG.debug("...ANNIE loaded");
//	}

	/** Tell ANNIE's controller about the corpus you want to run on */
	public void setCorpus(Corpus corpus) {
		App.annieController.setCorpus(corpus);
	}
//
	/** Run ANNIE */
	public void execute() throws GateException {
		LOG.debug("Running ANNIE...");
		App.annieController.execute();
		LOG.debug("...ANNIE complete");
	}

	/**
	 * This code will run with all the documents in memory - if you want to
	 * unload each from memory after use, add code to store the corpus in a
	 * DataStore.
	 */
	public void annotate() {

		try {
//			// initialise the GATE library
//			LOG.debug("Initialising GATE...");
//			Gate.init();
//			LOG.debug("...GATE initialised");
//
//			// initialise ANNIE (this may take several minutes)
//			// AnnieStandAlone annie = new AnnieStandAlone();
//			initAnnie();

			// create a GATE corpus and add a document for file input
			Corpus corpus = Factory.newCorpus("StandAloneAnnie corpus");
			File docFile = filepath.toFile();
			FeatureMap params = Factory.newFeatureMap();
			params.put("sourceUrl", docFile.toURL());
			params.put("preserveOriginalContent", new Boolean(true));
			params.put("collectRepositioningInfo", new Boolean(true));
			Out.prln("Creating doc for " + docFile.toURL());
			Document doc0 = (Document) Factory.createResource("gate.corpora.DocumentImpl", params);
			corpus.add(doc0);

			// tell the pipeline about the corpus and run it
			setCorpus(corpus);

			execute();

			// for each document, get an XML document with the
			// person and location names added
			Iterator iter = corpus.iterator();
			int count = 0;
			String startTagPart_1 = "<span GateID=\"";
			String startTagPart_2 = "\" title=\"";
			String startTagPart_3 = "\" style=\"background:Red;\">";
			String endTag = "</span>";

			while (iter.hasNext()) {
				Document doc = (Document) iter.next();
				AnnotationSet defaultAnnotSet = doc.getAnnotations();
				Set annotTypesRequired = new HashSet();
				annotTypesRequired.add("Person");
				annotTypesRequired.add("Location");
				annotTypesRequired.add("Organization");
				annotTypesRequired.add("Address");
				Set<Annotation> peopleAndPlaces = new HashSet<Annotation>(defaultAnnotSet.get(annotTypesRequired));

				FeatureMap features = doc.getFeatures();
				String originalContent = (String) features.get(GateConstants.ORIGINAL_DOCUMENT_CONTENT_FEATURE_NAME);
				RepositioningInfo info = (RepositioningInfo) features
						.get(GateConstants.DOCUMENT_REPOSITIONING_INFO_FEATURE_NAME);

				++count;
				File file = new File(OUTPUTDIR + File.separator + docFile.getName() + OUTPUTEXT);
				LOG.info("File name: '" + file.getAbsolutePath() + "'");

				HashSet<String> entitiescsv = new HashSet<String>();

				if (originalContent != null && info != null) {
					Out.prln("OrigContent and reposInfo existing. Generate file...");

					Iterator it = peopleAndPlaces.iterator();
					Annotation currAnnot;
					SortedAnnotationList sortedAnnotations = new SortedAnnotationList();

					while (it.hasNext()) {
						currAnnot = (Annotation) it.next();
						sortedAnnotations.addSortedExclusive(currAnnot);
					}

					//StringBuffer editableContent = new StringBuffer(originalContent);
					long insertPositionEnd;
					long insertPositionStart;
					// insert anotation tags backward
					LOG.info("Unsorted annotations count: " + peopleAndPlaces.size());
					LOG.debug("Sorted annotations count: " + sortedAnnotations.size());
					for (int i = sortedAnnotations.size() - 1; i >= 0; --i) {
						currAnnot = (Annotation) sortedAnnotations.get(i);
						insertPositionStart = currAnnot.getStartNode().getOffset().longValue();
						insertPositionStart = info.getOriginalPos(insertPositionStart);
						insertPositionEnd = currAnnot.getEndNode().getOffset().longValue();
						insertPositionEnd = info.getOriginalPos(insertPositionEnd, true);

						entitiescsv
								.add(docFile.getName().replace(".html.txt", ".html") + D
										+ originalContent.substring(Integer.parseInt("" + insertPositionStart),
												Integer.parseInt("" + insertPositionEnd))
										+ D + "_" + currAnnot.getType() + D);
						// if(insertPositionEnd != -1 && insertPositionStart !=
						// -1)
						// {
						// editableContent.insert((int)insertPositionEnd,
						// endTag);
						// editableContent.insert((int)insertPositionStart,
						// startTagPart_3);
						// editableContent.insert((int)insertPositionStart,
						// currAnnot.getType());
						// editableContent.insert((int)insertPositionStart,
						// startTagPart_2);
						// editableContent.insert((int)insertPositionStart,
						// currAnnot.getId().toString());
						// editableContent.insert((int)insertPositionStart,
						// startTagPart_1);
						// } // if
					} // for

					StringBuilder csvresponse = new StringBuilder();
					for (String row : entitiescsv) {
						csvresponse.append(row + "\n");
					}

					FileWriter writer = new FileWriter(file);
					// writer.write(editableContent.toString());
					writer.write(csvresponse.toString());
					writer.close();
				} // if - should generate
				else if (originalContent != null) {
					LOG.debug("OrigContent existing. Generate file...");

					Iterator it = peopleAndPlaces.iterator();
					Annotation currAnnot;
					SortedAnnotationList sortedAnnotations = new SortedAnnotationList();

					while (it.hasNext()) {
						currAnnot = (Annotation) it.next();
						sortedAnnotations.addSortedExclusive(currAnnot);
					} // while

					//StringBuffer editableContent = new StringBuffer(originalContent);
					long insertPositionEnd;
					long insertPositionStart;
					// insert anotation tags backward
					LOG.info("Unsorted annotations count: " + peopleAndPlaces.size());
					LOG.debug("Sorted annotations count: " + sortedAnnotations.size());
					for (int i = sortedAnnotations.size() - 1; i >= 0; --i) {
						currAnnot = (Annotation) sortedAnnotations.get(i);
						insertPositionStart = currAnnot.getStartNode().getOffset().longValue();
						insertPositionEnd = currAnnot.getEndNode().getOffset().longValue();

						entitiescsv
								.add(docFile.getName().replace(".html.txt", ".html") + D
										+ originalContent.substring(Integer.parseInt("" + insertPositionStart),
												Integer.parseInt("" + insertPositionEnd))
										+ D + "_" + currAnnot.getType() + D);

//						if (insertPositionEnd != -1 && insertPositionStart != -1) {
//							editableContent.insert((int) insertPositionEnd, endTag);
//							editableContent.insert((int) insertPositionStart, startTagPart_3);
//							editableContent.insert((int) insertPositionStart, currAnnot.getType());
//							editableContent.insert((int) insertPositionStart, startTagPart_2);
//							editableContent.insert((int) insertPositionStart, currAnnot.getId().toString());
//							editableContent.insert((int) insertPositionStart, startTagPart_1);
//						}
					}

					StringBuilder csvresponse = new StringBuilder();
					for (String row : entitiescsv) {
						csvresponse.append(row + "\n");
					}

					FileWriter writer = new FileWriter(file);
					// writer.write(editableContent.toString());
					writer.write(csvresponse.toString());
					writer.close();
				} else {
					LOG.debug("Content : " + originalContent);
					LOG.debug("Repositioning: " + info);
				}

				// String xmlDocument = doc.toXml(peopleAndPlaces, false);
				// String fileName = new String("StANNIE_toXML_" + count +
				// ".HTML");
				// FileWriter writer = new FileWriter(fileName);
				// writer.write(xmlDocument);
				// writer.close();

			}

		} catch (Exception e) {
			LOG.error("ERROR: " + e.getMessage());
		}

	}

	public static class SortedAnnotationList extends Vector {
		public SortedAnnotationList() {
			super();
		}

		public boolean addSortedExclusive(Annotation annot) {
			Annotation currAnot = null;

			// overlapping check
			for (int i = 0; i < size(); ++i) {
				currAnot = (Annotation) get(i);
				if (annot.overlaps(currAnot)) {
					return false;
				}
			}

			long annotStart = annot.getStartNode().getOffset().longValue();
			long currStart;
			// insert
			for (int i = 0; i < size(); ++i) {
				currAnot = (Annotation) get(i);
				currStart = currAnot.getStartNode().getOffset().longValue();
				if (annotStart < currStart) {
					insertElementAt(annot, i);
					/*
					 * Out.prln("Insert start: "+annotStart+" at position: "+i+
					 * " size="+size()); Out.prln("Current start: "+currStart);
					 */
					return true;
				}
			}

			int size = size();
			insertElementAt(annot, size);
			// Out.prln("Insert start: "+annotStart+" at size position: "+size);
			return true;
		}
	}
}