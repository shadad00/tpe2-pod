cd client/target/
tar -xvf tpe2-g14-client-1.0-SNAPSHOT-bin.tar.gz
cd tpe2-g14-client-1.0-SNAPSHOT/
chmod u+x *
while getopts i:a:o:m: flag
do
    case "${flag}" in
        i) inPath=${OPTARG};;
        a) addresses=${OPTARG};;
        o) outPath=${OPTARG};;
        m) min=${OPTARG};;
    esac
done
./query3.sh -DinPath="$inPath" -Daddresses="$addresses" -DoutPath="$outPath" -Dmin=$min