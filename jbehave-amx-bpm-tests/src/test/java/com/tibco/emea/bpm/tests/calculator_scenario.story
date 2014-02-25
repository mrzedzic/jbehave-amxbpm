Narrative: Calculator owner execute ADD operation
    As a Calculator owner
    I want to add 2 values
    To receive result of operation

Scenario: Both values are greater than zero
Given send request via ems message with <value1> and <value2>
Then the calculator return <result> in less than <maxWait> seconds

Examples:
|value1|value2|result|maxWait|
|100   |100   |200   |5		 |
|100   |300   |400   |5		 |
|-100  |300   |200   |5		 |
|-100  |350   |200   |5		 |