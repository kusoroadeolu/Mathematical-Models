# FUNCTIONS WITH MULTIPLE INDEPENDENT VARIABLES
## OVERVIEW 10/4/2025

The major foundation of this sandbox is my failed task class. 
It models scenarios in production systems where tasks that failed are categorized by their priority(LOW, MEDIUM, HIGH)
We can represent any instance of my failed task class as f(x, y) || f(failure count, priority) where x is the failure count and y is the priority of the task
The domain of this function states that {
y > 0 && y < 4, 
x >= 0,
y == 1 -> x >= 0 && x <= 3,
y == 2 -> x >= 0 && x <= 6,
y == 3 -> x >= 0 && x <= 9
}
If any function does not adhere to the domain, it is marked as corrupted, otherwise, it is marked as success
The range of outputs we can get from this function are either {SUCCESS OR CORRUPTED}


## EXPERIMENT
This experiment aimed to compare the difference between two probability functions.
A harsher linear function where each time a failed task didn't succeed, it would divide its base probability to succeed by its failure count
A less harsh exponential function where each time a failed task didn't succeed, it would multiply its base probability to succeed by the (0.8 ^ failure count) leading to a more controlled exponential decay

HIGH PRIO TASKS -> Retried after 100ms
MED PRIO TASKS -> Retried after 300ms
LOW PRIO TASKS -> Retried after 500ms

The test was carried out in a controlled and uncontrolled environment(Random with a set seed for both iterations and Math.random method)

FINAL VARIABLES
LOW BASE PROB -> .35
MED BASE PROB -> .45
HIGH BASE PROB -> .55
NO OF FAILED TASKS GENERATED -> 100
Linear/Harsher decay -> Base prob is divided each iteration by the failure count of the task
Exponential/Less Harsh decay -> Base prob is multiplied by (0.8 to the power of the failure count)

## Totally Random
### TEST -> Math.Random(Both in task generator and main method)
Harsh stats after one iteration
Total time: 66,062 ms (~66 seconds)
Avg time per task: 660 ms
Success: 63
Corrupted: 37

Exponential stats after one iteration
Total time: 45,731 ms (~46 seconds)
Avg time per task: 456 ms
Success: 79
Corrupted: 21

Metric        Linear Decay Exponential Decay  Method Difference
Total Time    66,062 ms    45,731 ms         Exponential 31% faster
Avg Time/Task 660 ms       456 ms            Exponential 31% faster
Success Rate  63%          79%               Exponential 25% higher
Corrupted     37           21                Exponential 43% fewer failures

## Controlled
### TEST -> Random Class, Seed 12345
Harsh stats after one iteration
Total time: 61,353 ms (~61 seconds)
Avg time per task: 613 ms
Success: 63
Corrupted: 37

Exponential stats after one iteration
Total time: 56,922 ms (~57 seconds)
Avg time per task: 569 ms
Success: 72
Corrupted: 28


Metric        Linear Decay     Exponential Decay      Method Difference
Total Time    61,353 ms         56,922 ms             Exponential 7% faster
Avg Time/Task 613 ms            569 ms                Exponential 7% faster
Success Rate  63%               72%                   Exponential 14% higher
Corrupted     37                28                    Exponential 24% fewer failures



## CONCLUSION
This experiment concluded that the exponential decay dropped less failed tasks and averaged more successes in both the controlled and uncontrolled environments
The exponential decay function outperformed linear decay across all metrics in both controlled and uncontrolled tests:
- Achieved 9-16% higher success rates
- Completed 7-31% faster
- Produced 24-43% fewer corrupted tasks
Counter intuitively, It also showed that the gentler exponential decay was **faster** (while dropping less tasks), than the harsher linear decay
The reason being the exponential decay kept the base probability higher for longer giving tasks more of an opportunity to succeed while linear decay dropped the probability to succeed faster which cause more
failures which meant more retries and delays

#### EXAMPLE 
Base probability to succeed (bp) = 0.55
Linear decay (bp / (failure count + 1))         Exponential decay (bp * (0.8 ^ failure count))
0.55 / (0 + 1) =  .550                          0.55 * (0.8 ^ 0) = .550
0.55 / (1 + 1) =  .275                          0.55 * (0.8 ^ 1) = .440
0.55 / (2 + 1) =  .183                          0.55 * (0.8 ^ 2) = .352
We can see that the linear decay drops the probability to succeed by **50%** on the first iteration while the exponential decay drops the probability by around 20%
Hence proving the statement made before correct.
This demonstrates why exponential backoff is the industry standard for retry policies in distributed systems. Its not just guess work, its mathematical convention

**KEY LEARNINGS**
Multivariable functions with piecewise domain constraints
Real world comparison and application of linear and exponential decay based retries
