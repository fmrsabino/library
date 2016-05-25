jarLocation=build/libs/library.jar
adaptDeploy=deploys/adapt
clientDeploy=deploys/client
bftDeploy=deploys/bft
echo "Building jar..."
gradle jar
echo "Creating deploy configurations..."
mkdir -p -- "deploys/adapt"
mkdir -p -- "deploys/bft"
mkdir -p -- "deploys/client"
cp $jarLocation $adaptDeploy
cp $jarLocation $clientDeploy
cp $jarLocation $bftDeploy
cp -r config $adaptDeploy
cp -r config $clientDeploy
cp -r config $bftDeploy
cp -r adapt-config $adaptDeploy
#servers
#scp -i ~/.ssh/do deploys/bft/library.jar root@162.243.127.249:~/bft
#scp -i ~/.ssh/do deploys/bft/library.jar root@162.243.239.5:~/bft
#scp -i ~/.ssh/do deploys/bft/library.jar root@107.170.2.69:~/bft
#scp -i ~/.ssh/do deploys/bft/library.jar root@107.170.108.250:~/bft
#clients
scp -i ~/.ssh/do deploys/bft/library.jar root@107.170.116.14:~/client
#scp -i ~/.ssh/do deploys/bft/library.jar root@107.170.118.59:~/client
#scp -i ~/.ssh/do deploys/bft/library.jar root@107.170.122.83:~/client
#scp -i ~/.ssh/do deploys/bft/library.jar root@107.170.116.14:~/client