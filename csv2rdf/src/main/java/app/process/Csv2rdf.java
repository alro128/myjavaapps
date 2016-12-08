package app.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Path;
import org.apache.commons.io.FilenameUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Seq;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.apache.log4j.Logger;

import app.App;
import app.helper.PropertiesCache;

public class Csv2rdf {

    final static Logger LOG = Logger.getLogger(Csv2rdf.class);
    Path filepath;

    public Csv2rdf(Path filepath) {
        this.filepath = filepath;
    }

    public void convert() {

        final String DELIMITER = PropertiesCache.getInstance().getProperty("csv.delimeter");
        final String TRANSFORMDIR = PropertiesCache.getInstance().getProperty("dir.rdf");
        String fileinput = filepath.toString();

        LOG.info("converting " + fileinput);
        try {
            String fileoutput = FilenameUtils.getBaseName(fileinput) + ".rdf";

            // Read transform RDF
            String transformfile = TRANSFORMDIR + File.separator + fileoutput.replace(".html.txt.rdf", ".xml.rdf");
            Model modelr = ModelFactory.createDefaultModel();
            InputStream in = FileManager.get().open(transformfile);
            if (in == null) {
                LOG.error("ERROR: File: " + transformfile + " not found");
            }
            modelr.read(in, "");
            
            Resource article = ResourceFactory.createResource("http://eresources.nlb.gov.sg/ID/NLBDM/vocab/Article");
            Resource languageMaterial = ResourceFactory.createResource("http://eresources.nlb.gov.sg/ID/NLBDM/vocab/LanguageMaterial");
            
            String entityURI = "";
            StmtIterator sia = modelr.listStatements((Resource)null, RDF.type,article);
            if(sia.hasNext()){
                Statement s = sia.next();
                LOG.info("OBJECT LITERAL: " + s.getObject().toString());
                entityURI = s.getSubject().toString();
                LOG.info("SUBJECT: " + entityURI);
            } else { 
                StmtIterator sil = modelr.listStatements((Resource)null, RDF.type,languageMaterial);
                if (sil.hasNext()) { 
                    Statement s = sil.next();
                    LOG.info("OBJECT LITERAL: " + s.getObject().toString());
                    entityURI = s.getSubject().toString();
                    LOG.info("SUBJECT: " + entityURI);
                } else {
                    LOG.error("ERROR: entity URI not found for " + fileoutput);
                }
            } 

            // Generate new RDF Model
            Model model = ModelFactory.createDefaultModel();
            model.setNsPrefix("esv", "http://eresources.nlb.gov.sg/ID/NLBDM/vocab-esv/");
            model.setNsPrefix("foaf", "http://xmlns.com/foaf/0.1/");
            model.setNsPrefix("nlbdm", "http://eresources.nlb.gov.sg/ID/NLBDM/vocab/");
            model.setNsPrefix("owl", "http://www.w3.org/2002/07/owl#");
            model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
            model.setNsPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
            model.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");

            Resource root = model.createResource(entityURI);

            Property contains = ResourceFactory.createProperty("http://eresources.nlb.gov.sg/ID/NLBDM/vocab-esv/contains");

            Seq seq = model.createSeq();
            
            String currentLine = null;
            BufferedReader br = new BufferedReader(new FileReader(fileinput));
            while ((currentLine = br.readLine()) != null) {
                String str[] = currentLine.split(DELIMITER);
                seq.add("" + str[4]);
            }
            br.close();

            Statement s1 = model.createStatement(root, contains, seq);
            model.add(s1);

            Statement s0 = model.createStatement(root, RDF.type, languageMaterial);
            model.add(s0);

            FileOutputStream fileOutputStream = new FileOutputStream(App.DIR_OUTPUT + File.separator + fileoutput);
            model.write(fileOutputStream, "RDF/XML");
            LOG.info("file created: " + fileoutput);
            model.close();
            fileOutputStream.close();

        } catch (Exception ex) {
            LOG.error("ERROR: " + ex.getMessage());
        }
    }

}
