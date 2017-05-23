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
~~ bash
eval $(docker-machine env default)
docker info
~~