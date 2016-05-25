declare -a replicas=(162.243.127.249 162.243.239.5 107.170.2.69 107.170.108.250 107.170.116.14 107.170.118.59 107.170.122.83)
i=0
for server in "${replicas[@]}"; do
	ssh -i ~/.ssh/do root@$server "cd bft ; rm config/currentView" &
	let i=i+1
done
wait
echo "Finished"