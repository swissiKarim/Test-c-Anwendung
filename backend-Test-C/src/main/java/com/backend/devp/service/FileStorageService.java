package com.backend.devp.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.kafka.core.KafkaTemplate;
import com.backend.devp.exception.FileStorageException;
import com.backend.devp.exception.MyFileNotFoundException;
import com.backend.devp.gitGogs.GogsGitVerwaltung;
import com.backend.devp.model.FileInfo;
import com.backend.devp.property.FileStorageProperties;



@Service
public class FileStorageService {
	@Autowired
	private FileStorageService fileStorageService;
	private final Path fileStorageLocation;
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	 @Autowired
	    public FileStorageService(FileStorageProperties fileStorageProperties) {
	        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
	                .toAbsolutePath().normalize();

	        try {
	            Files.createDirectories(this.fileStorageLocation);
	        } catch (Exception ex) {
	            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
	        }
	    }
	 public String storeFile(MultipartFile file) {
	        // Normalize file name
	        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

	        try {
	            // Check if the file's name contains invalid characters
	            if(fileName.contains("..")) {
	                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
	            }

	            // Copy file to the target location (Replacing existing file with the same name)
	            Path targetLocation = this.fileStorageLocation.resolve(fileName);
	            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

	            return fileName;
	        } catch (IOException ex) {
	            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
	        }
	    }
	  public Resource loadFileAsResource(String fileName) {
	        try {
	            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
	            Resource resource = new UrlResource(filePath.toUri());
	            if(resource.exists()) {
	                return resource;
	            } else {
	                throw new MyFileNotFoundException("File not found " + fileName);
	            }
	        } catch (MalformedURLException ex) {
	            throw new MyFileNotFoundException("File not found " + fileName, ex);
	        }
	    }
	  


