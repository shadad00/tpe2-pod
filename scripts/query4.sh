cd client/target/
tar -xvf tpe2-g14-client-1.0-SNAPSHOT-bin.tar.gz
cd tpe2-g14-client-1.0-SNAPSHOT/
chmod u+x *
while getopts i:a:o:n:y: flag
do
    case "${flag}" in
        i) inPath=${OPTARG};;
        a) addresses=${OPTARG};;
        o) outPath=${OPTARG};;
        n) n=${OPTARG};;
        y) year=${OPTARG};;
    esac
done
./query4.sh -DinPath="$inPath" -Daddresses="$addresses" -DoutPath="$outPath" -Dyear="$year" -Dn="$n" -Dram