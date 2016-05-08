workspaceDir=workspace
sensorsDir=s
adaptDir=a
bftDir=b
nSensors=4
nAdapt=4
nBft=7

mkdir -p -- "$workspaceDir"
i=0
while [ $i -lt "$nSensors" ]; do
	mkdir -p -- "$workspaceDir/$sensorsDir$i"
	cp -r "config/" "$workspaceDir/$sensorsDir$i"
	cp -r "adapt-config" "$workspaceDir/$sensorsDir$i"
	cp -r "sensor" "$workspaceDir/$sensorsDir$i"
	let i=i+1
done
echo "Created sensors workspace"
i=0
while [ $i -lt "$nAdapt" ]; do
	mkdir -p -- "$workspaceDir/$adaptDir$i"
	cp -r "config/" "$workspaceDir/$adaptDir$i"
	cp -r "adapt-config" "$workspaceDir/$adaptDir$i"
	let i=i+1
done
echo "Created Adapt workspace"
i=0
while [ $i -lt "$nBft" ]; do
	mkdir -p -- "$workspaceDir/$bftDir$i"
	cp -r "config/" "$workspaceDir/$bftDir$i"
	let i=i+1
done
echo "Created BFT-SMaRt workspace"