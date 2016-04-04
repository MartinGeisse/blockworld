/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import name.martingeisse.blockworld.common.network.s2c_message.SectionDataResponseMessage;
import name.martingeisse.blockworld.common.protocol.SectionDataId;
import name.martingeisse.blockworld.common.util.task.Task;
import name.martingeisse.blockworld.server.section.SectionWorkingSet;
import name.martingeisse.blockworld.server.section.entry.SectionDataCacheEntry;

/**
 * This is a helper class used by the {@link MinerServer} to fetch
 * sections from the {@link SectionWorkingSet} in parallel and ship
 * them to the client. The point of this class is to fetch as many
 * section datas as possible from the section storage in a single query.
 */
public final class SectionToClientShipper {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(SectionToClientShipper.class);
	
	/**
	 * the workingSet
	 */
	private final SectionWorkingSet workingSet;
	
	/**
	 * the jobQueue
	 */
	private final BlockingQueue<ShippingJob> jobQueue;
	
	/**
	 * the handleAllJobsTask
	 */
	private final HandleAllJobsTask handleAllJobsTask;

	/**
	 * Constructor.
	 * @param workingSet the working set
	 */
	@Inject
	public SectionToClientShipper(final SectionWorkingSet workingSet) {
		this.workingSet = workingSet;
		this.jobQueue = new LinkedBlockingQueue<>();
		this.handleAllJobsTask = new HandleAllJobsTask();
	}
	
	/**
	 * Adds a shipping job.
	 * 
	 * @param sectionDataId the ID of the section data to ship
	 * @param session the session to ship to
	 */
	public void addJob(SectionDataId sectionDataId, MinerSession session) {
		SectionDataCacheEntry presentEntry = workingSet.getIfPresent(sectionDataId);
		if (presentEntry == null) {
			ShippingJob job = new ShippingJob();
			job.sectionDataId = sectionDataId;
			job.session = session;
			jobQueue.add(job);
			handleAllJobsTask.schedule();
		} else {
			send(presentEntry, session);
		}
	}
	
	/**
	 * Fetches all jobs from the job queue and handles them.
	 */
	public void handleJobs() {
		
		// Fetch pending jobs, returning if there is nothing to do. This happens quite often because
		// the job handling task gets scheduled for every added job, but the task handles *all*
		// pending jobs, so the remaining task executions only find an empty job queue.
		if (jobQueue.isEmpty()) {
			return;
		}
		ArrayList<ShippingJob> jobs = new ArrayList<SectionToClientShipper.ShippingJob>();
		jobQueue.drainTo(jobs);
		if (jobs.isEmpty()) {
			return;
		}
		
		// collect section data IDs
		Set<SectionDataId> sectionDataIds = new HashSet<>();
		for (ShippingJob job : jobs) {
			sectionDataIds.add(job.sectionDataId);
		}
		
		// fetch the objects from the cache
		ImmutableMap<SectionDataId, SectionDataCacheEntry> cacheEntries = workingSet.getAll(sectionDataIds);
		
		// complete the jobs by sending data to the clients
		for (ShippingJob job : jobs) {
			send(cacheEntries.get(job.sectionDataId), job.session);
		}
		
	}
	
	/**
	 * 
	 */
	private void send(SectionDataCacheEntry cacheEntry, MinerSession session) {
		// TODO bundle multiple results for the same client
		ImmutableMap<SectionDataId, byte[]> dataBySectionDataId = ImmutableMap.of(cacheEntry.getSectionDataId(), cacheEntry.getDataForClient());
		session.sendMessage(new SectionDataResponseMessage(dataBySectionDataId));
	}
	
	static volatile long total = 0;
	
	/**
	 * Contains the data for a single shipping job.
	 */
	static final class ShippingJob {
		SectionDataId sectionDataId;
		MinerSession session;
	}
	
	/**
	 * A task that gets scheduled repeatedly to finish the jobs.
	 */
	class HandleAllJobsTask extends Task {
		@Override
		public void run() {
			try {
				SectionToClientShipper.this.handleJobs();
			} catch (Throwable t) {
				logger.error("unexpected exception", t);
			}
		}
	}
	
}
