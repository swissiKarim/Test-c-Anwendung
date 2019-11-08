package com.backend.devp.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.xml.sax.SAXException;

import com.backend.devp.copyFile.CopyFileMethods;
import com.backend.devp.gitGogs.GogsGitVerwaltung;
import com.backend.devp.gitGogs.GogsJgit;
import com.backend.devp.jenkinsApi.JenkinsException;
import com.backend.devp.jenkinsApi.JenkinsServiceImpl;
import com.backend.devp.model.FileInfo;
import com.backend.devp.payload.UploadFileResponse;
import com.backend.devp.service.FileStorageService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class HauptController {

	private static final Logger logger = LoggerFactory.getLogger(HauptController.class);

	private static final String remotePath = "http://swissi:Mh123456@localhost:3000/swissi/Aufgabenblatt_1.git";

	private static final String localPath = "/src/main/resources/static/aufgabenblatt";

	@Autowired
	private FileStorageService fileStorageService;
	GogsJgit git = new GogsJgit();

	@PostMapping("/uploadFile")
	public UploadFileResponse uploadFile(@RequestParam("ulam") MultipartFile file,
			@RequestParam() String AufgabeblattNum) throws IOException {
		String fileName = fileStorageService.storeFile(file);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/targetLocation/")
				.path(fileName).toUriString();

		System.out.println("file uploaded");
		return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize(),
				AufgabeblattNum);
	}

	@GetMapping("/pushFileToRepo")
	public Boolean pushFileToRepo() {
		try {
			final String path = "/uploadfolder";
			Git myrepo = GogsGitVerwaltung.cloneRepo("http://localhost:3000/swissi/Aufgabenblatt_1.git", path, "swissi",
					"Mh123456");
			GogsGitVerwaltung.createBranch(myrepo, "Student1");
			System.out.println("File added  : " + GogsGitVerwaltung.addFileToIndex(myrepo));
			System.out.println("File comiited  : " + GogsGitVerwaltung.commitChanges(myrepo, "Erste Commit"));
			final Boolean ispushed = GogsGitVerwaltung.pushChanges(myrepo);
			System.out.println("File pushed  : " + ispushed);
			return ispushed;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	@GetMapping("/jenkinsConsolOutput")
	public boolean JenkinsConsolOutput() {
		try {
			return GogsGitVerwaltung.getJenkinsConsolOut();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	@GetMapping("/buildJenkinsJob")
	public boolean buildJenkinsJob() {
		try {
			return GogsGitVerwaltung.jenkinsJobBuild();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	@GetMapping("/getfilefrompath")
	public String Getfilefrompath() throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(
				new FileReader("C:/Users/swissi/eclipse-workspace1/backend-Test-C/uploadfolder/consoleOut.txt"))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			sb.append(" protokoll_blatt01");
			sb.append(System.lineSeparator());
			sb.append(" ============================");
			sb.append(System.lineSeparator());
			sb.append("+−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−+");
			sb.append(System.lineSeparator());
			sb.append("| ULAM (Blatt 01)");
			sb.append(System.lineSeparator());
			sb.append("+−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−+");
			sb.append(System.lineSeparator());

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			sb.delete(200, 2566);
			String everything = sb.toString();
			return everything;
		}
	}

	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> sendFileToGitfolder(@PathVariable String fileName, HttpServletRequest request)
			throws IOException {
		// Load file as Resource
		Resource resource = (Resource) fileStorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext()
					.getMimeType(((org.springframework.core.io.Resource) resource).getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		// copy files using java.nio FileChannel
		File source = new File(resource.getURI());
		File dest = new File("C:/uploadfolder/src/ulam.c");
		long start = System.nanoTime();
		CopyFileMethods.copyFileUsingChannel(source, dest);
		System.out.println("Time taken by Channel Copy = " + (System.nanoTime() - start));

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(
				HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + ((org.springframework.core.io.Resource) resource).getFilename() + "\"")
				.body(resource);
	}

	@PostMapping("/Aufgabenblatt")
	public String newAufgabenblatt(@RequestParam("file") MultipartFile file, @RequestParam String folder,@RequestParam String subfolder ) {
		String returnValue = "start";

		try {
			JenkinsServiceImpl jenkinsS= new JenkinsServiceImpl("http://localhost:8080", "swissi", "Mh123456");
			String jobxml = fileStorageService.modifyXmlFile(folder);
			if (!jenkinsS.isJobExists(folder)) {
			jenkinsS.createJob(folder, jobxml);
			fileStorageService.saveaufgabenblatt(file, folder, subfolder);
			}else {
				System.out.println("Job Exist");
				fileStorageService.saveaufgabenblatt(file, folder, subfolder);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error saving Aufgabenblatt", e);
			returnValue = "error";
		}

		return returnValue;
	}

	@PostMapping("/Aufgabenblattlosung")
	public String newAufgabenblattlosung(@RequestParam("file") MultipartFile file, @RequestParam String aufgabenblattN) {
		String returnValue = "start";

		try {
			
			
				fileStorageService.saveaufgabenblattlosung(file, aufgabenblattN);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error saving Aufgabenblatt", e);
			returnValue = "error";
		}

		return returnValue;
	}
//	@PostMapping("/jgittest")
//	public String jgittest(@RequestParam("aufgabenblattFile") MultipartFile aufgabenblatt, @RequestParam String aufgabenblattNummer)
//			throws IOException, InvalidRemoteException, TransportException, GitAPIException {
//		String returnValue = "start";
//		final String path = "/aufgabenblaette/" + aufgabenblattNummer;
//		Git myrepo = GogsGitVerwaltung.cloneRepo("http://localhost:3000/swissi/Aufgabenblatt_1.git", path, "swissi",
//				"Mh123456");
//
//		try {
//			fileStorageService.saveaufgabenblatt(path, aufgabenblatt, aufgabenblattNummer);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			logger.error("Error saving photo", e);
//			returnValue = "error";
//		}
//
//		return returnValue;
//	}

	@PostMapping("/newstudent")
	public void newstudent() throws IOException{
		final String path = "/uploadfolder";
		GogsGitVerwaltung.createnewStudent(path);
	}

	
	
	
	@GetMapping("/jenkinsBuild")
	public int  jenkinsBuild(@RequestParam String aufgabenblattN) throws JenkinsException, IOException, URISyntaxException  {
		
		JenkinsServiceImpl jenkinsS= new JenkinsServiceImpl("http://localhost:8080", "swissi", "Mh123456");
		
		
		return jenkinsS.buildJob(aufgabenblattN);
	}
	
	@GetMapping("/jenkinsBuildlog")
	public String  jenkinsBuildlog(@RequestParam String aufgabenblattN) throws JenkinsException, IOException, URISyntaxException  {
		
		JenkinsServiceImpl jenkinsS= new JenkinsServiceImpl("http://localhost:8080", "swissi", "Mh123456");
		
		StringBuilder buildlog =  jenkinsS.jenkinsgetjobCosole(aufgabenblattN);
		return buildlog.toString();
	}
	
	@PostMapping("/newJenkinsAufgabenblatt")
	public void newJenkinsAufgabenblatt(@RequestParam String jobname) throws IOException, JenkinsException, ParserConfigurationException, SAXException{
		JenkinsServiceImpl jenkinsS= new JenkinsServiceImpl("http://localhost:8080", "swissi", "Mh123456");
		String jobxml = fileStorageService.modifyXmlFile(jobname);
		jenkinsS.createJob(jobname, jobxml);
	}
	
//	@PostMapping("/modifyxml")
//	public void newJenkinsAufgabenblatt() throws IOException, JenkinsException, ParserConfigurationException, SAXException{
//		fileStorageService.modifyXmlFile();
//	}
	
	@GetMapping("/filesList")
	public List<FileInfo> listdirectory (@RequestParam String augabenblattN, @RequestParam String subfolder, @RequestParam Boolean isDirectory) throws IOException {
		
		return fileStorageService.filesList(augabenblattN, subfolder, isDirectory);

	
	}
	
	@GetMapping("/deletfilefromaufgabenblatt")
	public void deletfilefromaufgabenblatt (@RequestParam String filename, @RequestParam String folder, @RequestParam String subfolder) throws IOException, GitAPIException, URISyntaxException {
		
		fileStorageService.deletfile(filename, folder, subfolder);
	}
	
	@GetMapping("/getusercurrent")
	public String getusercurrent() {
		
		 return String.format(SecurityContextHolder.getContext().getAuthentication().getName());
	}
	
    @GetMapping(value = "/hello")
    public String sayHello() {
        return String.format("Hello %s", SecurityContextHolder.getContext().getAuthentication().getName());
    }
    
	
//	@GetMapping("/newbrach")
//	public void newuserbranch(@RequestParam String aufgabenblattN) throws GitAPIException, IOException {
//	 String currentUser = fileStorageService.printUser();
//
//	  Path currentPath = Paths.get("/");
//		String savePath ="/aufgabenblaette/" + aufgabenblattN ;
//		
//		Git myrepo = GogsGitVerwaltung.cloneRepo("http://localhost:3000/swissi/"+aufgabenblattN+".git", savePath, "swissi","Mh123456");
//		GogsGitVerwaltung.createBranch(myrepo, currentUser);
//	}
}
