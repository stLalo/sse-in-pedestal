(ns sse-app.core
  (:require [sse-app.server :as server])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (server/start-server))
