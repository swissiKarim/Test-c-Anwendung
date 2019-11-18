package com.backend.devp.gitGogs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.commons.io.FileDeleteStrategy;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class GogsGitVerwaltung {

	 private static final transient Logger LOG = LoggerFactory.getLogger(GogsGitVerwaltung.class);
	
	public static Git cloneRepo(String repositoryUrl, String gitLocalRepositoryPath, String tfsUser, String password) throws GitAPIException, IOException {
		Path path = Paths.get(gitLocalRepositoryPath);
		if (!Files.exists(path)) {
		CloneCommand cloneCommand = Git.cloneRepository()
	        .setURI(repositoryUrl)
	        .setDirectory(new File(gitLocalRepositoryPath));
	 
	    UsernamePasswordCredentialsProvider user = new UsernamePasswordCredentialsProvider(tfsUser, password);
	    cloneCommand.setCredentialsProvider(user);
	   Git repository = cloneCommand.call();
	    return repository;
		}else{
			Git repository = Git.open(new File(gitLocalRepositoryPath));
			System.out.println("exist" + repository.toString());
			
			return repository;
		}
	}
	
	protected static boolean localBranchExists(Git git, String branch) throws GitAPIException {
        List<Ref> list = git.branchList().call();
        String fullName = "refs/heads/" + branch;
        boolean localBranchExists = false;
        for (Ref ref : list) {
            String name = ref.getName();
            if (Objects.equals(name, fullName)) {
                localBranchExists = true;
            }
        }
        return localBranchExists;
    }
	  public static String currentBranch(Git git) {
	        try {
	            return git.getRepository().getBranch();
	        } catch (IOException e) {
	            LOG.warn("Failed to get the current branch due: " + e.getMessage() + ". This exception is ignored.", e);
	            return null;
	        }
	    }
	  protected static void configureBranch(Git git, String branch) {
	        // lets update the merge config
	        if (Strings.isNotBlank(branch)) {
	            StoredConfig config = git.getRepository().getConfig();
	            if (Strings.isBlank(config.getString("branch", branch, "remote")) || Strings.isBlank(config.getString("branch", branch, "merge"))) {
	                config.setString("branch", branch, "remote", "master");
	                config.setString("branch", branch, "merge", "refs/heads/" + branch);
	                try {
	                    config.save();
	                } catch (IOException e) {
	                    LOG.error("Failed to save the git configuration to " 
	                            + " with branch " + branch + " on remote repo:  due: " + e.getMessage() + ". This exception is ignored.", e);
	                }
	            }
	        }
	    }
	public static void createBranch(Git repository, String branchName) throws IOException, GitAPIException {
		 String current = currentBranch(repository);
		    if (Objects.equals(current, branchName)) {
		        return;
		    }
		    System.out.println("Checking out branch: " + branchName);
		    // lets check if the branch exists
		    CheckoutCommand command = repository.checkout().setName(branchName);
		    boolean exists = localBranchExists(repository, branchName);
		    if (!exists) {
		        command = command.setCreateBranch(true).setForce(true).
		                setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK);
		                
		    }
		    Ref ref = command.call();
		    if (LOG.isDebugEnabled()) {
		        LOG.debug("Checked out branch " + branchName + " with results " + ref.getName());
		    }
		    configureBranch(repository, branchName);
		
	}
	
	public static boolean addFileToIndex(Git repository) throws IOException, GitAPIException {
		try {
          repository.add().addFilepattern(".").call();
	    return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	public static boolean commitChanges(Git repository, String commitMessage) throws GitAPIException, IOException {
		repository.commit()
         .setAll(true)
         .setMessage("Commit changes to all files")
         .call();


        System.out.println("Committed all changes to repository at ");
		return true;
	}
	
	
//	
//	public static boolean addFileToIndex(String gitLocalRepositoryPath) throws IOException, GitAPIException {
//		runCommand("git add .", gitLocalRepositoryPath, "", false);
//		return true;
//	}
//	public static boolean commitChanges(Git repository, String commitMessage, String path) throws GitAPIException, IOException {
//		runCommand("git commit -m \"" + commitMessage + "\"", path, "", false);
//		return true;
//	}
//	
//	public static boolean pushChanges(Git repository, String user, String password, String path, String repo) throws GitAPIException, URISyntaxException, IOException {
//		final String pushCommand = "git push http://swissi:Mh123456@localhost:3000/swissi/" + repo + ".git";
//		runCommand(pushCommand, path, "", false);
//	    return true;
//	}
//	
	public static boolean pushChanges(Git repository) throws GitAPIException, URISyntaxException, IOException {
		   try
		    {
		        PushCommand pushCommand = repository.push();
		        pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider("swissi", "Mh123456"));
		        pushCommand.call();
		        return true;
		    }
		    catch (GitAPIException e)
		    {
		        throw new RuntimeException(e);
		       
		    }
	  
	}
	
	public static boolean getJenkinsConsolOut() throws GitAPIException, URISyntaxException, IOException {
		final String jenkinsConsol = "curl -s -S  -u \"swissi\":\"Mh123456\" \"http://localhost:8080/job/C-Test-Project/lastBuild/consoleText\"";
		runCommand(jenkinsConsol, "", "", true);
	    return true;
	}
	public static boolean jenkinsJobBuild() throws GitAPIException, URISyntaxException, IOException {
		final String jenkinsJob = "curl -X POST \"http://swissi:Mh123456@localhost:8080/job/C-Test-Project/build?token=qwert123456asdfg\"";
		runCommand(jenkinsJob, "", "", false);
	    return true;
	}
	public static void deletfilecont(String path) throws IOException {
		//relative path
		File directory = new File(path);
		PrintWriter writer = new PrintWriter(directory);
		writer.print("");
		writer.close();
    } 
	
	public static boolean createnewStudent(String path ) throws IOException {
		final String repo = "Aufgabenblatt_1";
		final String pushCommand = "git checkout -b student1";
		runCommand(pushCommand, "", "", false);
		final String pushCommand1 = "git push http://swissi:Mh123456@localhost:3000/swissi/"+ repo +  ".git student1:master";
		runCommand(pushCommand1, "", "", false);
	    return true;
	}
	
	
	
	
	private static void runCommand(String command, String path, String branch, Boolean istofile) throws IOException {
		ProcessBuilder builder = new ProcessBuilder(
	            "cmd.exe", "/c", "cd " + path + " && " + command);
		builder.redirectErrorStream(true);
        builder.start();
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        if (istofile == true) {
        	deletfilecont("C:\\Users\\swissi\\eclipse-workspace1\\backend-Test-C\\uploadfolder\\consoleOut.txt");
        	 while (true) { 
        	line = r.readLine();
            FileWriter fileWriter = new FileWriter("C:\\Users\\swissi\\eclipse-workspace1\\backend-Test-C\\uploadfolder\\consoleOut.txt", true); //Set true for append mode
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(line);  //New line
            if (line == null) { break; }
            printWriter.close();
            fileWriter.close();
        	}
        }
        	else {
        		  while (true) {
        	            line = r.readLine();
        	            if (line == null) { break; }
        	           System.out.println(line);
        	        }
        	}
        
      
	}
}
