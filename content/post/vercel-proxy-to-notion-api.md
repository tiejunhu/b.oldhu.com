+++
draft = false
toc = false
date = "2022-10-25T10:59:54+08:00"
title = "Use Vercel as Notion API Proxy"
+++

If you call notion API directly from a web page, it will complain about CORS. So we need a proxy to it.

### rewrite rule

in vercel.json, add:

```json
{
    ......

    "rewrites": [
        {
            "source": "/notion-api/(.*)",
            "destination": "/api/proxy"
        }
    ]
	
	......
}
```

### /api/proxy.js

create `/api/proxy.js` file, with the following content

```javascript
const { createProxyMiddleware } = require('http-proxy-middleware');
const cors = require('cors');
const API_NOTION = "https://api.notion.com"

function onRequest(preq, req) {
  const headers = preq.getHeaderNames();
  headers.forEach((header) => {
    if (header.startsWith('x-')) {
      preq.removeHeader(header);
    }
  })
}

function onResponse(pres) {
  pres.headers["Access-Control-Allow-Origin"] = "*";
}

const proxy = createProxyMiddleware({
  target: API_NOTION,
  changeOrigin: true,
  pathRewrite: {
    '^/notion-api': ''
  },
  toProxy: true,
  logLevel: 'debug',
  onProxyReq: onRequest,
  onProxyRes: onResponse
});

const corsFunc = cors({ origin: true });

module.exports = (req, res) => {
  let prefix = "/notion-api"
  if (!req.url.startsWith(prefix)) {
    return;
  }

  if (req.method == 'OPTIONS') {
    corsFunc(req, res);
    return;
  }

  proxy(req, res);
}
```

under `/api`, run `yarn add http-proxy-middleware && yarn add cors`

push and deploy the project

### access the api

access the api from `https://vercel-domain-name/notion-api/xxxxxx`
