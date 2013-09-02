(defproject url-shortener "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring "1.2.0"]
                 [ring/ring-json "0.2.0"]
                 [compojure "1.1.5"]]
  :plugins [[lein-ring "0.8.6"]]
  :ring {:handler url-shortener.core/app}
  :main url-shortener.core
  :uberjar-name "url-shortener-standalone.jar"
  :min-lein-version "2.0.0"
  :profiles {:uberjar {:aot :all}})
