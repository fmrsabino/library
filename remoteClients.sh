declare -a clients=(107.170.122.83 107.170.132.177)
#107.170.116.14 107.170.118.59 107.170.122.83 107.170.132.177 107.170.137.87
ssh -i ~/.ssh/do root@107.170.122.83 "cd client ; java -cp library.jar bftsmart.demo.microbenchmarks.ThroughputLatencyClient 15 1001 10000 512 0 false false false" &
ssh -i ~/.ssh/do root@107.170.132.177 "cd client ; java -cp library.jar bftsmart.demo.microbenchmarks.ThroughputLatencyClient 15 1021 10000 512 0 false false false" &
wait
echo "Finished"