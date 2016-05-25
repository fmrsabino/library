declare -a remotes=(162.243.127.249 162.243.239.5 107.170.2.69 107.170.108.250 107.170.116.14 107.170.118.59 107.170.122.83 107.170.132.177 107.170.137.87)
for server in "${remotes[@]}"; do
	ssh -i ~/.ssh/do root@$server "pkill -f java" &
done
wait
echo "Finished"