# FUNCTIONS WITH MULTIPLE INDEPENDENT VARIABLES
## OVERVIEW 10/3/2025

The major foundation of this sandbox is my failed task class. 
It models scenarios in production systems where tasks that failed are categorized by their priority(LOW, MEDIUM, HIGH)
We can represent any instance of my failed task class as f(x, y) where x is the failure count and y is the priority of the task
The domain of this function states that {
y > 0 && y < 4, 
x >= 0,
x == 1 -> y >= 0 || y <= 3,
x == 2 -> y >= 0 || y <= 6,
x == 3 -> y >= 0 || y <= 9
}
If any function does not adhere to the domain, it is marked as corrupted, otherwise, it is marked as success
The range of outputs we can get from this function are either {SUCCESS OR CORRUPTED}


## EXPERIMENT
This experiment aimed to document the difference between two probability functions.
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
This experiment concluded that the exponential decay dropped less failed tasks and average more successes in both the controlled and uncontrolled environments
It also showed that the exponential decay even while dropping less tasks, completed in less time, while the linear decay completed which dropped more tasks on average completed in less time
The success rate between both were also noticeable by around 9% in the controlled and 16% in the uncontrolled environment
The linear rate also produced more corrupted tasks taking into account the failed task generators can also produce corrupted tasks initially, but it's definitely negligible
In conclusion, I learnt how multiple independent variables in a function work, piece wise functions and why exponential decays are more favored in production environments