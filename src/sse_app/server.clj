(ns sse-app.server
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [sse-app.sse :as my-sse]
            [io.pedestal.http.sse :as sse]
            [sse-app.api :as api]
            [clojure.data.json :as json]
            [io.pedestal.http.content-negotiation :as conneg]))

(defonce server (atom nil))

(def supported-types ["text/html" "application/edn" "application/json" "text/plain"])

(def content-neg-intc (conneg/negotiate-content supported-types))

(defn accepted-type
  [context]
  (get-in context [:request :accept :field] "text/plain"))

(defn transform-content
  [body content-type]
  (case content-type
    "text/html"        body
    "text/plain"       body
    "application/json" (json/write-str body)
    "application/edn"  (pr-str body)))

(defn coerce-to
  [response content-type]
  (-> response
      (update :body transform-content content-type)
      (assoc-in [:headers "Content-Type"] content-type)))

(def coerce-body
  {:name ::coerce-body
   :leave
   (fn [context]
     (cond-> context
       (nil? (get-in context [:response :headers "Content-Type"]))
       (update-in [:response] coerce-to (accepted-type context))))})

(def routes
  (route/expand-routes
   #{["/random" :get [coerce-body content-neg-intc api/gen-random] :route-name ::random]
     ["/sse-random" :get '(sse/start-event-stream my-sse/sse-stream) :route-name ::sse-random]
     ["/howdy" :get [coerce-body content-neg-intc api/say-your-name] :route-name ::howdy]}))

(defn create-server
  []
  (-> {::http/join? false
       ::http/routes routes
       ::http/host "0.0.0.0"
       ::http/resource-path "/public"
       ::http/allowed-origins {:creds           true
                               :allowed-origins (constantly true)}
       ::http/type            :immutant
       ::http/port            8890}
      http/default-interceptors
      http/create-server
      http/start))

(defn start-server
  []
  (let [ser (create-server)]
    (reset! server ser)))

(defn stop-server
  []
  (http/stop @server)
  (reset! server nil))