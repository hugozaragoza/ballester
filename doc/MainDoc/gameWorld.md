###Agent Behviour###

####Actions####

At each turn an agents can carry an *Action* if its preconditions are met.

Several actions can be put in an *ActionSequence* and several of these into an *ActionTree*. 

Each actions in an actions sequence is carried out until it checks its completionCondition.

##### Emotional State  #####

Agents have an *EmotionalState* depending on the environment and their own state : hungry, afraid, scared, agressive... these states can be used in action pre-conditions.

An *EmotionalStateSequence* can be used to prioritize emotional states: the first checking its precondition is activated.

An emotional state can also be forced externally (by an order, a potion...) for a period of time.