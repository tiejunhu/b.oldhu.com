+++
draft = false
toc = false
date = "2022-09-26T14:22:41+08:00"
title = "Integrate Webvitals of Hugo to Vercel Analytics"
+++

### Add the following to hugo config.yaml

```yaml
security:
  funcs:
    getenv:
      - ^VERCEL_
```

### Add the following code to header.html partial

```html
  {{ if not hugo.IsServer }}
  <script type="module">
   import { getCLS, getFCP, getFID, getLCP, getTTFB } from 'https://unpkg.com/web-vitals@3?module';
   const vitalsUrl = 'https://vitals.vercel-analytics.com/v1/vitals';

   function getConnectionSpeed() {
     return 'connection' in navigator &&
            navigator['connection'] &&
            'effectiveType' in navigator['connection']
          ? navigator['connection']['effectiveType']
          : '';
   }

   function sendToAnalytics(metric, options) {
     const body = {
       dsn: {{ os.Getenv "VERCEL_ANALYTICS_ID" }}, // qPgJqYH9LQX5o31Ormk8iWhCxZO
       id: metric.id, // v2-1653884975443-1839479248192
       page: {{ .Page.RelPermalink }}, // /blog/[slug]
       href: location.href, // https://my-app.vercel.app/blog/my-test
       event_name: metric.name, // TTFB
       value: metric.value.toString(), // 60.20000000298023
       speed: getConnectionSpeed(), // 4g
     };

     if (options.debug) {
       console.log('[Analytics]', metric.name, JSON.stringify(body, null, 2));
     }

     const blob = new Blob([new URLSearchParams(body).toString()], {
       // This content type is necessary for `sendBeacon`
       type: 'application/x-www-form-urlencoded',
     });
     if (navigator.sendBeacon) {
       navigator.sendBeacon(vitalsUrl, blob);
     } else
       fetch(vitalsUrl, {
         body: blob,
         method: 'POST',
         credentials: 'omit',
         keepalive: true,
       });
   }

   function webVitals(options) {
     try {
       getFID((metric) => sendToAnalytics(metric, options));
       getTTFB((metric) => sendToAnalytics(metric, options));
       getLCP((metric) => sendToAnalytics(metric, options));
       getCLS((metric) => sendToAnalytics(metric, options));
       getFCP((metric) => sendToAnalytics(metric, options));
     } catch (err) {
       console.error('[Analytics]', err);
     }
   }

   webVitals({ debug: true });

  </script>
  {{ end }}
```
