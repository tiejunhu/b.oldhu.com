+++
categories = ["technology"]
date = "2017-05-03T10:57:14+08:00"
draft = false
tags = ["scripts"]
title = "Delete All Records From Dingtalk Workflow"
toc = false

+++

~~~ javascript
document.getElementsByTagName('head')[0].appendChild(document.createElement('script')).setAttribute('src','https://code.jquery.com/jquery-3.2.1.min.js');

function deleteall() {
    while($(".delete-message").length > 0) {
        $(".delete-message")[0].click();
        $(".kuma-dialog-confirm").click();    
    }
    $(".prev").click();
    setTimeout(deleteall, 1000);
}

deleteall();
~~~