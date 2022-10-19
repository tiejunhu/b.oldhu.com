const httpProxy = require('http-proxy');
const API_NOTION = "https://api.notion.com"

const proxy = httpProxy.createProxyServer();

module.exports = (req, res) => {
  // proxy middleware options
  let prefix = "/notion-api"
  if (!req.url.startsWith(prefix)) {
    return;
  }

  let target = API_NOTION + req.url.substring(prefix.length);

  req.url = target;

  proxy.web(req, res, { target: API_NOTION });
}
