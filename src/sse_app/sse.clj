(ns sse-app.sse
  (:require [clojure.core.async :as async :refer [>!! >! <! <!! go go-loop]]
            [io.pedestal.http.sse :as s]
            [taoensso.timbre :as timbre]
            [sse-app.api :as api]))

(defn send-random
  [event-ch & args]
  (go-loop []
    (cond (nil? event-ch)
          event-ch
          :else
          (do (>!! event-ch {:name "sse-random" :data (rand 999)})
              (Thread/sleep 1000)))
    (recur)))

(defn sse-stream
  [event-ch ctx]
  (let [{:keys [request response-channel]} ctx]
    (try (async/close! (send-random event-ch))
         (catch Exception e
           (timbre/info "Channel Closed")))))