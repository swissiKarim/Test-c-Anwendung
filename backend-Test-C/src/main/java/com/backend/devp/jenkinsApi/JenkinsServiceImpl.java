/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.backend.devp.jenkinsApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.ContentType;

import com.google.common.base.Optional;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.FolderJob;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;

/**
 * Jenkins service implementation class
 *
 * @author Zhang Zhizheng <zhizheng118@gmail.com>
 * @since 0.0.1-SNAPSHOT
 */
public class JenkinsServiceImpl implements JenkinsService {

	private String jenkinsUrl;
	private String jenkinsUser;
	private String jenkinsPassword;
	
	/**
	 * default constructor
	 */
	public JenkinsServiceImpl() {
		super();
	}

	/**
	 * constructor
	 * 
	 * @param jenkinsUrl
	 * @param jenkinsUser
	 * @param jenkinsPassword
	 */
	public JenkinsServiceImpl(String jenkinsUrl, String jenkinsUser, String jenkinsPassword) {
		super();
		this.jenkinsUrl = jenkinsUrl;
		this.jenkinsUser = jenkinsUser;
		this.jenkinsPassword = jenkinsPassword;
	}

	@Override
	public boolean isJobExists(String jobName) throws JenkinsException {
		try {
			JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl), jenkinsUser, jenkinsPassword);
			Map<String, Job> jobs = jenkinsServer.getJobs();
			Job job = jobs.get(jobName);
			if (job != null) {
				return true;
			}
		} catch (Exception e) {
			throw new JenkinsException(e);
		}
		return false;
	}

	@Override
	public String getJobXml(String jobName) throws JenkinsException {
		try {
    		JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl), jenkinsUser, jenkinsPassword);
            String xml = jenkinsServer.getJobXml(jobName);
            return xml;
		} catch (Exception e) {
			throw new JenkinsException(e);
		}
	}

	@Override
	public void createJob(String jobName, String jobXml) throws JenkinsException {
		try{
            JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl), jenkinsUser, jenkinsPassword);
            jenkinsServer.createJob(jobName, jobXml, true);
        }catch (Exception e){
            throw new JenkinsException(e);
        } 
	}

	@Override
	public void deleteJob(String jobName) throws JenkinsException {
		try{
            JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl), jenkinsUser, jenkinsPassword);
            jenkinsServer.deleteJob(jobName,  true);
        }catch (Exception e){
        	throw new JenkinsException(e);
        }
	}

	@Override
	public void updateJob(String jobName, String jobXml) throws JenkinsException {
		try{
    		JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl), jenkinsUser, jenkinsPassword);
    		jenkinsServer.updateJob(jobName, jobXml, true);
    	}catch (Exception e){
    		throw new JenkinsException(e);
    	}
	}

	@Override
	public int buildJob(String jobName) throws JenkinsException {
		int buildNumber = 0;
        try{
            JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl), jenkinsUser, jenkinsPassword);
            Map<String, Job> jobs = jenkinsServer.getJobs();
            JobWithDetails job = jobs.get(jobName).details();
            buildNumber = job.getNextBuildNumber();
            job.build(true);
        }catch (Exception e){
        	throw new JenkinsException(e);
        	
        }
        return buildNumber;
	}

	@Override
	public void cancelBuildJob(String jobName, int buildNum) throws JenkinsException {
		try{
	    	   JenkinsHttpClient client = new JenkinsHttpClient(new URI(jenkinsUrl), jenkinsUser, jenkinsPassword);
	    	   String path = "job/" + jobName + "/" + buildNum + "/" + "stop";
	    	   client.post(path, true);
	       }catch (Exception e){
	    	   throw new JenkinsException(e);
	       }
	}

	@Override
	public String getJobBuildLog(String jobName, int buildNum) throws JenkinsException {
		if (buildNum <= 0) {
			throw new IllegalArgumentException("buildNum must greater than 0!");
		}
		String buildLog = "";
		try {
			JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl), jenkinsUser, jenkinsPassword);
			Map<String, Job> jobs = jenkinsServer.getJobs();
			JobWithDetails job = jobs.get(jobName).details();
			Build build = job.getBuildByNumber(buildNum);
			if (build != null) {
				BuildWithDetails buildWithDetails = build.details();
				buildLog = buildWithDetails.getConsoleOutputText();
			}
			return buildLog;
		} catch (Exception e) {
			throw new JenkinsException(e);
		}
	}
	
	@Override
	public String getJobBuildLastLog(String jobName) throws JenkinsException {
		
		String buildLog = "";
		try {
			JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl), jenkinsUser, jenkinsPassword);
			Map<String, Job> jobs = jenkinsServer.getJobs();
			JobWithDetails job = jobs.get(jobName).details();
			
			Build build = job.getLastBuild();
			if (build != null) {
				BuildWithDetails buildWithDetails = build.details();
				buildLog = buildWithDetails.getConsoleOutputText();
			}
			return buildLog;
		} catch (Exception e) {
			throw new JenkinsException(e);
		}
	}

	@Override
	public boolean isJobBuilding(String jobName, int buildNum) throws JenkinsException {
		if (buildNum <= 0) {
			throw new IllegalArgumentException("buildNum must greater than 0!");
		}
		try {
			JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl), jenkinsUser, jenkinsPassword);
			Map<String, Job> jobs = jenkinsServer.getJobs();
			JobWithDetails job = jobs.get(jobName).details();
			Build build = job.getBuildByNumber(buildNum);
			if (build != null) {
				BuildWithDetails buildWithDetails = build.details();
				boolean isBuilding = buildWithDetails.isBuilding();
				return isBuilding;
			}
			return false;
		} catch (Exception e) {
			throw new JenkinsException(e);
		}
	}

	@Override
	public boolean isJobFinish(String jobName, int buildNum) throws JenkinsException {
		if (buildNum <= 0) {
			throw new IllegalArgumentException("buildNum must greater than 0!");
		}
		try {
			JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl), jenkinsUser, jenkinsPassword);
			Map<String, Job> jobs = jenkinsServer.getJobs();
			
			JobWithDetails job = jobs.get(jobName).details();
			Build build = job.getBuildByNumber(buildNum);
			if (build != null) {
				BuildWithDetails buildWithDetails = build.details();
				boolean isBuilding = buildWithDetails.isBuilding();
				return !isBuilding;
			}
			return false;
		} catch (Exception e) {
			throw new JenkinsException(e);
		}
	}

	@Override
	public boolean isJobSuccess(String jobName, int buildNum) throws JenkinsException {
		if (buildNum <= 0) {
			throw new IllegalArgumentException("buildNum must greater than 0!");
		}
		try {
			JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl), jenkinsUser, jenkinsPassword);
			Map<String, Job> jobs = jenkinsServer.getJobs();
			JobWithDetails job = jobs.get(jobName).details();
			BuildWithDetails buildWithDetails = job.getBuildByNumber(buildNum).details();
			BuildResult result = buildWithDetails.getResult();
			return result == BuildResult.SUCCESS;
		} catch (Exception e) {
			throw new JenkinsException(e);
		}
	}

	@Override
	public void downloadFile(String jobName, String relativePath, String destPath) throws JenkinsException {
		InputStream is = null;
		OutputStream os = null;
		try {
			// job
			JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl), jenkinsUser, jenkinsPassword);
			Map<String, Job> jobs = jenkinsServer.getJobs();
			JobWithDetails job = jobs.get(jobName).details();
			// InputStream
			JenkinsHttpClient client = job.getClient();
			String url = job.getUrl();
			is = client.getFile(URI.create(url + "/ws/" + relativePath));
			// OutputStream
			os = new FileOutputStream(new File(destPath));
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) != -1) {
				os.write(buffer, 0, length);
			}
		} catch (Exception e) {
			throw new JenkinsException(e);
		} finally {
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void createCredentials(String id, String username, String password, String desc) throws JenkinsException {
		createCredentialsByJson(id, username, password, desc);
	}
	
	@Override
	public void createCredentialsByJson(String id, String username, String password, String desc) throws JenkinsException {
		try {
			JenkinsHttpClient client = new JenkinsHttpClient(new URI(jenkinsUrl), jenkinsUser, jenkinsPassword);
			String req = "json={\"\":\"0\",\"credentials\":{\"scope\":\"GLOBAL\",\"username\":\""+username+"\",\"password\":\""+password+"\",\"id\":\""+id+"\",\"description\":\""+desc+"\",\"$class\":\"com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl\"}}";
			client.post_text("credentials/store/system/domain/_/createCredentials", req, ContentType.APPLICATION_FORM_URLENCODED,  true);
		} catch (Exception e) {
			throw new JenkinsException(e);
		}
	}
	
	@Override
	public void createCredentialsByXml(String id, String username, String password, String desc) throws JenkinsException {
		try {
			JenkinsHttpClient client = new JenkinsHttpClient(new URI(jenkinsUrl), jenkinsUser, jenkinsPassword);
			String req = "<com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl>"
									 + "<scope>GLOBAL</scope>"
									 + "<id>" + id + "</id>"
									 + "<description>" + desc + "</description>"
									 + "<username>" + username + "</username>"
									 + "<password>" + password + "</password>"
							 + "</com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl>";
			client.post_xml("credentials/store/system/domain/_/createCredentials", req,  true);
		} catch (Exception e) {
			throw new JenkinsException(e);
		}
	}

	@Override
	public void deleteCredentials(String id) throws JenkinsException {
		try {
    		JenkinsHttpClient client = new JenkinsHttpClient(new URI(jenkinsUrl), jenkinsUser, jenkinsPassword);
    		client.post("credentials/store/system/domain/_/credential/" + id + "/doDelete", true);
		} catch (Exception e) {
			throw new JenkinsException(e);
		}
	}

	public String getJenkinsUrl() {
		return jenkinsUrl;
	}

	public void setJenkinsUrl(String jenkinsUrl) {
		this.jenkinsUrl = jenkinsUrl;
	}

	public String getJenkinsUser() {
		return jenkinsUser;
	}

	public void setJenkinsUser(String jenkinsUser) {
		this.jenkinsUser = jenkinsUser;
	}

	public String getJenkinsPassword() {
		return jenkinsPassword;
	}

	public void setJenkinsPassword(String jenkinsPassword) {
		this.jenkinsPassword = jenkinsPassword;
	}

	@Override
	public StringBuilder jenkinsgetjobCosole(String jobName) throws JenkinsException {
		String buildLog = "";
		int i = 0;
		StringBuilder buildLogBuilder = null;
		try {
			JenkinsServer jenkinsServer = new JenkinsServer(new URI(jenkinsUrl), jenkinsUser, jenkinsPassword);
			Map<String, Job> jobs = jenkinsServer.getJobs();
			
			JobWithDetails job = jobs.get(jobName).details();
			Optional<FolderJob> ggg = jenkinsServer.getFolderJob(job);
			 Map<String, Job> branchs = jenkinsServer.getJobs(ggg.get());
			 JobWithDetails branch = branchs.get("master").details();
			 
			 Build build = branch.getLastBuild();
				if (build != null) {
					BuildWithDetails buildWithDetails = build.details();
					buildLog = buildWithDetails.getConsoleOutputText();
					Reader inputString = new StringReader(buildLog);
					BufferedReader reader = new BufferedReader(inputString);
				 buildLogBuilder = new StringBuilder();  
				 buildLogBuilder.append(" protokoll_blatt01");
				 buildLogBuilder.append(System.lineSeparator());
				 buildLogBuilder.append(" ============================");
				 buildLogBuilder.append(System.lineSeparator());
				 buildLogBuilder.append("+−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−+");
				 buildLogBuilder.append(System.lineSeparator());
				 buildLogBuilder.append("| ULAM (Blatt 01)");
				 buildLogBuilder.append(System.lineSeparator());
				 buildLogBuilder.append("+−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−−+");
				 buildLogBuilder.append(System.lineSeparator());
				 String line = reader.readLine();
					while (line != null) {
						i++;
						line = reader.readLine();
						if (i >71) {
						buildLogBuilder.append(line);
						buildLogBuilder.append(System.lineSeparator());
						}
					}
				  
				}
				
				return buildLogBuilder;
		} catch (Exception e) {
			throw new JenkinsException(e);
		}
	}
}
