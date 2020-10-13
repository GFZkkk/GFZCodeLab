versionName="9.79"
versionCode="979"
output0="/Users/gaofengze/StudioProjects/apk/unsign"
output1="/Users/gaofengze/StudioProjects/apk/jiagu"
output2="/Users/gaofengze/StudioProjects/apk/sign"
output3="/Users/gaofengze/StudioProjects/apk/channel"
output4="/Users/gaofengze/Desktop"

source .bash_profile
zipalign -v -p 4 ${output1}/app-release_${versionCode}_jiagu.apk ${output2}/my-app-unsigned-aligned.apk
apksigner sign --ks key_store/key --ks-key-alias minimud_towords --ks-pass pass:2081101 --key-pass pass:2081101 --out ${output2}/towords_${versionName}.apk ${output2}/my-app-unsigned-aligned.apk
java -jar packer-ng-2.0.1.jar generate --channels=@markets.txt --output=${output3} ${output2}/towords_${versionName}.apk
cp -R ${output3} ${output4}

cd ${output4}/channel
for eachfile in `ls -B`
do
  filename=${eachfile%.apk}
  channelName=${filename##*-}
  nameWithoutChannel=${filename%%-*}
  versionCode=${nameWithoutChannel##*_}
  echo towords_${channelName}_${versionCode}
  mv ${filename}.apk towords_${channelName}_${versionCode}.apk
done
#export PATH=${PATH}:/Users/gaofengze/Library/Android/sdk/tools:/Users/gaofengze/Library/Android/sdk/platform-tools/
#export PATH=${PATH}:/Users/gaofengze/Library/Android/sdk/tools:/Users/gaofengze/Library/Android/sdk/build-tools/27.0.3/
