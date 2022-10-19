const proxy = require('./proxy');
const http = require('http');

const server = http.createServer(proxy);
console.log(":3000 started");
server.listen(3000);
