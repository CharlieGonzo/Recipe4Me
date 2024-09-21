sudo mvn compile jib:dockerBuild
sudo docker tag your-image-name charlieg2k1/your-image-name:latest
sudo docker login -u charlieg2k1
sudo docker push charlieg2k1/your-image-name:latest
