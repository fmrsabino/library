#!/bin/bash

echo "Enter the number of clients"
read nClients
if [[ $nClients =~ ^[[:digit:]]+$ ]]; then
	echo "Starting clients"
	i=0
	while [ $i -lt "$nClients" ]; do
		java -cp library.jar bftsmart.demo.microbenchmarks.LatencyClient "$((1001 + i))" 500 10 10 false &
		let i=i+1
	done
	wait
	echo "FINISHED JOBS!"
else
	echo "not integer"
fi
