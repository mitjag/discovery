#!/bin/bash
#
# Script for downloading Skywire node data from project Skycoin
#

DATE=`date +%Y%m%d%H%M%S`
DAY=`date +%Y%m%d`

mkdir /var/skycoin/$DAY

# download data
curl 'http://discovery.skycoin.net:8001/conn/getAll' -o /var/skycoin/$DAY/discovery_$DATE.json

# detele old data
DAY14=`date +%Y%m%d -d "14 day ago"`
rm -fr /root/skycoin/$DAY14
