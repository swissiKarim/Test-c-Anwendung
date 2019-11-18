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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.FolderJob;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.TestReport;

/**
 * Jenkins service interface
 *
 */
public interface JenkinsService {

    /**
     * is job exists?
     * 
     * @param jobName
     * @return true or false
     * @throws JenkinsException 
     */
    public boolean isJobExists(String jobName) throws JenkinsException;
    
    /**
     * fetch config.xml
     * 
     * @param jobName
     * @return
     * @throws JenkinsException 
     */
    public String getJobXml(String jobName) throws JenkinsException;
	
	/**
	 * create job
	 * 
	 * @param jobName
	 * @param jobXml
	 * @throws JenkinsException 
	 */
    public void createJob(String jobName, String jobXml) throws JenkinsException;
    
	/**
	 * delete job
	 * 
	 * @param jobName
	 * @throws JenkinsException 
	 */
    public void deleteJob(String jobName) throws JenkinsException;
    
    /**
     * update job
     * 
     * @param jobName
     * @param jobXml
     * @throws JenkinsException 
     */
    public void updateJob(String jobName, String jobXml) throws JenkinsException;

    /**
     *  build job
     * 
     * @param jobName
     * @return buildNum
     * @throws JenkinsException
     */
    public int buildJob(String jobName) throws JenkinsException, IOException, URISyntaxException;
    
    /**
    * cancel build
    *
    * @param jobName
    * @param buildNum
    * @throws JenkinsException 
    */
   public void cancelBuildJob(String jobName, int buildNum) throws JenkinsException;
   
   /**
    *　get build log
    *
    * @param jobName
    * @param buildNum
    * @return build log
    * @throws JenkinsException 
    */
   public String getJobBuildLog(String jobName, int buildNum) throws JenkinsException;

   /**
    *　is job building?
    *
    * @param jobName
    * @param buildNum
    * @return true or false
    * @throws JenkinsException 
    */
   public boolean isJobBuilding(String jobName, int buildNum) throws JenkinsException;

    /**
     *　is job Finished?
     *
     * @param jobName
     * @param buildNum
     * @return true or false
     * @throws JenkinsException 
     */
    public boolean isJobFinish(String jobName, int buildNum) throws JenkinsException;
    
    /**
     * is job successful?
     * 
     * @param jobName
     * @param buildNum
     * @return true or false
     * @throws JenkinsException 
     */
    public boolean isJobSuccess(String jobName, int buildNum) throws JenkinsException;
    
    /**
     * download file
     * 
     * @param jobName
     * @param relativePath
     * @param destPath
     * @throws JenkinsException 
     */
    public void downloadFile(String jobName, String relativePath, String destPath) throws JenkinsException;
    
    /**
     * create Credentials(default)
     * 
     * @param id
     * @param username
     * @param password
     * @param desc
     * @throws JenkinsException 
     */
    public void createCredentials(String id, String username, String password, String desc) throws JenkinsException;
    
    /**
     * create Credentials(json format)
     * 
     * @param id
     * @param username
     * @param password
     * @param desc
     * @throws JenkinsException 
     */
    public void createCredentialsByJson(String id, String username, String password, String desc) throws JenkinsException;
    
    /**
     * create Credentials(xml format)
     * 
     * @param id
     * @param username
     * @param password
     * @param desc
     * @throws JenkinsException 
     */
    public void createCredentialsByXml(String id, String username, String password, String desc) throws JenkinsException;
    
    /**
     * delete Credentials
     * 
     * @param id
     * @throws JenkinsException 
     */
    public void deleteCredentials(String id) throws JenkinsException;

    public String getJobBuildLastLog(String jobName) throws JenkinsException;

    public StringBuilder jenkinsgetjobCosole(String jobName) throws JenkinsException ;
}