	  public void saveaufgabenblatt(MultipartFile file, String folder , String subfolder) throws Exception {
		
		  Path currentPath = Paths.get("/");
			Path absolutePath = currentPath.toAbsolutePath();
			String savePath ="/aufgabenblaette/" + folder ;
			
			Git myrepo = GogsGitVerwaltung.cloneRepo("http://localhost:3000/swissi/"+folder+".git", savePath, "swissi","Mh123456");
			
			byte[] filebytes = file.getBytes();
		
			if (subfolder.equalsIgnoreCase("/")) {
				Path filepath = Paths.get(savePath +"/"+ file.getOriginalFilename());
				Files.write(filepath, filebytes);
				System.out.println(filepath);
			}else if (subfolder.equalsIgnoreCase("src")) {
				new File(savePath + "/" + subfolder+"/").mkdir();
				Path filepath = Paths.get(savePath + "/"+subfolder+"/" + file.getOriginalFilename());
				System.out.println(filepath);
				Files.write(filepath, filebytes);
			}else if(subfolder.equalsIgnoreCase("test")) {
				new File(savePath + "/"+subfolder+"/").mkdir();
				Path filepath = Paths.get(savePath + "/"+subfolder+"/" + file.getOriginalFilename());
				Files.write(filepath, filebytes);
				System.out.println(filepath);
				
			}else {
				System.out.println(subfolder);
			}

			
			System.out.println("File added  : " + GogsGitVerwaltung.addFileToIndex(myrepo));
			System.out.println("File comiited  : " + GogsGitVerwaltung.commitChanges(myrepo, "Erste Commit"));
			final Boolean ispushed = GogsGitVerwaltung.pushChanges(myrepo);
			System.out.println("File pushed  : " + ispushed);
			//kafkaTemplate.send("photoIn", path.normalize().toString());
		}
	  
	  
	  public void saveaufgabenblattlosung(MultipartFile file, String aufgabenblattN) throws Exception {
		 // String currentUser = fileStorageService.printUser();
		  Path currentPath = Paths.get("/");
			String savePath ="/aufgabenblaette/" + aufgabenblattN ;
			new File(savePath + "/src/").mkdir();
			String savepathfile = savePath +"/src/";
			Git myrepo = GogsGitVerwaltung.cloneRepo("http://localhost:3000/swissi/"+aufgabenblattN+".git", savePath, "swissi","Mh123456");
		//	GogsGitVerwaltung.createBranch(myrepo, currentUser);
			byte[] filebytes = file.getBytes();
			
			
				Path filepath = Paths.get(savepathfile + file.getOriginalFilename());
				System.out.println(filepath);
				Files.write(filepath, filebytes);
				
				String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
				
			GogsGitVerwaltung.createBranch(myrepo, currentUser);
			
			System.out.println("File added  : " + GogsGitVerwaltung.addFileToIndex(myrepo));
			System.out.println("File comiited  : " + GogsGitVerwaltung.commitChanges(myrepo, "Erste Commit"));
			final Boolean ispushed = GogsGitVerwaltung.pushChanges(myrepo);
			System.out.println("File pushed  : " + ispushed);
			
		}
	  
	  
	  public void deletfile(String filename,  String folder , String subfolder) throws IOException, GitAPIException, URISyntaxException {
		  Path currentPath = Paths.get("/");
			Path absolutePath = currentPath.toAbsolutePath();
			String savePath ="/aufgabenblaette/" + folder ;
			
			Git myrepo = GogsGitVerwaltung.cloneRepo("http://localhost:3000/swissi/"+folder+".git", savePath, "swissi","Mh123456");
			
		File file = new File(savePath+"/"+subfolder+"/"+filename); 
		System.out.println(file);
	        if(file.delete()) 
	        { 
	            System.out.println("File deleted successfully"); 
	        } 
	        else
	        { 
	            System.out.println("Failed to delete the file"); 
	        }
	        

			System.out.println("File added  : " + GogsGitVerwaltung.addFileToIndex(myrepo));
			System.out.println("File comiited  : " + GogsGitVerwaltung.commitChanges(myrepo, "Erste Commit"));
			final Boolean ispushed = GogsGitVerwaltung.pushChanges(myrepo);
			System.out.println("File pushed  : " + ispushed);
    } 
	  
	  
	  public String modifyXmlFile(String jobname) throws ParserConfigurationException, SAXException, IOException {
		  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
		  try {
		         // use the factory to create a documentbuilder
		         DocumentBuilder builder = factory.newDocumentBuilder();

		         // create a new document from file
		         File file = new File("config.xml");
		         Document doc = builder.parse(file);
		        
		         // get the first element
		         Element element = doc.getDocumentElement();

		         // get all child nodes
		         NodeList nodes = element.getChildNodes();
		         NodeList  sources = nodes.item(19).getChildNodes();
		        // attributes.item(1).getChildNodes().item(1).getChildNodes().item(1).getNodeName()
		         Node  data =  sources.item(1);
		         Node  branchSource =  data.getChildNodes().item(1);
		         Node  source =  branchSource.getChildNodes().item(1);
		         NodeList sourceNodes = source.getChildNodes();
		         Node remote = sourceNodes.item(3);
		         remote.setTextContent("http://172.24.0.4:3000/swissi/"+jobname+".git");
		         DOMSource domSource = new DOMSource(doc);
		         StringWriter writer = new StringWriter();
		         StreamResult result = new StreamResult(writer);
		         TransformerFactory tf = TransformerFactory.newInstance();
		         Transformer transformer = tf.newTransformer();
		         transformer.transform(domSource, result);
		         return writer.toString();    
		         
		         // print the text content of each child
//		         for (int i = 0; i < nodes.getLength(); i++) {
//		             System.out.println("" + nodes.item(i).getTextContent());
//		          }
		         } catch (Exception ex) {
		         ex.printStackTrace();
		         return null;
		      }
	
	  }

	  
		public List<FileInfo> filesList (String dirName, String subfdirName, Boolean isDirectory) throws IOException {
		
		    List<FileInfo> filesinfo = new ArrayList<FileInfo>();

			  File f = new File("C:\\aufgabenblaette\\"+dirName+"\\"+subfdirName); // current directory

				FileFilter directoryFilter = new FileFilter() {
					public boolean accept(File file) {
						if (isDirectory) {
							return file.isDirectory();
						}else {
							return !file.isDirectory();
						}
						
					}
				};

				File[] files = f.listFiles(directoryFilter);
				
				for (File file : files) {
					FileInfo fileinfo = new FileInfo();
					fileinfo.setName(file.getName());
					fileinfo.setPath(file.getAbsolutePath());
					filesinfo.add(fileinfo);
				}
				
				
				
				return filesinfo;
		}
		
		 public String printUser() {
			 
		      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		      String name = auth.getName(); //get logged in username
		     
		    return name;
		 
		  }
}
