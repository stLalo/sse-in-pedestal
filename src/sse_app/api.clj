(ns sse-app.api)

(defn ok
  [ent]
  {:status 200
   :body ent})

(defn not-found
  []
  {:status 404 :body "Not Found\n"})

(defn get-greeting
  [name]
  (cond
    (empty? name) "Ah, you are annonimous"
    :else
    (str "Howdy, " name "!\n")))

(defn say-your-name
  [request]
  (let [name (-> request :query-params :name)
        resp (get-greeting name)]
    (if resp
      (ok resp)
      (not-found))))

(defn gen-random
  [request]
  (-> 999 rand str ok))

(def echo
  {:name ::echo
   :enter #(assoc % :response (ok (:request %)))})