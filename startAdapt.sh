#!/bin/bash
clear
echo "Starting Adapt BFT Service"

bash -c './runscripts/smartrun.sh bftsmart.demo.adapt.AdaptServer 0; bash' &
sleep 2
bash -c './runscripts/smartrun.sh bftsmart.demo.adapt.AdaptServer 1; bash' &
sleep 2
bash -c './runscripts/smartrun.sh bftsmart.demo.adapt.AdaptServer 2; bash' &
sleep 2
bash -c './runscripts/smartrun.sh bftsmart.demo.adapt.AdaptServer 3; bash' &
sleep 2
wait