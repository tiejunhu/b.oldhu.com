+++
date = "2017-03-28T11:17:03+08:00"
title = "how to install AWS python SDK boto3"
draft = false

+++

Instead of doing ```pip install boto3```, you should do:

~~~ bash
pip install botocore
pip install boto3
~~~

Otherwise you won't have latest botocore installed. If you had done that, you should ```pip install -U botocore```