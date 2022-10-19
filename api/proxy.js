const { createProxyMiddleware } = require('http-proxy-middleware');
const API_NOTION = "https://api.notion.com"

const proxy = createProxyMiddleware({
  target: API_NOTION,
  changeOrigin: true,
  pathRewrite: {
    '^/notion-api': '/'
  }
});

module.exports = (req, res) => {
  // proxy middleware options
  let prefix = "/notion-api"
  if (!req.url.startsWith(prefix)) {
    return;
  }

  proxy(req, res);
}
