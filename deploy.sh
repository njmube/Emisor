VERSION=0.0.1
IMAGEN=kster/emisor
NAME=emisor
mvn package
docker build -t $IMAGEN:$VERSION .
docker push $IMAGEN:$VERSION

ssh -o "StrictHostKeyChecking no" deploy@18.220.61.105 << EOF
docker pull $IMAGEN:$VERSION
docker service update \
  --image $IMAGEN:$VERSION \
    $NAME || true

docker service create \
        --name $NAME \
        --network revnet \
        --network appnet \
        --restart-condition any \
        --replicas=2  \
        --restart-delay 5s \
        --update-delay 10s \
        --update-parallelism 1 \
        --limit-cpu 0.2 \
        --mount type=bind,source=/etc/localtime,destination=/etc/localtime \
        $IMAGEN:$VERSION || true

EOF
