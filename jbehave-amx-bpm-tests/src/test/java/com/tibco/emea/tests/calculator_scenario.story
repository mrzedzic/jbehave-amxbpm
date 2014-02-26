
Scenario: Both values are greater than zero
Given send request via ems message with <value1> and <value2>
And I want to add them
Then the calculator return <result> in less than <maxWait> seconds

Examples:
|value1|value2|result|maxWait|
|100   |100   |200   |5		 |
|100   |300   |400   |5		 |
|-100  |300   |200   |5		 |

Scenario: Both values are greater than zero
Given send request via ems message with <value1> and <value2>
And I want to substract them
Then the calculator return <result> in less than <maxWait> seconds

Examples:
|value1|value2|result|maxWait|
|100   |100   |0     |5		 |
|100   |300   |-200  |5		 |
|-100  |300   |-400   |5		 |
|-100  |300   |-400   |5		 |