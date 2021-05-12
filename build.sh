#!/bin/bash

echo "cd AlgorithmCompareFE"
cd AlgorithmCompareFE

sleep 2

echo "ng build --prod"
ng build --prod

sleep 2

echo "cd .."
cd ..

sleep 2

echo "cd AlgorithmCompareBE"
cd AlgorithmCompareBE

sleep 2

echo "mvn clean install"
mvn clean install


sleep 2