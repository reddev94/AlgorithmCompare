#!/bin/bash
export NODE_OPTIONS=--openssl-legacy-provider;

echo "cd AlgorithmCompareFE"
cd AlgorithmCompareFE

sleep 2

echo "ng build"
ng build

sleep 2

echo "cd dist/AlgorithmCompareFE"
cd dist/AlgorithmCompareFE

sleep 2

echo "cp -a * . ../../../AlgorithmCompareBE/Core/src/main/resources/public/"
cp -a * . ../../../AlgorithmCompareBE/Core/src/main/resources/public/

sleep 2

echo "cd ../../../"
cd ../../../

sleep 2

echo "cd AlgorithmCompareBE"
cd AlgorithmCompareBE

sleep 2

echo "mvn clean install"
mvn clean install

sleep 2