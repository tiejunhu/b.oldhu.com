+++
categories = ["technology"]
date = "2017-05-23T10:32:31+08:00"
draft = false
tags = ["docker","mac","homebrew","vmware"]
title = "Using Docker With Homebrew and VMware Fusion on Mac"
toc = false
+++

## Install Docker with Homebrew
~~~ bash
brew install docker
brew install docker-machine
~~~

## Create Default Machine with VMware Fusion Driver
~~~ bash
docker-machine create --driver vmwarefusion default
~~~

## Eval Env and Manage
~~~ bash
eval $(docker-machine env default)
docker info
~~~

## Config Network
Get the HWaddr of VM and stop VM
~~~ bash
docker-machine ssh default ifconfig -a | grep eth0
docker-machine stop default
~~~

Edit the VMware NAT network adapter
~~~ bash
sudo vi "/Library/Preferences/VMware Fusion/vmnet8/dhcpd.conf"
~~~
Added the following line
~~~
host default {
    hardware ethernet 00:0C:29:E6:F9:AC;
    fixed-address 192.168.220.20;
}
~~~
Stop VMware network services
~~~
sudo /Applications/VMware\ Fusion.app/Contents/Library/vmnet-cli --stop
~~~
Add static route
~~~
sudo route -n add 172.17.0.0/16 192.168.220.20
~~~
Start VM and regenerate certificates
~~~
docker-machine start default
docker-machine regenerate-certs default
~~~

