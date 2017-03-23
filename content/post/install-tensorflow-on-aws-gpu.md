+++
date = "2017-03-23T13:23:09+08:00"
title = "Install Tensorflow on AWS GPU Instance"
draft = false
comments: true
+++
The following procedure will guide you through installation of tensorflow on AWS GPU instances with 64bit Ubuntu1604

## make sure .bashrc has the following content
~~~ bash
export CUDA_HOME=/usr/local/cuda
export CUDA_ROOT=/usr/local/cuda
export PATH=$PATH:$CUDA_ROOT/bin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$CUDA_ROOT/lib64
~~~
## CUDA and cuDNN Installation
### Install CUDA Toolkit 8.0
~~~ shell
# note 1
wget http://developer.download.nvidia.com/compute/cuda/repos/ubuntu1604/x86_64/cuda-repo-ubuntu1604_8.0.61-1_amd64.deb
sudo dpkg -i cuda-repo-ubuntu1604_8.0.61-1_amd64.deb
sudo apt-get update

# note 2
sudo apt-get install -y nvidia-367 libcuda1-367
sudo apt-get install -y cuda-toolkit-8-0

sudo reboot
~~~
##### note 1
If your linux is not ubuntu1604, you should browse [here](http://developer.download.nvidia.com/compute/cuda/repos/) to find your distribution and version of linux.
##### note 2
Currently AWS has two instance types with GPU support: G2 and P2. G2 is installed with K520 card and P2 is with P80. nvidia-367 driver is for the K520 card therefore the G2 instances. If you are using P2 instance, you should use ```nvidia-375``` and ```libcuda1-375```

### Check driver state
```nvidia-smi``` should show all your GPU cards now.

### cuDNN v5.1
~~~ bash
wget https://dl.dropboxusercontent.com/u/2762665/cudnn-8.0-linux-x64-v5.1.tgz
sudo tar -xvf cudnn-8.0-linux-x64-v5.1.tgz -C /usr/local
~~~

## Tensorflow Installation
### install
~~~ bash
sudo apt-get install -y python3-pip
pip3 install tensorflow-gpu
pip3 install opencv-python
~~~
### verify
~~~ python
import tensorflow as tf
tf_session = tf.Session()
x = tf.constant(1)
y = tf.constant(1)
tf_session.run(x + y)
~~~

## Ansible script
~~~ yaml
- hosts: aws
  tasks:
  - name: modify .bashrc
    blockinfile:
      dest: ~/.bashrc
      block: |
        export CUDA_HOME=/usr/local/cuda
        export CUDA_ROOT=/usr/local/cuda
        export PATH=$PATH:$CUDA_ROOT/bin
        export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$CUDA_ROOT/lib64
  - name: install nvidia repo deb
    become: yes
    apt:
      deb: http://developer.download.nvidia.com/compute/cuda/repos/ubuntu1604/x86_64/cuda-repo-ubuntu1604_8.0.61-1_amd64.deb
  - name: update apt
    become: yes
    apt:
      update_cache: yes
  - name: install nvidia_driver_367/cuda/pip3
    become: yes
    apt: name={{item}} state=installed
    with_items:
      - nvidia-367
      - libcuda1-367
      - cuda-toolkit-8-0
      - python3-pip
  - name: install cuDNN
    become: yes
    unarchive:
      src: https://dl.dropboxusercontent.com/u/2762665/cudnn-8.0-linux-x64-v5.1.tgz
      dest: /usr/local
      remote_src: True
  - name: install tensorflow and opencv
    pip: name={{item}} executable=pip3
    with_items:
      - tensorflow-gpu
      - opencv-python
  - name: Reboot
    become: yes
    shell: shutdown -r now
~~~
