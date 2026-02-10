const express = require('express');
const path = require('path');
const http = require('http');

const app = express();
const PORT = process.env.PORT || 3000;
const publicDir = path.join(__dirname, 'public');
const backendBaseUrl = 'http://localhost:8082';

app.use('/service1-spring', (req, res) => {
  const backendUrl = new URL(req.originalUrl, backendBaseUrl);
  const proxyReq = http.request(
    backendUrl,
    {
      method: req.method,
      headers: req.headers,
    },
    (proxyRes) => {
      res.status(proxyRes.statusCode || 502);
      Object.entries(proxyRes.headers).forEach(([key, value]) => {
        if (value !== undefined) {
          res.setHeader(key, value);
        }
      });
      proxyRes.pipe(res);
    }
  );

  proxyReq.on('error', (err) => {
    res.status(502).send(`Proxy error: ${err.message}`);
  });

  req.pipe(proxyReq);
});

app.use(express.static(publicDir));

// Fallback to index.html so the SPA can handle routing if needed
app.get('*', (req, res) => {
  res.sendFile(path.join(publicDir, 'index.html'));
});

app.listen(PORT, () => {
  console.log(`SOA Lab 2 client running at http://localhost:${PORT}`);
});

