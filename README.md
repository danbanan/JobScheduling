## Job Scheduling Program

You will create a "JobSchedule" data structure (with a constructor), which will hopefully use an underlying directed, acyclic graph. You will also have an inner class, JobSchedule.Job.

For JobSchedule, you will have a public constructor (no parameters). Additionally, you will have three public methods:

1. public Job addJob(int time) adds a new job to the schedule, where the time needed to complete the job is time. (time will be positive.) Jobs will be numbered, by the order in which they are added to your structure: the first job added is (implicitly) job 0, the next 1, even though they won't be labeled.

2. public Job getJob(int index) method, which returns the job by its number. (Use the implicit number, as described in addJob. That number has nothing to do with the time for completion.) (I will only call this method with valid indices. I want you to correctly code the algorithm, rather than worrying about bullet-proofing your code against bad inputs.)

3. public int minCompletionTime() should return the minimum possible completion time for the entire JobSchedule, or -1 if it is not possible to complete it. (It is not possible to complete if there is a prerequisite cycle within the underlying graph).

Your Job class should have exactly two public methods. It should not have a public constructor. (Its constructor should only be called from within the JobSchedule class.)

1. public void requires(Job j) sets up the requirement that this job requires job j to be completed before it begins.

2. public int getStartTime() will return the earliest possible start time for the job. (The very first first start time, for jobs with no pre-requisites, is 0.) If there IS a cycle, and thus the given job can never be started (because it is on the cycle, or something on the cycle must be completed before this job starts), return -1. However, if the job can be started, even if other jobs within the overall schedule cannot be, return a valid time.
Example: for the following calls, I show the return values expected:

``````````````````````
JobSchedule schedule = new JobSchedule();
schedule.addJob(8);  //adds job 0 with time 8
JobSchedule.Job j1 = schedule.addJob(3);  //adds job 1 with time 3
schedule.addJob(5);  //adds job 2 with time 5
schedule.minCompletionTime();  //should return 8, since job 0 takes time 8 to complete.
//Note it is not the min completion time of any job, but the earliest the entire set can complete.
schedule.getJob(0).requires(schedule.getJob(2));  //job 2 must precede job 0
schedule.minCompletionTime();  //should return 13 (job 0 cannot start until time 5)
schedule.getJob(0).requires(j1);  //job 1 must precede job 0
schedule.minCompletionTime();  //should return 13
schedule.getJob(0).getStartTime();  //should return 5
j1.getStartTime();  //should return 0
schedule.getJob(2).getStartTime();  //should return 0
j1.requires(schedule.getJob(2));  //job 2 must precede job 1
schedule.minCompletionTime();  //should return 16
schedule.getJob(0).getStartTime();  //should return 8
schedule.getJob(1).getStartTime();  //should return 5
schedule.getJob(2).getStartTime();  //should return 0
schedule.getJob(1).requires(schedule.getJob(0));  //job 0 must precede job 1 (creates loop)
schedule.minCompletionTime();  //should return -1
schedule.getJob(0).getStartTime();  //should return -1
schedule.getJob(1).getStartTime();  //should return -1
schedule.getJob(2).getStartTime();  //should return 0 (no loops in prerequisites)
``````````````````````

- Your algorithm should look pretty similar to the acyclic single source shortest path one. The primary difference is that, for every node, the initial (default) start time, before any constraints are added, is 0. Instead of relaxing edges to give shorter paths, you are actually "relaxing" constraints to give higher start times. That is, in the example above, the first constraint raises 0's start time because, if 2 starts at its start time 0, and takes 5 to complete, then the start time for 0 is now max(0,5), the previous start time and the new constraint from job 2.

- Your code should be efficient. This includes two aspects: your graph should have an efficient enough implementation for basic functions, and also, you can try to figure out how, algorithmically, you can make your code more efficient specifically for this problem. That being said, there will be some tests that should pass if your code is correct but not efficient (but it can't be absurdly inefficient), and others that should pass only if you have efficient code. This is the "interesting" part of the assignment: rather than just translating from high-level pseudocode to a program (a programming problem), you are being asked to think a bit about what the algorithm does, and how to improve it.