(def project 'sse-app)
(def version "1.0.0-SNAPSHOT")

(set-env! :resource-paths #{"src"}
          :source-paths   #{"test"}
          :dependencies   '[[org.clojure/clojure "1.10.1"]
                            [io.pedestal/pedestal.service "0.5.7"]
                            [io.pedestal/pedestal.route "0.5.7"]
                            [io.pedestal/pedestal.immutant "0.5.7"]
                            [io.pedestal/pedestal.jetty   "0.5.1"]
                            [org.clojure/core.async "0.4.500"]
                            [com.taoensso/timbre "4.10.0"]
                            [org.clojure/data.json "0.2.6"]
                            [adzerk/boot-test "RELEASE" :scope "test"]])

(task-options!
 aot {:namespace   #{'sse-app.core}}
 pom {:project     project
      :version     version
      :description "Simple SSE service"
      :scm         {:url "https://github.com/stLalo/sse-in-pedestal"}}
 repl {:init-ns    'sse-app.core}
 jar {:main        'sse-app.core
      :file        (str "sse-app-" version "-standalone.jar")})

(deftask build
  "Build the project locally as a JAR."
  [d dir PATH #{str} "the set of directories to write to (target)."]
  (let [dir (if (seq dir) dir #{"target"})]
    (comp (aot) (pom) (uber) (jar) (target :dir dir))))

(deftask run
  "Run the project."
  [a args ARG [str] "the arguments for the application."]
  (with-pass-thru fs
    (require '[sse-app.core :as app])
    (apply (resolve 'app/-main) args)))

(require '[adzerk.boot-test :refer [test]])
