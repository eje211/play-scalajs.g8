module.exports = {
  "entry": {
    "client-opt": ["/Users/eje211/src/play-scalajs.g8/status/client/target/scala-2.12/scalajs-bundler/main/client-opt.js"]
  },
  "output": {
    "path": "/Users/eje211/src/play-scalajs.g8/status/client/target/scala-2.12/scalajs-bundler/main",
    "filename": "[name]-bundle.js"
  },
  "mode": "production",
  "devtool": "source-map",
  "module": {
    "rules": [{
      "test": new RegExp("\\.js$"),
      "enforce": "pre",
      "use": ["source-map-loader"]
    }]
  }
}