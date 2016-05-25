declare -a replicas=(162.243.127.249 162.243.239.5 107.170.2.69 107.170.108.250 107.170.116.14 107.170.118.59 107.170.122.83)
declare -a clients=(107.170.122.83 107.170.132.177 107.170.137.87)

for server in "${replicas[@]}"; do
	scp -i ~/.ssh/do ./config/hosts.config root@$server:bft/config/ &
	scp -i ~/.ssh/do ./config/system.config root@$server:bft/config/ &
done
for server in "${clients[@]}"; do
	scp -i ~/.ssh/do ./config/hosts.config root@$server:client/config/ &
	scp -i ~/.ssh/do ./config/system.config root@$server:client/config/ &
done
wait
echo "Finished"