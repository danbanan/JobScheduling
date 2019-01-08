import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MyTests {
	
	JobSchedule schedule;
	JobSchedule.Job j;
	
	JobSchedule schedule2;
	JobSchedule.Job j0;
	JobSchedule.Job j1;
	JobSchedule.Job j2;
	JobSchedule.Job j3;
	JobSchedule.Job j4;
	JobSchedule.Job j5;
	JobSchedule.Job j6;
	JobSchedule.Job j7;
	JobSchedule.Job j8;
	JobSchedule.Job j9;
	JobSchedule.Job j10;
	
	@Before
	public void setUp() throws Exception
	{
		schedule = new JobSchedule();
		schedule.addJob(8);  //adds job 0 with time 8
		j = schedule.addJob(3);  //adds job 1 with time 3
		schedule.addJob(5);  //adds job 2 with time 5
		
		schedule2 = new JobSchedule();
		j0 = schedule2.addJob(3);
		j1 = schedule2.addJob(5);
		j2 = schedule2.addJob(7);
		j3 = schedule2.addJob(2);
		j4 = schedule2.addJob(1);
		j5 = schedule2.addJob(6);
		j6 = schedule2.addJob(5);
		j7 = schedule2.addJob(8);
		j8 = schedule2.addJob(30);
		j9 = schedule2.addJob(1);
		j10 = schedule2.addJob(3);
		
		j10.requires(j8);
		j10.requires(j4);
		j7.requires(j9);
		j6.requires(j5);
		j5.requires(j4);
		j4.requires(j3);
		j3.requires(j2);
		j2.requires(j1);
		j1.requires(j0);
	}
	
	@Test
	public void testA() 
	{
		assertEquals(8, schedule.minCompletionTime());
		assertEquals(0, schedule.getJob(0).getStartTime());
		assertEquals(0, schedule.getJob(1).getStartTime());
		assertEquals(0, schedule.getJob(2).getStartTime());
	}
	
	@Test
	public void testB()
	{
		schedule.getJob(0).requires(schedule.getJob(2));  //job 2 must precede job 0
		assertEquals(13, schedule.minCompletionTime());
		
		schedule.getJob(0).requires(j);  //job 1 must precede job 0
		assertEquals(13, schedule.minCompletionTime());
		
		assertEquals(5, schedule.getJob(0).getStartTime());		
		assertEquals(0, j.getStartTime());		
		assertEquals(0, schedule.getJob(2).getStartTime());
		
		j.requires(schedule.getJob(2));  //job 2 must precede job 1
		assertEquals(16, schedule.minCompletionTime());
		
		assertEquals(8, schedule.getJob(0).getStartTime());		
		assertEquals(5, schedule.getJob(1).getStartTime());		
		assertEquals(0, schedule.getJob(2).getStartTime());
		
		schedule.getJob(1).requires(schedule.getJob(0));  //job 0 must precede job 1 (creates loop)
		assertEquals(-1, schedule.minCompletionTime());
		assertEquals(0, schedule.getJob(2).getStartTime());
		assertEquals(-1, schedule.getJob(0).getStartTime());
		assertEquals(-1, schedule.getJob(1).getStartTime());
	}
	
	@Test
	public void testC() 
	{
		assertEquals(1, j3.inDegree);
		assertEquals(2, j10.inDegree);
		assertEquals(1, j7.inDegree);
		assertEquals(0, j8.inDegree);
		assertEquals(30, j8.jobTime);
		assertEquals(0, j0.inDegree);
		
		assertEquals(1, j8.pre_req.size());
		assertEquals(2, j4.pre_req.size());
		
		assertEquals(3, j8.pre_req.get(0).jobTime);
		assertEquals(3, j4.pre_req.get(0).jobTime);
		assertEquals(6, j4.pre_req.get(1).jobTime);
		
		assertEquals(1, j7.getStartTime());
		assertEquals(0, j8.getStartTime());
		assertEquals(30, j10.getStartTime());
		assertEquals(15, j3.getStartTime());
		assertEquals(0, j0.getStartTime());
		assertEquals(33, schedule2.minCompletionTime());
	}

	@Test
	//Creating cycle between j9 and j7, which is disconnected to the rest of the 
	//graph.
	public void testD1()
	{
		assertEquals(33, schedule2.minCompletionTime());
		
		j9.requires(j7);
		
		assertEquals(1, j9.inDegree);
		assertEquals(1, j7.inDegree);
		
		assertEquals(-1, schedule2.minCompletionTime());
		assertEquals(-1, j9.getStartTime());
		assertEquals(-1, j7.getStartTime());
		assertEquals(17, j4.getStartTime());
	}
	
	@Test
	//Creating inner cycle between j2, j3 and j8
	public void testD()
	{
		assertEquals(33, schedule2.minCompletionTime());
		
		j8.requires(j3);
		j2.requires(j8);
		
		assertEquals(2, j2.inDegree);
		assertEquals(1, j8.inDegree);
		assertEquals(1, j3.inDegree);
		
		assertEquals(-1, schedule2.minCompletionTime());
		assertEquals(1, j7.getStartTime());
		assertEquals(3, j1.getStartTime());
		assertEquals(-1, j2.getStartTime());
		assertEquals(-1, j5.getStartTime());
		assertEquals(-1, j8.getStartTime());
		assertEquals(-1, j3.getStartTime());
		assertEquals(-1, j10.getStartTime());
	}

}
