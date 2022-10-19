const httpProxy = require('http-proxy');
const API_NOTION = "https://api.notion.com"

module.exports = (req, res) => {
  // proxy middleware options
  let prefix = "/notion-api"
  if (!req.url.startsWith(prefix)) {
    return;
  }

  let target = API_NOTION + req.url.substring(prefix.length);

  req.url = target;

  httpProxy.web(req, res, { target: API_NOTION });
}
