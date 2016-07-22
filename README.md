# ByTAM - Byzantine Tolerant Adaptation Manager

This project uses gradle. Please ensure that you have the latest stable version.

To compile the project run the compileJava target
```
gradle compileJava
```

To generate a jar with all the dependencies run the jar target. You can find the generated jar in build/libs
```
gradle jar
```

##Components and Process Flow
This project focuses on 2 components: Adaptation Manager and Managed System.

An Adaptation Manager replica is represented by the AdaptReplica class.
This replica receives values from sensors (can be any process that sends messages and it is tour choice if you replicate them or not). The messages from the sensors must be signed so the Adaptation Manager can verify its authenticity (an example is provided in SensorsTest).

The Adaptation Manager also uses two dynamic "rules" known as checkers and policies.
The checkers see if upon receiving a value (BasicValueChecker) or when a time interval is reached (TimedValueChecker) it needs to run the policy currently active. There's also an HybridValueChecker if we want the two behaviors. These checkers serve just as an example. You are invited to implement your own checker by implementing the ValueChecker interface. These checkers have access to a Registry where values sent from the sensors are stored in the replica.

The policies simply create a new configuration using the Managed System Reconfiguration API (BFT-SMaRt provided one for adding and removing replicas). This configuration will be executed by each replica of the Managed System.
To create a new policy one must implement the AdaptPolicy interface. The result of the execute() method should be an AdaptMessage.

