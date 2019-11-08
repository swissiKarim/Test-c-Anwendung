package com.backend.devp.gitGogs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import com.backend.devp.enums.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheEditor;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.dircache.DirCacheEditor.PathEdit;
import org.eclipse.jgit.internal.storage.reftree.RefTreeDatabase;
import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

public class GogsJgit {

	  private Repository repository;

	    public GogsJgit() {
	        FileRepositoryBuilder builder = new FileRepositoryBuilder();
	        try {
	            repository = builder.setGitDir(new File("C:/learn/repo/repo.git")).readEnvironment().findGitDir().setBare()
	                    .build();
	        } catch (IOException e) {
	        }
	    }

	    public void createRepository() {
	        try {
	            final Git git = Git.init().setBare(true).setGitDir(new File("C:/learn/repo/git.git")).call();
	            final Repository repository = git.getRepository();
	            File dir = repository.getDirectory();
	            // ObjectId headId = repository.resolve("master^{commit}");
//	            ObjectInserter inserter = repository.newObjectInserter();
//	            System.out.println(inserter);

	            System.out.println(dir.getAbsolutePath());

	            File file = File.createTempFile("git", ".file", new File("C:/learn/repo/"));
	            FilterOutputStream os = new FilterOutputStream(new FileOutputStream(file) {
	                @Override
	                public void close() throws IOException {
	                    super.close();

	                    final DirCacheEditor editor = DirCache.newInCore().editor();

	                    // Ref ref = repository.findRef("master");
	                    ObjectInserter inserter = repository.newObjectInserter();
	                    InputStream in = new FileInputStream(file);
	                    ObjectId objectId = inserter.insert(Constants.OBJ_BLOB, file.length(), in);
	                    editor.add(new DirCacheEditor.PathEdit(new DirCacheEntry("README.md")) {
	                        @Override
	                        public void apply(DirCacheEntry ent) {
	                            ent.setLength(file.length());
	                            ent.setLastModified(file.lastModified());
	                            ent.setFileMode(FileMode.REGULAR_FILE);
	                            ent.setObjectId(objectId);
	                        }
	                    });
	                    editor.finish();
	                    ObjectId treeId = editor.getDirCache().writeTree(inserter);

	                    if (treeId != null) {
	                        PersonIdent ident = new PersonIdent("system", "system@system.com", new Date(),
	                                TimeZone.getDefault());
	                        CommitBuilder cb = new CommitBuilder();
	                        cb.setAuthor(ident);
	                        cb.setCommitter(ident);
	                        cb.setEncoding(Charset.forName("UTF-8"));
	                        cb.setMessage("add README.md");
	                        cb.setTreeId(treeId);
	                        final ObjectId commitId = inserter.insert(cb);
	                        inserter.flush();
	                        try (final ObjectReader reader = repository.newObjectReader()) {
	                            RevCommit commit = RevCommit.parse(repository.open(commitId).getBytes());
	                            if (repository.getRefDatabase() instanceof RefTreeDatabase) {
	                            } else {
	                                final RefUpdate ru = repository.updateRef(Constants.R_HEADS + Constants.MASTER);
	                                ru.setExpectedOldObjectId(ObjectId.zeroId());
	                                ru.setNewObjectId(commit.getId());
	                                ru.setRefLogMessage(commit.getShortMessage(), false);
	                                ru.forceUpdate();
	                            }
	                        }
	                    }
	                }
	            });
	            String readme = "# Description\n\nthis is a readme file!\n";
	            os.write(readme.getBytes());
	            os.close();
	            System.out.println(file.delete());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    @SuppressWarnings("resource")
	    public void addFiles() {
	        try {
	            final DirCacheEditor editor = DirCache.newInCore().editor();

	            final File file = new File("C:/learn/repo/Helo.java");
	            ObjectInserter inserter = repository.newObjectInserter();
	            InputStream in = new FileInputStream(file);
	            ObjectId objectId = inserter.insert(Constants.OBJ_BLOB, file.length(), in);
	            editor.add(new DirCacheEditor.PathEdit(new DirCacheEntry("src/org/wrolp/helo/Helo.java")) {
	                @Override
	                public void apply(DirCacheEntry ent) {
	                    ent.setLength(file.length());
	                    ent.setLastModified(file.lastModified());
	                    ent.setFileMode(FileMode.REGULAR_FILE);
	                    ent.setObjectId(objectId);
	                }
	            });
	            final File file2 = new File("D:/learn/repo/Wold.java");
	            in = new FileInputStream(file2);
	            final ObjectId objectId2 = inserter.insert(Constants.OBJ_BLOB, file2.length(), in);
	            editor.add(new DirCacheEditor.PathEdit(new DirCacheEntry("src/org/wrolp/wold/Wold.java")) {
	                @Override
	                public void apply(DirCacheEntry ent) {
	                    ent.setLength(file2.length());
	                    ent.setLastModified(file2.lastModified());
	                    ent.setFileMode(FileMode.REGULAR_FILE);
	                    ent.setObjectId(objectId2);
	                }
	            });

	            RevCommit headCommit = getLastCommit(Constants.MASTER);
	            TreeWalk treeWalk = new TreeWalk(repository.newObjectReader());
	            treeWalk.setRecursive(true);
	            RevWalk revWalk = new RevWalk(repository);
	            RevTree revTree = revWalk.parseTree(headCommit);
	            int index = treeWalk.addTree(revTree);
	            while (treeWalk.next()) {
	                String path = treeWalk.getPathString();
	                final CanonicalTreeParser parser = treeWalk.getTree(index, CanonicalTreeParser.class);
	                DirCacheEntry entry = new DirCacheEntry(path);
	                final ObjectId id = parser.getEntryObjectId();
	                final FileMode mode = parser.getEntryFileMode();
	                editor.add(new PathEdit(entry) {
	                    @Override
	                    public void apply(DirCacheEntry ent) {
	                        ent.setObjectId(id);
	                        ent.setFileMode(mode);
	                    }
	                });
	            }

	            editor.finish();
	            ObjectId treeId = editor.getDirCache().writeTree(inserter);

	            if (treeId != null) {
	                PersonIdent ident = new PersonIdent("system", "system@system.com", new Date(),
	                        TimeZone.getDefault());
	                CommitBuilder cb = new CommitBuilder();
	                cb.setAuthor(ident);
	                cb.setCommitter(ident);
	                cb.setEncoding(Charset.forName("UTF-8"));
	                cb.setMessage("Helo & Wold");
	                cb.setParentId(headCommit);
	                cb.setTreeId(treeId);
	                final ObjectId commitId = inserter.insert(cb);
	                inserter.flush();
	                try (final ObjectReader reader = repository.newObjectReader()) {
	                    RevCommit commit = RevCommit.parse(repository.open(commitId).getBytes());
	                    if (repository.getRefDatabase() instanceof RefTreeDatabase) {
	                        System.out.println("ref tree");
	                    } else {
	                        RevCommit last = getLastCommit(Constants.MASTER);
	                        final RefUpdate ru = repository.updateRef(Constants.R_HEADS + "master");
	                        ru.setExpectedOldObjectId(last.getId());
	                        ru.setNewObjectId(commit.getId());
	                        ru.setRefLogMessage(commit.getShortMessage(), false);
	                        ru.forceUpdate();
	                    }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    @SuppressWarnings("resource")
	    public void deleteFiles() {
	        try {
	            final DirCacheEditor editor = DirCache.newInCore().editor();
	            ObjectInserter inserter = repository.newObjectInserter();

	            RevCommit headCommit = getLastCommit(Constants.MASTER);
	            TreeWalk treeWalk = new TreeWalk(repository.newObjectReader());
	            treeWalk.setRecursive(true);
	            RevWalk revWalk = new RevWalk(repository);
	            RevTree revTree = revWalk.parseTree(headCommit);
	            int index = treeWalk.addTree(revTree);
	            while (treeWalk.next()) {
	                String path = treeWalk.getPathString();
	                if (!"README.md".equals(path)) {
	                    final CanonicalTreeParser parser = treeWalk.getTree(index, CanonicalTreeParser.class);
	                    DirCacheEntry entry = new DirCacheEntry(path);
	                    final ObjectId id = parser.getEntryObjectId();
	                    final FileMode mode = parser.getEntryFileMode();
	                    editor.add(new PathEdit(entry) {
	                        @Override
	                        public void apply(DirCacheEntry ent) {
	                            ent.setObjectId(id);
	                            ent.setFileMode(mode);
	                        }
	                    });
	                }
	            }

	            editor.finish();
	            ObjectId treeId = editor.getDirCache().writeTree(inserter);

	            if (treeId != null) {
	                PersonIdent ident = new PersonIdent("system", "system@system.com", new Date(),
	                        TimeZone.getDefault());
	                CommitBuilder cb = new CommitBuilder();
	                cb.setAuthor(ident);
	                cb.setCommitter(ident);
	                cb.setEncoding(Charset.forName("UTF-8"));
	                cb.setMessage("Delete {README.md} file");
	                cb.setParentId(headCommit);
	                cb.setTreeId(treeId);
	                final ObjectId commitId = inserter.insert(cb);
	                inserter.flush();
	                try (final ObjectReader reader = repository.newObjectReader()) {
	                    RevCommit commit = RevCommit.parse(repository.open(commitId).getBytes());
	                    if (repository.getRefDatabase() instanceof RefTreeDatabase) {
	                        System.out.println("ref tree");
	                    } else {
	                        RevCommit last = getLastCommit(Constants.MASTER);
	                        final RefUpdate ru = repository.updateRef(Constants.R_HEADS + Constants.MASTER);
	                        ru.setExpectedOldObjectId(last.getId());
	                        ru.setNewObjectId(commit.getId());
	                        ru.setRefLogMessage(commit.getShortMessage(), false);
	                        ru.forceUpdate();
	                    }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public void changeFile() {
	        try (TreeWalk treeWalk = new TreeWalk(repository.newObjectReader())) {
	            final DirCacheEditor editor = DirCache.newInCore().editor();

	            final File file = new File("D:/learn/repo/Helo.java");
	            ObjectInserter inserter = repository.newObjectInserter();
	            InputStream in = new FileInputStream(file);
	            ObjectId objectId = inserter.insert(Constants.OBJ_BLOB, file.length(), in);
	            editor.add(new DirCacheEditor.PathEdit(new DirCacheEntry("src/org/wrolp/helo/Helo.java")) {
	                @Override
	                public void apply(DirCacheEntry ent) {
	                    ent.setLength(file.length());
	                    ent.setLastModified(file.lastModified());
	                    ent.setFileMode(FileMode.REGULAR_FILE);
	                    ent.setObjectId(objectId);
	                }
	            });

	            RevCommit headCommit = getLastCommit(Constants.MASTER);
	            RevTree revTree = headCommit.getTree();

	            treeWalk.setRecursive(true);
	            int index = treeWalk.addTree(revTree);
	            while (treeWalk.next()) {
	                String path = treeWalk.getPathString();
	                if (!"src/org/wrolp/helo/Helo.java".equals(path)) {
	                    final CanonicalTreeParser parser = treeWalk.getTree(index, CanonicalTreeParser.class);
	                    DirCacheEntry entry = new DirCacheEntry(path);
	                    final ObjectId id = parser.getEntryObjectId();
	                    final FileMode mode = parser.getEntryFileMode();
	                    editor.add(new PathEdit(entry) {
	                        @Override
	                        public void apply(DirCacheEntry ent) {
	                            ent.setObjectId(id);
	                            ent.setFileMode(mode);
	                        }
	                    });
	                }
	                // XXX
//	                if (treeWalk.isSubtree()) {
//	                    treeWalk.enterSubtree();
//	                }
	            }

	            editor.finish();
	            ObjectId treeId = editor.getDirCache().writeTree(inserter);

	            if (treeId != null) {
	                PersonIdent ident = new PersonIdent("system", "system@system.com", new Date(),
	                        TimeZone.getDefault());
	                CommitBuilder cb = new CommitBuilder();
	                cb.setAuthor(ident);
	                cb.setCommitter(ident);
	                cb.setEncoding(Charset.forName("UTF-8"));
	                cb.setMessage("Change Helo!!");
	                cb.setParentId(headCommit);
	                cb.setTreeId(treeId);
	                final ObjectId commitId = inserter.insert(cb);
	                inserter.flush();
	                try (final ObjectReader reader = repository.newObjectReader()) {
	                    RevCommit commit = RevCommit.parse(repository.open(commitId).getBytes());
	                    if (repository.getRefDatabase() instanceof RefTreeDatabase) {
	                        System.out.println("ref tree");
	                    } else {
	                        RevCommit last = getLastCommit("master");
	                        final RefUpdate ru = repository.updateRef(Constants.R_HEADS + "master");
	                        ru.setExpectedOldObjectId(last.getId());
	                        ru.setNewObjectId(commit.getId());
	                        ru.setRefLogMessage(commit.getShortMessage(), false);
	                        ru.forceUpdate();
	                    }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public List<Ref> listRefs() {
	        try {
	            List<Ref> refs = repository.getRefDatabase().getRefsByPrefix(Constants.R_HEADS);
	            if (refs != null) {
	                refs.forEach(ref -> {
	                    System.out.println(ref.getName());
	                });
	            }
	            return refs;
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	    public RevCommit getLastCommit(String branch) {
	        try {
	            Ref master = repository.findRef(branch);
	            if (master != null) {
	                ObjectId masterId = master.getTarget().getObjectId();
	                ObjectReader reader = repository.newObjectReader();
	                RevCommit commit = RevCommit.parse(reader.open(masterId).getBytes());
//	                System.out.println(commit.getShortMessage());
//	                System.out.println(commit.getFullMessage());
	                return commit;
	            }
	        } catch (IOException e) {
	        }
	        return null;
	    }

	    public ObjectId getTreeFromRef(String treeRefName) {
	        RevCommit commit = getLastCommit(treeRefName);
	        if (commit == null) {
	            return null;
	        }
	        ObjectId tree = commit.getTree().getId();
	        return tree;
	    }

	    public void readFile() {
	        try (TreeWalk treeWalk = new TreeWalk(repository);
	             RevWalk revWalk = new RevWalk(repository);) {
//	            RevCommit commit = revWalk.parseCommit(ObjectId.fromString("ef2c5567aaa45b93d7d4cc7dd99081e05a0869b7"));
	            RevCommit commit = revWalk.parseCommit(repository.resolve("ef2c5567aaa45b93d7d4cc7dd99081e05a0869b7"));
	            RevTree revTree = commit.getTree();
	            treeWalk.addTree(revTree);
	            treeWalk.setRecursive(true);
	            treeWalk.setFilter(PathFilter.create("src/org/wrolp/helo/Helo.java"));
	            if (!treeWalk.next()) {
	                System.err.println("This commit have no file {src/org/wrolp/helo/Helo.java}");
	            }
	            ObjectId objectId = treeWalk.getObjectId(0);
	            ObjectLoader loader = repository.open(objectId);
	            loader.copyTo(System.err);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public void listCommits() {
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        try (RevWalk revWalk = new RevWalk(repository)) {
	            RevCommit head = getLastCommit(Constants.MASTER);
	            revWalk.markStart(head);
	            int count = 0;
	            for (RevCommit commit : revWalk) {
//	                PersonIdent author = commit.getAuthorIdent();
//	                PersonIdent committer = commit.getCommitterIdent();
	                Date time = new Date(((long) commit.getCommitTime()) * 1000L);
	                System.out.println(++count + " " + commit + " " + formatter.format(time) + " ");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public void logsOfFile() {
	        Git git = Git.wrap(repository);
	        try {
	            git.log().addPath("src/org/wrolp/helo/Helo.java").call().forEach(commit -> {
	                System.out.println(commit);
	            });
	        } catch (GitAPIException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public void gc() {
	        Git git = Git.wrap(repository);
	        try {
	            git.gc().call();
	        } catch (GitAPIException e) {
	            e.printStackTrace();
	        }
	    }
/**
 * @throws GitAPIException 
 * @throws TransportException 
 * @throws InvalidRemoteException ************************************/	    
	    public void gitclonem(String remotePath, String localPath) throws InvalidRemoteException, TransportException, GitAPIException {
	    	File localRepo=new File(localPath);
	        if(localRepo.isDirectory()){
	       	 deleteDirectory(localPath);
	        }else{
	       	 localRepo.deleteOnExit();
	        }
	   		
	   		Git git=Git.cloneRepository().setURI(remotePath).
	   		setBranch("master").
	   		setDirectory(localRepo).call();
	   		System.out.println(git.getRepository().getConfig().toText());
	    	
	    	
	    }
	    
	    public static boolean deleteDirectory(String filePath) {  
	        boolean flag = false;  
	        if (!filePath.endsWith(File.separator)) {  
	            filePath = filePath + File.separator;  
	        }  
	        File dirFile = new File(filePath);  
	        if (!dirFile.exists() || !dirFile.isDirectory()) {  
	            return false;  
	        }  
	        flag = true;  
	        File[] files = dirFile.listFiles();  
	        for (int i = 0; i < files.length; i++) {    
	            if (files[i].isFile()) {  
	                flag = deleteFile(files[i].getAbsolutePath());  
	                if (!flag)  
	                    break;  
	            } 
	            else {  
	                flag = deleteDirectory(files[i].getAbsolutePath());  
	                if (!flag)  
	                    break;  
	            }  
	        }  
	        if (!flag)  
	            return false;   
	        if (dirFile.delete()) {  
	            return true;  
	        } else {  
	            return false;  
	        }  
	    }  
	    public static boolean deleteFile(String filePath) {  
	        boolean flag = false;  
	        File file = new File(filePath);  
	        if (file.isFile() && file.exists()) {  
	            file.delete();  
	            flag = true;  
	        }  
	        return flag;  
	    }  
	  
	    public void gitpushm(String PROJECT_PATH, String PROJECT_NAME, String COMMIT_MSG) throws NoFilepatternException, GitAPIException, IOException {
	    	
	    	String filePath = PROJECT_PATH + PROJECT_NAME;
			File f = new File(filePath);
			Git git = Git.open(f);

			// git status
			Status status = git.status().call();
			Set<String> m = status.getModified();
			Set<String> u = status.getUntracked();
			Set<String> finalFile = new HashSet<String>();
			if (m.isEmpty() && u.isEmpty()) {
				System.out.println("Keine Dateiänderung, keine Einreichung erforderlich");
				return;
			}
			if (!m.isEmpty()) {
				System.out.println("Liste der geänderten Dateien:");
				for (String s : m) {
					
					if(!PROJECT_NAME.equals(Modules.project.jiaxin_gw_dataconf.name())){
						if (s.contains("x_dubbox.xml") || s.contains("log4j.xml")) {
							continue;
						}
					}
					System.out.println(s);
					finalFile.add(s);
					git.add().addFilepattern(s).setUpdate(true).call();
				}
			}

			System.out.println("");

			if (!u.isEmpty()) {
				System.out.println("Neue Dateiliste:");
				for (String s : u) {
					System.out.println(s);
					finalFile.add(s);
					git.add().addFilepattern(s).call();
				}
			}
			if (finalFile.isEmpty() || finalFile.size() == 0) {
				System.out.println("Es sind keine Dateien zum Senden vorhanden!");
				return;
			}
			// git add
			// DirCache dirCache =
			System.out.println("文件add成功！");
			// RevCommit commit =
			git.commit().setMessage(COMMIT_MSG).setInsertChangeId(true).call();
			System.out.println("文件commit成功！");
			Iterable<PushResult> iterable = git.push().add("HEAD:refs/for/master").call();
			RemoteRefUpdate remoteUpdate = iterable.iterator().next().getRemoteUpdate("refs/for/master");
			if (remoteUpdate != null && remoteUpdate.getStatus() != null && remoteUpdate.getStatus().name().equals("OK")) {
				System.out.println("push成功！");
			} else {
				System.out.println("push失败！");
				System.out.println("执行reset HEAD^！");
				git.reset().setRef("HEAD^").call();
				System.out.println("reset HEAD^ 成功！");
			}

	    }
}
