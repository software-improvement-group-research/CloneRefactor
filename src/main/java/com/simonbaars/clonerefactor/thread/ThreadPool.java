package com.simonbaars.clonerefactor.thread;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.simonbaars.clonerefactor.metrics.Metrics;
import com.simonbaars.clonerefactor.model.DetectionResults;
import com.simonbaars.clonerefactor.util.FileUtils;
import com.simonbaars.clonerefactor.util.SavePaths;

public class ThreadPool {
	private final File OUTPUT_FOLDER = new File(SavePaths.getFullOutputFolder());
	private final File FULL_METRICS = new File(OUTPUT_FOLDER.getParent()+"/full_metrics.txt");
	private final int NUMBER_OF_THREADS = 4;
	private final int THREAD_TIMEOUT = 360000;
	private final Metrics fullMetrics = new Metrics();
	
	private final CorpusThread[] threads;
	
	public ThreadPool () {
		threads = new CorpusThread[NUMBER_OF_THREADS];
		OUTPUT_FOLDER.mkdirs();
	}

	public void waitForThreadToFinish() {
		if(Arrays.stream(threads).allMatch(e -> e==null))
			return;
		while(Arrays.stream(threads).filter(e -> e!=null).allMatch(e -> e.isAlive())) {
			try {
				Thread.sleep(100);
				nullifyThreadIfStarved();
			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private void nullifyThreadIfStarved() {
		IntStream.range(0,size()).filter(i -> threads[i]!=null && threads[i].creationTime+THREAD_TIMEOUT<System.currentTimeMillis()).forEach(i -> {
			threads[i].timeout();
		});
	}

	private int size() {
		return NUMBER_OF_THREADS;
	}

	public void addToAvailableThread(File file) {
		for(int i = 0; i<threads.length; i++) {
			if(threads[i]==null || !threads[i].isAlive()) {
				enableNewThread(file, i);
				break;
			}
		}
	}
	
	public void finishFinalThreads() {
		System.out.println("Finishing up!");
		while(Arrays.stream(threads).anyMatch(e -> e!=null)) {
			System.out.println(Arrays.stream(threads).filter(e -> e!=null).count()+" to go!");
			waitForThreadToFinish();
			for(int i = 0; i<threads.length; i++) {
				if(threads[i] != null && !threads[i].isAlive())
					threads[i] = null;
			}
		}
	}

	private void enableNewThread(File file, int i) {
		writePreviousThreadResults(file, i);
		threads[i] = new CorpusThread(file);
	}

	private void writePreviousThreadResults(File file, int i) {
		if(threads[i]!=null && !threads[i].isAlive()) {
			if(threads[i].res != null)
				writeResults(file, threads[i].res);
			else writeError(i);
			JavaParserFacade.clearInstances();
			threads[i]=null;
		}
	}

	private void writeError(int i) {
		try {
			FileUtils.writeStringToFile(new File(SavePaths.createDirectoryIfNotExists(SavePaths.getErrorFolder())+threads[i].getFile().getName()+".txt"), ExceptionUtils.getFullStackTrace(threads[i].error));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	private void writeResults(File file, DetectionResults res) {
		fullMetrics.add(res.getMetrics());
		try {
			FileUtils.writeStringToFile(new File(OUTPUT_FOLDER.getAbsolutePath()+"/"+file.getName()+"-"+res.getClones().size()+".txt"), res.toString());
			FileUtils.writeStringToFile(FULL_METRICS, fullMetrics.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
