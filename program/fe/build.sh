ROOT=semv2
ng build --base-href /$ROOT/ --prod
cd dist
zip -r ../$ROOT.war ./*
cd ..
