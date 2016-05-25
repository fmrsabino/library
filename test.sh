#!/bin/bash
echo "Running tests..."
for i in {0..20}
	do
		if [ "$i" = 0 ]; then
			echo "1 client"
		else
			echo "$((i*5)) clients" 
		fi
		for j in {0..30}
			do
				if [ "$i" = 0 ]; then
					java -cp library.jar bftsmart.demo.microbenchmarks.ThroughputLatencyClient 1 1001 2 10 0 false false false
				else
					java -cp library.jar bftsmart.demo.microbenchmarks.ThroughputLatencyClient $((i*5)) 1001 2 10 0 false false false
				fi
		done
		echo "======"
done
echo "Finished..."