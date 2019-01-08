import java.util.ArrayList;

public class JobSchedule
{
	ArrayList <Job> jobs;
	boolean needsSorting;
	int maxTime, orderIndex;
	
	public JobSchedule() 
	{
		jobs = new ArrayList<Job>();
		needsSorting = true;
	}
	
	//for testing
	public Job addJob(int time)
	{
		Job j = new Job(time);
		jobs.add(j);
		needsSorting = true;
		
		return j;
	}
	
	public Job getJob(int index)
	{
		return jobs.get(index);
	}
	
	public int minCompletionTime()
	{
		topologicalSort();
		
		return maxTime;
	}
		
	//topological sort using Khan's algorithm
	private void topologicalSort()
	{
		if(needsSorting)
		{
			ArrayList<Job> topOrder = new ArrayList<Job>();
			for(Job j: jobs)
			{
				j.tempDegree = j.inDegree;
				j.startTime = -1;	//initialize to infinity
				
				if(j.tempDegree == 0)
				{
					j.startTime = 0;
					topOrder.add(j);
				}
			}
			
			
			orderIndex = 0;
			while(orderIndex < topOrder.size())
			{
				Job u = topOrder.get(orderIndex);
				
				//v is in the adjacency list of u (u is a pre-req to v)
				for(Job v: u.pre_req)
				{
					if(--v.tempDegree == 0)
						topOrder.add(v);
					
					relax(u, v);
				}
				
				int uTime = u.jobTime + u.startTime; 
				if(uTime > maxTime)
					maxTime = uTime;
				
				orderIndex++;
			}
						
			//Cycle is found
			if(topOrder.size() != jobs.size())
				maxTime = -1;
			
			needsSorting = false;
		}
	}
	
	private void relax(Job u, Job v)
	{
		int d = u.jobTime + u.startTime;
		if(d > v.startTime)
			v.startTime = d;
	}
			
	class Job
	{
		int jobTime, inDegree, tempDegree, startTime;
		ArrayList <Job> pre_req;	//This job is a pre-requisite to the jobs 
									//in the list.
		
		private Job(int time) 
		{
			jobTime = time;
			pre_req = new ArrayList<Job>();
		}
		
		public void requires(Job j)
		{
			j.pre_req.add(this);	
			inDegree++;
			needsSorting = true;
		}
		
		public int getStartTime()
		{
			topologicalSort();
			
			//After running topologicalSort, tempDegree should be 0 unless the
			//node/job is part of a cycle.
			if(tempDegree > 0)
				startTime = -1;
				
			return startTime;
		}
	}
}